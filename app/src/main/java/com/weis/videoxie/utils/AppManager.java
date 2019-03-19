package com.weis.videoxie.utils;

/**
 * Created by root on 17-9-7.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author BiHaidong
 * @version 1.0
 * @created 2015-9-21
 */
public class AppManager {
    private static Stack<FragmentActivity> activityStack = new Stack();

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(FragmentActivity activity) {
        activityStack.push(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static FragmentActivity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        FragmentActivity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(FragmentActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    //临时中间变量
    private static Stack<FragmentActivity> tempList = new Stack<>();

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {

        for (FragmentActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                tempList.add(activity);
            }
        }

        for (FragmentActivity fragmentActivity : tempList) {
            activityStack.remove(fragmentActivity);
            tempList.remove(fragmentActivity);
            if (!fragmentActivity.isFinishing()) {
                fragmentActivity.finish();
            }
        }

    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressLint("MissingPermission")
    public static void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

