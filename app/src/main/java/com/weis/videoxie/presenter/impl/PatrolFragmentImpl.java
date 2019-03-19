package com.weis.videoxie.presenter.impl;

import android.content.Context;

import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.PatrolBean;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.presenter.listener.PatrolFragmentListener;

import java.util.Map;

public class PatrolFragmentImpl {
    private Context mContext;

    public PatrolFragmentImpl(Context mContext) {
        this.mContext = mContext;
    }

    public void getPatrol(Map map, final PatrolFragmentListener listener) {
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<PatrolBean>() {
            @Override
            public void onNext(PatrolBean info) {
                listener.getPatrolNext(info);
            }

            @Override
            public void onError(String Code, String Msg) {
                listener.onError(Integer.parseInt(Code), Msg);
            }
        };
        HttpMethods.getInstance().getPatrol(new ProgressSubscriber<HttpBean<PatrolBean>>(nextListener, mContext), map);
    }
}
