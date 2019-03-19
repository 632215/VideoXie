package com.weis.videoxie.bean;

import java.io.Serializable;

public class AccessTokenBean implements Serializable {

    /**
     * id : 9
     * account : cqndkj
     * wxdepartid : 1
     * appKey : 7bff7bf2d34849679fd5b40c5eab4f64
     * appSecret : 0a5ff0675487086dbaf9d2d102ebe292
     * accessToken : at.3zmo3ly57nnxvr1k9ooqzals03jvxxfp-17313jfq68-1cl76l9-s3uxpuusq
     */

    private int id;
    private String account;
    private int wxdepartid;
    private String appKey;
    private String appSecret;
    private String accessToken;

    public AccessTokenBean(String account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getWxdepartid() {
        return wxdepartid;
    }

    public void setWxdepartid(int wxdepartid) {
        this.wxdepartid = wxdepartid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
