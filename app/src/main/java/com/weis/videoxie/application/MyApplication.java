package com.weis.videoxie.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.lody.turbodex.TurboDex;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EzvizAPI;
import com.weis.videoxie.config.AppConfig;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018/7/31.
 */

public class MyApplication extends Application {
    private static MyApplication intance;
    private Typeface typeface;

    public static MyApplication getInstance() {
        return intance;
    }

    public static EZOpenSDK getEzSDKInstance() {
        return EZOpenSDK.getInstance();
    }

    public void setInstance(MyApplication intance) {
        this.intance = intance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //极光
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

        setInstance(this);
        //分包处理
        ButterKnife.setDebug(false);
        //萤石
        /** * sdk日志开关，正式发布需要去掉 */
        EZOpenSDK.showSDKLog(true);
        /** * 设置是否支持P2P取流,详见api */
        EZOpenSDK.enableP2P(false);
        /** * APP_KEY请替换成自己申请的 */
        EZOpenSDK.initLib(this, AppConfig.YingShiKey);
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });//设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);//指定为经典Footer，默认是 BallPulseFooter
            }
        });
        CrashReport.initCrashReport(getApplicationContext(), AppConfig.BuglyId, false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        TurboDex.enableTurboDex();
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
