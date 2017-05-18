package com.framgia.mobileprototype.library;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.bumptech.glide.Glide;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.databinding.ActivityLibraryBinding;
import com.framgia.mobileprototype.ui.widget.AddView;
import com.framgia.mobileprototype.ui.widget.AutoResizeTextView;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import com.mikepenz.iconics.view.IconicsImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class LibraryActivity extends BaseActivity
    implements LibraryContract.View, View.OnTouchListener {
    private static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    private static final int PATTERN_OVAL = 0;
    private static final int PATTERN_RECTANGLE = 1;
    private static final int PATTERN_TEXT = 2;
    private static final int PATTERN_IMAGE = 3;
    private static final int PATTERN_ICON = 4;
    private static final int PATTERN_WIREFRAMES = 5;
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final int ICON_REQUEST_CODE = 1;
    private ActivityLibraryBinding mLibraryBinding;
    private LibraryContract.Presenter mLibraryPresenter;
    private int mActionBarHeight;
    private int mStatusBarHeight;
    private RelativeLayout mRelativeLayout;
    private Project mProject;
    private ColorPickerDialog mColorPickerDialog;
    private MenuItem mColorItem, mDeleteItem, mBringToFrontItem;
    private ArrayList<String> mDeniedPermissions = new ArrayList<>();
    private boolean mIsSaving;

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
                IconicsImageView iconicsImageView = (IconicsImageView) addView.getTag();
                iconicsImageView.setColor(color);
                break;
            case PATTERN_IMAGE:
                break;
            case PATTERN_TEXT:
                AutoResizeTextView textView = (AutoResizeTextView) addView.getTag();
                textView.setTextColor(color);
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
        mBringToFrontItem = menu.findItem(R.id.action_bring_to_front);
        hideOption();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void hideOption() {
        mColorItem.setVisible(false);
        mDeleteItem.setVisible(false);
        mBringToFrontItem.setVisible(false);
    }

    @Override
    public void showOption() {
        mColorItem.setVisible(true);
        mDeleteItem.setVisible(true);
        mBringToFrontItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
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
                startCrop(getDraw());
                mIsSaving = true;
                break;
            case R.id.action_clear:
                clearView();
                break;
            case R.id.action_bring_to_front:
                bringViewToFront();
                break;
            default:
                break;
        }
        return true;
    }

    private void bringViewToFront() {
        AddView addView = (AddView) mRelativeLayout.getTag();
        addView.bringToFront();
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
                        showOption();
                        break;
                    case PATTERN_ICON:
                        startActivityForResult(new Intent(getBaseContext(), IconActivity.class),
                                ICON_REQUEST_CODE);
                        break;
                    case PATTERN_IMAGE:
                        checkPermission();
                        showOption();
                        break;
                    case PATTERN_TEXT:
                        showTextPatternDialog();
                        break;
                    case PATTERN_RECTANGLE:
                        addRectangle();
                        showOption();
                        break;
                    case PATTERN_WIREFRAMES:
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void showTextPatternDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_text_pattern, null);
        dialogBuilder.setView(dialogView);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
        dialogBuilder.setPositiveButton(R.string.action_done, new DialogInterface.OnClickListener
                () {
            public void onClick(DialogInterface dialog, int whichButton) {
                String text = editText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) return;
                addText(text);
            }
        });
        dialogBuilder.setNegativeButton(R.string.action_cancel_project, null);
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void addText(String text) {
        AddView addView = (AddView) View.inflate(this, R.layout.add, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constant.MIN_SIZE,
                Constant.MIN_SIZE);
        addView.setLayoutParams(params);
        addView.setPresenter(mLibraryPresenter);
        addView.setType(PATTERN_TEXT);
        RelativeLayout relativeLayout =
                (RelativeLayout) addView.findViewById(R.id.relative_layout);
        relativeLayout.setBackgroundColor(0);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        AutoResizeTextView autoResizeTextView = new AutoResizeTextView(this);
        autoResizeTextView.setTextA(text);
        autoResizeTextView.setLayoutParams(textParams);
        addView.setTag(autoResizeTextView);
        relativeLayout.addView(autoResizeTextView);
        hideCurrentAddViewIsFocused();
        mRelativeLayout.addView(addView);
        mRelativeLayout.setTag(addView);
    }

    private void addIcon(String icon) {
        AddView addView = (AddView) View.inflate(this, R.layout.add, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Constant.MIN_SIZE,
                Constant.MIN_SIZE);
        addView.setLayoutParams(params);
        addView.setPresenter(mLibraryPresenter);
        addView.setType(PATTERN_ICON);
        RelativeLayout relativeLayout =
                (RelativeLayout) addView.findViewById(R.id.relative_layout);
        relativeLayout.setBackgroundColor(0);
        IconicsImageView imageView = new IconicsImageView(this);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageParams);
        relativeLayout.addView(imageView);
        addView.setTag(imageView);
        imageView.setIcon(icon);
        hideCurrentAddViewIsFocused();
        mRelativeLayout.addView(addView);
        mRelativeLayout.setTag(addView);
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

    private void addImage(Uri uri) {
        AddView addView = (AddView) View.inflate(this, R.layout.add, null);
        int w = ScreenSizeUtil.sWidth / 3;
        int h = w * ScreenSizeUtil.sHeight / ScreenSizeUtil.sWidth;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        addView.setLayoutParams(params);
        addView.setPresenter(mLibraryPresenter);
        addView.setType(PATTERN_IMAGE);
        RelativeLayout relativeLayout =
                (RelativeLayout) addView.findViewById(R.id.relative_layout);
        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageParams);
        relativeLayout.addView(imageView);
        Glide.with(this).load(uri).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
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
        hideOption();
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

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pickImage();
            return;
        }
        String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                mDeniedPermissions.add(permission);
            }
        }
        if (mDeniedPermissions.size() == 0) {
            pickImage();
            return;
        }
        ActivityCompat.requestPermissions(this,
                mDeniedPermissions.toArray(new String[mDeniedPermissions.size()]),
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(
                            this, R.string.msg_grant_permission, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                    Uri imageUri = CropImage.getPickImageResultUri(this, data);
                    cropImage(imageUri);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (result.getUri() == null) return;
                    if (!mIsSaving) {
                        addImage(result.getUri());
                    } else {
                        startResult(result.getUri());
                    }
                    break;
                case ICON_REQUEST_CODE:
                    if (data == null) return;
                    String icon = data.getStringExtra(IconActivity.EXTRA_ICON);
                    addIcon(icon);
                    showOption();
                    break;
                default:
                    break;
            }
        }
        mIsSaving = false;
    }

    private void startResult(Uri uri) {
        Intent intent = new Intent();
        intent.setData(uri);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    private void cropImage(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAutoZoomEnabled(false)
                .setMinCropResultSize(100, 100)
                .start(this);
    }

    public void clearView() {
        mRelativeLayout.removeAllViews();
    }

    private void startCrop(Bitmap bitmap) {
        OutputStream fOut = null;
        File file = new File(Constant.FILE_TEMP);
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
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
        cropImage(getUriFromFile(file.getAbsolutePath()));
    }

    private Uri getUriFromFile(String filePath) {
        File file = new File(filePath);
        Uri uri = null;
        try {
            uri = Uri.parse(MediaStore.Images.Media
                    .insertImage(getContentResolver(), file.getAbsolutePath(), null, null));
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private Bitmap getDraw() {
        hideAnotherView();
        mRelativeLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mRelativeLayout.getDrawingCache());
        mRelativeLayout.destroyDrawingCache();
        return bitmap;
    }

    private void hideAnotherView() {
        for (int i = 0; i < mRelativeLayout.getChildCount(); i++) {
            AddView addView = (AddView) mRelativeLayout.getChildAt(i);
            addView.hideControl();
        }
    }
}
