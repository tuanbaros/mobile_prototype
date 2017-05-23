package com.framgia.mobileprototype.explore;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private int mVisibleThreshold = 5;
    private int mLastVisibleItem, mTotalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExploreBinding = DataBindingUtil.setContentView(this, R.layout.activity_explore);
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

    private void setUpLoadMore() {
        RecyclerView recyclerProject = mExploreBinding.recyclerProject;
        final LinearLayoutManager linearLayoutManager =
                (LinearLayoutManager) recyclerProject.getLayoutManager();
        recyclerProject.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!mViewControl.isLoadMore() && mTotalItemCount <= (mLastVisibleItem +
                        mVisibleThreshold)) {
                    mExplorePresenter.getProjects(mExploreAdapter.getItemCount());
                    mExploreAdapter.addLoadMoreView();
                    mViewControl.setLoadMore(true);
                }
            }
        });
    }

    @Override
    public void prepareGetProjects() {
        mViewControl.getIsError().set(false);
        mViewControl.getIsLoading().set(true);
        mExplorePresenter.getProjects(mExploreAdapter.getItemCount());
    }

    @Override
    public void getProjectsSuccess(final List<Project> projects) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mExploreBinding.swipeRefresh.isRefreshing()) {
                    mExploreAdapter.clearData();
                    mExploreBinding.swipeRefresh.setRefreshing(false);
                }
                if (projects.size() >= 10) {
                    mViewControl.setLoadMore(false);
                    setUpLoadMore();
                }
                mExploreAdapter.removeLoadMoreView();
                mExploreAdapter.updateData(projects);
                mViewControl.getIsLoading().set(false);
            }
        });
    }

    @Override
    public void getProjectsError() {
        mViewControl.getIsLoading().set(false);
        mViewControl.getIsError().set(true);
        mExploreAdapter.removeLoadMoreView();
        mViewControl.setLoadMore(false);
    }

    @Override
    public void emptyProjects() {
        // TODO: 5/21/17 show empty
        mExploreAdapter.removeLoadMoreView();
    }
}
