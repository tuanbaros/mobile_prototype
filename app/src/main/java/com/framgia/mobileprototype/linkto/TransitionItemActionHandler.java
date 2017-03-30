package com.framgia.mobileprototype.linkto;

import com.framgia.mobileprototype.data.model.Element;

/**
 * Created by tuannt on 17/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.linkto
 */
public class TransitionItemActionHandler {
    private LinkToContract.Presenter mListener;

    public TransitionItemActionHandler(LinkToContract.Presenter listener) {
        mListener = listener;
    }

    public void chooseTransition(String transition, Element element) {
        if (mListener == null) return;
        mListener.chooseTransition(transition, element);
    }
}
