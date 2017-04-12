package com.framgia.mobileprototype.data.source.mock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.element.ElementPersistenceContract;
import com.framgia.mobileprototype.data.source.project.ProjectPersistenceContract;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.mock
 */
public class MockLocalDataSource extends DataHelper implements MockDataSource {
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
        String sortOrder = MockPersistenceContract.MockEntry.COLUMN_NAME_POSITION + " ASC";
        Cursor cursor = sqLiteDatabase.query(
            MockPersistenceContract.MockEntry.TABLE_NAME,
            null, whereClause, whereArgs, null, null, sortOrder);
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
        long numberRowChange = INSERT_ERROR;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = MockPersistenceContract.MockEntry._ID + "=?";
        String[] whereArgs = {data.getId()};
        sqLiteDatabase.beginTransaction();
        try {
            numberRowChange = sqLiteDatabase.update(
                MockPersistenceContract.MockEntry.TABLE_NAME,
                getContentValues(data),
                whereClause,
                whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return numberRowChange;
    }

    @Override
    public void deleteData(Mock data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        String whereClause = MockPersistenceContract.MockEntry._ID + "=?";
        String[] whereArgs = {data.getId()};
        try {
            sqLiteDatabase.delete(
                MockPersistenceContract.MockEntry.TABLE_NAME, whereClause, whereArgs);
            removeAllElementInMock(sqLiteDatabase, data.getId());
            removeAllElementRelateMock(sqLiteDatabase, data.getEntryId());
            sqLiteDatabase.setTransactionSuccessful();
            deleteImage(data.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
    }

    private void deleteImage(String filename) {
        if (filename == null) return;
        String filePath = Constant.FILE_PATH + filename;
        File file = new File(filePath);
        if (file.exists()) file.delete();
    }

    private void removeAllElementInMock(SQLiteDatabase sqLiteDatabase, String mockId) {
        String whereClause = ElementPersistenceContract.ElementEntry.COLUMN_NAME_MOCK_ID + "=?";
        String[] whereArgs = {mockId};
        sqLiteDatabase.delete(
            ElementPersistenceContract.ElementEntry.TABLE_NAME, whereClause, whereArgs);
    }

    private void removeAllElementRelateMock(SQLiteDatabase sqLiteDatabase, String mockEntryId) {
        String whereClause = ElementPersistenceContract.ElementEntry.COLUMN_NAME_LINK_TO + "=?";
        String[] whereArgs = {mockEntryId};
        sqLiteDatabase.delete(
            ElementPersistenceContract.ElementEntry.TABLE_NAME, whereClause, whereArgs);
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
        contentValues.put(
            MockPersistenceContract.MockEntry.COLUMN_NAME_POSITION, mock.getPosition());
        return contentValues;
    }

    @Override
    public void getMockByEntryId(String mockEntryId, GetCallback getCallback) {
        Mock mock = null;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = MockPersistenceContract.MockEntry.COLUMN_NAME_ENTRY_ID + "=?";
        String[] whereArgs = {mockEntryId};
        Cursor cursor = sqLiteDatabase.query(
            MockPersistenceContract.MockEntry.TABLE_NAME,
            null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            mock = new Mock(cursor);
        }
        if (cursor != null) cursor.close();
        if (mock == null) getCallback.onMockNotAvailable();
        else getCallback.onMockLoaded(mock);
        sqLiteDatabase.close();
    }

    @Override
    public void getSameOrientationProject(String orientation,
            GetListCallback<Project> getListCallback) {
        List<Project> projects = null;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sortOrder = ProjectPersistenceContract.ProjectEntry._ID + " DESC";
        String whereClause = ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_ORIENTATION + "=?";
        String[] whereArgs = {orientation};
        Cursor cursor = sqLiteDatabase.query(
                ProjectPersistenceContract.ProjectEntry.TABLE_NAME,
                null, whereClause, whereArgs, null, null, sortOrder);
        if (cursor != null && cursor.getCount() > 0) {
            projects = new ArrayList<>();
            while (cursor.moveToNext()) {
                projects.add(new Project(cursor));
            }
        }
        if (cursor != null) cursor.close();
        if (projects == null) {
            getListCallback.onError();
        } else {
            getListCallback.onSuccess(projects);
        }
        sqLiteDatabase.close();
    }
}
