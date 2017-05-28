package com.framgia.mobileprototype.explore;

/**
 * Created by tuannt on 5/28/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.explore
 */
public class Download {
    private String mName;
    private String mUrl;

    public Download(String name, String url) {
        mName = name;
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }
}
