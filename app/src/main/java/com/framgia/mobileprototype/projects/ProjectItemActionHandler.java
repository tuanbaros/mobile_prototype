package com.framgia.mobileprototype.projects;

import com.framgia.mobileprototype.data.model.Project;

/**
 * Created by tuannt on 24/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projects
 */
public class ProjectItemActionHandler {
    private ProjectsContract.Presenter mListener;

    public ProjectItemActionHandler(ProjectsContract.Presenter listener) {
        mListener = listener;
    }

    public void editProject(Project project) {
        mListener.editProject(project);
    }

    public void removeProject(Project project, int position) {
        mListener.requestDeleteProject(project, position);
    }
}
