package com.framgia.mobileprototype.about;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.title_about);
    }
}
