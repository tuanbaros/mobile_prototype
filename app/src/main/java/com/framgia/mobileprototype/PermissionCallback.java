package com.framgia.mobileprototype;

/**
 * Created by tuannt on 27/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.util
 */
public interface PermissionCallback {
    void onPermissionGranted(String[] permissions);
    void onPermissionDenied(String[] permissions);
}
