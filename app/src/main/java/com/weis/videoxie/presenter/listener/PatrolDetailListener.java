package com.weis.videoxie.presenter.listener;

import com.weis.videoxie.bean.PatrolMsgBean;

public interface PatrolDetailListener {
    void getPushInfoNext(PatrolMsgBean.LogListBean info);

    void getPushInfoError(int i, String msg);
}
