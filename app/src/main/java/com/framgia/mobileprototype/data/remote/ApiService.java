package com.framgia.mobileprototype.data.remote;

/**
 * Created by tuannt on 5/20/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.remote
 */
public class ApiService {

    public static final String FIREBASE_BUCKET = "gs://test-40620.appspot.com";

    public static final String FIREBASE_FOLDER = "mp/";

    private static final String HOME_HOST = "http://192.168.0.9/prototype/api/";

    private static final String COMPANY_HOST = "http://192.168.6.94/prototype/api/";

    private static final String LIVE_HOST = "http://tongdaifpthanoi.net/prototype/api/";

    public static final String LOGIN = "login";

    public static final String UPLOAD = "upload";

    public static final String PROJECTS = "projects/{offset}";

    public static final String DOWNLOAD = "download";

    public static final String COMMENTS = "comments/{project_entry_id}/{offset}";

    public static final String COMMENT = "comment";

    public static final int TIME_OUT = 15;

    public static String getApi(String api) {
        return HOME_HOST + api;
    }

    public class Param {
        public static final String OPEN_ID = "open_id";
        public static final String TOKEN = "token";
        public static final String NAME = "name";
        public static final String PROJECT = "project";
        public static final String PROJECT_ENTRY_ID = "project_entry_id";
        public static final String OFFSET = "offset";
        public static final String COMMENT = "comment";
        public static final String LAST_ID = "last_id";
    }

    public class Response {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
    }
}
