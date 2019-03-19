package com.weis.videoxie.bean;

import java.util.List;

public class DevicePicBean {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * deviceSerial : C54378457
         * status : 1
         * poster : http://www.cqset.com:3000/C543784571550541600056.jpg
         */

        private String deviceSerial;
        private int status;
        private String poster;

        public String getDeviceSerial() {
            return deviceSerial;
        }

        public void setDeviceSerial(String deviceSerial) {
            this.deviceSerial = deviceSerial;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }
    }
}
