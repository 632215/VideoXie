package com.weis.videoxie.presenter.impl;

import android.content.Context;

import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.presenter.PatrolMsgPresenter;
import com.weis.videoxie.presenter.listener.PatrolMsgListener;

import java.util.Map;

public class PatrolMsgImpl {
    private Context mContext = null;

    public PatrolMsgImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void getCMsg(Map map, final PatrolMsgListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<PatrolMsgBean>() {
            @Override
            public void onNext(PatrolMsgBean info) {
                listener.getMsgNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.onError(Integer.parseInt(Code), Msg);
            }
        };
        HttpMethods.getInstance().getPatrolCMsg(new ProgressSubscriber<HttpBean<PatrolMsgBean>>(nextListener, mContext), map);
    }

    public void getVMsg(Map map, final PatrolMsgListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<PatrolMsgBean>() {
            @Override
            public void onNext(PatrolMsgBean info) {
                listener.getMsgNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.onError(Integer.parseInt(Code), Msg);
            }
        };
        HttpMethods.getInstance().getPatrolVMsg(new ProgressSubscriber<HttpBean<PatrolMsgBean>>(nextListener, mContext), map);

    }
}
