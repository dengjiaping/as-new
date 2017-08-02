package com.jkpg.ruchu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by qindi on 2017/5/19.
 */

public class DateUtil {
    public static String dateFormat(String date, String format) {
        //yyyy-MM-dd HH:mm:ss
        return new SimpleDateFormat(format, Locale.CANADA).format(new Date(Long.parseLong(date)));
    }
}
