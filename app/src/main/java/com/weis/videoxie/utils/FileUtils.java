package com.weis.videoxie.utils;

import android.content.Context;


import com.weis.videoxie.config.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Administrator on 2018/3/29.
 */

public class FileUtils {
//    public static void saveBitmap(byte[] bitmap, String fileName, Context mContext) {
//        if (bitmap != null) {
//            File file = new File(AppConfig.PICTURE_ADDRESS + "/" + fileName + ".png");
//            if (file.exists()) {
//                file.delete();
//            }
//            if (!file.getParentFile().exists())
//                file.getParentFile().mkdirs();
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write(bitmap);
//                fileOutputStream.close();
//            } catch (Exception e) {
//                LogUtils.e("32s：保存失败");
//                e.printStackTrace();
//            }
//        } else
//            LogUtils.e("32s：图片不存在");
//    }
}
