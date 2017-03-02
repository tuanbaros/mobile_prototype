package com.framgia.mobileprototype.projectdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Window;

import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.databinding.ActivityProjectDetailBinding;
import com.framgia.mobileprototype.databinding.DialogAddMockBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

public class ProjectDetailActivity extends BaseActivity implements ProjectDetailContract.View {
    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    public static final int NUMBER_COLUMN_GRID = 3;
    private Project mProject;
    private ProjectDetailContract.Presenter mProjectDetailPresenter;
    private ActivityProjectDetailBinding mProjectDetailBinding;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private ObservableBoolean mIsEmptyMock = new ObservableBoolean();
    private ObservableField<MockAdapter> mMockAdapter = new ObservableField<>();
    private Dialog mCreateMockDialog;
    private DialogAddMockBinding mAddMockBinding;

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
        mMockAdapter.set(new MockAdapter(this, mocks, mProjectDetailPresenter));
    }

    @Override
    public void mocksNotAvailable() {
        mIsLoading.set(false);
        mIsEmptyMock.set(true);
        mMockAdapter.set(new MockAdapter(this, null, mProjectDetailPresenter));
    }

    @Override
    public void showDeleteMockDialog() {
        // TODO: 01/03/2017 show Delete dialog 
    }

    @Override
    public void showCreateMockDialog() {
        if (mCreateMockDialog == null) setUpCreateMockDialog();
        mAddMockBinding.setMock(new Mock());
        mCreateMockDialog.show();
    }

    @Override
    public void pickImage() {
        CropImage.startPickImageActivity(this);
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
                case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                    Uri imageUri = CropImage.getPickImageResultUri(this, data);
                    cropImage(imageUri);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (mCreateMockDialog == null) setUpCreateMockDialog();
                    if (mProject.getOrientation().equals(Project.PORTRAIT)) {
                        mAddMockBinding.imagePortraitMock.setImageURI(result.getUri());
                    } else {
                        mAddMockBinding.imageLandscapeMock.setImageURI(result.getUri());
                    }
                    mProjectDetailPresenter.openCreateMockDialog();
                    break;
                default:
                    break;
            }
        }
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
        setSupportActionBar(mProjectDetailBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mProject.getTitle());
        }
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

    public ObservableBoolean getIsEmptyMock() {
        return mIsEmptyMock;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public ObservableField<MockAdapter> getMockAdapter() {
        return mMockAdapter;
    }
}
