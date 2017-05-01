package com.framgia.mobileprototype.projects;

import com.framgia.mobileprototype.data.model.User;

/**
 * Created by tuannt on 12/04/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.projects
 */
public class ProjectEvent {
    private int mId;
    private User mUser;

    public ProjectEvent(int id) {
        mId = id;
    }

    public ProjectEvent() {}

    public int getId() {
        return mId;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }
}
