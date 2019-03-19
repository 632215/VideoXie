package com.weis.videoxie.config;

public class AppConfig {
    public static String AcacheUserBean = "AcacheUserBean";
    public static String AcachePoliceBean = "AcachePoliceBean";
    public static String AcacheToken = "TokenList";
    public static String YingShiKey = "7bff7bf2d34849679fd5b40c5eab4f64";
    public static int CameraNo = 1;
    public static String WeatherUrlNow = "https://free-api.heweather.com/v5/weather";
    public static String WeatherKey = "dae6c5ea9a58403d9618adb20aa95117";
    public static String BuglyId = "59b3f78481";
    public static final int RECORD_SOURCE_SD = 0;
    public static final int RECORD_SOURCE_CLOUD = 1;
    public static String RECORD_SOURCE_SD_TX = "SD";
    public static String RECORD_SOURCE_CLOUD_TX= "云存储";

    //定义常量
    public static final int GET_EZDEVICE_SUCCESS = 3200;//获取设备成功
    public static final int GET_EZDEVICE_FAIL = 3201;//获取设备失败
    public static final int MSG_SET_VEDIOMODE_SUCCESS = 3202;//设置画质成功
    public static final int MSG_SET_VEDIOMODE_FAIL = 3203;//设置画质失败
    public static final int SET_CAMERA_ZOOM_SUCCESS = 3204;//设置摄像头缩放画质成功
    public static final int SET_CAMERA_ZOOM_FAIL = 3205;//设置摄像头缩放画质失败
    public static final int EZ_GET_CLOUD_RECORD_SUCCESS = 3206;//获取云存储成功
    public static final int EZ_GET_CLUOD_RECORD_FAIL = 3207;//获取云存储失败
    public static final int EZ_GET_SD_RECORD_SUCCESS = 3208;//获取SD存储成功
    public static final int EZ_GET_SD_RECORD_FAIL = 3209;//获取SD存储失败
    public static final int GET_EZDEVICE_SD_SUCCESS = 3210;//获取设备存储信息成功
    public static final int GET_EZDEVICE_SD_FAIL = 3211;//获取设备存储信息失败
    public static final int CLEAR_EZDEVICE_SD_SUCCESS = 3212;//清除设备存储信息成功
    public static final int CLEAR_EZDEVICE_SD_FAIL = 3213;//清除设备存储信息失败
    public static final int EZ_DATA_EMPTY = 3000;//数据为空

}
