package com.framgia.mobileprototype;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.facebook.FacebookSdk;

import com.framgia.mobileprototype.data.remote.ApiService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by tuannt on 5/1/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(ApiService.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(ApiService.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(ApiService.TIME_OUT, TimeUnit.SECONDS)
            .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
    }
}
