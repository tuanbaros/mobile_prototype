package com.framgia.mobileprototype.projects;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.login.LoginManager;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.model.User;
import com.framgia.mobileprototype.data.remote.ApiService;
import com.framgia.mobileprototype.data.source.DataImport;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 23/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projects
 */
public class ProjectsPresenter implements ProjectsContract.Presenter {
    private ProjectsContract.View mProjectsView;
    private ProjectRepository mProjectRepository;
    private MockRepository mMockRepository;
    private ElementRepository mElementRepository;
    private Project mEditProject;
    private List<String> mImageNames = new ArrayList<>();
    private int mCount;
    private Project mUploadProject;

    public ProjectsPresenter(ProjectsContract.View projectsView,
                             ProjectRepository projectRepository,
                             MockRepository mockRepository,
                             ElementRepository elementRepository) {
        mProjectsView = projectsView;
        mProjectRepository = projectRepository;
        mMockRepository = mockRepository;
        mElementRepository = elementRepository;
    }

    @Override
    public void saveSampleProject(boolean isFirstApp, InputStream inputStream) {
        if (isFirstApp) {
            DataImport dataImport = new DataImport(
                mElementRepository, mMockRepository, mProjectRepository);
            dataImport.save(inputStream);
        }
        getProjects();
    }

