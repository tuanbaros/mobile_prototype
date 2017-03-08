package com.framgia.mobileprototype;

import android.os.Environment;

/**
 * Created by tuannt on 27/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype
 */
public class Constant {
    public static final String FILE_PATH =
        Environment.getExternalStorageDirectory() + "/MobilePrototype/";
    public static final String PACKAGE = "package:";
    public static final String DEFAULT_COMPRESS_FORMAT = ".png";
    public static final int DEFAULT_IMAGE_WIDTH = 100;
    public static final int DEFAULT_IMAGE_HEIGHT = 150;
    public static final String ASSET_PATH = "file:///android_asset/";
    public static final String BASE_MARKET_SCHEMA = "market://details?id=";
    public static final String BASE_MARKET_URL = "https://play.google.com/store/apps/details?id=";
    public static final int NUMBER_COLUMN_GRID_PORTRAIT = 3;
    public static final int NUMBER_COLUMN_GRID_LANDSCAPE = 2;
}
