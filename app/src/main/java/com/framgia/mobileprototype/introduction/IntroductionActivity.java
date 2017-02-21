package com.framgia.mobileprototype.introduction;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.databinding.ActivityIntroductionBinding;

public class IntroductionActivity extends AppCompatActivity implements IntroductionContract.View {
    private static final String PREF_NAME = "PREF_NAME";
    private static final int DOT_COUNT = 5;
    private static final int MARGIN_TOP = 0;
    private static final int MARGIN_BOTTOM = 0;
    private static final int MARGIN_LEFT = 10;
    private static final int MARGIN_RIGHT = 10;
    private ActivityIntroductionBinding mIntroductionBinding;
    private IntroductionContract.Presenter mIntroductionPresenter;
    private ObservableBoolean mCanStart = new ObservableBoolean();
    private ImageView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntroductionBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_introduction);
        mIntroductionPresenter = new IntroductionPresenter(this);
        mIntroductionBinding.setIntroductionActivity(this);
        mIntroductionBinding.setPresenter(mIntroductionPresenter);
        mIntroductionPresenter.checkFirstOpenApp(
            getSharedPreferences(PREF_NAME, MODE_PRIVATE));
    }

    private void setUpView() {
        setUpActionBar();
        setUpIncubator();
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

    @Override
    public void showProjectsScreenUi() {
        // TODO: start project activity
    }

    @Override
    public void start() {
        setUpView();
    }

    public ObservableBoolean getCanStart() {
        return mCanStart;
    }
}
