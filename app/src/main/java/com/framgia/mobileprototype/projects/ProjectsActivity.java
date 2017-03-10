package com.framgia.mobileprototype.projects;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.PermissionActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.RegisterPermission;
import com.framgia.mobileprototype.about.AboutActivity;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.element.ElementLocalDataSource;
import com.framgia.mobileprototype.data.source.element.ElementRepository;
import com.framgia.mobileprototype.data.source.mock.MockLocalDataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.data.source.project.ProjectLocalDataSource;
import com.framgia.mobileprototype.data.source.project.ProjectRepository;
import com.framgia.mobileprototype.databinding.ActivityProjectsBinding;
import com.framgia.mobileprototype.databinding.DialogAddProjectBinding;
import com.framgia.mobileprototype.databinding.DialogEditProjectBinding;
import com.framgia.mobileprototype.databinding.NavHeaderBinding;
import com.framgia.mobileprototype.introduction.IntroductionActivity;
import com.framgia.mobileprototype.projectdetail.ProjectDetailActivity;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RegisterPermission(permissions = {
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.CAMERA})
public class ProjectsActivity extends PermissionActivity implements
    ProjectsContract.View,
    NavigationView.OnNavigationItemSelectedListener {
    private static final String EXTRA_FIRST_OPEN_APP = "EXTRA_FIRST_OPEN_APP";
    private static final int PROJECT_DETAIL_REQUEST_CODE = 1;
    private static final String SAMPLE_PROJECT_FILE_NAME = "sample_project.json";
    private ActivityProjectsBinding mProjectsBinding;
    private ObservableBoolean mIsDrawerOpen = new ObservableBoolean();
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ObservableBoolean mIsLoading = new ObservableBoolean();
    private ObservableBoolean mIsEmptyProject = new ObservableBoolean();
    private ProjectsContract.Presenter mProjectsPresenter;
    private ObservableField<ProjectsAdapter> mProjectsAdapter = new ObservableField<>();
    private Dialog mCreateProjectDialog, mEditProjectDialog;
    private DialogAddProjectBinding mAddProjectBinding;
    private DialogEditProjectBinding mEditProjectBinding;
    private Project mProject;
    private ObservableInt mNumberProjects = new ObservableInt();
    private ObservableInt mNumberMocks = new ObservableInt();

    public static Intent getProjectsIntent(Context context, boolean isFirstOpenApp) {
        Intent intent = new Intent(context, ProjectsActivity.class);
        intent.putExtra(EXTRA_FIRST_OPEN_APP, isFirstOpenApp);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_projects);
        mProjectsBinding.setProjectsActivity(this);
        mProjectsPresenter = new ProjectsPresenter(this,
            ProjectRepository.getInstance(ProjectLocalDataSource.getInstance(this)),
            MockRepository.getInstance(MockLocalDataSource.getInstance(this)),
            ElementRepository.getInstance(ElementLocalDataSource.getInstance(this)));
        mProjectsBinding.setPresenter(mProjectsPresenter);
        mProjectsBinding.setListener(this);
        start();
    }

    private void setUpDrawerListener() {
        mActionBarDrawerToggle =
            new ActionBarDrawerToggle(this,
                mProjectsBinding.drawer,
                mProjectsBinding.appBar.toolBar,
                R.string.msg_open_drawer,
                R.string.msg_close_drawer) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    mIsDrawerOpen.set(false);
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    mIsDrawerOpen.set(true);
                    super.onDrawerOpened(drawerView);
                }
            };
        mProjectsBinding.drawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    private void setUpNavigationHeader() {
        NavHeaderBinding navHeaderBinding = NavHeaderBinding.inflate(getLayoutInflater());
        navHeaderBinding.setActivity(this);
        mProjectsBinding.navView.addHeaderView(navHeaderBinding.getRoot());
    }

    private void setUpEditProjectDialog() {
        mEditProjectBinding = DataBindingUtil.inflate(getLayoutInflater(),
            R.layout.dialog_edit_project, null, false);
        mEditProjectBinding.setPresenter(mProjectsPresenter);
        mEditProjectDialog = new Dialog(this);
        mEditProjectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mEditProjectDialog.setContentView(mEditProjectBinding.getRoot());
        mEditProjectDialog.setCanceledOnTouchOutside(false);
    }

    private void setUpCreateProjectDialog() {
        mAddProjectBinding = DataBindingUtil.inflate(getLayoutInflater(),
            R.layout.dialog_add_project, null, false);
        mAddProjectBinding.setPresenter(mProjectsPresenter);
        mCreateProjectDialog = new Dialog(this);
        mCreateProjectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCreateProjectDialog.setContentView(mAddProjectBinding.getRoot());
        mCreateProjectDialog.setCanceledOnTouchOutside(false);
    }

    private void setUpScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ScreenSizeUtil.sWidth = metrics.widthPixels;
        ScreenSizeUtil.sHeight = metrics.heightPixels;
    }

    public ObservableBoolean getIsDrawerOpen() {
        return mIsDrawerOpen;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public ObservableField<ProjectsAdapter> getProjectsAdapter() {
        return mProjectsAdapter;
    }

    public ObservableBoolean getIsEmptyProject() {
        return mIsEmptyProject;
    }

    @Override
    public void onBackPressed() {
        if (mIsDrawerOpen.get()) {
            mIsDrawerOpen.set(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionBarDrawerToggle.onOptionsItemSelected(item) ||
            super.onOptionsItemSelected(item);
    }

    @Override
    public void prepare() throws IOException {
        mIsLoading.set(true);
        mProjectsPresenter.saveSampleProject(
            getIntent().getBooleanExtra(EXTRA_FIRST_OPEN_APP, true),
            getAssets().open(SAMPLE_PROJECT_FILE_NAME));
    }

    @Override
    public void projectsLoaded(final List<Project> projects) {
        mIsLoading.set(false);
        mProjectsAdapter.set(new ProjectsAdapter(this, projects, mProjectsPresenter));
        mNumberProjects.set(projects.size());
        int count = 0;
        for (Project project : projects) {
            count += project.getNumberMocks();
        }
        mNumberMocks.set(count);
    }

    @Override
    public void projectsNotAvailable() {
        mIsLoading.set(false);
        mIsEmptyProject.set(true);
        mProjectsAdapter.set(new ProjectsAdapter(this, null, mProjectsPresenter));
    }

    @Override
    public void showCreateProjectDialog(Project project) {
        if (mCreateProjectDialog == null) setUpCreateProjectDialog();
        mAddProjectBinding.setProject(project);
        if (!mCreateProjectDialog.isShowing()) mCreateProjectDialog.show();
    }

    @Override
    public void cancelCreateProjectDialog() {
        if (mCreateProjectDialog.isShowing()) mCreateProjectDialog.cancel();
    }

    @Override
    public void cancelEditProjectDialog() {
        if (mEditProjectDialog.isShowing()) mEditProjectDialog.cancel();
    }

    @Override
    public void showErrorEmptyProjectName() {
        Toast.makeText(this, R.string.error_empty_project_name, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorProjectNameExist() {
        Toast.makeText(this, R.string.error_project_name_exist, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateListProjects(Project project) {
        cancelCreateProjectDialog();
        mProjectsAdapter.get().updateData(project);
        if (mIsEmptyProject.get()) mIsEmptyProject.set(false);
        mNumberProjects.set(mNumberProjects.get() + 1);
    }

    @Override
    public void savePojectPoster(String filename) {
        ImageView imageView;
        if (mCreateProjectDialog != null && mCreateProjectDialog.isShowing()) {
            imageView = mAddProjectBinding.imageProjectPoster;
        } else {
            imageView = mEditProjectBinding.imageProjectPoster;
        }
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        OutputStream fOut = null;
        File root = new File(Constant.FILE_PATH);
        File sdImageMainDirectory = new File(root, filename);
        try {
            fOut = new FileOutputStream(sdImageMainDirectory);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut == null) return;
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pickPoster() {
        CropImage.startPickImageActivity(this);
    }

    @Override
    public void showUpdateProjectDialog(Project project) {
        if (mEditProjectDialog == null) setUpEditProjectDialog();
        mEditProjectBinding.setProject(project);
        if (!mEditProjectDialog.isShowing()) mEditProjectDialog.show();
    }

    public void showDeleteProjectDialog(final Project project, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_delete_project);
        builder.setMessage(R.string.msg_delete_project);
        builder.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mProjectsPresenter.removeProject(project, position);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onProjectRemoved(int position, int numberMockRemoved) {
        mProjectsAdapter.get().removeItem(position);
        if (mProjectsAdapter.get().getItemCount() == 0) {
            mIsEmptyProject.set(true);
        }
        mNumberProjects.set(mNumberProjects.get() - 1);
        mNumberMocks.set(mNumberMocks.get() - numberMockRemoved);
    }

    @Override
    public void showDetailProjectUi(Project project) {
        mProject = project;
        startActivityForResult(
            ProjectDetailActivity.getProjectDetailIntent(this, project),
            PROJECT_DETAIL_REQUEST_CODE);
    }

    @Override
    public void start() {
        setUpScreenSize();
        setUpDrawerListener();
        setUpNavigationHeader();
        mProjectsPresenter.createAppStorageFolder(Constant.FILE_PATH);
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
                    if (mAddProjectBinding != null && mCreateProjectDialog.isShowing()) {
                        Glide.with(this)
                            .load(result.getUri()).into(mAddProjectBinding.imageProjectPoster);
                        mAddProjectBinding.setIsPosterChanged(true);
                    }
                    if (mEditProjectBinding != null && mEditProjectDialog.isShowing()) {
                        Glide.with(this)
                            .load(result.getUri()).into(mEditProjectBinding.imageProjectPoster);
                        mEditProjectBinding.setIsPosterChanged(true);
                    }
                    break;
                case PROJECT_DETAIL_REQUEST_CODE:
                    Project project =
                        (Project) data.getSerializableExtra(ProjectDetailActivity.EXTRA_PROJECT);
                    mNumberMocks.set(
                        mNumberMocks.get() - mProject.getNumberMocks() + project.getNumberMocks());
                    mProject.setNumberMocks(project.getNumberMocks());
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
            .setAspectRatio(100, 100)
            .start(this);
    }

    public ObservableInt getNumberMocks() {
        return mNumberMocks;
    }

    public ObservableInt getNumberProjects() {
        return mNumberProjects;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mIsDrawerOpen.set(false);
        switch (menuItem.getItemId()) {
            case R.id.nav_project:
                break;
            case R.id.nav_introduction:
                startActivity(
                    IntroductionActivity.getIntroductionIntent(this, true));
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.nav_rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        (Constant.BASE_MARKET_SCHEMA + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        (Constant.BASE_MARKET_URL + getPackageName())));
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int actionBarHeight = actionBar.getHeight();
            int statusBarHeight = getStatusBarHeight();
            int paddingDistance = 2 * (int) getResources().getDimension(R.dimen.dp_16);
            ScreenSizeUtil.sChildWidth = ScreenSizeUtil.sWidth - (paddingDistance);
            ScreenSizeUtil.sChildHeight =
                ScreenSizeUtil.sHeight - actionBarHeight - statusBarHeight - paddingDistance;
            ScreenSizeUtil.sScaleWidth =
                (float) ScreenSizeUtil.sWidth / ScreenSizeUtil.sChildWidth;
            ScreenSizeUtil.sScaleHeight =
                (float) ScreenSizeUtil.sHeight / ScreenSizeUtil.sChildHeight;
        }
        mProjectsPresenter.start();
        return super.onPrepareOptionsMenu(menu);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(
            Constant.STATUS_BAR_HEIGHT, Constant.DIMEN, Constant.ANDROID);
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
