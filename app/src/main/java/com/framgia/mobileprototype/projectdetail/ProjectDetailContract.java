package com.framgia.mobileprototype.projectdetail;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Mock;

import java.util.List;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public interface ProjectDetailContract {
    interface View extends BaseView {
        void onPrepare();
        void mocksLoaded(List<Mock> mocks);
        void mocksNotAvailable();
        void showDeleteMockDialog();
        void showCreateMockDialog();
    }

    interface Presenter extends BasePresenter {
        void getMocks(String projectId);
        void openDeleteMockDialog(Mock mock);
        void openCreateMockDialog();
    }
}
