package com.weis.videoxie.presenter.impl;

import android.content.Context;

import com.weis.videoxie.bean.DepartmentBean;
import com.weis.videoxie.bean.EmptyBean;
import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.PatrolBean;
import com.weis.videoxie.bean.RegisterResultBean;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.presenter.RegisterPresenter;
import com.weis.videoxie.presenter.listener.RegisterListener;

import java.util.Map;

public class RegisterImpl {
    private Context mContext;

    public RegisterImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void getDepartment(Map map, final RegisterListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<DepartmentBean>() {
            @Override
            public void onNext(DepartmentBean info) {
                listener.getDepartmentNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.onError(Integer.parseInt(Code), Msg);
            }
        };
        HttpMethods.getInstance().getDepartment(new ProgressSubscriber<HttpBean<DepartmentBean>>(nextListener, mContext), map);
    }

    public void register(Map map,  final RegisterListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<RegisterResultBean>() {
            @Override
            public void onNext(RegisterResultBean info) {
                listener.registerNext();
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.registerError(Integer.parseInt(Code), Msg);
            }
        };
        HttpMethods.getInstance().register(new ProgressSubscriber<HttpBean<RegisterResultBean>>(nextListener, mContext), map);
    }
}
