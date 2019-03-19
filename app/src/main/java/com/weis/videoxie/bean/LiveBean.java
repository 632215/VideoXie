package com.weis.videoxie.bean;

import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;

import java.io.Serializable;

public class LiveBean implements Serializable {
    DeviceListBean deviceBean;
    EZCameraInfo ezCameraInfo;
    EZDeviceInfo ezDeviceInfo;

    public LiveBean() {
    }

    public LiveBean(DeviceListBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    public LiveBean(DeviceListBean deviceBean, EZCameraInfo ezCameraInfo, EZDeviceInfo ezDeviceInfo) {
        this.deviceBean = deviceBean;
        this.ezCameraInfo = ezCameraInfo;
        this.ezDeviceInfo = ezDeviceInfo;
    }

    public DeviceListBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceListBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    public EZCameraInfo getEzCameraInfo() {
        return ezCameraInfo;
    }

    public void setEzCameraInfo(EZCameraInfo ezCameraInfo) {
        this.ezCameraInfo = ezCameraInfo;
    }

    public EZDeviceInfo getEzDeviceInfo() {
        return ezDeviceInfo;
    }

    public void setEzDeviceInfo(EZDeviceInfo ezDeviceInfo) {
        this.ezDeviceInfo = ezDeviceInfo;
    }
}
