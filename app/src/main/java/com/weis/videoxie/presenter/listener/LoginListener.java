package com.weis.videoxie.presenter.listener;

import com.weis.videoxie.bean.EmptyBean;

public interface LoginListener {
    void loginNext(EmptyBean info);

    void onError(String code, String msg);
}
