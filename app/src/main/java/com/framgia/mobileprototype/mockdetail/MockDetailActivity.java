package com.framgia.mobileprototype.mockdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.element.ElementLocalDataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.databinding.ActivityMockDetailBinding;

import java.util.List;

public class MockDetailActivity extends BaseActivity implements MockDetailContract.View {
    public static final String EXTRA_MOCK = "EXTRA_MOCK";
    public static final String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";
    private ActivityMockDetailBinding mMockDetailBinding;
    private MockDetailContract.Presenter mMockDetailPresenter;
    private Mock mMock;
    private ObservableBoolean mIsLoading = new ObservableBoolean();

    public static Intent getMockDetailIntent(Context context, Mock mock, String orientation) {
        Intent intent = new Intent(context, MockDetailActivity.class);
        intent.putExtra(EXTRA_MOCK, mock);
        intent.putExtra(EXTRA_ORIENTATION, orientation);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMockDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_mock_detail);
        mMockDetailPresenter = new MockDetailPresenter(this,
            ElementRepository.getInstance(ElementLocalDataSource.getInstance(this)));
        mMockDetailBinding.setActivity(this);
        start();
    }

    @Override
    public void onPrepare() {
        mIsLoading.set(true);
        mMockDetailPresenter.getElements(mMock.getId());
    }

    @Override
    public void elementsLoaded(List<Element> elements) {
        mMock.setElements(elements);
        setUpElement(elements);
        mIsLoading.set(false);
    }

    private void setUpElement(List<Element> elements) {
        // TODO: 07/03/2017 show element in mock layout 
    }

    @Override
    public void elementsNotAvailable() {
        mIsLoading.set(false);
    }

    @Override
    public void start() {
        getIntentData();
        setUpTitle();
        setUpView();
        mMockDetailPresenter.start();
    }

    private void setUpTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && mMock.getTitle() != null) {
            actionBar.setTitle(mMock.getTitle());
        }
    }

    private void getIntentData() {
        mMock = (Mock) getIntent().getSerializableExtra(EXTRA_MOCK);
    }

    private void setUpView() {
        mMockDetailBinding.setMock(mMock);
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }
}
