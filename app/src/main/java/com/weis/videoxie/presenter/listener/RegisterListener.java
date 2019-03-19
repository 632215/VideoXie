package com.weis.videoxie.presenter.listener;

import com.weis.videoxie.bean.DepartmentBean;

public interface RegisterListener {
    void getDepartmentNext(DepartmentBean info);

    void onError(int i, String msg);

    void registerNext();

    void registerError(int i, String msg);
}
