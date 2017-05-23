package com.framgia.mobileprototype.data.remote;

/**
 * Created by tuannt on 5/20/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.remote
 */
public class ApiService {

    public static final String FIREBASE_BUCKET = "gs://test-40620.appspot.com";

    public static final String FIREBASE_FOLDER = "mp/";

    //private static final String HOST = "http://192.168.0.9/prototype/api/";

    private static final String HOST = "http://tongdaifpthanoi.net/prototype/api/";

    public static final String LOGIN = "login";

    public static final String UPLOAD = "upload";

    public static final String PROJECTS = "projects/{offset}";

    public static final int TIME_OUT = 15;

    public static String getApi(String api) {
        return HOST + api;
    }

    public class Param {
        public static final String OPEN_ID = "open_id";
        public static final String TOKEN = "token";
        public static final String NAME = "name";
        public static final String PROJECT = "project";
        public static final String OFFSET = "offset";
    }

    public class Response {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
    }
}