    @Override
    public void createAppStorageFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) folder.mkdirs();
    }

    @Override
    public void getProjects() {
        mProjectRepository.getDatas(new DataSource.GetListCallback<Project>() {
            @Override
            public void onSuccess(List<Project> datas) {
                mProjectsView.projectsLoaded(datas);
            }

            @Override
            public void onError() {
                mProjectsView.projectsNotAvailable();
            }
        });
    }

    @Override
    public void createProject() {
        Project project = new Project();
        project.setPortrait(true);
        mProjectsView.showCreateProjectDialog(project);
    }

    @Override
    public void storeProject(Project project, boolean isPosterChanged) {
        if (project.getTitle() == null || project.getTitle().trim().equals("")) {
            mProjectsView.showErrorEmptyProjectName();
            return;
        }
        if (project.getOrientation().equals(Project.LANDSCAPE)) {
            project.setWidth(ScreenSizeUtil.sHeight);
            project.setHeight(ScreenSizeUtil.sWidth);
        } else {
            project.setWidth(ScreenSizeUtil.sWidth);
            project.setHeight(ScreenSizeUtil.sHeight);
        }
        project.setPoster(project.getTitle() + Constant.DEFAULT_COMPRESS_FORMAT);
        long id = mProjectRepository.saveData(project);
        if (id < 1) {
            mProjectsView.showErrorProjectNameExist();
            return;
        }
        if (isPosterChanged) {
            mProjectsView.savePojectPoster(project.getPoster());
        }
        project.setId(String.valueOf(id));
        mProjectsView.updateListProjects(project);
    }

    @Override
    public void cancelCreateProject() {
        mProjectsView.cancelCreateProjectDialog();
    }

    @Override
    public void cancelEditProject(Project project) {
        project.setTitle(mEditProject.getTitle());
        project.setDescription(mEditProject.getDescription());
        project.setPoster(mEditProject.getPoster());
        mProjectsView.cancelEditProjectDialog();
    }

    @Override
    public void changePoster() {
        mProjectsView.pickPoster();
    }

    @Override
    public void editProject(Project project) {
        try {
            mEditProject = (Project) project.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mProjectsView.showUpdateProjectDialog(project);
    }

    @Override
    public void updateProject(Project project, boolean isPosterChanged) {
        if (project.getTitle() == null || project.getTitle().trim().equals("")) {
            mProjectsView.showErrorEmptyProjectName();
            return;
        }
        if (isPosterChanged) {
            project.setPoster(project.getTitle() + Constant.DEFAULT_COMPRESS_FORMAT);
        }
        long check = mProjectRepository.updateData(project);
        if (check < 1) {
            mProjectsView.showErrorProjectNameExist();
            return;
        }
        if (isPosterChanged) {
            mProjectsView.savePojectPoster(project.getPoster());
        }
        mProjectsView.cancelEditProjectDialog();
    }

    public void requestDeleteProject(Project project, int position) {
        mProjectsView.showDeleteProjectDialog(project, position);
    }

    @Override
    public void removeProject(Project project, int position) {
        mProjectRepository.deleteData(project);
        mProjectsView.onProjectRemoved(position, project.getNumberMocks());
    }

    @Override
    public void openDetailProject(Project project) {
        mProjectsView.showDetailProjectUi(project);
    }

    @Override
    public void setUpCurrentUser() {
        mProjectsView.showCurrentUser();
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void openProgressDialog(Project project) {
        mProjectsView.showProgressDialog(project);
    }

    @Override
    public void getProjectToUpload(final Project project) {
        mImageNames.clear();
        mCount = 0;
        try {
            mUploadProject = (Project) project.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (mUploadProject == null) return;
        if (project.getPoster() != null) {
            mImageNames.add(project.getPoster());
            mUploadProject.setPoster(User.getCurrent().getOpenId() + project.getPoster());
        }
        if (mUploadProject.getDescription() == null) {
            mUploadProject.setDescription("");
        }
        mUploadProject.setEntryId(User.getCurrent().getOpenId() + mUploadProject.getId());
        mMockRepository.getData(project.getId(), new DataSource.GetListCallback<Mock>() {
            @Override
            public void onSuccess(List<Mock> datas) {
                for (Mock mock : datas) {
                    mImageNames.add(mock.getImage());
                    mock.setImage(User.getCurrent().getOpenId() + mock.getImage());
                    mock.setEntryId(mock.getImage());
                    mock.setProjectId(mUploadProject.getEntryId());
                    if (mock.getNote() == null) {
                        mock.setNote("");
                    }
                    getElementForProject(mock);
                }
                mUploadProject.setMocks(datas);
                uploadImageToFirebase();
            }

            @Override
            public void onError() {
                mProjectsView.showUploadStatus("Empty project!");
                mProjectsView.hideProgressDialog();
            }
        });
    }

    public void getElementForProject(final Mock mock) {
        mElementRepository.getData(mock.getId(), new DataSource.GetListCallback<Element>() {
            @Override
            public void onSuccess(List<Element> datas) {
                for (Element element : datas) {
                    if (element.getLinkTo() == null) {
                        element.setLinkTo("");
                    } else {
                        element.setLinkTo(User.getCurrent().getOpenId() + element.getLinkTo());
                    }
                    element.setMockId(mock.getEntryId());
                    element.setEntryId(mock.getEntryId() + element.getId());
                }
                mock.setElements(datas);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void uploadImageToFirebase() {
        if (mImageNames.size() == 0) {
            mProjectsView.hideProgressDialog();
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance(ApiService.FIREBASE_BUCKET);
        StorageReference storageRef = storage.getReference();
        for (String image : mImageNames) {
            if (image == null) continue;
            Uri file = Uri.fromFile(new File(Constant.FILE_PATH + image));
            String pathDes = ApiService.FIREBASE_FOLDER + User.getCurrent().getOpenId() + file
                .getLastPathSegment();
            StorageReference riversRef = storageRef.child(pathDes);
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    exception.getMessage();
                    mProjectsView.showUploadStatus(exception.getMessage());
                    mProjectsView.hideProgressDialog();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    taskSnapshot.getDownloadUrl();
                    mCount++;
                    if (mCount == mImageNames.size()) {
                        sendProjectToServer();
                    }

                }
            });
        }
    }

    private void sendProjectToServer() {
        if (mUploadProject == null) return;
        String project = new Gson().toJson(mUploadProject);
        AndroidNetworking.post(ApiService.getApi(ApiService.UPLOAD))
            .addBodyParameter(ApiService.Param.OPEN_ID, User.getCurrent().getOpenId())
            .addBodyParameter(ApiService.Param.TOKEN, User.getCurrent().getToken())
            .addBodyParameter(ApiService.Param.PROJECT, project)
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    if (response.equals(ApiService.Response.ERROR)) {
                        mProjectsView.showUploadStatus("Upload failed!");
                        mProjectsView.hideProgressDialog();
                        return;
                    }
                    mProjectsView.showUploadStatus("Upload successful!");
                    mProjectsView.hideProgressDialog();
                }

                @Override
                public void onError(ANError anError) {
                    mProjectsView.showUploadStatus("Upload failed!");
                    mProjectsView.hideProgressDialog();
                }
            });
    }

    @Override
    public void start() {
        try {
            mProjectsView.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
