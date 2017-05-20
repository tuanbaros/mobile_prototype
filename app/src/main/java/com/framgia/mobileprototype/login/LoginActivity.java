package com.framgia.mobileprototype.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.login.LoginManager;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.User;
import com.framgia.mobileprototype.databinding.ActivityLoginBinding;
import com.framgia.mobileprototype.projects.ProjectEvent;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.UUID;

public class LoginActivity extends BaseActivity implements LoginContract.View, GoogleApiClient.OnConnectionFailedListener {
    private ActivityLoginBinding mLoginBinding;
    private LoginContract.Presenter mLoginPresenter;
    public ObservableBoolean mLoading = new ObservableBoolean();
    private GoogleAuthHelper mGoogleAuthHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mLoginPresenter = new LoginPresenter(this);
        mLoginBinding.setPresenter(mLoginPresenter);
        mLoginBinding.setUser(new User());
        mLoginPresenter.start();
    }

    @Override
    public void start() {
        mGoogleAuthHelper = new GoogleAuthHelper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoogleAuthHelper.RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = mGoogleAuthHelper.getSignInResult(data);
            handleSignInResult(result);
            return;
        }
        mLoginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            User user = new User();
            if (acct != null) {
                user.setName(acct.getDisplayName());
                user.setEmail(acct.getEmail());
                user.setOpenId(acct.getId());
                user.setAvatar(acct.getPhotoUrl() == null ? null : acct.getPhotoUrl().toString());
                user.setToken(RandomHelper.randomString(100));
                mLoginPresenter.login(user);
            }
            return;
        }
        loginError();
    }

    @Override
    public void loginSuccess(User user) {
        user.save(this);
        ProjectEvent projectEvent = new ProjectEvent();
        projectEvent.setUser(user);
        EventBus.getDefault().post(projectEvent);
        finish();
    }

    @Override
    public void loginError() {
        mGoogleAuthHelper.logout();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void showRegisterUi() {
    }

    @Override
    public void loginFacebookUi() {
        LoginManager.getInstance().logInWithReadPermissions(this,
            Collections.singletonList("public_profile"));
    }

    @Override
    public void loginGoogleUi() {
        mGoogleAuthHelper.login();
    }

    @Override
    public void onPrepareLogin() {
        mLoading.set(true);
    }

    @Override
    public void onPostLogin() {
        mLoading.set(false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loginError();
    }
}
