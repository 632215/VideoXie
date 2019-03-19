package com.weis.videoxie.view;

import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.bean.WeatherBean;

public interface PatrolDetailView {
    void showMessage(int i, String s);

    void getWeatherNext(WeatherBean weatherBean);

    void getPushInfoNext(PatrolMsgBean.LogListBean info);

    void getPushInfoError(int i, String msg);
}
