package com.weis.videoxie.view;

import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.weis.videoxie.base.BaseView;
import com.weis.videoxie.bean.RecordCloudBean;
import com.weis.videoxie.bean.RecordSDBean;

import java.util.List;

public interface RecordView extends BaseView {
    public void getRecordSDSuccess(List<RecordSDBean> list);

    public void getRecordCloudSuccess(List<RecordCloudBean> list);

    public void showMessage(int code, String msg);

    void getDeviceInfoSuccess(EZDeviceInfo ezDeviceInfo, EZCameraInfo ezCameraInfo);

    void getDeviceInfoNext(EZDeviceInfo ezDeviceInfo);
}
