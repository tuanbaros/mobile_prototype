package com.framgia.mobileprototype.library;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ActivityLibraryBinding;
import com.framgia.mobileprototype.ui.widget.AddView;
import com.framgia.mobileprototype.util.ScreenSizeUtil;

public class LibraryActivity extends BaseActivity
    implements LibraryContract.View, View.OnTouchListener {
    private static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    private static final int PATTERN_OVAL = 0;
    private static final int PATTERN_RECTANGLE = 1;
    private static final int PATTERN_TEXT = 2;
    private static final int PATTERN_IMAGE = 3;
    private static final int PATTERN_ICON = 4;
    private static final int PATTERN_WIREFRAMES = 5;
    private ActivityLibraryBinding mLibraryBinding;
    private LibraryContract.Presenter mLibraryPresenter;
    private int mActionBarHeight;
    private int mStatusBarHeight;
    private RelativeLayout mRelativeLayout;
    private Project mProject;
    private ColorPickerDialog mColorPickerDialog;
    private MenuItem mColorItem, mDeleteItem;

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
                setUpColor(color);
            }
        });
        mColorPickerDialog.hideHexaDecimalValue();
        mColorPickerDialog.hideColorComponentsInfo();
        mColorPickerDialog.setInitialColor(ColorPickerDialog.getLastColor(this));
    }

    private void setUpColor(int color) {
        AddView addView = (AddView) mRelativeLayout.getTag();
        if (addView == null) return;
        RelativeLayout relativeLayout;
        switch (addView.getType()) {
            case PATTERN_OVAL:
                relativeLayout = (RelativeLayout) addView.getTag();
                if (relativeLayout != null) {
                    GradientDrawable gradientDrawable = (GradientDrawable) relativeLayout
                            .getBackground();
                    gradientDrawable.setColor(color);
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        relativeLayout.setBackgroundDrawable(gradientDrawable);
                    } else {
                        relativeLayout.setBackground(gradientDrawable);
                    }
                }
                break;
            case PATTERN_ICON:
                break;
            case PATTERN_IMAGE:
                break;
            case PATTERN_TEXT:
                break;
            case PATTERN_RECTANGLE:
                relativeLayout = (RelativeLayout) addView.getTag();
                if (relativeLayout != null) {
                    relativeLayout.setBackgroundColor(color);
                }
                break;
            case PATTERN_WIREFRAMES:
                break;
            default:
                break;
        }
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
        mRelativeLayout.setOnTouchListener(this);
    }

    private void setUpTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.title_design);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.activity_library, menu);
        mColorItem = menu.findItem(R.id.action_color);
        mDeleteItem = menu.findItem(R.id.action_remove);
        hideOption();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void hideOption() {
        mColorItem.setVisible(false);
        mDeleteItem.setVisible(false);
    }

    @Override
    public void showOption() {
        mColorItem.setVisible(true);
        mDeleteItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_add:
                showAlertDialog();
                break;
            case R.id.action_color:
                mColorPickerDialog.show();
                break;
            case R.id.action_remove:
                removeCurrentAddViewIsFocused();
                hideOption();
                break;
            case R.id.action_save:
                break;
            default:
                break;
        }
        return true;
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_choose_pattern);
        builder.setItems(R.array.title_add_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case PATTERN_OVAL:
                        addOval();
                        break;
                    case PATTERN_ICON:
                        break;
                    case PATTERN_IMAGE:
                        break;
                    case PATTERN_TEXT:
                        break;
                    case PATTERN_RECTANGLE:
                        addRectangle();
                        break;
                    case PATTERN_WIREFRAMES:
                        break;
                    default:
                        break;
                }
                showOption();
            }
        });
        builder.create().show();
    }

    private void addOval() {
        AddView addView = (AddView) View.inflate(this, R.layout.add, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constant.MIN_SIZE,
            Constant.MIN_SIZE);
        addView.setLayoutParams(params);
        addView.setPresenter(mLibraryPresenter);
        addView.setType(PATTERN_OVAL);
        RelativeLayout relativeLayout =
            (RelativeLayout) addView.findViewById(R.id.relative_layout);
        relativeLayout.setBackgroundResource(R.drawable.oval);
        addView.setTag(relativeLayout);
        hideCurrentAddViewIsFocused();
        mRelativeLayout.addView(addView);
        mRelativeLayout.setTag(addView);
    }

    private void addRectangle() {
        AddView addView = (AddView) View.inflate(this, R.layout.add, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constant.MIN_SIZE,
            Constant.MIN_SIZE);
        addView.setLayoutParams(params);
        addView.setPresenter(mLibraryPresenter);
        addView.setType(PATTERN_RECTANGLE);
        RelativeLayout relativeLayout =
            (RelativeLayout) addView.findViewById(R.id.relative_layout);
        addView.setTag(relativeLayout);
        hideCurrentAddViewIsFocused();
        mRelativeLayout.addView(addView);
        mRelativeLayout.setTag(addView);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        hideCurrentAddViewIsFocused();
        return true;
    }

    private void hideCurrentAddViewIsFocused() {
        AddView addView = (AddView) mRelativeLayout.getTag();
        if (addView == null) return;
        addView.hideControl();
    }

    private void removeCurrentAddViewIsFocused() {
        AddView addView = (AddView) mRelativeLayout.getTag();
        if (addView == null) return;
        mRelativeLayout.removeView(addView);
    }
}
