package com.framgia.mobileprototype.projectdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.databinding.ActivityProjectDetailBinding;

import java.util.List;

public class ProjectDetailActivity extends BaseActivity implements ProjectDetailContract.View {
    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    private Project mProject;
    private ProjectDetailContract.Presenter mProjectDetailPresenter;
    private ActivityProjectDetailBinding mProjectDetailBinding;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private ObservableBoolean mIsEmptyMock = new ObservableBoolean();

    public static Intent getProjectDetailIntent(Context context, Project project) {
        Intent intent = new Intent(context, ProjectDetailActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_project_detail);
        mProjectDetailPresenter = new ProjectDetailPresenter(
            MockRepository.getInstance(MockLocalDataSource.getInstance(this)),
            this);
        mProjectDetailBinding.setActivity(this);
        mProjectDetailBinding.setPresenter(mProjectDetailPresenter);
        start();
    }

    @Override
    public void onPrepare() {
        mIsLoading.set(true);
        mProjectDetailPresenter.getMocks(mProject.getId());
    }

    @Override
    public void mocksLoaded(List<Mock> mocks) {
        // TODO: 01/03/2017 show list mock ui
    }

    @Override
    public void mocksNotAvailable() {
        mIsLoading.set(false);
        mIsEmptyMock.set(true);
    }

    @Override
    public void showDeleteMockDialog() {
        // TODO: 01/03/2017 show Delete dialog 
    }

    @Override
    public void showCreateMockDialog() {
        // TODO: 01/03/2017 show create mock dialog 
    }

    @Override
    public void start() {
        getIntentData();
        setUpTitle();
        mProjectDetailPresenter.start();
    }

    private void getIntentData() {
        mProject = (Project) getIntent().getSerializableExtra(EXTRA_PROJECT);
    }

    private void setUpTitle() {
        setSupportActionBar(mProjectDetailBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mProject.getTitle());
        }
    }

    public ObservableBoolean getIsEmptyMock() {
        return mIsEmptyMock;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }
}
