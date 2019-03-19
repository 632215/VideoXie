package com.weis.videoxie.view;


import com.videogo.openapi.bean.EZDeviceInfo;
import com.weis.videoxie.bean.WeatherBean;

public interface LiveView {
    void getWeatherNext(WeatherBean weatherBean);

    void getDeviceInfoNext(EZDeviceInfo ezDeviceInfo);

    void showMessage(int errCode, String errMessage);
}
