package com.framgia.mobileprototype.login;

import java.security.SecureRandom;

/**
 * Created by tuannt on 5/3/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.login
 */
public class RandomHelper {
    private static final String AB =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
