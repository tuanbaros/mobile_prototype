package com.framgia.mobileprototype.data.model;

import android.database.Cursor;

import com.framgia.mobileprototype.data.source.element.ElementPersistenceContract;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.model
 */
public class Element implements Serializable {
    private String mId;
    @SerializedName("x")
    private int mX;
    @SerializedName("y")
    private int mY;
    @SerializedName("w")
    private int mWidth;
    @SerializedName("h")
    private int mHeight;
    @SerializedName("link_to")
    private String mLinkTo;
    @SerializedName("transition")
    private String mTransition;
    private String mMockId;

    public Element() {
    }

    public Element(Cursor cursor) {
        mId = cursor.getString(cursor.getColumnIndexOrThrow(
            ElementPersistenceContract.ElementEntry._ID));
        mX = cursor.getInt(cursor.getColumnIndexOrThrow(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_X));
        mY = cursor.getInt(cursor.getColumnIndexOrThrow(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_Y));
        mWidth = cursor.getInt(cursor.getColumnIndexOrThrow(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_WIDTH));
        mHeight = cursor.getInt(cursor.getColumnIndexOrThrow(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_HEIGHT));
        mTransition = cursor.getString(cursor.getColumnIndexOrThrow(
            ElementPersistenceContract.ElementEntry.COLUMN_NAME_TRANSITION));
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getLinkTo() {
        return mLinkTo;
    }

    public void setLinkTo(String linkTo) {
        this.mLinkTo = linkTo;
    }

    public String getMockId() {
        return mMockId;
    }

    public void setMockId(String mockId) {
        this.mMockId = mockId;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        this.mY = y;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public String getTransition() {
        return mTransition;
    }

    public void setTransition(String transition) {
        this.mTransition = transition;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }
}
