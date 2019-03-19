package com.weis.videoxie.http;

import com.weis.videoxie.bean.AccessTokenBean;
import com.weis.videoxie.bean.DepartmentBean;
import com.weis.videoxie.bean.DevicePicBean;
import com.weis.videoxie.bean.EmptyBean;
import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.PatrolBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.bean.RegisterResultBean;
import com.weis.videoxie.bean.SetNameBean;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.utils.AndroidUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukun on 16/icon_cs3/9.
 */
public class HttpMethods {
    public Retrofit retrofit;
    public AppService service;
    public static String BASE_URL = "http://www.cqset.com:3000/";

    private static final int DEFAULT_TIMEOUT = 150;
    private OkHttpClient.Builder builder;

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public AppService getService() {
        if (service == null)
            new Throwable("AppService is null ");
        return service;
    }

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new LogInterceptor());
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("machine", AndroidUtils.getUnId())
                        .build();
                return chain.proceed(request);
            }
        });
        builder.addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY));

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(AppService.class);
    }

    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 获取设备列表
     *
     * @param subscriber
     * @param map
     */
    public void getDevice(Subscriber<HttpBean<AcacheUserBean.DeviceBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getDevice(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取AccessToken
     *
     * @param subscriber
     * @param map
     */
    public void getAccessToken(Subscriber<HttpBean<AccessTokenBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getAccessToken(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改设备名
     *
     * @param subscriber
     * @param map
     */
    public void setDeviceName(Subscriber<HttpBean<SetNameBean>> subscriber, Map<String, String> map) {
        Observable observable = service.setDeviceName(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取设备列表用于巡检消息
     *
     * @param subscriber
     * @param map
     */
    public void getPatrol(Subscriber<HttpBean<PatrolBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getPatrol(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取设备巡检消息
     *
     * @param subscriber
     * @param map
     */
    public void getPatrolCMsg(Subscriber<HttpBean<PatrolMsgBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getPatrolCMsg(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取设备巡检消息（灯塔）
     * *
     * @param subscriber
     * @param map
     */
    public void getPatrolVMsg(Subscriber<HttpBean<PatrolMsgBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getPatrolVMsg(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取报警消息详情
     *
     * @param subscriber
     * @param map
     */
    public void getPushInfo(Subscriber<HttpBean<PatrolMsgBean.LogListBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getPushInfo(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 登陆
     *
     * @param subscriber
     * @param map
     */
    public void login(Subscriber<HttpBean<EmptyBean>> subscriber, Map<String, String> map) {
        Observable observable = service.login(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 注册
     *
     * @param subscriber
     * @param map
     */
    public void register(Subscriber<HttpBean<RegisterResultBean>> subscriber, Map<String, String> map) {
        Observable observable = service.register(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取部门列表
     *
     * @param subscriber
     * @param map
     */
    public void getDepartment(Subscriber<HttpBean<DepartmentBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getDepartment(map);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取设备封面
     *
     * @param subscriber
     * @param map
     */
    public void getDevicePic(Subscriber<HttpBean<DevicePicBean>> subscriber, Map<String, String> map) {
        Observable observable = service.getDevicePic(map);
        toSubscribe(observable, subscriber);
    }
}
