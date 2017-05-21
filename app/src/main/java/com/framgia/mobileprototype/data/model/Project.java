package com.framgia.mobileprototype.data.model;

import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.mobileprototype.BR;
import com.framgia.mobileprototype.data.source.project.ProjectPersistenceContract;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data
 */
public class Project extends BaseObservable implements Cloneable, Serializable {
    public static final String PORTRAIT = "portrait";
    public static final String LANDSCAPE = "landscape";
    private String mId;
    private String mEntryId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("type")
    private String mType;
    @SerializedName("w")
    private int mWidth;
    @SerializedName("h")
    private int mHeight;
    @SerializedName("orientation")
    private String mOrientation;
    @SerializedName("poster")
    private String mPoster;
    @SerializedName("mockups")
    private List<Mock> mMocks = new ArrayList<>();
    private int mNumberMocks;

    public Project(Cursor cursor) {
        mId = cursor.getString(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry._ID));
        mTitle = cursor.getString(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TITLE));
        mDescription = cursor.getString(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_DESCRIPTION));
        mType = cursor.getString(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_TYPE));
        mWidth = cursor.getInt(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_WIDTH));
        mHeight = cursor.getInt(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_HEIGHT));
        mOrientation = cursor.getString(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_ORIENTATION));
        mPoster = cursor.getString(cursor.getColumnIndexOrThrow(
            ProjectPersistenceContract.ProjectEntry.COLUMN_NAME_POSTER));
    }

    public Project() {
    }

    @Bindable
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
        notifyPropertyChanged(BR.description);
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getOrientation() {
        return mOrientation;
    }

    public void setOrientation(String orientation) {
        this.mOrientation = orientation;
    }

    @Bindable
    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        this.mPoster = poster;
        notifyPropertyChanged(BR.poster);
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
        notifyPropertyChanged(BR.title);
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public List<Mock> getMocks() {
        return mMocks;
    }

    public void setMocks(List<Mock> mocks) {
        mMocks = mocks;
    }

    @Bindable
    public int getNumberMocks() {
        return mNumberMocks;
    }

    public void setNumberMocks(int numberMocks) {
        mNumberMocks = numberMocks;
        notifyPropertyChanged(BR.numberMocks);
    }

    @Bindable
    public boolean isPortrait() {
        return PORTRAIT.equals(getOrientation());
    }

    public void setPortrait(boolean portrait) {
        if (portrait) setOrientation(PORTRAIT);
        else setOrientation(LANDSCAPE);
        notifyPropertyChanged(BR.portrait);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getEntryId() {
        return mEntryId;
    }

    public void setEntryId(String entryId) {
        mEntryId = entryId;
    }
}
