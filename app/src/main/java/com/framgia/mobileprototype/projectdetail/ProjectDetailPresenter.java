package com.framgia.mobileprototype.projectdetail;

import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;
import com.framgia.mobileprototype.projects.ProjectEvent;
import com.framgia.mobileprototype.util.EntryIdUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 01/03/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projectdetail
 */
public class ProjectDetailPresenter implements ProjectDetailContract.Presenter {
    private ProjectDetailContract.View mProjectDetailView;
    private MockRepository mMockRepository;
    public static final int NUMBER_BIT_RANDOM = 130;
    public static final int BASE_RANDOM = 32;
    private ArrayList<Mock> mRemoveMocks = new ArrayList<>();
    private ArrayList<Mock> mSortMocks = new ArrayList<>();
    private Mock mBackUpMock;
    private List<Project> mProjects;
    private Mock mBaseMockClone;
    private int mProjectId;

    public ProjectDetailPresenter(MockRepository mockRepository,
                                  ProjectDetailContract.View projectDetailView) {
        mMockRepository = mockRepository;
        mProjectDetailView = projectDetailView;
    }

    @Override
    public void getMocks(String projectId) {
        mMockRepository.getData(projectId, new DataSource.GetListCallback<Mock>() {
            @Override
            public void onSuccess(List<Mock> datas) {
                mSortMocks.addAll(datas);
                mProjectDetailView.mocksLoaded(datas);
            }

            @Override
            public void onError() {
                mProjectDetailView.mocksNotAvailable();
            }
        });
    }

    @Override
    public void openDeleteMockDialog() {
        if (mRemoveMocks.size() == 0) {
            mProjectDetailView.emptyMockToRemove();
            return;
        }
        mProjectDetailView.showDeleteMockDialog();
    }

    @Override
    public void chooseImage() {
        mProjectDetailView.checkPermission();
    }

    @Override
    public void openCreateMockDialog() {
        mProjectDetailView.showCreateMockDialog();
    }

    @Override
    public void closeCreateMockDialog() {
        mProjectDetailView.cancelCreateMockDialog();
    }

    @Override
    public void saveMock(Mock mock) {
        if (mock.getTitle() == null || mock.getTitle().trim().equals("")) {
            mProjectDetailView.showMockTitleEmpty();
            return;
        }
        savingMock(mock);
    }

    private void savingMock(Mock mock) {
//        SecureRandom random = new SecureRandom();
//        String entryId = new BigInteger(NUMBER_BIT_RANDOM, random).toString(BASE_RANDOM);
        String entryId = EntryIdUtil.get();
        mock.setEntryId(entryId);
        mock.setImage(entryId + Constant.DEFAULT_COMPRESS_FORMAT);
        if (checkIsCurrentProject(mock)) {
            if (mSortMocks.size() > 1) {
                Mock lastMock = mSortMocks.get(mSortMocks.size() - 1);
                mock.setPosition(lastMock.getPosition() + 1);
            } else {
                mock.setPosition(0);
            }
        }
        long id = mMockRepository.saveData(mock);
        if (id < 1) return;
        mock.setId(String.valueOf(id));
        saveMockImage(mProjectDetailView.getMockImagePath(), mock.getImage());
        if (checkIsCurrentProject(mock)) {
            mSortMocks.add(mock);
            mProjectDetailView.updateListMock(mock);
        }
    }

    private boolean checkIsCurrentProject(Mock mock) {
        return mBaseMockClone == null || mock.getProjectId().equals(mBaseMockClone.getProjectId());
    }

    @Override
    public void saveMockImage(Object path, String filename) {
        if (path == null) return;
        String destinationFilename = Constant.FILE_PATH + filename;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (path instanceof String) {
                bis = new BufferedInputStream(new FileInputStream((String) path));
            } else if (path instanceof FileInputStream) {
                bis = new BufferedInputStream((FileInputStream) path);
            } else {
                return;
            }
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mProjectDetailView.setDefaultImagePath();
    }

    @Override
    public void addMockToRemoveList(Mock mock) {
        mRemoveMocks.add(mock);
        mProjectDetailView.showNumberMockToRemove(mRemoveMocks.size());
    }

    @Override
    public void clearMockFromRemoveList(Mock mock) {
        mRemoveMocks.remove(mock);
        mProjectDetailView.showNumberMockToRemove(mRemoveMocks.size());
    }

