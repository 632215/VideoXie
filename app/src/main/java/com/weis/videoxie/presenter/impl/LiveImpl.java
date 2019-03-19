package com.weis.videoxie.presenter.impl;

import android.content.Context;
import android.os.Message;

import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.WeatherBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.presenter.listener.LiveListener;
import com.weis.videoxie.utils.GsonUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LiveImpl {
    private Context mContext;
    private LiveListener listener;

    public LiveImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void getWeather(DeviceListBean deviceBean, final LiveListener listener) {
        this.listener = listener;
        final Message msg = new Message();
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("city", deviceBean.getLat() + "," + deviceBean.getLon())
                .add("key", AppConfig.WeatherKey).build();
        final Request request = new Request.Builder()
                .url(AppConfig.WeatherUrlNow)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.showMessage(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() != 200)
                    listener.showMessage(response.message().toString());
                else {
                    WeatherBean weatherBean = new WeatherBean();
                    try {
                        weatherBean = GsonUtil.GsonToBean(response.body().string(), WeatherBean.class);
                    } catch (Exception e) {
                        listener.showMessage("解析GSON异常！");
                    }
                    listener.getWeatherNext(weatherBean);
                }
            }
        });
    }
}
