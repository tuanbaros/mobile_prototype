package com.framgia.mobileprototype.data.source.mock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.DataSource;

import java.util.ArrayList;
import java.util.List;

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
        List<Mock> mocks = null;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID + "=?";
        String[] whereArgs = {dataId};
        Cursor cursor = sqLiteDatabase.query(
            MockPersistenceContract.MockEntry.TABLE_NAME,
            null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            mocks = new ArrayList<>();
            while (cursor.moveToNext()) {
                mocks.add(new Mock(cursor));
            }
        }
        if (cursor != null) cursor.close();
        if (mocks == null) getListCallback.onError();
        else getListCallback.onSuccess(mocks);
        sqLiteDatabase.close();
    }

    @Override
    public long saveData(Mock data) {
        long mockId = INSERT_ERROR;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            mockId = sqLiteDatabase.insert(
                MockPersistenceContract.MockEntry.TABLE_NAME, null, getContentValues(data));
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return mockId;
    }

    @Override
    public long updateData(Mock data) {
        // TODO: 22/02/2017 update mock
        return 0;
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
