package com.framgia.mobileprototype.projects;

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
}
