package com.framgia.mobileprototype.explore;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.remote.ApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by tuannt on 5/21/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.explore
 */
public class ExplorePresenter implements ExploreContract.Presenter {

    private ExploreContract.View mExploreView;

    public ExplorePresenter(ExploreContract.View exploreView) {
        mExploreView = exploreView;
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
}
