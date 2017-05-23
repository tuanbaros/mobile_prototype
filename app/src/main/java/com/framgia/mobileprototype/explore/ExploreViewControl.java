package com.framgia.mobileprototype.explore;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

/**
 * Created by tuannt on 5/21/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.explore
 */
public class ExploreViewControl {
    private ObservableBoolean mIsLoading = new ObservableBoolean();

    private ObservableBoolean mIsError = new ObservableBoolean();

    private boolean mIsLoadMore;

    private ObservableField<ExploreAdapter> mExploreAdapterField = new ObservableField<>();

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public ObservableBoolean getIsError() {
        return mIsError;
    }

    public ObservableField<ExploreAdapter> getExploreAdapterField() {
        return mExploreAdapterField;
    }

    public boolean isLoadMore() {
        return mIsLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        mIsLoadMore = loadMore;
    }
}
