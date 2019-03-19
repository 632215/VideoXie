package com.weis.videoxie.view;

import com.weis.videoxie.base.BaseView;
import com.weis.videoxie.bean.AcacheUserBean;

public interface DeviceFragmentView extends BaseView {
    void getDeviceNext(AcacheUserBean.DeviceBean info);

    void getAccessTokenNext();

    void showMessage(int errCode, String errMsg);
}
