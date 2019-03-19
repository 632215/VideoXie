package com.weis.videoxie.presenter.impl;

import android.content.Context;

import com.weis.videoxie.bean.EmptyBean;
import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.presenter.listener.LoginListener;

import java.util.Map;

public class LoginImpl {
    private Context mContext;

    public LoginImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void login(Map map, final LoginListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<EmptyBean>() {
            @Override
            public void onNext(EmptyBean info) {
                listener.loginNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.onError(Code, Msg);
            }
        };
        HttpMethods.getInstance().login(new ProgressSubscriber<HttpBean<EmptyBean>>(nextListener, mContext), map);
    }
}
