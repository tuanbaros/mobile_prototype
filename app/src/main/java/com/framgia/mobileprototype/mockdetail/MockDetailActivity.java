package com.framgia.mobileprototype.mockdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.element.ElementLocalDataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.databinding.ActivityMockDetailBinding;
import com.framgia.mobileprototype.ui.widget.CustomRelativeLayout;
import com.framgia.mobileprototype.ui.widget.ElementView;

import java.util.List;

public class MockDetailActivity extends BaseActivity
    implements MockDetailContract.View {
    public static final String EXTRA_MOCK = "EXTRA_MOCK";
    public static final String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";
    private ActivityMockDetailBinding mMockDetailBinding;
    private MockDetailContract.Presenter mMockDetailPresenter;
    private Mock mMock;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private MenuItem mRemoveItem, mLinkToItem;
    private CustomRelativeLayout mCustomRelativeLayout;

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
        mMockDetailBinding.setPresenter(mMockDetailPresenter);
        mCustomRelativeLayout = mMockDetailBinding.relativeLayout;
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
    public void showElementOption() {
        mLinkToItem.setVisible(true);
        mRemoveItem.setVisible(true);
    }

    @Override
    public void hideElementOption() {
        mLinkToItem.setVisible(false);
        mRemoveItem.setVisible(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_mock_detail, menu);
        mLinkToItem = menu.findItem(R.id.action_link);
        mRemoveItem = menu.findItem(R.id.action_remove);
        hideElementOption();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_remove:
                mCustomRelativeLayout.removeView((View) mCustomRelativeLayout.getTag());
                break;
            case R.id.action_link:
                ((ElementView) mCustomRelativeLayout.getTag()).setLinkTo("");
                break;
            default:
                break;
        }
        hideElementOption();
        return super.onOptionsItemSelected(menuItem);
    }
}
