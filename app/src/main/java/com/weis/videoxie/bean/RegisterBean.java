package com.weis.videoxie.bean;

public class RegisterBean {

    /**
     * userid : xyb
     * password : 123456
     * name : xxx
     * wxdepartid : 1
     * position : 经理
     * sex : 2
     * mobile : 13996944444
     */

    private String userid;
    private String password;
    private String name;
    private int wxdepartid;
    private String position;
    private int sex;
    private String mobile;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWxdepartid() {
        return wxdepartid;
    }

    public void setWxdepartid(int wxdepartid) {
        this.wxdepartid = wxdepartid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
