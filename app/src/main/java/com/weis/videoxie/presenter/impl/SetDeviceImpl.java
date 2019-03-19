package com.weis.videoxie.presenter.impl;

import android.content.Context;

import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.SetNameBean;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.presenter.listener.SetDeviceListener;

import java.util.Map;

public class SetDeviceImpl {
    private Context mContext;

    public SetDeviceImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void setDeviceName(Map map, final SetDeviceListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<SetNameBean>() {
            @Override
            public void onNext(SetNameBean info) {
                listener.setDeviceNameNext();
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.showMessage(Integer.parseInt(Code), Msg);
            }
        };
        HttpMethods.getInstance().setDeviceName(new ProgressSubscriber<HttpBean<SetNameBean>>(nextListener, mContext), map);

    }
}
