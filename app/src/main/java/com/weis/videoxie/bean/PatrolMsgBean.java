package com.weis.videoxie.bean;

import java.io.Serializable;
import java.util.List;

public class PatrolMsgBean implements Serializable {

    /**
     * deviceSerial : 203490795
     * logList : [{"pushTime":"2019-1-22 14:00:00","picUrlList":["http://ip:port/C543660691547776800084.jpg"],"type":"定时巡检","weather":{"tmp":"-1","fl":"-5","txt":"晴","vis":"30","spd":"2.22","dir":"西北风","sc":"2","pcpn":"0.0","hum":"29","pres":"1038","aqi":"35","brf":"很强","qlty":"优","no2":"3","so2":"5","co":"0.6","pm25":"10","o3":"87"}},{"pushTime":"2019-1-21 16:00:00","picUrlList":["http://ip:port/C543660691547776800084.jpg"],"type":"定时巡检","weather":{"tmp":"-1","fl":"-5","txt":"晴","vis":"30","spd":"2.22","dir":"西北风","sc":"2","pcpn":"0.0","hum":"29","pres":"1038","aqi":"35","brf":"很强","qlty":"优","no2":"3","so2":"5","co":"0.6","pm25":"10","o3":"87"}}]
     */
    private String towerId;
    private String deviceSerial;
    private List<LogListBean> logList;

    public String getTowerId() {
        return towerId;
    }

    public void setTowerId(String towerId) {
        this.towerId = towerId;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public List<LogListBean> getLogList() {
        return logList;
    }

    public void setLogList(List<LogListBean> logList) {
        this.logList = logList;
    }

    public static class LogListBean implements Serializable{
        /**
         * pushTime : 2019-1-22 14:00:00
         * picUrlList : ["http://ip:port/C543660691547776800084.jpg"]
         * type : 定时巡检
         * weather : {"tmp":"-1","fl":"-5","txt":"晴","vis":"30","spd":"2.22","dir":"西北风","sc":"2","pcpn":"0.0","hum":"29","pres":"1038","aqi":"35","brf":"很强","qlty":"优","no2":"3","so2":"5","co":"0.6","pm25":"10","o3":"87"}
         */

        private String pushTime;
        private String type;
        private WeatherBean weather;
        private List<String> picUrlList;

        public String getPushTime() {
            return pushTime;
        }

        public void setPushTime(String pushTime) {
            this.pushTime = pushTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public WeatherBean getWeather() {
            return weather;
        }

        public void setWeather(WeatherBean weather) {
            this.weather = weather;
        }

        public List<String> getPicUrlList() {
            return picUrlList;
        }

        public void setPicUrlList(List<String> picUrlList) {
            this.picUrlList = picUrlList;
        }

        public static class WeatherBean implements Serializable{
            /**
             * tmp : -1
             * fl : -5
             * txt : 晴
             * vis : 30
             * spd : 2.22
             * dir : 西北风
             * sc : 2
             * pcpn : 0.0
             * hum : 29
             * pres : 1038
             * aqi : 35
             * brf : 很强
             * qlty : 优
             * no2 : 3
             * so2 : 5
             * co : 0.6
             * pm25 : 10
             * o3 : 87
             */

            private String tmp;
            private String fl;
            private String txt;
            private String vis;
            private String spd;
            private String dir;
            private String sc;
            private String pcpn;
            private String hum;
            private String pres;
            private String aqi;
            private String brf;
            private String qlty;
            private String no2;
            private String so2;
            private String co;
            private String pm25;
            private String o3;

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getNo2() {
                return no2;
            }

            public void setNo2(String no2) {
                this.no2 = no2;
            }

            public String getSo2() {
                return so2;
            }

            public void setSo2(String so2) {
                this.so2 = so2;
            }

            public String getCo() {
                return co;
            }

            public void setCo(String co) {
                this.co = co;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getO3() {
                return o3;
            }

            public void setO3(String o3) {
                this.o3 = o3;
            }
        }
    }
}
