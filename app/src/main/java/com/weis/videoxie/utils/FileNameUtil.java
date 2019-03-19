package com.weis.videoxie.utils;

import android.os.Environment;

import java.util.UUID;

/**
 * Created by Administrator on 2017/11/7.
 */

public class FileNameUtil {
    public static String getrandom() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().trim().replace("-", "");
    }

    public static String getRecordName() {
        java.util.Date date = new java.util.Date();
        String strRecordFile = Environment.getExternalStorageDirectory().getPath() + "/EZOpenSDK/Records/" + String.format("%tY", date)
                + String.format("%tm", date) + String.format("%td", date) + "/"
                + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) + ".mp4";

        return strRecordFile;
    }

    public static String getCaptureName() {
        java.util.Date date = new java.util.Date();
        final String path = Environment.getExternalStorageDirectory().getPath() + "/EZOpenSDK/CapturePicture/" + String.format("%tY", date)
                + String.format("%tm", date) + String.format("%td", date) + "/"
                + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) + ".jpg";
        return path;
    }

}
