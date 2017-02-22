package com.framgia.mobileprototype.data.source.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Mock;
import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.data.source.mock.MockPersistenceContract;

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
        openDb();
        String sortOrder = ProjectPersistenceContract.ProjectEntry._ID + " DESC";
        Cursor cursor = mSQLiteDatabase.query(
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
        closeDb();
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        List<Mock> mocks = null;
        openDb();
        Cursor cursor = mSQLiteDatabase.query(
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
    }

    @Override
    public long saveData(Project data) {
        long projectId = INSERT_ERROR;
        openDb();
        mSQLiteDatabase.beginTransaction();
        try {
            projectId = mSQLiteDatabase
                .insertWithOnConflict(ProjectPersistenceContract.ProjectEntry.TABLE_NAME, null,
                    getContentValues(data), SQLiteDatabase.CONFLICT_IGNORE);
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
            closeDb();
        }
        return projectId;
    }

    @Override
    public void updateData(Project data) {
        // TODO: 22/02/2017 update project 
    }

    @Override
    public void deleteData(Project data) {
        // TODO: 22/02/2017 delete project 
    }

    private ContentValues getContentValues(Project project) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TITLE, project.getTitle());
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
        openDb();
        int count = 0;
        mSQLiteDatabase.beginTransaction();
        String[] columns = {"COUNT(*)"};
        String selection = MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID + " = ?";
        String[] selectionArgs = {projectId};
        Cursor cursor = mSQLiteDatabase.query(
            MockPersistenceContract.MockEntry.TABLE_NAME, columns, selection, selectionArgs,
            null, null, null);
        if (cursor == null) return 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        closeDb();
        return count;
    }
}
