package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class AcacheUserBean implements Serializable {

    private String name;//用户名
    private String pwd;
    private DeviceBean deviceBean;

    public AcacheUserBean() {
    }

    public AcacheUserBean(String name) {
        this.name = name;
    }

    public AcacheUserBean(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public AcacheUserBean(String name, DeviceBean deviceBean) {
        this.name = name;
        this.deviceBean = deviceBean;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    public static class DeviceBean implements Serializable {
        private int wxdepartid;
        private List<ProvinceListBean> provinceList;

        public int getWxdepartid() {
            return wxdepartid;
        }

        public void setWxdepartid(int wxdepartid) {
            this.wxdepartid = wxdepartid;
        }

        public List<ProvinceListBean> getProvinceList() {
            return provinceList;
        }

        public void setProvinceList(List<ProvinceListBean> provinceList) {
            this.provinceList = provinceList;
        }

        public static class ProvinceListBean implements Serializable{
            /**
             * provinceName : 重庆市
             * provinceId : 1547609276321
             * wxdepartmentList : [{"wxdepartName":"重庆南电科技有限公司","wxdepartId":1,"provinceId":"","visualization":{"Name":"全塔可视化","lineList":[]},"comprehensive":{"Name":"综合在线监测","deviceList":[{"deviceSerial":"C86684173","deviceName":"测试","wxdepartId":1,"account":"cqndkj","lon":"106.502323","lat":"29.625484","isLocation":0,"timeList":""}]}},{"wxdepartName":"重庆南电测试","wxdepartId":9,"provinceId":"","visualization":{"Name":"全塔可视化","lineList":[]},"comprehensive":{"Name":"综合在线监测","deviceList":[{"deviceSerial":"C26620322","deviceName":"南电样机1","wxdepartId":9,"account":"cqndkj","lon":"104.06769","lat":"30.593995","isLocation":1,"timeList":""},{"deviceSerial":"C26620126","deviceName":"220kV卓旗Ⅱ线116号","wxdepartId":9,"account":"cqndkj","lon":"112.218504","lat":"41.024094","isLocation":1,"timeList":""},{"deviceSerial":"C61921817","deviceName":"220kV麻兴(原包头换回设备)","wxdepartId":9,"account":"cqset_nm_bt","lon":"109.90017","lat":"40.62749","isLocation":0,"timeList":""},{"deviceSerial":"C53078445","deviceName":"旗都","wxdepartId":9,"account":"cqndkj","lon":"111.236","lat":"39.8657","isLocation":0,"timeList":""}]}},{"wxdepartName":"南电销售","wxdepartId":16,"provinceId":"","visualization":{"Name":"全塔可视化","lineList":[]},"comprehensive":{"Name":"综合在线监测","deviceList":[]}},{"wxdepartName":"南电管理","wxdepartId":17,"provinceId":"","visualization":{"Name":"全塔可视化","lineList":[]},"comprehensive":{"Name":"综合在线监测","deviceList":[]}},{"wxdepartName":"重庆南电科技试用","wxdepartId":27,"provinceId":"","visualization":{"Name":"全塔可视化","lineList":[]},"comprehensive":{"Name":"综合在线监测","deviceList":[]}},{"wxdepartName":"重庆检修公司","wxdepartId":31,"provinceId":"","visualization":{"Name":"全塔可视化","lineList":[]},"comprehensive":{"Name":"综合在线监测","deviceList":[{"deviceSerial":"C26619659","deviceName":"500kV思长I线026号大号侧","wxdepartId":31,"account":"cqndkj","lon":"106.664","lat":"29.8012","isLocation":1,"timeList":""},{"deviceSerial":"C26619125","deviceName":"500kV思长I线026号小号侧","wxdepartId":31,"account":"cqndkj","lon":"106.664","lat":"29.8012","isLocation":1,"timeList":""}]}}]
             */

            private String provinceName;
            private String provinceId;
            private List<WxdepartmentListBean> wxdepartmentList;

            public String getProvinceName() {
                return provinceName;
            }

            public void setProvinceName(String provinceName) {
                this.provinceName = provinceName;
            }

            public String getProvinceId() {
                return provinceId;
            }

            public void setProvinceId(String provinceId) {
                this.provinceId = provinceId;
            }

            public List<WxdepartmentListBean> getWxdepartmentList() {
                return wxdepartmentList;
            }

            public void setWxdepartmentList(List<WxdepartmentListBean> wxdepartmentList) {
                this.wxdepartmentList = wxdepartmentList;
            }
        }
    }
}
