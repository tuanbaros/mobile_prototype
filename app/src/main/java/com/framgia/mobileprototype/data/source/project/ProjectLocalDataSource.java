package com.framgia.mobileprototype.data.source.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Project;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.DataSource;

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
        // TODO: 22/02/2017 get list projects 
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        // TODO: 22/02/2017 get one project
    }

    @Override
    public void saveData(Project data) {
        openDb();
        mSQLiteDatabase.beginTransaction();
        try {
            mSQLiteDatabase
                .insertWithOnConflict(ProjectPersistenceContract.ProjectEntry.TABLE_NAME, null,
                    getContentValues(data), SQLiteDatabase.CONFLICT_IGNORE);
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
            closeDb();
        }
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
}
