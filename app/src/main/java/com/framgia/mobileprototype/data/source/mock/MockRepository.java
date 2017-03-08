package com.framgia.mobileprototype.data.source.mock;

import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Mock;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.mock
 */
public class MockRepository implements MockDataSource {
    private static MockRepository sMockRepository;
    private MockLocalDataSource mMockLocalDataSource;

    private MockRepository(MockLocalDataSource mockLocalDataSource) {
        mMockLocalDataSource = mockLocalDataSource;
    }

    public static MockRepository getInstance(MockLocalDataSource mockLocalDataSource) {
        if (sMockRepository == null) {
            sMockRepository = new MockRepository(mockLocalDataSource);
        }
        return sMockRepository;
    }

    @Override
    public void getDatas(@NonNull GetListCallback getListCallback) {
        mMockLocalDataSource.getDatas(getListCallback);
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        mMockLocalDataSource.getData(dataId, getListCallback);
    }

    @Override
    public long saveData(Mock data) {
        return mMockLocalDataSource.saveData(data);
    }

    @Override
    public long updateData(Mock data) {
        return mMockLocalDataSource.updateData(data);
    }

    @Override
    public void deleteData(Mock data) {
        mMockLocalDataSource.deleteData(data);
    }

    @Override
    public void getMockByEntryId(String mockEntryId, GetCallback getCallback) {
        mMockLocalDataSource.getMockByEntryId(mockEntryId, getCallback);
    }
}
