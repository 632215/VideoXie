package com.weis.videoxie.presenter.impl;

import android.content.Context;

import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.AccessTokenBean;
import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.presenter.listener.DeviceFragmentListener;

import java.util.Map;

public class DeviceFragmentImpl {
    private Context mContext;

    public DeviceFragmentImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void getDevice(Map map, final DeviceFragmentListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<AcacheUserBean.DeviceBean>() {
            @Override
            public void onNext(AcacheUserBean.DeviceBean info) {
                listener.getDeviceNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.onError(Code, Msg);
            }
        };
        HttpMethods.getInstance().getDevice(new ProgressSubscriber<HttpBean<AcacheUserBean.DeviceBean>>(nextListener, mContext), map);
    }

    public void getAccessToken(Map map, final DeviceFragmentListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<AccessTokenBean>() {
            @Override
            public void onNext(AccessTokenBean info) {
                listener.getAccessTokenNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.onError(Code, Msg);
            }
        };
        HttpMethods.getInstance().getAccessToken(new ProgressSubscriber<HttpBean<AccessTokenBean>>(nextListener, mContext), map);
    }

}
