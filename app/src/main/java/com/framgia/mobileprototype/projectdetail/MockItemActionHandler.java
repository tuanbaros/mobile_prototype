package com.framgia.mobileprototype.projectdetail;

import com.framgia.mobileprototype.data.model.Mock;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class MockItemActionHandler {
    private ProjectDetailContract.Presenter mListener;

    public MockItemActionHandler(ProjectDetailContract.Presenter listener) {
        mListener = listener;
    }

    public void completeChanged(Mock mock, boolean isRemoving) {
        if (mListener == null) return;
        mock.setCheckToDelete(isRemoving);
        if (isRemoving) mListener.addMockToRemoveList(mock);
        else mListener.clearMockFromRemoveList(mock);
    }

    public void openMockDetail(Mock mock) {
        if (mListener == null) return;
        mListener.openMockDetail(mock);
    }
}
