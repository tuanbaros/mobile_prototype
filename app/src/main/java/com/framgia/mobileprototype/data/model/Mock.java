package com.framgia.mobileprototype.data.model;

import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.mobileprototype.BR;
import com.framgia.mobileprototype.data.source.mock.MockPersistenceContract;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.model
 */
public class Mock extends BaseObservable implements Serializable {
    private String mId;
    @SerializedName("client_id")
    private String mEntryId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("notes")
    private String mNote;
    @SerializedName("image")
    private String mImage;
    private String mProjectId;
    @SerializedName("elements")
    private List<Element> mElements;
    private boolean mCheckToDelete;

    public Mock(Cursor cursor) {
        mId = cursor.getString(cursor.getColumnIndexOrThrow(
            MockPersistenceContract.MockEntry._ID));
        mTitle = cursor.getString(cursor.getColumnIndexOrThrow(
            MockPersistenceContract.MockEntry.COLUMN_NAME_TITLE));
        mEntryId = cursor.getString(cursor.getColumnIndexOrThrow(
            MockPersistenceContract.MockEntry.COLUMN_NAME_ENTRY_ID));
        mNote = cursor.getString(cursor.getColumnIndexOrThrow(
            MockPersistenceContract.MockEntry.COLUMN_NAME_NOTE));
        mImage = cursor.getString(cursor.getColumnIndexOrThrow(
            MockPersistenceContract.MockEntry.COLUMN_NAME_IMAGE));
        mProjectId = cursor.getString(cursor.getColumnIndexOrThrow(
            MockPersistenceContract.MockEntry.COLUMN_NAME_PROJECT_ID));
    }

    public Mock() {
    }

    public String getEntryId() {
        return mEntryId;
    }

    public void setEntryId(String entryId) {
        this.mEntryId = entryId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @Bindable
    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
        notifyPropertyChanged(BR.image);
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        this.mNote = note;
    }

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        this.mProjectId = projectId;
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
        notifyPropertyChanged(BR.title);
    }

    public List<Element> getElements() {
        return mElements;
    }

    public void setElements(List<Element> elements) {
        this.mElements = elements;
    }

    @Bindable
    public boolean isCheckToDelete() {
        return mCheckToDelete;
    }

    public void setCheckToDelete(boolean checkToDelete) {
        this.mCheckToDelete = checkToDelete;
        notifyPropertyChanged(BR.checkToDelete);
    }
}
