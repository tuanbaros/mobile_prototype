package com.framgia.mobileprototype.library;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ActivityLibraryBinding;
import com.framgia.mobileprototype.util.ScreenSizeUtil;

public class LibraryActivity extends BaseActivity implements LibraryContract.View {
    private static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    private ActivityLibraryBinding mLibraryBinding;
    private LibraryContract.Presenter mLibraryPresenter;
    private int mActionBarHeight;
    private int mStatusBarHeight;
    private RelativeLayout mRelativeLayout;
    private Project mProject;
    private ColorPickerDialog mColorPickerDialog;

    public static Intent getLibraryIntent(Context context, Project project) {
        Intent intent = new Intent(context, LibraryActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProject = (Project) getIntent().getSerializableExtra(EXTRA_PROJECT);
        mLibraryBinding = DataBindingUtil.setContentView(this, R.layout.activity_library);
        mLibraryPresenter = new LibraryPresenter(this);
    }

    @Override
    public void start() {
        setUpTitle();
        setUpMainView();
        setUpColorPicker();
    }

    private void setUpColorPicker() {
        mColorPickerDialog = ColorPickerDialog.createColorPickerDialog(this);
        mColorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                // TODO: 5/8/17
            }
        });
        mColorPickerDialog.hideHexaDecimalValue();
        mColorPickerDialog.hideColorComponentsInfo();
        mColorPickerDialog.setInitialColor(ColorPickerDialog.getLastColor(this));
    }

    private void setUpMainView() {
        mRelativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams;
        int width, height;
        if (mProject != null && mProject.isPortrait()) {
            height = ScreenSizeUtil.sHeight - mActionBarHeight - mStatusBarHeight;
            width = height * ScreenSizeUtil.sWidth / ScreenSizeUtil.sHeight;
            layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.leftMargin = (ScreenSizeUtil.sWidth - width) / 2;
        } else {
            height = ScreenSizeUtil.sWidth - mActionBarHeight - mStatusBarHeight;
            width = height * ScreenSizeUtil.sHeight / ScreenSizeUtil.sWidth;
            layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.leftMargin = (ScreenSizeUtil.sHeight - width) / 2;
        }
        mRelativeLayout.setLayoutParams(layoutParams);
        mRelativeLayout.setBackgroundColor(Color.WHITE);
        mLibraryBinding.relativeLayout.addView(mRelativeLayout);
    }

    private void setUpTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.title_design);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mActionBarHeight > 0) return false;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            mActionBarHeight = actionBar.getHeight();
            mStatusBarHeight = getStatusBarHeight();
        }
        mLibraryPresenter.start();
        return true;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(
            Constant.STATUS_BAR_HEIGHT, Constant.DIMEN, Constant.ANDROID);
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
