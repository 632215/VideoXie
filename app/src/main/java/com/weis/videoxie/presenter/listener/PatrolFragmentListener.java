package com.weis.videoxie.presenter.listener;

import com.weis.videoxie.bean.PatrolBean;

public interface PatrolFragmentListener {
    void getPatrolNext(PatrolBean info);

    void onError(int code, String msg);
}
