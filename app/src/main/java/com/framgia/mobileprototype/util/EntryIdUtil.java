package com.framgia.mobileprototype.util;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuannt on 5/23/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.util
 */
public class EntryIdUtil {
    public static String get() {
        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
        String c = Calendar.getInstance().getTime().toString();
        Matcher match = pt.matcher(c);
        while (match.find()) {
            String s = match.group();
            c = c.replaceAll("\\" + s, "");
        }
        return c;
    }
}