    @Override
    public void deleteMocks() {
        for (Mock mock : mRemoveMocks) {
            mMockRepository.deleteData(mock);
        }
        mProjectDetailView.removeMockFromAdapter(mRemoveMocks);
        mRemoveMocks.clear();
    }

    @Override
    public void checkAction(boolean isRemoving) {
        if (isRemoving) openDeleteMockDialog();
        else chooseImage();
    }

    @Override
    public void clearAllMocksFromRemoveList() {
        for (Mock mock : mRemoveMocks) {
            mock.setCheckToDelete(false);
        }
        mRemoveMocks.clear();
    }

    @Override
    public void openMockDetail(Mock mock) {
        mProjectDetailView.showMockDetailUi(mock);
    }

    @Override
    public void openEditMockDialog(Mock mock) {
        try {
            mBackUpMock = (Mock) mock.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mProjectDetailView.showEditMockDialog(mock);
    }

    @Override
    public void closeEditMockDialog(Mock mock) {
        mock.setTitle(mBackUpMock.getTitle());
        mock.setImage(mock.getImage());
        mProjectDetailView.cancelEditMockDialog();
    }

    @Override
    public void updateMock(Mock mock) {
        if (mock.getTitle() == null || mock.getTitle().trim().equals("")) {
            mProjectDetailView.showMockTitleEmpty();
            return;
        }
        long id = mMockRepository.updateData(mock);
        if (id < 1) return;
        saveMockImage(mProjectDetailView.getMockImagePath(), mock.getImage());
        mock.setImage(mock.getImage());
        mProjectDetailView.cancelEditMockDialog();
    }

    @Override
    public void takePhoto() {
        mProjectDetailView.openCamera();
    }

    @Override
    public void importFromGallery() {
        mProjectDetailView.openGallery();
    }

    @Override
    public void openDrawPanel() {
        mProjectDetailView.showDrawUi();
    }

    @Override
    public void updateSortMock(final int fromPos, final int toPos) {
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                Mock mock = mSortMocks.get(fromPos);
                mSortMocks.remove(fromPos);
                mSortMocks.add(toPos, mock);
            }
        }).start();
    }

    @Override
    public void updateMockPosition() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mSortMocks.size(); i++) {
                    mSortMocks.get(i).setPosition(i);
                    mMockRepository.updateData(mSortMocks.get(i));
                }
            }
        }).start();
    }

    @Override
    public Mock getFirstMockItem() {
        return mSortMocks.size() > 0 ? mSortMocks.get(0) : null;
    }

    @Override
    public void removeMockFromSortMock(Mock mock) {
        mSortMocks.remove(mock);
    }

    @Override
    public void start() {
        mProjectDetailView.onPrepare();
    }

    @Override
    public ArrayList<Mock> getSortMocks() {
        return mSortMocks;
    }

    @Override
    public void openCloneMockDialog(boolean isPortrait, Mock mock) {
        mBaseMockClone = mock;
        String orientation = isPortrait ? Project.PORTRAIT : Project.LANDSCAPE;
        try {
            final Mock cloneMock = (Mock) mock.clone();
            if (mProjects == null) {
                mMockRepository.getSameOrientationProject(orientation,
                    new DataSource.GetListCallback<Project>() {
                        @Override
                        public void onSuccess(List<Project> datas) {
                            mProjects = datas;
                        }

                        @Override
                        public void onError() {
                        }
                    });
            }
            mProjectDetailView.showCloneMockDialogUi(mProjects, cloneMock);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cloneMock(Mock mock) {
        if (mock.getTitle() == null || mock.getTitle().trim().equals("")) {
            mock.setTitle(mBaseMockClone.getTitle());
        }
        savingMock(mock);
        if (!mock.getProjectId().equals(mBaseMockClone.getProjectId())) {
            EventBus.getDefault().post(new ProjectEvent(mProjectId));
        }
        closeCloneMockDialog();
    }

    @Override
    public void closeCloneMockDialog() {
        mProjectDetailView.closeCloneMockDialog();
        mBaseMockClone = null;
    }

    public void setProjectId(int projectId) {
        mProjectId = projectId;
    }

    @Override
    public void openLibrary() {
        mProjectDetailView.showLibraryUi();
    }
}
