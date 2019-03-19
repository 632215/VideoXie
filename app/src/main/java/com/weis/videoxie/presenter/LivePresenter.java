package com.weis.videoxie.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.LineListBean;
import com.weis.videoxie.bean.LiveBean;
import com.weis.videoxie.bean.TowerListBean;
import com.weis.videoxie.bean.WeatherBean;
import com.weis.videoxie.bean.WxdepartmentListBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.presenter.impl.LiveImpl;
import com.weis.videoxie.presenter.listener.LiveListener;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.WeatherUtils;
import com.weis.videoxie.view.LiveView;

import java.util.ArrayList;
import java.util.List;

public class LivePresenter extends BasePresenter<LiveView> implements LiveListener, WeatherUtils.WeatherListener {
    private Context mContext;
    private LiveImpl mImlp;
    private EZDeviceInfo ezDeviceInfo = null;
    private EZCameraInfo ezCameraInfo = null;
    private WeatherUtils weatherUtils = null;

    public LivePresenter(Context context) {
        this.mContext = context;
        mImlp = new LiveImpl(context);
    }

    public void getWeather(DeviceListBean deviceBean) {
        if (deviceBean == null || StringUtils.isEmpty(deviceBean.getLat()) || StringUtils.isEmpty(deviceBean.getLon())) {
            mView.showMessage(0, "位置信息错误，无法提供天气情况");
            return;
        }
//        mImlp.getWeather(deviceBean, this);
        weatherUtils = new WeatherUtils(this);
        weatherUtils.getWeather(mContext, deviceBean);
    }

    @Override
    public void getWeatherError(String msg) {
        mView.showMessage(0, msg);
    }

    @Override
    public void getWeatherNext(WeatherBean weatherBean) {
        if (mView != null)
            mView.getWeatherNext(weatherBean);
    }

    //将同一组的deviceListBean存入数组，便于同组可滑动观看
    public List initDataList(DeviceListBean deviceBean) {
        List liveBeanList = new ArrayList();
        AcacheUserBean acacheUserBean = (AcacheUserBean) ACache.get(mContext).getAsObject(AppConfig.AcacheUserBean);
        if (acacheUserBean == null || acacheUserBean.getDeviceBean() == null)
            return null;
        List<DeviceListBean> tempList = new ArrayList<>();
        for (AcacheUserBean.DeviceBean.ProvinceListBean provinceListBean : acacheUserBean.getDeviceBean().getProvinceList()) {
            for (WxdepartmentListBean wxdepartmentListBean : provinceListBean.getWxdepartmentList()) {
                //V
                for (LineListBean lineListBean : wxdepartmentListBean.getVisualization().getLineList()) {
                    for (TowerListBean towerListBean : lineListBean.getTowerList()) {
                        for (DeviceListBean deviceListBean : towerListBean.getDeviceList()) {
                            if (StringUtils.equals(deviceListBean.getDeviceSerial(), deviceBean.getDeviceSerial())) {
                                tempList = towerListBean.getDeviceList();
                                break;
                            }
                        }
                    }
                }
                //C
                for (DeviceListBean deviceListBean : wxdepartmentListBean.getComprehensive().getDeviceList())
                    if (StringUtils.equals(deviceListBean.getDeviceSerial(), deviceBean.getDeviceSerial())) {
                        tempList = wxdepartmentListBean.getComprehensive().getDeviceList();
                        break;
                    }
            }
        }
        liveBeanList = getLiveBeanList(deviceBean, tempList, liveBeanList);
        return liveBeanList;
    }

    private List getLiveBeanList(DeviceListBean deviceBean, List<DeviceListBean> deviceList, List targetList) {
        int preIndex = -1;
        List<Integer> preList = new ArrayList<>();
        for (int x = 0; x < deviceList.size(); x++) {
            if (StringUtils.equals(deviceList.get(x).getDeviceSerial(), deviceBean.getDeviceSerial())) {
                preIndex = x;
                for (int y = 0; y < x; y++)
                    preList.add(y);
            }
        }
        if (preIndex != -1) {
            for (int x = preIndex; x < deviceList.size(); x++) {
                targetList.add(new LiveBean(deviceList.get(x)));
            }
        }
        for (int index : preList) {
            targetList.add(new LiveBean(deviceList.get(index)));
        }
        return targetList;
    }

    @Override
    public void showMessage(String message) {
        if (mView != null)
            mView.showMessage(0, message);
    }

    //获取设备信息
    public void getDeviceInfo(final DeviceListBean deviceBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                try {
                    ezDeviceInfo = EZOpenSDK.getInstance().getDeviceInfo(deviceBean.getDeviceSerial());
                    msg.what = AppConfig.GET_EZDEVICE_SUCCESS;
                } catch (BaseException e) {
                    msg.what = AppConfig.GET_EZDEVICE_FAIL;
                    bundle.putInt("errCode", e.getErrorCode());
                    bundle.putString("errMessage", e.getErrorInfo().description.toString().trim());
                    e.printStackTrace();
                }
                msg.setData(bundle);
                mHander.sendMessage(msg);
            }
        }).start();
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (mView != null)
                switch (message.what) {
                    case AppConfig.GET_EZDEVICE_SUCCESS:
                        mView.getDeviceInfoNext(ezDeviceInfo);
                        break;

                    case AppConfig.GET_EZDEVICE_FAIL:
                        mView.showMessage(10, message.getData().getString("errMessage"));
                        break;
                }
        }
    };
}
