package com.framgia.mobileprototype.introduction;

import android.content.SharedPreferences;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;

/**
 * Created by tuannt on 20/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.introduction
 */
public interface IntroductionContract {
    interface View extends BaseView {
        void showProjectsScreenUi();
    }

    interface Presenter extends BasePresenter {
        void openProjectsScreen();
        void checkFirstOpenApp(SharedPreferences sharedPreferences);
    }
}
