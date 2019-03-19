package com.weis.videoxie.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.presenter.impl.SetDeviceImpl;
import com.weis.videoxie.presenter.listener.SetDeviceListener;
import com.weis.videoxie.view.SetDeviceView;

import java.util.HashMap;
import java.util.Map;

public class SetDevicePresenter extends BasePresenter<SetDeviceView> implements SetDeviceListener {
    private Context mContext;
    private SetDeviceImpl setDeviceImpl = null;

    public SetDevicePresenter(Context mContext) {
        this.mContext = mContext;
        setDeviceImpl = new SetDeviceImpl(mContext);
    }

    public void setDeviceName( DeviceListBean deviceBean, String name, AMapLocation aMapLocation) {
        Map map = new HashMap();
        name.replace("#", "|");
        map.put("account", deviceBean.getAccount());
        map.put("deviceName", name);
        map.put("deviceSerial", deviceBean.getDeviceSerial());
        map.put("lon", String.valueOf(aMapLocation.getLongitude()));
        map.put("lat", String.valueOf(aMapLocation.getLatitude()));
        setDeviceImpl.setDeviceName(map, this);
    }

    @Override
    public void setDeviceNameNext() {
        if (mView != null) {
            mView.setDeviceNameNext();
        }
    }

    @Override
    public void showMessage(int code, String msg) {
        if (mView != null) {
            mView.showMessage(code, msg);
        }
    }
}
