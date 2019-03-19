package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class LineListBean implements Serializable {
    private String lineName;
    private String lineId;
    private String wxdepartId;
    private List<TowerListBean> towerList;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getWxdepartId() {
        return wxdepartId;
    }

    public void setWxdepartId(String wxdepartId) {
        this.wxdepartId = wxdepartId;
    }

    public List<TowerListBean> getTowerList() {
        return towerList;
    }

    public void setTowerList(List<TowerListBean> towerList) {
        this.towerList = towerList;
    }
}
