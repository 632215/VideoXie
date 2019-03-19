package com.weis.videoxie.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Administrator on 2018/2/11.
 */

public class LocationServiceUtils {

    /**
     * 判断是否开启定位服务
     *
     * @param context
     * @return
     */
    public static boolean checkLocationEnable(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//>19
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (locationProviders != null && locationProviders.trim() != "") {
                return Boolean.parseBoolean(locationProviders);
            } else {
                return false;
            }
        }
    }
}
