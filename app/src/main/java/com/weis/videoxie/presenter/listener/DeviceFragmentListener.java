package com.weis.videoxie.presenter.listener;

import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.AccessTokenBean;

public interface DeviceFragmentListener {

    void onError(String code, String msg);

    void getDeviceNext(AcacheUserBean.DeviceBean info);

    void getAccessTokenNext(AccessTokenBean info);
}
