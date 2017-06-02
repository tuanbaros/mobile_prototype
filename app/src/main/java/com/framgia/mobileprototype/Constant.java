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
    public static final String FILE_TEMP =
        Environment.getExternalStorageDirectory() + "/temp";
    public static final String PACKAGE = "package:";
    public static final String DEFAULT_COMPRESS_FORMAT = ".png";
    public static final int DEFAULT_IMAGE_WIDTH = 100;
    public static final int DEFAULT_IMAGE_HEIGHT = 150;
    public static final String ASSET_PATH = "file:///android_asset/";
    public static final String BASE_MARKET_SCHEMA = "market://details?id=";
    public static final String BASE_MARKET_URL = "https://play.google.com/store/apps/details?id=";
    public static final int NUMBER_COLUMN_GRID_PORTRAIT = 3;
    public static final int NUMBER_COLUMN_GRID_LANDSCAPE = 2;
    public static final String ANDROID = "android";
    public static final String DIMEN = "dimen";
    public static final String STATUS_BAR_HEIGHT = "status_bar_height";
    public static final String IMAGE_RECENT_PATH = "image/*";
    public static final String DEFAULT_GESTURE = "Tap";
    public static final int MIN_SIZE = 150;
    public static final int SAMPLE_PROJECT_ID = 1;
    public static final int SAMPLE_MOCK_COUNT = 6;
    public static final String CURRENT_COLOR = "CURRENT_COLOR";
    public static final String EXPAND_TITLE = " (1)";
    public static final String SHARE_TYPE = "text/plain";
    public static final String PREFIX = "-";
}
