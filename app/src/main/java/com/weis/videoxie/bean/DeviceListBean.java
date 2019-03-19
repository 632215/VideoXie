package com.weis.videoxie.bean;

import java.io.Serializable;

public class DeviceListBean implements Serializable{
    /**
     * towerId :
     * deviceSerial : C54378332
     * deviceName : 110kv#测试
     * account : cqndkj
     * lon : 106.502323
     * lat : 29.625484
     * isLocation : 1
     * isMajorMinor : 1
     * timeList : 10,16
     */
    private String imgUrl;
    private int status;
    private String towerId;
    private String deviceSerial;
    private String deviceName;
    private String account;
    private String lon;
    private String lat;
    private int isLocation;
    private int isMajorMinor;
    private String timeList;
    private AccessTokenBean accessTokenBean;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public AccessTokenBean getAccessTokenBean() {
        return accessTokenBean;
    }

    public void setAccessTokenBean(AccessTokenBean accessTokenBean) {
        this.accessTokenBean = accessTokenBean;
    }

    public String getTowerId() {
        return towerId;
    }

    public void setTowerId(String towerId) {
        this.towerId = towerId;
    }

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public int getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(int isLocation) {
        this.isLocation = isLocation;
    }

    public int getIsMajorMinor() {
        return isMajorMinor;
    }

    public void setIsMajorMinor(int isMajorMinor) {
        this.isMajorMinor = isMajorMinor;
    }

    public String getTimeList() {
        return timeList;
    }

    public void setTimeList(String timeList) {
        this.timeList = timeList;
    }
}
