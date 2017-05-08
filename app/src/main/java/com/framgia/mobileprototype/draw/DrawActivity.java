package com.framgia.mobileprototype.draw;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ActivityDrawBinding;
import com.framgia.mobileprototype.ui.widget.DrawView;
import com.framgia.mobileprototype.util.ScreenSizeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DrawActivity extends BaseActivity {
    public static final String EXTRA_DRAW = "EXTRA_DRAW";
    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    private ActivityDrawBinding mDrawBinding;
    private RelativeLayout mRelativeLayout;
    private ColorPickerDialog mColorPickerDialog;
    private Project mProject;
    private int mActionBarHeight;
    private int mStatusBarHeight;
    private boolean mIsBackgroundColor;


    public static Intent getDrawIntent(Context context, Project project) {
        Intent intent = new Intent(context, DrawActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProject = (Project) getIntent().getSerializableExtra(EXTRA_PROJECT);
        mDrawBinding = DataBindingUtil.setContentView(this, R.layout.activity_draw);
    }

    private void setUpTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.title_draw);
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
        mDrawBinding.relativeLayout.addView(mRelativeLayout);
        DrawView drawView = new DrawView(this, getCurrentColor());
        mRelativeLayout.addView(drawView);
        mRelativeLayout.setTag(getCurrentColor());
    }

    private void setUpColorPicker() {
        mColorPickerDialog = ColorPickerDialog.createColorPickerDialog(this);
        mColorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                setColor(color);
            }
        });
        mColorPickerDialog.hideHexaDecimalValue();
        mColorPickerDialog.hideColorComponentsInfo();
        mColorPickerDialog.setInitialColor(getCurrentColor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_draw, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_color:
                mIsBackgroundColor = false;
                mColorPickerDialog.show();
                break;
            case R.id.action_background_color:
                mIsBackgroundColor = true;
                mColorPickerDialog.show();
                break;
            case R.id.action_undo:
                undo();
                break;
            case R.id.action_save:
                startResult();
                break;
            default:
                break;
        }
        return true;
    }

    private void setColor(final int color) {
        if (mIsBackgroundColor) {
            addBackgroundView();
            mRelativeLayout.setBackgroundColor(color);
            return;
        }
        mRelativeLayout.setTag(color);
        DrawView drawView = new DrawView(getBaseContext(), color);
        mRelativeLayout.addView(drawView);
    }

    private void startResult() {
        Intent intent = new Intent();
        Bitmap bitmap = getDraw();
        OutputStream fOut = null;
        File file = new File(Constant.FILE_TEMP);
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            intent.putExtra(EXTRA_DRAW, file.getAbsolutePath());
            setResult(RESULT_OK, intent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    private Bitmap getDraw() {
        mRelativeLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mRelativeLayout.getDrawingCache());
        mRelativeLayout.destroyDrawingCache();
        return bitmap;
    }

    private void undo() {
        int count = mRelativeLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            int index = count - 1 - i;
            if (mRelativeLayout.getChildAt(index) instanceof DrawView) {
                DrawView drawView = (DrawView) mRelativeLayout.getChildAt(index);
                if (drawView.getTag() != null && (boolean) drawView.getTag()) {
                    mRelativeLayout.removeViewAt(index);
                    break;
                }
            } else if (mRelativeLayout.getChildAt(index) instanceof TextView) {
                TextView view = (TextView) mRelativeLayout.getChildAt(index);
                mRelativeLayout.setBackgroundColor((Integer) view.getTag());
                mRelativeLayout.removeViewAt(index);
                break;
            }
        }
        if (mRelativeLayout.getChildCount() == 0) {
            mRelativeLayout.addView(new DrawView(this, (int) mRelativeLayout.getTag()));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mActionBarHeight > 0) return false;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            mActionBarHeight = actionBar.getHeight();
            mStatusBarHeight = getStatusBarHeight();
        }
        setUpTitle();
        setUpMainView();
        setUpColorPicker();
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

    private void addBackgroundView() {
        TextView view = new TextView(this);
        view.setVisibility(View.GONE);
        int color = Color.WHITE;
        Drawable background = mRelativeLayout.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        view.setTag(color);
        int lastDrawViewIndex = mRelativeLayout.getChildCount() - 1;
        if (mRelativeLayout.getChildAt(lastDrawViewIndex) instanceof DrawView) {
            mRelativeLayout.removeViewAt(lastDrawViewIndex);
        }
        mRelativeLayout.addView(view);
        mRelativeLayout.addView(new DrawView(this, (Integer) mRelativeLayout.getTag()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRelativeLayout.getTag() == null) return;
        mColorPickerDialog.setLastColor((Integer) mRelativeLayout.getTag());
    }

    private int getCurrentColor() {
        return ColorPickerDialog.getLastColor(this);
    }
}
