package com.framgia.mobileprototype.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.mobileprototype.BR;
import com.framgia.mobileprototype.R;

import java.io.Serializable;

/**
 * Created by tuannt on 5/1/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.model
 */
public class User extends BaseObservable implements Serializable {
    private String mEmail;
    private String mPassword;
    private String mOpenId;
    private String mName;
    private String mAvatar;
    private String mToken;
    private static User sUser;
    private final String KEY_EMAIL = "KEY_EMAIL";
    private final String KEY_NAME = "KEY_NAME";
    private final String KEY_OPENID = "KEY_OPENID";
    private final String KEY_AVATAR = "KEY_AVATAR";
    private final String KEY_TOKEN = "KEY_TOKEN";

    public static User getCurrent() {
        return sUser;
    }

    public static void setCurrent(User user) {
        sUser = user;
    }

    @Bindable
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        notifyPropertyChanged(BR.password);
    }

    public String getOpenId() {
        return mOpenId;
    }

    public void setOpenId(String openId) {
        mOpenId = openId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public void save(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_NAME, getName());
        editor.putString(KEY_EMAIL, getEmail());
        editor.putString(KEY_OPENID, getOpenId());
        editor.putString(KEY_AVATAR, getAvatar());
        editor.putString(KEY_TOKEN, getToken());
        editor.apply();
    }

    public void fetch(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setName(sharedPref.getString(KEY_NAME, null));
        setEmail(sharedPref.getString(KEY_EMAIL, null));
        setOpenId(sharedPref.getString(KEY_OPENID, null));
        setAvatar(sharedPref.getString(KEY_AVATAR, null));
        setToken(sharedPref.getString(KEY_TOKEN, null));
    }
}
