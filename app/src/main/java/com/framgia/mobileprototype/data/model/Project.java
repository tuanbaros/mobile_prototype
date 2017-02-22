package com.framgia.mobileprototype.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data
 */
public class Project {
    private String mId;
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
    private List<Mock> mMocks;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
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

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        this.mPoster = poster;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
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
}
