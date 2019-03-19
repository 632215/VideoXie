package com.weis.videoxie.moudle.custom;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.PopupWindow;

import com.weis.videoxie.R;
import com.weis.videoxie.utils.AndroidUtils;

import java.util.Date;

public class DatePopupWindows extends PopupWindow {
    private Context mContext;
    private CalendarView calendarView;
    private DatePopupListner mListener;
    private LayoutInflater inflater;
    private PopupWindow mPopupWindow;

    public DatePopupWindows(Context context, DatePopupListner listner) {
        super(context);
        this.mContext = context;
        this.mListener = listner;
        inflater = LayoutInflater.from(mContext);
        initView();
    }

    private void initView() {
        View rootView = inflater.inflate(R.layout.popupwindows_date, null);
        mPopupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.color.color_default_bg));
        calendarView = rootView.findViewById(R.id.calendar_view);
        calendarView.setMaxDate(new Date().getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                mListener.changeDate(i + "-" + i1 + 1 + "-" + i2);
                mPopupWindow.dismiss();
            }
        });
    }

    public void showPopWindow(View v) {
        if (mPopupWindow != null) {
            if (AndroidUtils.isLollipop())
                mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            else
                mPopupWindow.showAsDropDown(v);
        }
    }

    public interface DatePopupListner {
        void changeDate(String date);
    }
}
