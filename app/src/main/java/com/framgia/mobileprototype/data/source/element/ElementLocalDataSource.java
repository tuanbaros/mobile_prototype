package com.framgia.mobileprototype.data.source.element;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.framgia.mobileprototype.data.model.Element;
import com.framgia.mobileprototype.data.source.DataHelper;
import com.framgia.mobileprototype.data.source.DataSource;
import com.framgia.mobileprototype.util.EntryIdUtil;

import java.util.ArrayList;
import java.util.List;

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
        List<Element> elements = null;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = ElementPersistenceContract.ElementEntry.COLUMN_NAME_MOCK_ID + "=?";
        String[] whereArgs = {dataId};
        Cursor cursor = sqLiteDatabase.query(
            ElementPersistenceContract.ElementEntry.TABLE_NAME,
            null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            elements = new ArrayList<>();
            while (cursor.moveToNext()) {
                elements.add(new Element(cursor));
            }
        }
        if (cursor != null) cursor.close();
        if (elements == null) getListCallback.onError();
        else getListCallback.onSuccess(elements);
        sqLiteDatabase.close();
    }

    @Override
    public long saveData(Element data) {
        long elementId = INSERT_ERROR;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            elementId = sqLiteDatabase.insertWithOnConflict(
                ElementPersistenceContract.ElementEntry.TABLE_NAME, null, getContentValues(data),
                SQLiteDatabase.CONFLICT_IGNORE);
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
    public long updateData(Element data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        String whereClause = ElementPersistenceContract.ElementEntry._ID + "=?";
        String[] whereArgs = {String.valueOf(data.getId())};
        long changeRows = 0;
        try {
            changeRows = sqLiteDatabase.updateWithOnConflict(ElementPersistenceContract.ElementEntry
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
    public void deleteData(Element data) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        String whereClause = ElementPersistenceContract.ElementEntry._ID + "=?";
        String[] whereArgs = {String.valueOf(data.getId())};
        try {
            sqLiteDatabase.delete(ElementPersistenceContract.ElementEntry
                .TABLE_NAME, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
    }

    private ContentValues getContentValues(Element element) {
        ContentValues contentValues = new ContentValues();
        if (element.getId() > 0) {
            contentValues.put(ElementPersistenceContract.ElementEntry._ID, element.getId());
        }
        contentValues.put(ElementPersistenceContract.ElementEntry.COLUMN_NAME_ENTRY_ID, element
            .getEntryId() == null ? EntryIdUtil.get() : element.getEntryId());
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
        contentValues.put(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_GESTURE, element.getGesture());
        return contentValues;
    }
}
