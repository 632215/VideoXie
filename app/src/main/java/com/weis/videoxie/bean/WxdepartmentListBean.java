package com.weis.videoxie.bean;

import java.io.Serializable;

public class WxdepartmentListBean implements Serializable{
    /**
     * wxdepartName : 重庆南电科技有限公司
     * wxdepartId : 1
     * provinceId :
     * visualization : {"Name":"全塔可视化","lineList":[]}
     * comprehensive : {"Name":"综合在线监测","deviceList":[{"deviceSerial":"C86684173","deviceName":"测试","wxdepartId":1,"account":"cqndkj","lon":"106.502323","lat":"29.625484","isLocation":0,"timeList":""}]}
     */

    private String wxdepartName;
    private int wxdepartId;
    private String provinceId;
    private VisualizationBean visualization;
    private ComprehensiveBean comprehensive;

    public String getWxdepartName() {
        return wxdepartName;
    }

    public void setWxdepartName(String wxdepartName) {
        this.wxdepartName = wxdepartName;
    }

    public int getWxdepartId() {
        return wxdepartId;
    }

    public void setWxdepartId(int wxdepartId) {
        this.wxdepartId = wxdepartId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public VisualizationBean getVisualization() {
        return visualization;
    }

    public void setVisualization(VisualizationBean visualization) {
        this.visualization = visualization;
    }

    public ComprehensiveBean getComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(ComprehensiveBean comprehensive) {
        this.comprehensive = comprehensive;
    }
}
