package com.framgia.mobileprototype.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tuannt on 5/28/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.model
 */
public class Comment {
    @SerializedName("id")
    private String mId;

    @SerializedName("content")
    private String mContent;

    @SerializedName("user")
    private String mUsername;

    @SerializedName("project_id")
    private String mProjectId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        mProjectId = projectId;
    }
}
