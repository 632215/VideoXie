package com.weis.videoxie.utils;

import android.content.SharedPreferences;

/**
 * Created by root on 17-8-4.
 */

public class SharedPreferencesUtils {
    static SharedPreferences sharedPreferences;

//    /**
//     * 保存用户基本信息
//     *
//     * @param mContext
//     * @param userBean
//     */
//    public static void saveBaseUserBean(Context mContext, BaseUserBean userBean) {
//        sharedPreferences = mContext.getSharedPreferences("baseUser", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("openId", userBean.getOpenid());
//        editor.putInt("uid", userBean.getUid());
//        editor.commit();
//    }
//
//    /**
//     * 获取用户基本信息
//     *
//     * @param mContext 上下文对象
//     * @return
//     */
//    public static BaseUserBean getBaseUserBean(Context mContext) {
//        sharedPreferences = mContext.getSharedPreferences("baseUser", Activity.MODE_PRIVATE);
//        BaseUserBean bean = new BaseUserBean();
//        bean.setOpenid(sharedPreferences.getString("openId", ""));
//        return bean;
//    }
//
//    public static void clearBaseUserBean(Context mContext) {
//        sharedPreferences = mContext.getSharedPreferences("baseUser", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();
//    }
}
