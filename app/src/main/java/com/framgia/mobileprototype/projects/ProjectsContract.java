package com.framgia.mobileprototype.projects;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Project;

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
    }

    interface Presenter extends BasePresenter {
        void saveSampleProject(boolean isFirstApp, InputStream inputStream);
        void getProjects();
    }
}
