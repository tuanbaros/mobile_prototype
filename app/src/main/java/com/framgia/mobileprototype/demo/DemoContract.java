package com.framgia.mobileprototype.demo;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;

import java.util.List;

/**
 * Created by tuannt on 08/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.demo
 */
public interface DemoContract {
    interface View extends BaseView {
        void onElementLoaded(List<Element> elements);
        void onElementError();
        void onMockLoaded(Mock mock);
        void onMockError();
        void showNextScreen(String linkTo, String anim);
    }

    interface Presenter extends BasePresenter {
        void getElements(String mockId);
        void getMock(String mockEntryId);
        void openNextScreen(String linkTo, String anim);
    }
}
