package com.framgia.mobileprototype.data.source.element;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.DataSource;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.source.element
 */
public class ElementLocalDataSource extends DataHelper implements DataSource<Element> {
    private static ElementLocalDataSource sElementLocalDataSource;

    private ElementLocalDataSource(Context context) {
        super(context);
    }

    public static ElementLocalDataSource getInstance(Context context) {
        if (sElementLocalDataSource == null) {
            sElementLocalDataSource = new ElementLocalDataSource(context);
        }
        return sElementLocalDataSource;
    }

    @Override
    public void getDatas(@NonNull GetListCallback getListCallback) {
        // TODO: 22/02/2017 get all elements 
    }

    @Override
    public void getData(@NonNull String dataId, @NonNull GetListCallback getListCallback) {
        // TODO: 22/02/2017 get element detail 
    }

    @Override
    public long saveData(Element data) {
        long elementId = INSERT_ERROR;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            elementId = sqLiteDatabase.insert(
                ElementPersistenceContract.ElementEntry.TABLE_NAME, null, getContentValues(data));
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return elementId;
    }

    @Override
    public void updateData(Element data) {
        // TODO: 22/02/2017 update element 
    }

    @Override
    public void deleteData(Element data) {
        // TODO: 22/02/2017 delete element 
    }

    private ContentValues getContentValues(Element element) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_X, element.getX());
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_Y, element.getY());
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_WIDTH, element.getWidth());
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_HEIGHT, element.getHeight());
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_HEIGHT, element.getHeight());
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_LINK_TO, element.getLinkTo());
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_TRANSITION,
            element.getTransition());
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_MOCK_ID, element.getMockId());
        return contentValues;
    }
}
