package com.framgia.mobileprototype.introduction;

import android.content.SharedPreferences;

/**
 * Created by tuannt on 20/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.introduction
 */
public class IntroductionPresenter implements IntroductionContract.Presenter {
    private static final String PREF_FIRST_OPEN_APP = "PREF_FIRST_OPEN_APP";
    private IntroductionContract.View mIntroductionView;

    public IntroductionPresenter(IntroductionContract.View introductionView) {
        mIntroductionView = introductionView;
    }

    @Override
    public void openProjectsScreen() {
        mIntroductionView.showProjectsScreenUi();
    }

    @Override
    public void checkFirstOpenApp(SharedPreferences sharedPreferences) {
        if (sharedPreferences.getBoolean(PREF_FIRST_OPEN_APP, true)) {
            start();
            sharedPreferences.edit().putBoolean(PREF_FIRST_OPEN_APP, false).apply();
        } else {
            openProjectsScreen();
        }
    }

    @Override
    public void start() {
        mIntroductionView.start();
    }
}
