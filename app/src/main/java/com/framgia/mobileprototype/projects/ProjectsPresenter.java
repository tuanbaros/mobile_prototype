package com.framgia.mobileprototype.projects;

import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataImport;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.framgia.mobileprototype.util.ScreenSizeUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        project.setPoster(project.getTitle() + Constant.DEFAULT_COMPRESS_FORMAT);
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
    public void start() {
        try {
            mProjectsView.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
