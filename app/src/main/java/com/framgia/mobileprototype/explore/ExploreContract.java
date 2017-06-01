package com.framgia.mobileprototype.explore;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Project;

import java.util.List;

/**
 * Created by tuannt on 5/21/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.explore
 */
public interface ExploreContract {
    interface View extends BaseView {
        void prepareGetProjects();
        void getProjectsSuccess(List<Project> projects);
        void getProjectsError();
        void emptyProjects();
        void prepareDownloadProject();
        void downloadProjectSuccess(Project project);
        void downloadProjectError();
        void projectTitleDuplicate(Project project);
        void openCommentUi(Project project);
        void openDemoProjectUi(Project project);
        void openShareLinkProjectUi(String projectLink);
    }

    interface Presenter extends BasePresenter {
        void getProjects(int offset);
        void prepare();
        void refresh();
        void downloadProject(Project project);
        boolean checkValidProjectTitle(String title);
        void importProject(Project project);
        void showComment(Project project);
        void showDemoProject(Project project);
        void shareProjectLink(Project project);
    }
}
