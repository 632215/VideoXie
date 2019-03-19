package com.weis.videoxie.utils;

import android.content.Context;
import android.os.Message;

import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.WeatherBean;
import com.weis.videoxie.config.AppConfig;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeatherUtils {
    private WeatherListener listener;

    public WeatherUtils(WeatherListener listener) {
        this.listener = listener;
    }

    public void getWeather(Context context,DeviceListBean targetBean) {
        final Message msg = new Message();
        if (targetBean==null||StringUtils.isEmpty(targetBean.getLat())||StringUtils.isEmpty(targetBean.getLon()))
        {
            ToastUtils.showShort(context,"数据存在问题");
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("city", targetBean.getLat() + "," + targetBean.getLon())
                .add("key", AppConfig.WeatherKey).build();
        final Request request = new Request.Builder()
                .url(AppConfig.WeatherUrlNow)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.getWeatherError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() != 200)
                    listener.getWeatherError(response.message().toString());
                else {
                    WeatherBean weatherBean = new WeatherBean();
                    try {
                        weatherBean = GsonUtil.GsonToBean(response.body().string(), WeatherBean.class);
                    } catch (Exception e) {
                        listener.getWeatherError("解析GSON异常！");
                    }
                    listener.getWeatherNext(weatherBean);
                }
            }
        });
    }

    public interface WeatherListener {
        void getWeatherError(String msg);

        void getWeatherNext(WeatherBean weatherBean);
    }
}
