package com.framgia.mobileprototype;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.facebook.FacebookSdk;

/**
 * Created by tuannt on 5/1/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
