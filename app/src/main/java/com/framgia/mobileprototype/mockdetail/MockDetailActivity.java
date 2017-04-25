package com.framgia.mobileprototype.mockdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.element.ElementLocalDataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.databinding.ActivityMockDetailBinding;
import com.framgia.mobileprototype.databinding.DialogChoiceGestureBinding;
import com.framgia.mobileprototype.linkto.LinkToActivity;
import com.framgia.mobileprototype.ui.widget.CustomRelativeLayout;
import com.framgia.mobileprototype.ui.widget.ElementView;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import java.util.ArrayList;
import java.util.List;

public class MockDetailActivity extends BaseActivity implements MockDetailContract.View {
    public static final String EXTRA_MOCK = "EXTRA_MOCK";
    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    public static final int LINKTO_REQUEST_CODE = 3;
    private ActivityMockDetailBinding mMockDetailBinding;
    protected MockDetailContract.Presenter mMockDetailPresenter;
    protected Mock mMock;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private MenuItem mRemoveItem, mLinkToItem, mGestureItem;
    protected Project mProject;
    protected CustomRelativeLayout mCustomRelativeLayout;
    private GestureAdapter mGestureAdapter;
    private Dialog mGestureDialog;

    public static Intent getMockDetailIntent(Context context, Mock mock, Project project) {
        Intent intent = new Intent(context, MockDetailActivity.class);
        intent.putExtra(EXTRA_MOCK, mock);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMockDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_mock_detail);
        mMockDetailPresenter = new MockDetailPresenter(this,
                ElementRepository.getInstance(ElementLocalDataSource.getInstance(this)));
        mMockDetailBinding.setActivity(this);
        mMockDetailBinding.setPresenter(mMockDetailPresenter);
        mCustomRelativeLayout = mMockDetailBinding.relativeLayout;
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

    protected void setUpElement(List<Element> elements) {
        int paddingSize = (int) getResources().getDimension(R.dimen.dp_8);
        float sw = (float) 1 / ScreenSizeUtil.sScaleWidth;
        float sh = (float) 1 / ScreenSizeUtil.sScaleHeight;
        for (Element element : elements) {
            ElementView elementView =
                    (ElementView) View.inflate(getBaseContext(), R.layout.element, null);
            elementView.setTag(R.string.title_element, element);
            int width = (Math.round(element.getWidth() * sw)) + 2 * paddingSize;
            int height = (Math.round(element.getHeight() * sh)) + 2 * paddingSize;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            params.leftMargin = Math.round(element.getX() * sw) - paddingSize;
            params.topMargin = Math.round(element.getY() * sh) - paddingSize;
            elementView.setLayoutParams(params);
            elementView.setPresenter(mMockDetailPresenter);
            elementView.setGesture(element.getGesture());
            if (!TextUtils.isEmpty(element.getLinkTo())) elementView.setLinkTo(element.getLinkTo());
            mCustomRelativeLayout.addView(elementView);
        }
        mCustomRelativeLayout.hideControlOfChildView();
    }

    @Override
    public void elementsNotAvailable() {
        mIsLoading.set(false);
    }

    @Override
    public void showElementOption() {
        mLinkToItem.setVisible(true);
        mRemoveItem.setVisible(true);
        mGestureItem.setVisible(true);
    }

    @Override
    public void hideElementOption() {
        mLinkToItem.setVisible(false);
        mRemoveItem.setVisible(false);
        mGestureItem.setVisible(false);
    }

    @Override
    public void getAllElementView() {
        if (mCustomRelativeLayout.getChildCount() < 2) {
            Toast.makeText(this, R.string.msg_empty_element, Toast.LENGTH_SHORT).show();
            mCustomRelativeLayout.setEnabled(true);
        } else {
            int paddingSize = (int) getResources().getDimension(R.dimen.dp_8);
            List<Element> elements = new ArrayList<>();
            for (int i = 1; i < mCustomRelativeLayout.getChildCount(); i++) {
                ElementView elementView = (ElementView) mCustomRelativeLayout.getChildAt(i);
                Element element = (Element) elementView.getTag(R.string.title_element);
                element.setMockId(mMock.getId());
                int x = Math.round((elementView.getX() + paddingSize) * ScreenSizeUtil.sScaleWidth);
                int y = Math.round((elementView.getY() + paddingSize) * ScreenSizeUtil.sScaleHeight);
                int width = Math.round((elementView.getWidth() - 2 * paddingSize)
                        * ScreenSizeUtil.sScaleWidth);
                int height = Math.round((elementView.getHeight() - 2 * paddingSize)
                        * ScreenSizeUtil.sScaleHeight);
                element.setX(x);
                element.setY(y);
                element.setWidth(width);
                element.setHeight(height);
                if (elementView.getTag() != null) {
                    element.setLinkTo((String) elementView.getTag());
                }
                elements.add(element);
            }
            mMockDetailPresenter.saveAllElement(elements);
        }
    }

