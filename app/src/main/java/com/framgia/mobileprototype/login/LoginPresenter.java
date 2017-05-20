package com.framgia.mobileprototype.login;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.framgia.mobileprototype.data.model.User;
import com.framgia.mobileprototype.data.remote.ApiService;

import org.json.JSONObject;

/**
 * Created by tuannt on 5/1/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.login
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;
    private CallbackManager mCallbackManager;

    public LoginPresenter(LoginContract.View loginView) {
        mLoginView = loginView;
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void start() {
        mLoginView.start();
    }

    @Override
    public void loginFacebook() {
        LoginManager.getInstance().registerCallback(mCallbackManager,
            new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {
                    if (Profile.getCurrentProfile() == null) {
                        ProfileTracker profileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile oldProfile,
                                                                   Profile currentProfile) {
                                setUpUser(currentProfile);
                                this.stopTracking();
                            }
                        };
                        profileTracker.startTracking();
                        return;
                    }
                    setUpUser(Profile.getCurrentProfile());
                }

                @Override
                public void onCancel() {
                    mLoginView.loginError();
                }

                @Override
                public void onError(FacebookException exception) {
                    mLoginView.loginError();
                }

                private void setUpUser(Profile profile) {
                    if (profile == null) {
                        onCancel();
                        return;
                    }
                    User user = new User();
                    user.setName(profile.getName());
                    user.setOpenId(profile.getId());
                    user.setAvatar(profile.getProfilePictureUri(512, 512).toString());
                    user.setToken(AccessToken.getCurrentAccessToken().getToken());
//                    mLoginView.loginSuccess(user);
                    login(user);
                }
            });
        mLoginView.loginFacebookUi();
    }

    @Override
    public void loginGoogle() {
        mLoginView.loginGoogleUi();
    }

    @Override
    public void login(final User user) {
        AndroidNetworking.post(ApiService.getApi(ApiService.LOGIN))
            .addBodyParameter(ApiService.Param.OPEN_ID, user.getOpenId())
            .addBodyParameter(ApiService.Param.TOKEN, user.getToken())
            .addBodyParameter(ApiService.Param.NAME, user.getName())
            .setPriority(Priority.MEDIUM)
            .doNotCacheResponse()
            .build()
            .getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    if (response.contains(ApiService.Response.SUCCESS)) {
                        mLoginView.loginSuccess(user);
                        return;
                    }
                    mLoginView.loginError();
                }

                @Override
                public void onError(ANError anError) {
                    mLoginView.loginError();
                }
            });
    }

    @Override
    public void createAnAccount() {
    }

    @Override
    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }
}
