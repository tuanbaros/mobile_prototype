package com.framgia.mobileprototype.explore;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.remote.ApiService;
import com.framgia.mobileprototype.data.source.DataImport;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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
        DataImport dataImport = new DataImport(mElementRepository,
                mMockRepository, mProjectRepository);
        Project p = dataImport.save(gson.toJson(project));
        if (p == null) {
            mExploreView.downloadProjectError();
            return;
        }
        mExploreView.downloadProjectSuccess(p);
    }
}
