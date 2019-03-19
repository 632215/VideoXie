package com.weis.videoxie.presenter.impl;

import android.content.Context;

import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.presenter.PatrolDetailPresenter;
import com.weis.videoxie.presenter.listener.PatrolDetailListener;
import com.weis.videoxie.presenter.listener.PatrolMsgListener;

import java.util.Map;

public class PatrolDetailImpl {
    private Context mContext = null;

    public PatrolDetailImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void getMsg(Map map, final PatrolDetailListener listener) {
//        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<PatrolMsgBean>() {
//            @Override
//            public void onNext(PatrolMsgBean info) {
//                listener.getMsgNext(info);
//            }
//
//            @Override
//            public void onError(String Code, String Msg) {
//                listener.onError(Integer.parseInt(Code), Msg);
//            }
//        };
//        HttpMethods.getInstance().getPatrolMsg(new ProgressSubscriber<HttpBean<PatrolMsgBean>>(nextListener, mContext), map);
    }

    public void getPushInfo(Map map, final PatrolDetailListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<PatrolMsgBean.LogListBean>() {
            @Override
            public void onNext(PatrolMsgBean.LogListBean info) {
                listener.getPushInfoNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.getPushInfoError(Integer.parseInt(Code), Msg);
            }
        };
        HttpMethods.getInstance().getPushInfo(new ProgressSubscriber<HttpBean<PatrolMsgBean.LogListBean>>(nextListener, mContext), map);
    }
}
