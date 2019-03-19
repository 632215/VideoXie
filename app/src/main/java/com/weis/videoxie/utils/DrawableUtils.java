package com.weis.videoxie.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Field;


/**
 * Created by root on 17-8-19.
 */

public class DrawableUtils {
    public static void setDrawableLeft(Context mContext, TextView textView, int res) {
        Drawable drawable = mContext.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public static void setDrawableRight(Context mContext, TextView textView, int res) {
        Drawable drawable = mContext.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    public static void setDrawableTop(Context mContext, TextView textView, int res) {
        Drawable drawable = mContext.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    public static void setDrawableNone(Context mContext, TextView textView) {
        textView.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 设置CheckBox的图标
     *
     * @param mContext
     * @param checkBox
     * @param res
     */
    public static void setCheckBoxDrawableLeft(Context mContext, CheckBox checkBox, int res) {
        Drawable drawable = mContext.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        checkBox.setCompoundDrawables(drawable, null, null, null);
    }

//    /**
//     * 更改主 TAB图标
//     *
//     * @param context
//     * @param radioButton
//     */
//    public static void setMainDrawable(Context context, RadioButton radioButton) {
//        StateListDrawable drawable = new StateListDrawable();
//        Drawable defaultDrawable = context.getResources().getDrawable(R.drawable.icon_my_check);
//        Drawable selectedDrawable = context.getResources().getDrawable(R.drawable.icon_my_un);
//        drawable.addState(new int[]{
//                android.R.attr.state_checked}, defaultDrawable);
//        drawable.addState(new int[]{
//                -android.R.attr.state_checked}, selectedDrawable);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        radioButton.setCompoundDrawables(null, drawable, null, null);
//    }

    /**
     * 更改主 TAB图标
     *
     * @param context
     */
    public static void setMainDrawable(final Context context) {

    }

    public static int getDrawableId(String resName, Context context) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }
}
