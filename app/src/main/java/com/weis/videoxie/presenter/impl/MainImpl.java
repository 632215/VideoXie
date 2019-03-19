package com.weis.videoxie.presenter.impl;

import android.content.Context;

public class MainImpl {
    private Context mContext;

    public MainImpl(Context mContext) {
        this.mContext = mContext;
    }

//    public void getAccessToken(Map map, final MainListener listener) {
//        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<AcacheUserBean.DeviceBean.DeviceListBean.AccessTokenBean>() {
//            @Override
//            public void onNext(AcacheUserBean.DeviceBean.DeviceListBean.AccessTokenBean info) {
//                listener.getAccessTokenNext(info);
//            }
//
//            @Override
//            public void showMsg(String Code, String Msg) {
//                listener.showMsg(Code, Msg);
//            }
//        };
//        HttpMethods.getInstance().getAccessToken(new ProgressSubscriber<HttpBean<AcacheUserBean.DeviceBean.DeviceListBean.AccessTokenBean>>(nextListener, mContext), map);
//    }
}
