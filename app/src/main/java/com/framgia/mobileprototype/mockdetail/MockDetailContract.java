package com.framgia.mobileprototype.mockdetail;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Element;

import java.util.List;

/**
 * Created by tuannt on 06/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.mockdetail
 */
public interface MockDetailContract {
    interface View extends BaseView {
        void onPrepare();
        void elementsLoaded(List<Element> elements);
        void elementsNotAvailable();
        void showElementOption();
        void hideElementOption();
    }

    interface Presenter extends BasePresenter {
        void getElements(String mockId);
        void openElementOption();
    }
}
