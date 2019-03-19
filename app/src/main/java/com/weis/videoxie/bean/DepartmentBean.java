package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class DepartmentBean implements Serializable {

    private List<DepartmentListBean> departmentList;

    public List<DepartmentListBean> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentListBean> departmentList) {
        this.departmentList = departmentList;
    }

    public static class DepartmentListBean implements Serializable {
        /**
         * wxdepartid : 20
         * wxdepartname : 乌海电业局
         */

        private int wxdepartid;
        private String wxdepartname;

        public int getWxdepartid() {
            return wxdepartid;
        }

        public void setWxdepartid(int wxdepartid) {
            this.wxdepartid = wxdepartid;
        }

        public String getWxdepartname() {
            return wxdepartname;
        }

        public void setWxdepartname(String wxdepartname) {
            this.wxdepartname = wxdepartname;
        }
    }
}
