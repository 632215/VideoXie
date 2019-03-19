package com.weis.videoxie.view;

import com.weis.videoxie.bean.PatrolMsgBean;

public interface PatrolMsgView {
    void showMessage(int i, String msg);

    void getMsgNext(PatrolMsgBean info);
}
