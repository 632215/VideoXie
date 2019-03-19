package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class VisualizationBean implements Serializable{
    /**
     * Name : 全塔可视化
     * lineList : []
     */

    private String Name;
    private List<LineListBean> lineList;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public List<LineListBean> getLineList() {
        return lineList;
    }

    public void setLineList(List<LineListBean> lineList) {
        this.lineList = lineList;
    }
}
