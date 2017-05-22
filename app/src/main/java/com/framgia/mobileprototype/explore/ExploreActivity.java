package com.framgia.mobileprototype.explore;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ActivityExploreBinding;

import java.util.List;

public class ExploreActivity extends BaseActivity implements ExploreContract.View {

    private ExploreContract.Presenter mExplorePresenter;
    private ExploreViewControl mViewControl;
    private ExploreAdapter mExploreAdapter;
    private ActivityExploreBinding mExploreBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExploreBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_explore);
        mExplorePresenter = new ExplorePresenter(this);
        mViewControl = new ExploreViewControl();
        mExploreAdapter = new ExploreAdapter(mExplorePresenter);
        mExploreBinding.setControl(mViewControl);
        mExploreBinding.setPresenter(mExplorePresenter);
        mExploreBinding.setAdapter(mExploreAdapter);
        start();
    }

    @Override
    public void start() {
        prepareGetProjects();
    }

    @Override
    public void prepareGetProjects() {
        mViewControl.getIsError().set(false);
        mViewControl.getIsLoading().set(true);
        mExplorePresenter.getProjects(mExploreAdapter.getItemCount());
    }

    @Override
    public void getProjectsSuccess(List<Project> projects) {
        if (mExploreBinding.swipeRefresh.isRefreshing()) {
            mExploreAdapter.clearData();
            mExploreBinding.swipeRefresh.setRefreshing(false);
        }
        mExploreAdapter.updateData(projects);
        mViewControl.getIsLoading().set(false);
    }

    @Override
    public void getProjectsError() {
        mViewControl.getIsLoading().set(false);
        mViewControl.getIsError().set(true);
    }

    @Override
    public void emptyProjects() {
        // TODO: 5/21/17 show empty
    }
}
