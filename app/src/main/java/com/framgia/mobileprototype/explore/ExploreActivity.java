package com.framgia.mobileprototype.explore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.comment.CommentActivity;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.element.ElementLocalDataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectLocalDataSource;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.framgia.mobileprototype.databinding.ActivityExploreBinding;
import com.framgia.mobileprototype.demo.LandscapeOnlineActivity;
import com.framgia.mobileprototype.demo.OnlineDemoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ExploreActivity extends BaseActivity implements ExploreContract.View {
    private ExploreContract.Presenter mExplorePresenter;
    private ExploreViewControl mViewControl;
    private ExploreAdapter mExploreAdapter;
    private ActivityExploreBinding mExploreBinding;

    private int mVisibleThreshold = 5;
    private int mLastVisibleItem, mTotalItemCount;

    private ProgressDialog mProgressDialog;

    private Project mProject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExploreBinding = DataBindingUtil.setContentView(this, R.layout.activity_explore);
        mExplorePresenter = new ExplorePresenter(this,
                ProjectRepository.getInstance(ProjectLocalDataSource.getInstance(this)),
                MockRepository.getInstance(MockLocalDataSource.getInstance(this)),
                ElementRepository.getInstance(ElementLocalDataSource.getInstance(this)));
        mViewControl = new ExploreViewControl();
        mExploreAdapter = new ExploreAdapter(mExplorePresenter);
        mExploreBinding.setControl(mViewControl);
        mExploreBinding.setPresenter(mExplorePresenter);
        mExploreBinding.setAdapter(mExploreAdapter);
        EventBus.getDefault().register(this);
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
                } else {
                    mViewControl.setLoadMore(true);
                }
                mExploreAdapter.removeLoadMoreView();
                mExploreAdapter.updateData(projects);
                mViewControl.getIsLoading().set(false);
                if (projects.size() > 0) {
                    mViewControl.getIsError().set(false);
                }
            }
        });
    }

    @Override
    public void getProjectsError() {
        if (mExploreBinding.swipeRefresh.isRefreshing()) {
            mExploreBinding.swipeRefresh.setRefreshing(false);
        }
        mViewControl.getIsLoading().set(false);
        mViewControl.getIsError().set(true);
        mExploreAdapter.removeLoadMoreView();
        mViewControl.setLoadMore(false);
    }

    @Override
    public void emptyProjects() {
        if (mExploreBinding.swipeRefresh.isRefreshing()) {
            mExploreBinding.swipeRefresh.setRefreshing(false);
        }
        mViewControl.getIsLoading().set(false);
        mExploreAdapter.removeLoadMoreView();
    }

    private void setUpProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        String upload = getResources().getString(R.string.title_download);
        mProgressDialog.setMessage(upload);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void prepareDownloadProject() {
        if (mProgressDialog == null) {
            setUpProgressDialog();
        }
        mProgressDialog.show();
    }

    @Override
    public void downloadProjectSuccess(Project project) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        EventBus.getDefault().post(project);
        Toast.makeText(this, R.string.text_download_successful, Toast.LENGTH_LONG).show();
    }

    @Override
    public void downloadProjectError() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(this, R.string.text_download_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void projectTitleDuplicate(final Project project) {
        Toast.makeText(getBaseContext(), R.string.text_please_change_other_name,
                Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_text_pattern, null);
        dialogBuilder.setView(dialogView);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
        editText.setHint(R.string.text_new_project_name);
        dialogBuilder.setPositiveButton(R.string.action_done, null);
        dialogBuilder.setNegativeButton(R.string.action_cancel_project, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadProjectError();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String text = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(text)) return;
                        if (mExplorePresenter.checkValidProjectTitle(text)) {
                            String currentName = project.getTitle();
                            project.setTitle(text);
                            mExplorePresenter.importProject(project);
                            project.setTitle(currentName);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getBaseContext(), R.string.text_please_change_other_name,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        b.show();
    }

    @Override
    public void openCommentUi(Project project) {
        mProject = project;
        startActivity(
            CommentActivity.getCommentInstance(this, project.getTitle(), project.getEntryId()));
    }

    @Override
    public void openDemoProjectUi(Project project) {
        if (project.isPortrait()) {
            startActivity(OnlineDemoActivity.getOnlineDemoIntent(this, project));
            return;
        }
        startActivity(LandscapeOnlineActivity.getOnlineDemoIntent(this, project));

    }

    @Override
    public void openShareLinkProjectUi(String projectLink) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, projectLink);
        sendIntent.setType(Constant.SHARE_TYPE);
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string
                .text_send_to)));
    }

    @Subscribe
    public void onEvent(Integer num) {
        mProject.setNumComment(mProject.getNumComment() + num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
