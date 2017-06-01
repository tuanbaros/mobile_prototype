package com.framgia.mobileprototype.explore;

import android.support.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.remote.ApiService;
import com.framgia.mobileprototype.data.source.DataImport;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/**
 * Created by tuannt on 5/21/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.explore
 */
public class ExplorePresenter implements ExploreContract.Presenter {

    private ExploreContract.View mExploreView;
    private ProjectRepository mProjectRepository;
    private MockRepository mMockRepository;
    private ElementRepository mElementRepository;
    private int mCount;
    private DataImport mDataImport;

    public ExplorePresenter(ExploreContract.View exploreView, ProjectRepository projectRepository,
            MockRepository mockRepository, ElementRepository elementRepository) {
        mExploreView = exploreView;
        mProjectRepository = projectRepository;
        mMockRepository = mockRepository;
        mElementRepository = elementRepository;
    }

    @Override
    public void start() {

    }

    public void getProjects(final int offset) {
        AndroidNetworking.get(ApiService.getApi(ApiService.PROJECTS))
            .addPathParameter(ApiService.Param.OFFSET, String.valueOf(offset))
            .doNotCacheResponse()
            .build()
            .getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    if (response == null) {
                        mExploreView.getProjectsError();
                        return;
                    }
                    if (response.length() == 0) {
                        mExploreView.emptyProjects();
                        return;
                    }
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Project>>(){}.getType();
                    List<Project> projects = gson.fromJson(response.toString(), listType);
                    mExploreView.getProjectsSuccess(projects);
                }

                @Override
                public void onError(ANError anError) {
                    mExploreView.getProjectsError();
                }
            });
    }

    @Override
    public void prepare() {
        mExploreView.prepareGetProjects();
    }

    @Override
    public void refresh() {
        getProjects(0);
    }

    @Override
    public void downloadProject(final Project project) {
        if (project.getMocks().size() > 0) {
            if (checkValidProjectTitle(project.getTitle())) {
                importProject(project);
            } else {
                mExploreView.projectTitleDuplicate(project);
            }
            return;
        }
        mExploreView.prepareDownloadProject();
        AndroidNetworking.post(ApiService.getApi(ApiService.DOWNLOAD))
                .addBodyParameter(ApiService.Param.PROJECT_ENTRY_ID, project.getEntryId())
                .doNotCacheResponse()
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null || response.length() == 0) {
                            mExploreView.downloadProjectError();
                            return;
                        }
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Mock>>(){}.getType();
                        List<Mock> mocks = gson.fromJson(response.toString(), listType);
                        project.setMocks(mocks);
                        if (checkValidProjectTitle(project.getTitle())) {
                            importProject(project);
                        } else {
                            mExploreView.projectTitleDuplicate(project);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mExploreView.downloadProjectError();
                    }
                });
    }

    @Override
    public boolean checkValidProjectTitle(String title) {
        return mProjectRepository.validTitle(title);
    }

    @Override
    public void importProject(Project project) {
        Gson gson = new Gson();
        mDataImport = new DataImport(mElementRepository,
                mMockRepository, mProjectRepository);
        List<Download> downloads = new ArrayList<>();
        Project p = mDataImport.changeImageName(downloads, gson.toJson(project));
        downloadImage(p, downloads);
    }

    private void downloadImage(final Project project, final List<Download> downloads) {
        mCount = 0;
        if (downloads.size() == 0) {
            mDataImport.save(project);
            mExploreView.downloadProjectSuccess(project);
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance(ApiService.FIREBASE_BUCKET);
        StorageReference baseReference = storage.getReference(ApiService.FIREBASE_FOLDER);
        for (Download download : downloads) {
            StorageReference reference = baseReference.child(download.getUrl());

            File localFile = new File(Constant.FILE_PATH + download.getName());

            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask
                            .TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    mCount++;
                    if (mCount == downloads.size()) {
                        mDataImport.save(project);
                        mExploreView.downloadProjectSuccess(project);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    mExploreView.downloadProjectError();
                }
            });
        }

    }

    @Override
    public void showComment(Project project) {
        mExploreView.openCommentUi(project);
    }

    @Override
    public void showDemoProject(Project project) {
        mExploreView.openDemoProjectUi(project);
    }
}
