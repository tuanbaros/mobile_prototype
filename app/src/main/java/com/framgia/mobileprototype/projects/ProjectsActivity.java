package com.framgia.mobileprototype.projects;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.databinding.ActivityProjectsBinding;
import com.framgia.mobileprototype.databinding.NavHeaderBinding;

public class ProjectsActivity extends AppCompatActivity {
    private ActivityProjectsBinding mProjectsBinding;
    private ObservableBoolean mIsDrawerOpen = new ObservableBoolean();
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_projects);
        mProjectsBinding.setProjectsActivity(this);
        setUpDrawerListener();
        setUpNavigationHeader();
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
}
