package com.framgia.mobileprototype.linkto;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.databinding.ActivityLinkToBinding;

import java.util.List;

public class LinkToActivity extends BaseActivity implements LinkToContract.View {
    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    public static final String EXTRA_MOCK_ENTRYID = "EXTRA_MOCK_ENTRYID";
    private ActivityLinkToBinding mLinkToBinding;
    private LinkToContract.Presenter mLinkToPresenter;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private Project mProject;
    private ObservableField<LinkToAdapter> mLinkToAdapter = new ObservableField<>();

    public static Intent getLinkToIntent(Context context, Project project) {
        Intent intent = new Intent(context, LinkToActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLinkToBinding = DataBindingUtil.setContentView(this, R.layout.activity_link_to);
        mLinkToPresenter = new LinkToPresenter(this,
            MockRepository.getInstance(MockLocalDataSource.getInstance(this)));
        mLinkToBinding.setActivity(this);
        mLinkToBinding.setPresenter(mLinkToPresenter);
        start();
    }

    @Override
    public void start() {
        getIntentData();
        setUpTitle();
        mLinkToPresenter.getMocks(mProject.getId());
    }

    private void setUpTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) setSupportActionBar(mLinkToBinding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mProject.getTitle());
    }

    private void getIntentData() {
        mProject = (Project) getIntent().getSerializableExtra(EXTRA_PROJECT);
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    @Override
    public void mockLoaded(List<Mock> mocks) {
        mIsLoading.set(false);
        mLinkToAdapter.set(new LinkToAdapter(this, mocks, mLinkToPresenter));
    }

    @Override
    public void mockNotAvailable() {
        mIsLoading.set(false);
        mLinkToAdapter.set(new LinkToAdapter(this, null, mLinkToPresenter));
    }

    @Override
    public void saveLinkTo(Mock mock) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOCK_ENTRYID, mock.getEntryId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void saveLinkToBack() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOCK_ENTRYID, Constant.LINK_TO_BACK);
        setResult(RESULT_OK, intent);
        finish();
    }

    public ObservableField<LinkToAdapter> getLinkToAdapter() {
        return mLinkToAdapter;
    }
}
