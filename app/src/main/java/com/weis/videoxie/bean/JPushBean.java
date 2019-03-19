package com.weis.videoxie.bean;

import java.io.Serializable;

public class JPushBean implements Serializable {

    /**
     * alarmTime : 2019-1-24 14:56:39
     * pushTime : 1548312999040
     * picUrl : http://ozsdxplk0p.cdhttp.cn/C821902761548312999040.jpg
     * deviceSerial : C82190276
     * deviceName : 设备2
     * isVisualizition : false
     */

    private String name;
    private String alarmTime;
    private long pushTime;
    private String picUrl;
    private String deviceSerial;
    private String deviceName;
    private boolean isVisualizition;
    private PatrolMsgBean.LogListBean.WeatherBean weatherBean;
    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getPushTime() {
        return pushTime;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public boolean isIsVisualizition() {
        return isVisualizition;
    }

    public void setIsVisualizition(boolean isVisualizition) {
        this.isVisualizition = isVisualizition;
    }

    public boolean isVisualizition() {
        return isVisualizition;
    }

    public void setVisualizition(boolean visualizition) {
        isVisualizition = visualizition;
    }

    public PatrolMsgBean.LogListBean.WeatherBean getWeatherBean() {
        return weatherBean;
    }

    public void setWeatherBean(PatrolMsgBean.LogListBean.WeatherBean weatherBean) {
        this.weatherBean = weatherBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
