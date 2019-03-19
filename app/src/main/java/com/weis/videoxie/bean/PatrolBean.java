package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class PatrolBean implements Serializable{

    private List<DeviceListBean> deviceList;
    private List<TowerListBean> towerList;

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }

    public List<TowerListBean> getTowerList() {
        return towerList;
    }

    public void setTowerList(List<TowerListBean> towerList) {
        this.towerList = towerList;
    }

    public static class DeviceListBean implements Serializable{
        /**
         * deviceSerial : 185376041
         * deviceName : 110kV固西Ⅰ 回100号
         */

        private String deviceSerial;
        private String deviceName;

        public String getDeviceSerial() {
            return deviceSerial;
        }

        public void setDeviceSerial(String deviceSerial) {
            this.deviceSerial = deviceSerial;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }
    }

    public static class TowerListBean implements Serializable{
        /**
         * towerId : 1542956299719
         * towerName : 110kV渝黄线50#杆塔
         */

        private String towerId;
        private String towerName;

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
    }
}
