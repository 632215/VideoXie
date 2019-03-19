package com.weis.videoxie.view;

import com.videogo.openapi.bean.EZDeviceInfo;

import java.util.List;

public interface DeviceView {
    void getDeviceInfoNext(EZDeviceInfo ezDeviceInfo);

    void showMessage(int errCode, String errMessage);

    void getSDInfoNext(List sdList);
}
