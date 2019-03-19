package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class TowerListBean implements Serializable {
    private String towerId;
    private String towerName;
    private String lineId;
    private List<DeviceListBean> deviceList;

    public String getTowerId() {
        return towerId;
    }

    public void setTowerId(String towerId) {
        this.towerId = towerId;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }
}
