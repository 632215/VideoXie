package com.weis.videoxie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/2.
 */

public class DateFormatUtils {
    private static SimpleDateFormat sdf;
    private Date date;

    public static String dateToMs(String format, String time) throws ParseException {
        sdf = new SimpleDateFormat(format);
        return String.valueOf(sdf.parse(time).getTime());
    }
}