    @Override
    public void onSaveElementDone() {
        mCustomRelativeLayout.setEnabled(true);
        for (int i = 0; i < mCustomRelativeLayout.getChildCount(); i++) {
            View child = mCustomRelativeLayout.getChildAt(i);
            child.setEnabled(true);
        }
        mCustomRelativeLayout.hideControlOfChildView();
    }

    @Override
    public void showElementGesture(String gesture) {
        ElementView elementView = (ElementView) mCustomRelativeLayout.getTag();
        elementView.setGesture(gesture);
    }

    @Override
    public void hideGestureDialog() {
        if (mGestureDialog != null && mGestureDialog.isShowing()) {
            mGestureDialog.cancel();
        }
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
        mProject = (Project) getIntent().getSerializableExtra(EXTRA_PROJECT);
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
        mGestureItem = menu.findItem(R.id.action_gesture);
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
                ElementView elementView = (ElementView) mCustomRelativeLayout.getTag();
                mCustomRelativeLayout.removeView(elementView);
                mMockDetailPresenter.deleteElement(
                        (Element) elementView.getTag(R.string.title_element));
                hideElementOption();
                break;
            case R.id.action_link:
                ElementView ev = (ElementView) mCustomRelativeLayout.getTag();
                Element element = (Element) ev.getTag(R.string.title_element);
                startActivityForResult(LinkToActivity.getLinkToIntent(this, mProject, element),
                        LINKTO_REQUEST_CODE);
                break;
            case R.id.action_gesture:
                showGestureDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setUpGestureDialog() {
        DialogChoiceGestureBinding dialogChoiceGestureBinding = DataBindingUtil.inflate(
                getLayoutInflater(), R.layout.dialog_choice_gesture, null, false);
        mGestureDialog = new Dialog(this);
        if (mGestureAdapter == null) {
            mGestureAdapter = new GestureAdapter(
                    this, getResources().getString(R.string.title_gesture_tap),
                    mMockDetailPresenter);
        }
        dialogChoiceGestureBinding.setGestureAdapter(mGestureAdapter);
        mGestureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGestureDialog.setContentView(dialogChoiceGestureBinding.getRoot());
    }

    protected void showGestureDialog() {
        if (mGestureDialog == null) setUpGestureDialog();
        ElementView elementView = (ElementView) mCustomRelativeLayout.getTag();
        Element element = (Element) elementView.getTag(R.string.title_element);
        mGestureAdapter.updateStringGesture(element.getGesture());
        mGestureDialog.show();
    }

    private void saveElement() {
        mCustomRelativeLayout.setEnabled(false);
        for (int i = 0; i < mCustomRelativeLayout.getChildCount(); i++) {
            View child = mCustomRelativeLayout.getChildAt(i);
            child.setEnabled(false);
        }
        mMockDetailPresenter.getAllElementInMock();
        hideElementOption();
    }

    @Override
    public void onBackPressed() {
        saveElement();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LINKTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Element extra = (Element) data.getSerializableExtra(LinkToActivity.EXTRA_ELEMENT);
            ElementView elementView = (ElementView) mCustomRelativeLayout.getTag();
            elementView.setLinkTo(extra.getLinkTo());
            Element element = (Element) elementView.getTag(R.string.title_element);
            element.setTransition(extra.getTransition());
            mMockDetailPresenter.saveElement(element);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Project getProject() {
        return mProject;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (ScreenSizeUtil.sScaleWidth == 0 || ScreenSizeUtil.sScaleHeight == 0) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                int actionBarHeight = actionBar.getHeight();
                int statusBarHeight = getStatusBarHeight();
                int paddingDistance = 2 * (int) getResources().getDimension(R.dimen.dp_16);
                int childWidth = ScreenSizeUtil.sWidth - paddingDistance;
                int childHeight = ScreenSizeUtil.sHeight
                        - actionBarHeight
                        - statusBarHeight
                        - paddingDistance;
                ScreenSizeUtil.sScaleWidth = (float) ScreenSizeUtil.sWidth / childWidth;
                ScreenSizeUtil.sScaleHeight = (float) ScreenSizeUtil.sHeight / childHeight;
            }
        }
        start();
        return super.onPrepareOptionsMenu(menu);
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(Constant.STATUS_BAR_HEIGHT, Constant.DIMEN,
                Constant.ANDROID);
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
