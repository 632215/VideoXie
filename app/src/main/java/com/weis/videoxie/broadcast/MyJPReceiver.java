package com.weis.videoxie.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.weis.videoxie.bean.AcachePoliceBean;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.JPushBean;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.bean.PoliceBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.activity.PatrolDetailActivity;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.LogUtils;
import com.weis.videoxie.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyJPReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String content = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                //send the Registration Id to your server...
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                processCustomMessage(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                receivingNotification(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                //打开自定义的Activity
                openNotification(context, bundle);
                //根据后台推送的设备信息显示报警详情，数据缓存到acache中，在mainactivity中查看时在进一步请求详细数据
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            } else {
            }
        } catch (Exception e) {
        }
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String content = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            content = extrasJson.optString("key");
        } catch (Exception e) {
            LogUtils.w(TAG, "Unexpected: extras is not a valid json");
            return;
        }
        if (StringUtils.isEmpty(content))
            return;
        JPushBean jPushBean = null;
        try {
            jPushBean = new Gson().fromJson(content, JPushBean.class);
        } catch (Exception e) {
            return;
        }
        if (jPushBean != null) {
            Intent i = new Intent(context, PatrolDetailActivity.class);
            PatrolDeviceBean patrolDeviceBean = new PatrolDeviceBean();
            PatrolMsgBean.LogListBean logListBean = new PatrolMsgBean.LogListBean();
            List<String> picUrlList = new ArrayList();
            picUrlList.add(jPushBean.getPicUrl());
            patrolDeviceBean.setId(jPushBean.getDeviceSerial());
            patrolDeviceBean.setName(jPushBean.getDeviceName());
            logListBean.setPicUrlList(picUrlList);
            logListBean.setPushTime(jPushBean.getAlarmTime());
            logListBean.setWeather(jPushBean.getWeatherBean());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("PatrolDeviceBean", patrolDeviceBean);
            i.putExtra("MsgBean", logListBean);
            context.startActivity(i);
            ACache aCache = ACache.get(context);
            AcachePoliceBean acachePoliceBean = (AcachePoliceBean) aCache.getAsObject(AppConfig.AcachePoliceBean);
            AcacheUserBean userBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
            if (userBean != null) {
                if (acachePoliceBean == null || acachePoliceBean.getList() == null) {
                    acachePoliceBean = new AcachePoliceBean();
                    acachePoliceBean.setList(new ArrayList<JPushBean>());
                }
                jPushBean.setName(userBean.getName());
                acachePoliceBean.getList().add(jPushBean);
                aCache.put(AppConfig.AcachePoliceBean, acachePoliceBean);
            }
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        LogUtils.w(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        LogUtils.w(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtils.w(TAG, "extras : " + extras);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (StringUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
    }
}
