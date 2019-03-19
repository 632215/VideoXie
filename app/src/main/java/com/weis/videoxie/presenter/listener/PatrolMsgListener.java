package com.weis.videoxie.presenter.listener;

import com.weis.videoxie.bean.PatrolMsgBean;

public interface PatrolMsgListener {
    void getMsgNext(PatrolMsgBean info);

    void onError(int i, String msg);
}
