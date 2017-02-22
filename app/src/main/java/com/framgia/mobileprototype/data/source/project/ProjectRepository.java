package com.framgia.mobileprototype.data.source.project;

import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataSource;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source
 */
public class ProjectRepository implements DataSource<Project> {
    private static ProjectRepository sProjectRepository;
    private ProjectLocalDataSource mProjectLocalDataSource;

    private ProjectRepository(ProjectLocalDataSource projectLocalDataSource) {
        mProjectLocalDataSource = projectLocalDataSource;
    }

    public static ProjectRepository getInstance(ProjectLocalDataSource projectLocalDataSource) {
        if (sProjectRepository == null) {
            sProjectRepository = new ProjectRepository(projectLocalDataSource);
        }
        return sProjectRepository;
    }

    @Override
    public void getDatas(@NonNull GetListCallback getListCallback) {
        mProjectLocalDataSource.getDatas(getListCallback);
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        mProjectLocalDataSource.getData(dataId, getListCallback);
    }

    @Override
    public void saveData(Project data) {
        mProjectLocalDataSource.saveData(data);
    }

    @Override
    public void updateData(Project data) {
        mProjectLocalDataSource.updateData(data);
    }

    @Override
    public void deleteData(Project data) {
        mProjectLocalDataSource.deleteData(data);
    }
}
