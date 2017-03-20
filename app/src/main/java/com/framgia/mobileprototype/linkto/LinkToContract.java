package com.framgia.mobileprototype.linkto;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.model.Mock;

import java.util.List;

/**
 * Created by tuannt on 08/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.linkto
 */
public interface LinkToContract {
    interface View extends BaseView {
        void mockLoaded(List<Mock> mocks);
        void mockNotAvailable();
        void saveLinkTo(Mock mock);
    }

    interface Presenter extends BasePresenter {
        void getMocks(String projectId);
        void chooseMock(Mock mock, Element element);
        void chooseTransition(String transition, Element element);
    }
}
