package com.framgia.mobileprototype.data.source.mock;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.DataSource;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.mock
 */
public class MockLocalDataSource extends DataHelper implements DataSource<Mock> {
    private static MockLocalDataSource sMockLocalDataSource;

    private MockLocalDataSource(Context context) {
        super(context);
    }

    public static MockLocalDataSource getInstance(Context context) {
        if (sMockLocalDataSource == null) {
            sMockLocalDataSource = new MockLocalDataSource(context);
        }
        return sMockLocalDataSource;
    }

    @Override
    public void getDatas(@NonNull GetListCallback getListCallback) {
        // TODO: 22/02/2017 get all mocks 
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        // TODO: 22/02/2017 get mock detail 
    }

    @Override
    public void saveData(Mock data) {
        openDb();
        mSQLiteDatabase.beginTransaction();
        try {
            mSQLiteDatabase.insert(
                MockPersistenceContract.MockEntry.TABLE_NAME, null, getContentValues(data));
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
            closeDb();
        }
    }

    @Override
    public void updateData(Mock data) {
        // TODO: 22/02/2017 update mock 
    }

    @Override
    public void deleteData(Mock data) {
        // TODO: 22/02/2017 delete mock 
    }

    private ContentValues getContentValues(Mock mock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(
            MockPersistenceContract.MockEntry.COLUMN_NAME_ENTRY_ID, mock.getEntryId());
        contentValues.put(
            MockPersistenceContract.MockEntry.COLUMN_NAME_TITLE, mock.getTitle());
        contentValues.put(
            MockPersistenceContract.MockEntry.COLUMN_NAME_NOTE, mock.getNote());
        contentValues.put(
            MockPersistenceContract.MockEntry.COLUMN_NAME_IMAGE, mock.getImage());
        contentValues.put(
            MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID, mock.getProjectId());
        return contentValues;
    }
}
