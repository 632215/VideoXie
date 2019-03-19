package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class AcachePoliceBean implements Serializable{
    private List<JPushBean> list;

    public List<JPushBean> getList() {
        return list;
    }

    public void setList(List<JPushBean> list) {
        this.list = list;
    }
}
