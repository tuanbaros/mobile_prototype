package com.framgia.mobileprototype.login;

import com.facebook.CallbackManager;
import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.User;

/**
 * Created by tuannt on 5/1/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.login
 */
public interface LoginContract {
    interface View extends BaseView {
        void loginSuccess(User user);
        void loginError();
        void showRegisterUi();
        void loginFacebookUi();
        void loginGoogleUi();
        void onPrepareLogin();
        void onPostLogin();
    }

    interface Presenter extends BasePresenter {
        void loginFacebook();
        void loginGoogle();
        void login(User user);
        void createAnAccount();
        CallbackManager getCallbackManager();
    }
}
