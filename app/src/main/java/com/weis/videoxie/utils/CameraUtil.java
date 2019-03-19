package com.weis.videoxie.utils;

import android.hardware.Camera;

/**
 * Created by Administrator on 2018/6/8.
 */

public class CameraUtil {
    //判断相机是否可以使用
    public static boolean checkCameraPermission() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }
}
