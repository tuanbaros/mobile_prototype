package com.framgia.mobileprototype.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tuannt on 22/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.model
 */
public class Mock {
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

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
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

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public List<Element> getElements() {
        return mElements;
    }

    public void setElements(List<Element> elements) {
        this.mElements = elements;
    }
}
