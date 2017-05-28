package com.framgia.mobileprototype.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuannt on 5/23/17.
 * Project: mobile_prototype
 * Package: com.framgia.mobileprototype.util
 */
public class EntryIdUtil {
    public static String get() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("HHmmssSSSddMMyyyyZ", Locale.ENGLISH);
//        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
//        Matcher match = pt.matcher(c);
//        while (match.find()) {
//            String s = match.group();
//            c = c.replaceAll("\\" + s, "");
//        }
        return format.format(date).replaceAll("\\-", "").replaceAll("\\+", "");
    }
}
