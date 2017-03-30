package com.framgia.mobileprototype.introduction;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.databinding.ActivityIntroductionBinding;
import com.framgia.mobileprototype.projects.ProjectsActivity;

public class IntroductionActivity extends AppCompatActivity implements
    IntroductionContract.View, ViewPager.OnPageChangeListener {
    public static final String EXTRA_APP_OPENED = "EXTRA_APP_OPENED";
    private static final String PREF_NAME = "PREF_NAME";
    private static final int DOT_COUNT = 4;
    private static final int MARGIN_TOP = 0;
    private static final int MARGIN_BOTTOM = 0;
    private static final int MARGIN_LEFT = 10;
    private static final int MARGIN_RIGHT = 10;
    private ActivityIntroductionBinding mIntroductionBinding;
    private IntroductionContract.Presenter mIntroductionPresenter;
    private ObservableBoolean mCanStart = new ObservableBoolean();
    private ImageView[] mDots;
    private ObservableField<ViewPagerAdapter> mViewPagerAdapter = new ObservableField<>();
    private boolean mIsFirstOpenApp;
    private boolean mIsStartFromMain;

    public static Intent getIntroductionIntent(Context context, boolean isStartFromMain) {
        Intent intent = new Intent(context, IntroductionActivity.class);
        intent.putExtra(EXTRA_APP_OPENED, isStartFromMain);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntroductionBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_introduction);
        mIntroductionPresenter = new IntroductionPresenter(this);
        mIntroductionBinding.setIntroductionActivity(this);
        mIntroductionBinding.setPresenter(mIntroductionPresenter);
        mIntroductionBinding.setListener(this);
        mIsStartFromMain = getIntent().getBooleanExtra(EXTRA_APP_OPENED, false);
        if (!mIsStartFromMain) {
            mIntroductionPresenter.checkFirstOpenApp(getSharedPreferences(PREF_NAME, MODE_PRIVATE));
        } else {
            setUpView();
        }
    }

    private void setUpView() {
        setUpActionBar();
        setUpIncubator();
        setUpViewPager();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }

    private void setUpIncubator() {
        mDots = new ImageView[DOT_COUNT];
        for (int i = 0; i < DOT_COUNT; i++) {
            mDots[i] = new ImageView(this);
            mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.dot_normal));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);
            mIntroductionBinding.linearDot.addView(mDots[i], params);
        }
        mDots[0].setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));
    }

    private void setUpViewPager() {
        mViewPagerAdapter.set(new ViewPagerAdapter(this));
    }

    @Override
    public void showProjectsScreenUi() {
        if (!mIsStartFromMain) {
            startActivity(ProjectsActivity.getProjectsIntent(this, mIsFirstOpenApp));
        }
        finish();
    }

    @Override
    public void start() {
        mIsFirstOpenApp = true;
        setUpView();
    }

    public ObservableBoolean getCanStart() {
        return mCanStart;
    }

    public ObservableField<ViewPagerAdapter> getViewPagerAdapter() {
        return mViewPagerAdapter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < DOT_COUNT; i++) {
            mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.dot_normal));
        }
        mDots[position].setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));
        if (position + 1 == DOT_COUNT) mCanStart.set(true);
        else mCanStart.set(false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
