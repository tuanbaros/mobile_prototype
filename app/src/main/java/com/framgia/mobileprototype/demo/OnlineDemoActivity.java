package com.framgia.mobileprototype.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.remote.ApiService;
import com.framgia.mobileprototype.databinding.ActivityOnlineDemoBinding;
import com.framgia.mobileprototype.ui.widget.DemoView;
import com.framgia.mobileprototype.util.ScreenSizeUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;

public class OnlineDemoActivity extends AppCompatActivity implements View.OnTouchListener,
        DemoContract.View {

    public static final String EXTRA_PROJECT = "EXTRA_PROJECT";
    public static final String EXTRA_MOCK_ENTRY_ID = "EXTRA_MOCK_ENTRY_ID";
    public static final String EXTRA_TRANSITION = "EXTRA_TRANSITION";

    public static Intent getOnlineDemoIntent(Context context, Project project) {
        Intent intent = new Intent(context, OnlineDemoActivity.class);
        intent.putExtra(EXTRA_PROJECT, project);
        return intent;
    }

    public static Intent getOnlineDemoIntent(Context context, String mockEntryId, String anim) {
        Intent intent = new Intent(context, OnlineDemoActivity.class);
        intent.putExtra(EXTRA_MOCK_ENTRY_ID, mockEntryId);
        intent.putExtra(EXTRA_TRANSITION, anim);
        return intent;
    }

    private ActivityOnlineDemoBinding mOnlineDemoBinding;
    private Project mProject;
    private int mCount;
    private ProgressDialog mProgressDialog;
    public static Map<String, Mock> mMockMap = new HashMap<>();
    private Mock mCurrentMock;
    private DemoContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mOnlineDemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_online_demo);
        mProject = (Project) getIntent().getSerializableExtra(EXTRA_PROJECT);
        mPresenter = new OnlineDemoPresenter(this);
        if (mProject == null) {
            String mockEntryId = getIntent().getStringExtra(EXTRA_MOCK_ENTRY_ID);
            setAnimation(getIntent().getStringExtra(EXTRA_TRANSITION));
            showScreen(mockEntryId);
            return;
        }
        mMockMap.clear();
        showProgressDialog();
        if (mProject.getMocks().size() > 0) {
            scaleProject();
        } else {
            getProjectDetail();
        }
    }

    private void setUpProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.text_loading));
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            setUpProgressDialog();
        }
        mProgressDialog.show();
    }

    private void getProjectDetail() {
        AndroidNetworking.post(ApiService.getApi(ApiService.DOWNLOAD))
                .addBodyParameter(ApiService.Param.PROJECT_ENTRY_ID, mProject.getEntryId())
                .doNotCacheResponse()
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null || response.length() == 0) {
                            error();
                            return;
                        }
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Mock>>(){}.getType();
                        List<Mock> mocks = gson.fromJson(response.toString(), listType);
                        mProject.setMocks(mocks);
                        scaleProject();
                    }

                    @Override
                    public void onError(ANError anError) {
                        error();
                    }
                });
    }

    public void error() {
        Toast.makeText(getBaseContext(), R.string.text_network_error, Toast.LENGTH_SHORT)
                .show();
        finish();
    }

    private void scaleProject() {
        List<String> images = new ArrayList<>();
        float scaleWidth, scaleHeight;
        if (mProject.getOrientation().equals(Project.LANDSCAPE)) {
            scaleWidth = (float) ScreenSizeUtil.sHeight / mProject.getWidth();
            scaleHeight = (float) ScreenSizeUtil.sWidth / mProject.getHeight();
            mProject.setWidth(ScreenSizeUtil.sHeight);
            mProject.setHeight(ScreenSizeUtil.sWidth);
        } else {
            scaleWidth = (float) ScreenSizeUtil.sWidth / mProject.getWidth();
            scaleHeight = (float) ScreenSizeUtil.sHeight / mProject.getHeight();
            mProject.setWidth(ScreenSizeUtil.sWidth);
            mProject.setHeight(ScreenSizeUtil.sHeight);
        }
        for (int i = 0; i < mProject.getMocks().size(); i++) {
            Mock mock = mProject.getMocks().get(i);
            images.add(mock.getImage());
            mMockMap.put(mock.getEntryId(), mock);
            for (int j = 0; j < mock.getElements().size(); j++) {
                Element element = mock.getElements().get(j);
                if (element.getGesture() == null) {
                    element.setGesture(Constant.DEFAULT_GESTURE);
                }
                element.setX((int) (element.getX() * scaleWidth));
                element.setY((int) (element.getY() * scaleHeight));
                element.setWidth((int) (element.getWidth() * scaleWidth));
                element.setHeight((int) (element.getHeight() * scaleHeight));
                element.setEntryId(null);
            }
        }
        downloadImage(images);
    }

    private void downloadImage(final List<String> images) {
        mCount = 0;
        if (images.size() == 0) {
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance(ApiService.FIREBASE_BUCKET);
        StorageReference baseReference = storage.getReference(ApiService.FIREBASE_FOLDER);
        for (String image : images) {
            StorageReference reference = baseReference.child(image);

            File localFile = new File(Constant.FILE_PATH + image);
            //if (localFile.exists()) {
            //    mCount++;
            //    if (mCount == images.size()) {
            //        if (mProgressDialog.isShowing()) {
            //            showScreen(mProject.getMocks().get(0).getEntryId());
            //            mProgressDialog.dismiss();
            //        }
            //    }
            //    continue;
            //}

            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask
                    .TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    mCount++;
                    if (mCount == images.size()) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        showScreen(mProject.getMocks().get(0).getEntryId());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    error();
                }
            });
        }
    }

    private void showScreen(String entryId) {
        mCurrentMock = mMockMap.get(entryId);
        Glide.with(this).
                load(Constant.FILE_PATH + mCurrentMock.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mOnlineDemoBinding
                .imageTest);
        setUpElement();
    }

    private void setUpElement() {
        RelativeLayout relativeLayout = (RelativeLayout) mOnlineDemoBinding.getRoot();
        for (Element element : mCurrentMock.getElements()) {
            if (TextUtils.isEmpty(element.getLinkTo())) continue;
            DemoView view = new DemoView(this, element.getGesture(), mPresenter);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(element.getWidth(), element.getHeight());
            params.leftMargin = element.getX();
            params.topMargin = element.getY();
            view.setLayoutParams(params);
            view.setTag(R.string.title_link_to, element.getLinkTo());
            view.setTag(R.string.title_element, element);
            relativeLayout.addView(view);
        }
        relativeLayout.setOnTouchListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String mockEntryId = intent.getStringExtra(EXTRA_MOCK_ENTRY_ID);
        setAnimation(intent.getStringExtra(EXTRA_TRANSITION));
        showScreen(mockEntryId);
    }

    protected void setAnimation(String anim) {
        Resources resources = getResources();
        if (resources.getString(R.string.title_transition_default).equals(anim)) {
            return;
        }
        if (resources.getString(R.string.title_transition_fade).equals(anim)) {
            overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_top).equals(anim)) {
            overridePendingTransition(R.anim.slide_top, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_left).equals(anim)) {
            overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_bottom).equals(anim)) {
            overridePendingTransition(R.anim.slide_bottom, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_slide_right).equals(anim)) {
            overridePendingTransition(R.anim.slide_right, android.R.anim.fade_out);
            return;
        }
        if (resources.getString(R.string.title_transition_push_up).equals(anim)) {
            overridePendingTransition(R.anim.slide_top, R.anim.slide_out_bottom);
            return;
        }
        if (resources.getString(R.string.title_transition_push_down).equals(anim)) {
            overridePendingTransition(R.anim.slide_bottom, R.anim.slide_out_top);
            return;
        }
        if (resources.getString(R.string.title_transition_push_right).equals(anim)) {
            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
            return;
        }
        if (resources.getString(R.string.title_transition_push_left).equals(anim)) {
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
        }
    }

    protected void showHightlight() {
        RelativeLayout relativeLayout = (RelativeLayout) mOnlineDemoBinding.getRoot();
        for (int i = 1; i < relativeLayout.getChildCount(); i++) {
            View view = relativeLayout.getChildAt(i);
            view.setBackgroundResource(R.drawable.link_to_selector);
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideHightlight();
                    }
                });
            }
        };
        thread.start();
    }

    protected void hideHightlight() {
        RelativeLayout relativeLayout = (RelativeLayout) mOnlineDemoBinding.getRoot();
        for (int i = 1; i < relativeLayout.getChildCount(); i++) {
            View view = relativeLayout.getChildAt(i);
            view.setBackgroundDrawable(null);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            showHightlight();
        }
        return false;
    }

    protected void clearAllElement() {
        RelativeLayout relativeLayout = (RelativeLayout) mOnlineDemoBinding.getRoot();
        relativeLayout.removeViews(1, relativeLayout.getChildCount() - 1);
    }

    @Override
    public void start() {

    }

    @Override
    public void onElementLoaded(List<Element> elements) {

    }

    @Override
    public void onElementError() {

    }

    @Override
    public void onMockLoaded(Mock mock) {

    }

    @Override
    public void onMockError() {

    }

    @Override
    public void showNextScreen(String linkTo, String anim) {
        clearAllElement();
        startActivity(OnlineDemoActivity.getOnlineDemoIntent(this, linkTo, anim));
        finish();
    }
}
