package com.weis.videoxie.presenter.listener;


import com.weis.videoxie.bean.WeatherBean;

public interface LiveListener{
    void getWeatherNext(WeatherBean weatherBean);

    void showMessage(String message);
}
