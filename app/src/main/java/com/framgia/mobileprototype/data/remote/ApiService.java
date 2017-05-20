package com.framgia.mobileprototype.data.remote;

/**
 * Created by tuannt on 5/20/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.data.remote
 */
public class ApiService {
    private static final String HOST = "http://192.168.0.9/prototype/api/";

    public static final String LOGIN = "login";

    public static String getApi(String api) {
        return HOST + api;
    }

    public class Param {
        public static final String OPEN_ID = "open_id";
        public static final String TOKEN = "token";
        public static final String NAME = "name";
    }

    public class Response {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
    }
}
