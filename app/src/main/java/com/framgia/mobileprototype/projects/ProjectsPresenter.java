package com.framgia.mobileprototype.projects;

import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataImport;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;

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
    public void start() {
        try {
            mProjectsView.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
