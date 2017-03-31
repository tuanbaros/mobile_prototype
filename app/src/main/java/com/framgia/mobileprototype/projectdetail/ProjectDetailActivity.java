package com.framgia.mobileprototype.projectdetail;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.databinding.ActivityProjectDetailBinding;
import com.framgia.mobileprototype.databinding.DialogAddMockBinding;
import com.framgia.mobileprototype.databinding.DialogEditMockBinding;
import com.framgia.mobileprototype.databinding.DialogPickImageBinding;
import com.framgia.mobileprototype.demo.DemoActivity;
import com.framgia.mobileprototype.demo.LandcapeDemoActivity;
import com.framgia.mobileprototype.draw.DrawActivity;
import com.framgia.mobileprototype.helper.ItemTouchCallbackHelper;
import com.framgia.mobileprototype.helper.OnStartDragListener;
import com.framgia.mobileprototype.mockdetail.LandcapeMockDetailActivity;
import com.framgia.mobileprototype.mockdetail.MockDetailActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends BaseActivity implements ProjectDetailContract.View,
    OnStartDragListener {
    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    public static final int PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_REQUEST_CODE = 3;
    public static final int GALLERY_REQUEST_CODE = 4;
    public static final int DRAW_REQUEST_CODE = 5;
    private static final int DEFAULT_NUMBER_MOCKS_TO_REMOVE = 0;
    private Project mProject;
    private ProjectDetailContract.Presenter mProjectDetailPresenter;
    private ActivityProjectDetailBinding mProjectDetailBinding;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private ObservableBoolean mIsEmptyMock = new ObservableBoolean();
    private ObservableField<MockAdapter> mMockAdapter = new ObservableField<>();
    private Dialog mCreateMockDialog, mEditMockDialog, mPickImageDialog;
    private DialogAddMockBinding mAddMockBinding;
    private DialogEditMockBinding mEditMockBinding;
    private String mMockImagePath;
    private ArrayList<String> mDeniedPermissions = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private ObservableBoolean mIsRemoving = new ObservableBoolean();

    public static Intent getProjectDetailIntent(Context context, Project project) {
        Intent intent = new Intent(context, ProjectDetailActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_project_detail);
        mProjectDetailPresenter = new ProjectDetailPresenter(
            MockRepository.getInstance(MockLocalDataSource.getInstance(this)),
            this);
        mProjectDetailBinding.setActivity(this);
        mProjectDetailBinding.setPresenter(mProjectDetailPresenter);
        start();
    }

    @Override
    public void onPrepare() {
        mIsLoading.set(true);
        mProjectDetailPresenter.getMocks(mProject.getId());
    }

    @Override
    public void mocksLoaded(List<Mock> mocks) {
        mIsLoading.set(false);
        mMockAdapter.set(
            new MockAdapter(this, mocks, mProjectDetailPresenter, this, mProject.isPortrait()));
        ItemTouchHelper.Callback callback = new ItemTouchCallbackHelper(mMockAdapter.get());
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mProjectDetailBinding.recyclerView);
    }

    @Override
    public void mocksNotAvailable() {
        mIsLoading.set(false);
        mIsEmptyMock.set(true);
        mMockAdapter.set(
            new MockAdapter(this, null, mProjectDetailPresenter, this, mProject.isPortrait()));
    }

    @Override
    public void showDeleteMockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setTitle(R.string.title_delete_mock)
            .setMessage(R.string.msg_delete_mocks)
            .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mProjectDetailPresenter.deleteMocks();
                }
            })
            .setNegativeButton(R.string.action_no, null);
        builder.create().show();
    }

    @Override
    public void showCreateMockDialog() {
        if (mCreateMockDialog == null) setUpCreateMockDialog();
        Mock mock = new Mock();
        mock.setProjectId(mProject.getId());
        mAddMockBinding.setMock(mock);
        mCreateMockDialog.show();
    }

    public void cancelCreateMockDialog() {
        if (mCreateMockDialog != null) mCreateMockDialog.cancel();
    }

    @Override
    public void pickImage() {
        if (mPickImageDialog == null) setUpPickImageDialog();
        mPickImageDialog.show();
    }

    private void setUpPickImageDialog() {
        DialogPickImageBinding mPickImageBinding = DataBindingUtil.inflate(getLayoutInflater(),
            R.layout.dialog_pick_image, null, false);
        mPickImageBinding.setPresenter(mProjectDetailPresenter);
        mPickImageDialog = new Dialog(this);
        mPickImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPickImageDialog.setContentView(mPickImageBinding.getRoot());
    }

    @Override
    public void showMockTitleEmpty() {
        Toast.makeText(this, R.string.error_empty_mock_name, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateListMock(Mock mock) {
        cancelCreateMockDialog();
        mMockAdapter.get().updateData(mock);
        if (mIsEmptyMock.get()) mIsEmptyMock.set(false);
    }

    @Override
    public String getMockImagePath() {
        return mMockImagePath;
    }

    @Override
    public void setDefaultImagePath() {
        mMockImagePath = null;
    }

    @Override
    public void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pickImage();
            return;
        }
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
    public void emptyMockToRemove() {
        Toast.makeText(this, R.string.msg_empty_mocks_to_remove, Toast.LENGTH_LONG).show();
    }

    @Override
    public void removeMockFromAdapter(ArrayList<Mock> mocks) {
        mMockAdapter.get().removeMultipItem(mocks);
        mMockAdapter.get().setIsRemoving(false);
        mIsRemoving.set(false);
        setUpTitle();
        if (mMockAdapter.get().getItemCount() == 0) mIsEmptyMock.set(true);
    }

    @Override
    public void showNumberMockToRemove(int numberMocks) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && isRemoving()) {
            Resources res = getResources();
            actionBar.setTitle(res.getString(R.string.msg_number_mock_selected, numberMocks));
        }
    }

    @Override
    public void showMockDetailUi(Mock mock) {
        if (mProject.isPortrait()) {
            startActivity(
                MockDetailActivity.getMockDetailIntent(this, mock, mProject));
            return;
        }
        startActivity(
            LandcapeMockDetailActivity.getMockDetailIntent(this, mock, mProject));
    }

    @Override
    public void showEditMockDialog(Mock mock) {
        if (mEditMockDialog == null) setUpEditMockDialog();
        mEditMockBinding.setMock(mock);
        mEditMockDialog.show();
    }

    @Override
    public void cancelEditMockDialog() {
        if (mEditMockDialog != null && mEditMockDialog.isShowing()) {
            mEditMockDialog.cancel();
        }
    }

    @Override
    public void openCamera() {
        mPickImageDialog.cancel();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(new File(Constant.FILE_TEMP)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void openGallery() {
        mPickImageDialog.cancel();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(Constant.IMAGE_RECENT_PATH);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void showDrawUi() {
        mPickImageDialog.cancel();
        startActivityForResult(
            DrawActivity.getDrawIntent(this, mProject),
            DRAW_REQUEST_CODE);
    }

    @Override
    public void start() {
        getIntentData();
        setUpTitle();
        mProjectDetailPresenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    Uri cameraResultUri = getUriFromFile(Constant.FILE_TEMP);
                    if (cameraResultUri != null) cropImage(cameraResultUri);
                    break;
                case GALLERY_REQUEST_CODE:
                    Uri galleryResultUri = data.getData();
                    cropImage(galleryResultUri);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    mMockImagePath = result.getUri().getPath();
                    ImageView imageView;
                    if (mEditMockDialog != null && mEditMockDialog.isShowing()) {
                        imageView = mProject.isPortrait() ?
                            mEditMockBinding.imagePortraitMock :
                            mEditMockBinding.imageLandscapeMock;
                        Glide.with(this).load(result.getUri()).into(imageView);
                        return;
                    }
                    if (mCreateMockDialog == null) setUpCreateMockDialog();
                    imageView = mProject.isPortrait() ?
                        mAddMockBinding.imagePortraitMock : mAddMockBinding.imageLandscapeMock;
                    Glide.with(this).load(result.getUri()).into(imageView);
                    mProjectDetailPresenter.openCreateMockDialog();
                    break;
                case DRAW_REQUEST_CODE:
                    Uri drawUri = getUriFromFile(data.getStringExtra(DrawActivity.EXTRA_DRAW));
                    if (drawUri != null) cropImage(drawUri);
                    break;
                default:
                    break;
            }
        }
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
    public void onBackPressed() {
        if (mMockAdapter.get().getIsRemoving().get()) {
            mMockAdapter.get().setIsRemoving(false);
            mIsRemoving.set(false);
            mProjectDetailPresenter.clearAllMocksFromRemoveList();
            mMockAdapter.get().notifyDataSetChanged();
            setUpTitle();
            return;
        }
        if (mProject.getNumberMocks() != mMockAdapter.get().getItemCount()) {
            mProject.setNumberMocks(mMockAdapter.get().getItemCount());
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PROJECT, mProject);
            setResult(RESULT_OK, intent);
        }
        mProjectDetailPresenter.updateMockPosition();
        super.onBackPressed();
    }

    private void cropImage(Uri imageUri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAutoZoomEnabled(false)
            .setAspectRatio(mProject.getWidth(), mProject.getHeight())
            .start(this);
    }

    private void getIntentData() {
        mProject = (Project) getIntent().getSerializableExtra(EXTRA_PROJECT);
    }

    private void setUpTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) setSupportActionBar(mProjectDetailBinding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mProject.getTitle());
    }

    private void setUpCreateMockDialog() {
        mAddMockBinding = DataBindingUtil.inflate(getLayoutInflater(),
            R.layout.dialog_add_mock, null, false);
        mAddMockBinding.setPresenter(mProjectDetailPresenter);
        mAddMockBinding.setIsPortrait(mProject.isPortrait());
        mCreateMockDialog = new Dialog(this);
        mCreateMockDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCreateMockDialog.setContentView(mAddMockBinding.getRoot());
        mCreateMockDialog.setCanceledOnTouchOutside(false);
    }

    private void setUpEditMockDialog() {
        mEditMockBinding = DataBindingUtil.inflate(getLayoutInflater(),
            R.layout.dialog_edit_mock, null, false);
        mEditMockBinding.setPresenter(mProjectDetailPresenter);
        mEditMockBinding.setIsPortrait(mProject.isPortrait());
        mEditMockDialog = new Dialog(this);
        mEditMockDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mEditMockDialog.setContentView(mEditMockBinding.getRoot());
        mEditMockDialog.setCanceledOnTouchOutside(false);
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

    public ObservableBoolean getIsEmptyMock() {
        return mIsEmptyMock;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public ObservableField<MockAdapter> getMockAdapter() {
        return mMockAdapter;
    }

    public ObservableBoolean getIsRemoving() {
        return mIsRemoving;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public int numberColumns() {
        return mProject.isPortrait() ? Constant.NUMBER_COLUMN_GRID_PORTRAIT :
            Constant.NUMBER_COLUMN_GRID_LANDSCAPE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_project_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_remove:
                if (mIsRemoving.get()) break;
                mMockAdapter.get().setIsRemoving(true);
                mIsRemoving.set(true);
                showNumberMockToRemove(DEFAULT_NUMBER_MOCKS_TO_REMOVE);
                break;
            case R.id.action_play:
                Mock mock = mProjectDetailPresenter.getFirstMockItem();
                if (mock == null) {
                    Toast.makeText(this, R.string.msg_empty_mock, Toast.LENGTH_LONG).show();
                    return false;
                }
                if (mProject.isPortrait()) {
                    startActivity(DemoActivity.getDemoIntent(this, mock.getEntryId()));
                } else {
                    startActivity(LandcapeDemoActivity.getDemoIntent(this, mock.getEntryId()));
                }
                break;
            default:
                break;
        }
        return false;
    }

    public boolean isRemoving() {
        return mIsRemoving.get();
    }
}
