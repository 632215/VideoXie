package com.weis.videoxie.bean;

public class DeviceHelpBean {
    private int position;
    private String url;

    public DeviceHelpBean(int position, String url) {
        this.position = position;
        this.url = url;
    }

    public DeviceHelpBean() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
