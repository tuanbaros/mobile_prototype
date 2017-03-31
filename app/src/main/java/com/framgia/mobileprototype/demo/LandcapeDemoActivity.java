package com.framgia.mobileprototype.demo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.element.ElementLocalDataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.databinding.ActivityLandcapeDemoBinding;
import com.framgia.mobileprototype.ui.widget.DemoView;
import com.framgia.mobileprototype.util.ScreenSizeUtil;

import java.util.List;

public class LandcapeDemoActivity extends AppCompatActivity
    implements DemoContract.View, View.OnTouchListener {
    public static final String EXTRA_MOCK_ENTRY_ID = "EXTRA_MOCK_ENTRY_ID";
    public static final String EXTRA_TRANSITION = "EXTRA_TRANSITION";
    protected ObservableField<Mock> mMock = new ObservableField<>();
    protected ActivityLandcapeDemoBinding mDemoBinding;
    protected DemoContract.Presenter mDemoPresenter;
    protected ObservableBoolean mIsLoading = new ObservableBoolean();
    protected String mMockEntryId;

    public static Intent getDemoIntent(Context context, String mockEntryId) {
        Intent intent = new Intent(context, LandcapeDemoActivity.class);
        intent.putExtra(EXTRA_MOCK_ENTRY_ID, mockEntryId);
        return intent;
    }

    public static Intent getDemoIntent(Context context, String mockEntryId, String anim) {
        Intent intent = new Intent(context, LandcapeDemoActivity.class);
        intent.putExtra(EXTRA_MOCK_ENTRY_ID, mockEntryId);
        intent.putExtra(EXTRA_TRANSITION, anim);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDemoBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_landcape_demo);
        mDemoBinding.setActivity(this);
        mDemoPresenter = new DemoPresenter(this,
            ElementRepository.getInstance(ElementLocalDataSource.getInstance(this)),
            MockRepository.getInstance(MockLocalDataSource.getInstance(this)));
        start();
    }

    public ObservableField<Mock> getMock() {
        return mMock;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    @Override
    public void start() {
        getIntentData();
        if (!TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_TRANSITION))) {
            setAnimation(getIntent().getStringExtra(EXTRA_TRANSITION));
        }
        mDemoPresenter.getMock(mMockEntryId);
    }

    protected void getIntentData() {
        mMockEntryId = getIntent().getStringExtra(EXTRA_MOCK_ENTRY_ID);
    }

    @Override
    public void onElementLoaded(List<Element> elements) {
        RelativeLayout relativeLayout = (RelativeLayout) mDemoBinding.getRoot();
        for (Element element : elements) {
            if (TextUtils.isEmpty(element.getLinkTo())) continue;
            DemoView view = new DemoView(this, element.getGesture(), mDemoPresenter);
            int width = (int) (element.getWidth() * ScreenSizeUtil.sScaleWidth);
            int height = (int) (element.getHeight() * ScreenSizeUtil.sScaleHeight);
            int paddingSize = (int) getResources().getDimension(R.dimen.dp_8);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                width - 2 * paddingSize,
                height - 2 * paddingSize);
            params.leftMargin = (int) (element.getX() * ScreenSizeUtil.sScaleWidth) + paddingSize;
            params.topMargin = (int) (element.getY() * ScreenSizeUtil.sScaleHeight) + paddingSize;
            view.setLayoutParams(params);
            view.setTag(R.string.title_link_to, element.getLinkTo());
            view.setTag(R.string.title_element, element);
            relativeLayout.addView(view);
        }
        relativeLayout.setOnTouchListener(this);
        mIsLoading.set(false);
    }

    @Override
    public void onElementError() {
        mIsLoading.set(false);
    }

    @Override
    public void onMockLoaded(Mock mock) {
        mMock.set(mock);
        mDemoPresenter.getElements(mock.getId());
    }

    @Override
    public void onMockError() {
        mIsLoading.set(false);
    }

    @Override
    public void showNextScreen(String linkTo, String anim) {
        clearAllElement();
        startActivity(LandcapeDemoActivity.getDemoIntent(this, linkTo, anim));
        finish();
    }

    protected void showHightlight() {
        RelativeLayout relativeLayout = (RelativeLayout) mDemoBinding.getRoot();
        for (int i = 1; i < relativeLayout.getChildCount(); i++) {
            View view = relativeLayout.getChildAt(i);
            view.setBackgroundResource(R.drawable.link_to_selector);
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideHightlight();
                    }
                });
            }
        };
        thread.start();
    }

    protected void hideHightlight() {
        RelativeLayout relativeLayout = (RelativeLayout) mDemoBinding.getRoot();
        for (int i = 1; i < relativeLayout.getChildCount(); i++) {
            View view = relativeLayout.getChildAt(i);
            view.setBackgroundDrawable(null);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            showHightlight();
        }
        return false;
    }

    protected void clearAllElement() {
        RelativeLayout relativeLayout = (RelativeLayout) mDemoBinding.getRoot();
        relativeLayout.removeViews(1, relativeLayout.getChildCount() - 1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mMockEntryId = intent.getStringExtra(EXTRA_MOCK_ENTRY_ID);
        setAnimation(intent.getStringExtra(EXTRA_TRANSITION));
        mDemoPresenter.getMock(mMockEntryId);
    }

    protected void setAnimation(String anim) {
        Resources resources = getResources();
        if (resources.getString(R.string.title_transition_default).equals(anim)) {
            return;
        }
        if (resources.getString(R.string.title_transition_fade).equals(anim)) {
            overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_top).equals(anim)) {
            overridePendingTransition(R.anim.slide_top, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_left).equals(anim)) {
            overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_bottom).equals(anim)) {
            overridePendingTransition(R.anim.slide_bottom, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_right).equals(anim)) {
            overridePendingTransition(R.anim.slide_right, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_push_up).equals(anim)) {
            overridePendingTransition(R.anim.slide_top, R.anim.slide_out_bottom);
            return;
        }
        if (resources.getString(R.string.title_transition_push_down).equals(anim)) {
            overridePendingTransition(R.anim.slide_bottom, R.anim.slide_out_top);
            return;
        }
        if (resources.getString(R.string.title_transition_push_right).equals(anim)) {
            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
            return;
        }
        if (resources.getString(R.string.title_transition_push_left).equals(anim)) {
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
        }
    }
}
