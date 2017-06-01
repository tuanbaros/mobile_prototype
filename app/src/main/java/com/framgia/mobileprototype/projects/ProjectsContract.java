package com.framgia.mobileprototype.projects;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by tuannt on 23/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projects
 */
public interface ProjectsContract {
    interface View extends BaseView {
        void prepare() throws IOException;
        void projectsLoaded(List<Project> projects);
        void projectsNotAvailable();
        void showCreateProjectDialog(Project project);
        void cancelCreateProjectDialog();
        void cancelEditProjectDialog();
        void showErrorEmptyProjectName();
        void showErrorProjectNameExist();
        void updateListProjects(Project project);
        void savePojectPoster(String fileName);
        void pickPoster();
        void showUpdateProjectDialog(Project project);
        void showDeleteProjectDialog(Project project, int position);
        void onProjectRemoved(int position, int numberMockRemoved);
        void showDetailProjectUi(Project project);
        void showCurrentUser();
        void showProgressDialog(Project project);
        void hideProgressDialog();
        void showUploadStatus(int id);
        void showUploadStatus(String s);
        void showDialogAskShare(String projectName, String projectLink);
    }

    interface Presenter extends BasePresenter {
        void saveSampleProject(boolean isFirstApp, InputStream inputStream);
        void createAppStorageFolder(String path);
        void getProjects();
        void createProject();
        void storeProject(Project project, boolean isPosterChanged);
        void cancelCreateProject();
        void cancelEditProject(Project project);
        void changePoster();
        void editProject(Project project);
        void updateProject(Project project, boolean isPosterChanged);
        void requestDeleteProject(Project project, int position);
        void removeProject(Project project, int position);
        void openDetailProject(Project project);
        void setUpCurrentUser();
        void logout();
        void openProgressDialog(Project project);
        void getProjectToUpload(Project project);
    }
}
