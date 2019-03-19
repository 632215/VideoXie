package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class AcacheTokenBean implements Serializable {
    private List<AccessTokenBean> list;

    public AcacheTokenBean() {
    }

    public AcacheTokenBean(List<AccessTokenBean> list) {
        this.list = list;
    }

    public List<AccessTokenBean> getList() {
        return list;
    }

    public void setList(List<AccessTokenBean> list) {
        this.list = list;
    }
}
