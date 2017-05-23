package com.framgia.mobileprototype.data.source.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.framgia.mobileprototype.Constant;
import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.element.ElementPersistenceContract;
import com.framgia.mobileprototype.data.source.mock.MockPersistenceContract;
import com.framgia.mobileprototype.util.EntryIdUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.project
 */
public class ProjectLocalDataSource extends DataHelper implements DataSource<Project> {
    private static ProjectLocalDataSource sProjectLocalDataSource;

    private ProjectLocalDataSource(Context context) {
        super(context);
    }

    public static ProjectLocalDataSource getInstance(Context context) {
        if (sProjectLocalDataSource == null) {
            sProjectLocalDataSource = new ProjectLocalDataSource(context);
        }
        return sProjectLocalDataSource;
    }

    @Override
    public void getDatas(@NonNull GetListCallback getListCallback) {
        List<Project> projects = null;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sortOrder = ProjectPersistenceContract.ProjectEntry._ID + " DESC";
        Cursor cursor = sqLiteDatabase.query(
            ProjectPersistenceContract.ProjectEntry.TABLE_NAME,
            null, null, null, null, null, sortOrder);
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
            for (Project project : projects) {
                project.setNumberMocks(countMocks(project.getId()));
            }
            getListCallback.onSuccess(projects);
        }
        sqLiteDatabase.close();
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        List<Mock> mocks = null;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(
            MockPersistenceContract.MockEntry.TABLE_NAME,
            null, null, null, null, null, null);
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
    public long saveData(Project data) {
        long projectId = INSERT_ERROR;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            projectId = sqLiteDatabase
                .insertWithOnConflict(ProjectPersistenceContract.ProjectEntry.TABLE_NAME, null,
                    getContentValues(data), SQLiteDatabase.CONFLICT_IGNORE);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return projectId;
    }

    @Override
    public long updateData(Project data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        String whereClause = ProjectPersistenceContract.ProjectEntry._ID + "=?";
        String[] whereArgs = {data.getId()};
        long changeRows = 0;
        try {
            changeRows = sqLiteDatabase.updateWithOnConflict(ProjectPersistenceContract.ProjectEntry
                    .TABLE_NAME, getContentValues(data), whereClause, whereArgs,
                SQLiteDatabase.CONFLICT_IGNORE);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return changeRows;
    }

    @Override
    public void deleteData(Project data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        String whereClause = ProjectPersistenceContract.ProjectEntry._ID + "=?";
        String[] whereArgs = {data.getId()};
        try {
            sqLiteDatabase.delete(
                ProjectPersistenceContract.ProjectEntry.TABLE_NAME, whereClause, whereArgs);
            removeDetailProject(sqLiteDatabase, data.getId());
            removeAllMockInProject(sqLiteDatabase, data.getId());
            sqLiteDatabase.setTransactionSuccessful();
            deleteImage(data.getPoster());
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

    private void removeDetailProject(SQLiteDatabase sqLiteDatabase, String projectId) {
        String[] columns = {
            MockPersistenceContract.MockEntry._ID,
            MockPersistenceContract.MockEntry.COLUMN_NAME_IMAGE
        };
        String whereClause = MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID + "=?";
        String[] whereArgs = {projectId};
        Cursor cursor = sqLiteDatabase.query(
            MockPersistenceContract.MockEntry.TABLE_NAME,
            columns, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                removeAllElementInMock(sqLiteDatabase,
                    cursor.getString(cursor.getColumnIndexOrThrow(
                        MockPersistenceContract.MockEntry._ID)));
                deleteImage(cursor.getString(cursor.getColumnIndexOrThrow(
                    MockPersistenceContract.MockEntry.COLUMN_NAME_IMAGE)));
            }
        }
        if (cursor != null) cursor.close();
    }

    private void removeAllElementInMock(SQLiteDatabase sqLiteDatabase, String mockId) {
        String whereClause = ElementPersistenceContract.ElementEntry.COLUMN_NAME_MOCK_ID + "=?";
        String[] whereArgs = {mockId};
        sqLiteDatabase.delete(
            ElementPersistenceContract.ElementEntry.TABLE_NAME, whereClause, whereArgs);
    }

    private void removeAllMockInProject(SQLiteDatabase sqLiteDatabase, String projectId) {
        String whereClause = MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID + "=?";
        String[] whereArgs = {projectId};
        sqLiteDatabase.delete(
            MockPersistenceContract.MockEntry.TABLE_NAME, whereClause, whereArgs);
    }

    private ContentValues getContentValues(Project project) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TITLE, project.getTitle().trim());
        contentValues.put(ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_ENTRY_ID, project
            .getEntryId() == null ? EntryIdUtil.get() : project.getEntryId());
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TYPE, project.getType());
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_DESCRIPTION,
            project.getDescription());
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_WIDTH, project.getWidth());
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_HEIGHT, project.getHeight());
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_ORIENTATION,
            project.getOrientation());
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_POSTER, project.getPoster());
        return contentValues;
    }

    private int countMocks(String projectId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int count = 0;
        String[] columns = {"COUNT(*)"};
        String selection = MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID + " = ?";
        String[] selectionArgs = {projectId};
        Cursor cursor = sqLiteDatabase.query(
            MockPersistenceContract.MockEntry.TABLE_NAME, columns, selection, selectionArgs,
            null, null, null);
        if (cursor == null) return 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return count;
    }
}
