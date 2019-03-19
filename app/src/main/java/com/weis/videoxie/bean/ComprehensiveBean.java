package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class ComprehensiveBean implements Serializable {
    /**
     * Name : 综合在线监测
     * deviceList : [{"deviceSerial":"C86684173","deviceName":"测试","wxdepartId":1,"account":"cqndkj","lon":"106.502323","lat":"29.625484","isLocation":0,"timeList":""}]
     */

    private String Name;
    private List<DeviceListBean> deviceList;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }
}
