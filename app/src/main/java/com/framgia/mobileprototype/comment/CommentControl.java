package com.framgia.mobileprototype.comment;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

/**
 * Created by tuannt on 5/28/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.comment
 */
public class CommentControl {
    private ObservableBoolean mIsLoading = new ObservableBoolean();

    private ObservableBoolean mIsError = new ObservableBoolean();

    private boolean mIsLoadMore;

    private String mProjectId;

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        mProjectId = projectId;
    }

    private ObservableField<CommentAdapter> mCommentAdapterField = new ObservableField<>();

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public ObservableBoolean getIsError() {
        return mIsError;
    }

    public ObservableField<CommentAdapter> getCommentAdapterField() {
        return mCommentAdapterField;
    }

    public boolean isLoadMore() {
        return mIsLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        mIsLoadMore = loadMore;
    }
}
