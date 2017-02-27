package com.framgia.mobileprototype.projects;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.element.ElementLocalDataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectLocalDataSource;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.framgia.mobileprototype.databinding.ActivityProjectsBinding;
import com.framgia.mobileprototype.databinding.NavHeaderBinding;

import java.io.IOException;
import java.util.List;

public class ProjectsActivity extends AppCompatActivity implements ProjectsContract.View {
    private static final String EXTRA_FIRST_OPEN_APP = "EXTRA_FIRST_OPEN_APP";
    private static final String SAMPLE_PROJECT_FILE_NAME = "sample_project.json";
    private ActivityProjectsBinding mProjectsBinding;
    private ObservableBoolean mIsDrawerOpen = new ObservableBoolean();
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private ObservableBoolean mIsEmptyProject = new ObservableBoolean();
    private ProjectsContract.Presenter mProjectsPresenter;
    private ObservableField<ProjectsAdapter> mProjectsAdapter = new ObservableField<>();

    public static Intent getProjectsIntent(Context context, boolean isFirstOpenApp) {
        Intent intent = new Intent(context, ProjectsActivity.class);
        intent.putExtra(EXTRA_FIRST_OPEN_APP, isFirstOpenApp);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_projects);
        mProjectsBinding.setProjectsActivity(this);
        mProjectsPresenter = new ProjectsPresenter(this,
            ProjectRepository.getInstance(ProjectLocalDataSource.getInstance(this)),
            MockRepository.getInstance(MockLocalDataSource.getInstance(this)),
            ElementRepository.getInstance(ElementLocalDataSource.getInstance(this)));
        start();
    }

    private void setUpDrawerListener() {
        mActionBarDrawerToggle =
            new ActionBarDrawerToggle(this,
                mProjectsBinding.drawer,
                mProjectsBinding.appBar.toolBar,
                R.string.msg_open_drawer,
                R.string.msg_close_drawer) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    mIsDrawerOpen.set(false);
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    mIsDrawerOpen.set(true);
                    super.onDrawerOpened(drawerView);
                }
            };
        mProjectsBinding.drawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    private void setUpNavigationHeader() {
        NavHeaderBinding navHeaderBinding = NavHeaderBinding.inflate(getLayoutInflater());
        mProjectsBinding.navView.addHeaderView(navHeaderBinding.getRoot());
    }

    public ObservableBoolean getIsDrawerOpen() {
        return mIsDrawerOpen;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public ObservableField<ProjectsAdapter> getProjectsAdapter() {
        return mProjectsAdapter;
    }

    public ObservableBoolean getIsEmptyProject() {
        return mIsEmptyProject;
    }

    @Override
    public void onBackPressed() {
        if (mIsDrawerOpen.get()) {
            mIsDrawerOpen.set(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionBarDrawerToggle.onOptionsItemSelected(item) ||
            super.onOptionsItemSelected(item);
    }

    @Override
    public void prepare() throws IOException {
        mIsLoading.set(true);
        mProjectsPresenter.saveSampleProject(
            getIntent().getBooleanExtra(EXTRA_FIRST_OPEN_APP, true),
            getAssets().open(SAMPLE_PROJECT_FILE_NAME));
    }

    @Override
    public void projectsLoaded(List<Project> projects) {
        mIsLoading.set(false);
        mProjectsAdapter.set(new ProjectsAdapter(this, projects, mProjectsPresenter));
    }

    @Override
    public void projectsNotAvailable() {
        mIsLoading.set(false);
        mIsEmptyProject.set(true);
    }

    @Override
    public void start() {
        setUpDrawerListener();
        setUpNavigationHeader();
        mProjectsPresenter.start();
    }
}
