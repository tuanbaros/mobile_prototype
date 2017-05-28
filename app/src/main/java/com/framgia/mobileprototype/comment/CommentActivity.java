package com.framgia.mobileprototype.comment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Comment;
import com.framgia.mobileprototype.databinding.ActivityCommentBinding;


import java.util.List;

public class CommentActivity extends BaseActivity implements CommentContract.View {
    private static final String EXTRA_PROJECT_NAME = "EXTRA_PROJECT_NAME";
    private static final String EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID";
    private ActivityCommentBinding mCommentBinding;
    private CommentContract.Presenter mCommentPresenter;
    private CommentControl mCommentControl;
    private CommentAdapter mCommentAdapter;
    private String mProjectName;
    private String mProjectId;
    private int mVisibleThreshold = 5;
    private int mLastVisibleItem, mTotalItemCount;

    public static Intent getCommentInstance(Context context, String projectName, String projectId) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(EXTRA_PROJECT_NAME, projectName);
        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        mCommentPresenter = new CommentPresenter(this);
        mCommentControl = new CommentControl();
        mCommentAdapter = new CommentAdapter(mCommentPresenter);
        mCommentBinding.setPresenter(mCommentPresenter);
        mCommentBinding.setControl(mCommentControl);
        mCommentBinding.setAdapter(mCommentAdapter);
        start();
    }

    @Override
    public void start() {
        getIntentData();
        setUpActionBarTitle();
        prepareGetComments();
    }

    private void setUpActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(mProjectName);
    }

    private void getIntentData() {
        mProjectName = getIntent().getStringExtra(EXTRA_PROJECT_NAME);
        mProjectId = getIntent().getStringExtra(EXTRA_PROJECT_ID);
        mCommentControl.setProjectId(mProjectId);
    }

    @Override
    public void prepareGetComments() {
        mCommentControl.getIsLoading().set(true);
        mCommentControl.getIsError().set(false);
        mCommentPresenter.getComments(mProjectId, mCommentAdapter.getItemCount());
    }

    @Override
    public void getCommentsSuccess(final List<Comment> comments) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCommentBinding.swipeRefresh.isRefreshing()) {
                    mCommentAdapter.clearData();
                    mCommentBinding.swipeRefresh.setRefreshing(false);
                }
                if (comments.size() >= 10) {
                    mCommentControl.setLoadMore(false);
                    setUpLoadMore();
                }
                mCommentAdapter.removeLoadMoreView();
                mCommentAdapter.updateData(comments);
                mCommentControl.getIsLoading().set(false);
                if (comments.size() > 0) {
                    mCommentControl.getIsError().set(false);
                }
            }
        });
    }

    private void setUpLoadMore() {
        RecyclerView recyclerProject = mCommentBinding.recyclerComments;
        final LinearLayoutManager linearLayoutManager =
            (LinearLayoutManager) recyclerProject.getLayoutManager();
        recyclerProject.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!mCommentControl.isLoadMore() && mTotalItemCount <= (mLastVisibleItem +
                    mVisibleThreshold)) {
                    mCommentPresenter.getComments(mProjectId, mCommentAdapter.getItemCount());
                    mCommentAdapter.addLoadMoreView();
                    mCommentControl.setLoadMore(true);
                }
            }
        });
    }

    @Override
    public void getCommentsError() {
        if (mCommentBinding.swipeRefresh.isRefreshing()) {
            mCommentBinding.swipeRefresh.setRefreshing(false);
        }
        mCommentControl.getIsLoading().set(false);
        mCommentControl.getIsError().set(true);
        mCommentAdapter.removeLoadMoreView();
        mCommentControl.setLoadMore(false);
    }

    @Override
    public void emptyComments() {
        if (mCommentBinding.swipeRefresh.isRefreshing()) {
            mCommentBinding.swipeRefresh.setRefreshing(false);
        }
        mCommentControl.getIsLoading().set(false);
        mCommentAdapter.removeLoadMoreView();
    }
}
