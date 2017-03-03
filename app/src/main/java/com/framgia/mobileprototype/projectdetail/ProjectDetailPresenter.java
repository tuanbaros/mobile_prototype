package com.framgia.mobileprototype.projectdetail;

import android.text.TextUtils;

import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.mock.MockRepository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
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
                mProjectDetailView.mocksLoaded(datas);
            }

            @Override
            public void onError() {
                mProjectDetailView.mocksNotAvailable();
            }
        });
    }

    @Override
    public void openDeleteMockDialog(Mock mock) {
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
        if (TextUtils.isEmpty(mock.getTitle().trim())) {
            mProjectDetailView.showMockTitleEmpty();
            return;
        }
        SecureRandom random = new SecureRandom();
        String entryId = new BigInteger(NUMBER_BIT_RANDOM, random).toString(BASE_RANDOM);
        mock.setEntryId(entryId);
        mock.setImage(entryId + Constant.DEFAULT_COMPRESS_FORMAT);
        long id = mMockRepository.saveData(mock);
        if (id < 1) return;
        saveMockImage(mProjectDetailView.getMockImagePath(), mock.getImage());
        mProjectDetailView.updateListMock(mock);
    }

    @Override
    public void saveMockImage(String path, String filename) {
        String destinationFilename = Constant.FILE_PATH + filename;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
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
    }

    @Override
    public void start() {
        mProjectDetailView.onPrepare();
    }
}
