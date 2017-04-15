package com.framgia.mobileprototype.projectdetail;

import com.framgia.mobileprototype.BasePresenter;
import com.framgia.mobileprototype.BaseView;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;

import java.util.ArrayList;
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
        void cancelCreateMockDialog();
        void pickImage();
        void showMockTitleEmpty();
        void updateListMock(Mock mock);
        Object getMockImagePath();
        void setDefaultImagePath();
        void checkPermission();
        void emptyMockToRemove();
        void removeMockFromAdapter(ArrayList<Mock> mocks);
        void showNumberMockToRemove(int numberMocks);
        void showMockDetailUi(Mock mock);
        void showEditMockDialog(Mock mock);
        void cancelEditMockDialog();
        void openCamera();
        void openGallery();
        void showDrawUi();
        void showCloneMockDialogUi(List<Project> projects, Mock mock);
        void closeCloneMockDialog();
    }

    interface Presenter extends BasePresenter {
        void getMocks(String projectId);
        void openDeleteMockDialog();
        void chooseImage();
        void openCreateMockDialog();
        void closeCreateMockDialog();
        void saveMock(Mock mock);
        void saveMockImage(Object path, String filename);
        void addMockToRemoveList(Mock mock);
        void clearMockFromRemoveList(Mock mock);
        void deleteMocks();
        void checkAction(boolean isRemoving);
        void clearAllMocksFromRemoveList();
        void openMockDetail(Mock mock);
        void openEditMockDialog(Mock mock);
        void closeEditMockDialog(Mock mock);
        void updateMock(Mock mock);
        void takePhoto();
        void importFromGallery();
        void openDrawPanel();
        void updateSortMock(int fromPos, int toPos);
        void updateMockPosition();
        Mock getFirstMockItem();
        void removeMockFromSortMock(Mock mock);
        ArrayList<Mock> getSortMocks();
        void openCloneMockDialog(boolean isPortrait, Mock mock);
        void cloneMock(Mock mock);
        void closeCloneMockDialog();
        void setProjectId(int id);
    }
}
