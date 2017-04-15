package com.framgia.mobileprototype.linkto;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.databinding.ActivityLinkToBinding;

import java.util.List;

public class LinkToActivity extends BaseActivity implements LinkToContract.View {
    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    public static final String EXTRA_ELEMENT = "EXTRA_ELEMENT";
    private ActivityLinkToBinding mLinkToBinding;
    private LinkToContract.Presenter mLinkToPresenter;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private Project mProject;
    private Element mElement;
    private ObservableField<LinkToAdapter> mLinkToAdapter = new ObservableField<>();
    private ObservableField<TransitionAdapter> mTransitionAdapter = new ObservableField<>();

    public static Intent getLinkToIntent(Context context, Project project, Element element) {
        Intent intent = new Intent(context, LinkToActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        intent.putExtra(EXTRA_ELEMENT, element);
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
        mElement = (Element) getIntent().getSerializableExtra(EXTRA_ELEMENT);
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    @Override
    public void mockLoaded(List<Mock> mocks) {
        mIsLoading.set(false);
        mLinkToAdapter.set(new LinkToAdapter(this, mocks, mElement, mLinkToPresenter));
        mTransitionAdapter.set(new TransitionAdapter(this, mElement, mLinkToPresenter));
    }

    @Override
    public void mockNotAvailable() {
        mIsLoading.set(false);
        mLinkToAdapter.set(new LinkToAdapter(this, null, mElement, mLinkToPresenter));
    }

    @Override
    public void saveLinkTo() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ELEMENT, mElement);
        setResult(RESULT_OK, intent);
        finish();
    }

    public ObservableField<LinkToAdapter> getLinkToAdapter() {
        return mLinkToAdapter;
    }

    public ObservableField<TransitionAdapter> getTransitionAdapter() {
        return mTransitionAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_link_to, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_checkmark) saveLinkTo();
        return super.onOptionsItemSelected(menuItem);
    }
}
