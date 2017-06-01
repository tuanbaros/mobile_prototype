package com.framgia.mobileprototype.comment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Comment;
import com.framgia.mobileprototype.databinding.ActivityCommentBinding;
import com.framgia.mobileprototype.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;

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
    private int mVisibleThreshold = 1;
    //    private int mLastVisibleItem, mTotalItemCount;
    private int mPastVisiblesItems, mTotalItemCount, mVisibleItemCount;

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
                } else {
                    mCommentControl.setLoadMore(true);
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
//                mTotalItemCount = linearLayoutManager.getItemCount();
                mVisibleItemCount = linearLayoutManager.getChildCount();
                mPastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                mTotalItemCount = linearLayoutManager.getItemCount();
                if (!mCommentControl.isLoadMore() &&
                    (mVisibleItemCount + mPastVisiblesItems) >= mTotalItemCount) {
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

    @Override
    public void showDialogRequestLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_login);
        builder.setMessage(R.string.text_do_you_want_login_to_comment);
        builder.setNegativeButton(R.string.action_cancel_project, null);
        builder.setPositiveButton(R.string.action_ok,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                }
            });
        builder.create().show();
    }

    @Override
    public void commentSuccess(List<Comment> comments) {
        mCommentBinding.textNothing.setVisibility(View.GONE);
        mCommentBinding.editCommentContent.setText("");
        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mCommentBinding.editCommentContent.getWindowToken(), 0);
        mCommentAdapter.updateNewComment(comments);
        mCommentBinding.recyclerComments.smoothScrollToPosition(0);
        EventBus.getDefault().post(comments.size());
    }

    @Override
    public void commentError() {
        Toast.makeText(this, R.string.text_comment_error, Toast.LENGTH_SHORT).show();
    }
}
