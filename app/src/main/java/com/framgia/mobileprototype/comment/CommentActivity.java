package com.framgia.mobileprototype.comment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.mobileprototype.BaseActivity;
import com.framgia.mobileprototype.R;
import com.framgia.mobileprototype.databinding.ActivityCommentBinding;

public class CommentActivity extends BaseActivity {

    private ActivityCommentBinding mCommentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
    }
}
