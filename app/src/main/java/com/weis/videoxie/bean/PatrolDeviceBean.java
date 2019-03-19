package com.weis.videoxie.bean;

import java.io.Serializable;

public class PatrolDeviceBean implements Serializable{
    String name;
    String id;
    String type;

    public PatrolDeviceBean() {
    }

    public PatrolDeviceBean(String name, String id, String type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
