package com.framgia.mobileprototype.linkto;

import com.framgia.mobileprototype.data.model.Mock;

/**
 * Created by tuannt on 08/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.linkto
 */
public class LinkToItemActionHandler {
    private LinkToContract.Presenter mListener;

    public LinkToItemActionHandler(LinkToContract.Presenter listener) {
        mListener = listener;
    }

    public void chooseMock(Mock mock) {
        if (mListener == null) return;
        mock.setCheckToDelete(!mock.isCheckToDelete());
        mListener.chooseMock(mock);
    }
}
