package com.weis.videoxie.http;

import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.AccessTokenBean;
import com.weis.videoxie.bean.DepartmentBean;
import com.weis.videoxie.bean.DevicePicBean;
import com.weis.videoxie.bean.EmptyBean;
import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.PatrolBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.bean.RegisterResultBean;
import com.weis.videoxie.bean.SetNameBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by liukun on 16/icon_cs3/9.
 */
public interface AppService {
    //根据用户信息获取设备列表
    @GET("hkys_device_get_by_users")
    Observable<HttpBean<AcacheUserBean.DeviceBean>> getDevice(@QueryMap Map<String, String> map);

    //获取token
    @GET("hkys_accountInfo_get_by_account")
    Observable<HttpBean<AccessTokenBean>> getAccessToken(@QueryMap Map<String, String> map);

    //更新设备名字
    @GET("hkys_update_deviceName")
    Observable<HttpBean<SetNameBean>> setDeviceName(@QueryMap Map<String, String> map);

    //获取设备列表用于巡检信息展示
    @GET("hkys_get_pushLog_deviceList_by_users")
    Observable<HttpBean<PatrolBean>> getPatrol(@QueryMap Map<String, String> map);

    //获取设备巡检信息
    @GET("hkys_get_pushLog_by_deviceSerial")
    Observable<HttpBean<PatrolMsgBean>> getPatrolCMsg(@QueryMap Map<String, String> map);

    //获取设备巡检信息(灯塔)
    @GET("hkys_get_pushLog_by_towerId")
    Observable<HttpBean<PatrolMsgBean>> getPatrolVMsg(@QueryMap Map<String, String> map);

    //获取报警消息详情
    @GET("hkys_get_alarmLog_by_pushTime")
    Observable<HttpBean<PatrolMsgBean.LogListBean>> getPushInfo(@QueryMap Map<String, String> map);

    //登陆
    @GET("hkys_user_login_confirm")
    Observable<HttpBean<EmptyBean>> login(@QueryMap Map<String, String> map);

    //注册
    @GET("wx_user_add")
    Observable<HttpBean<RegisterResultBean>> register(@QueryMap Map<String, String> map);

    //获取部门列表
    @GET("hkys_get_wxdepartment_by_userId")
    Observable<HttpBean<DepartmentBean>> getDepartment(@QueryMap Map<String, String> map);

    //获取设备封面
    @GET("hkys_device_get_status")
    Observable<HttpBean<DevicePicBean>> getDevicePic(@QueryMap Map<String, String> map);
}