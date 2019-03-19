package com.weis.videoxie.view;

import com.weis.videoxie.base.BaseView;

public interface LoginView extends BaseView {
    void showMessage(String code, String msg);

    void loginNext();
}
