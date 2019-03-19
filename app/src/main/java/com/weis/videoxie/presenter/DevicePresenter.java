package com.weis.videoxie.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZStorageStatus;
import com.weis.videoxie.application.MyApplication;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.view.DeviceView;

import java.util.List;

public class DevicePresenter extends BasePresenter<DeviceView> {
    Context mContext;
    private EZDeviceInfo ezDeviceInfo = null;
    private List sdList = null;

    public DevicePresenter(Context mContext) {
        this.mContext = mContext;
    }

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
                    postException(msg, AppConfig.GET_EZDEVICE_FAIL, bundle, e);
                }
                msg.setData(bundle);
                mHander.sendMessage(msg);
            }
        }).start();
    }

    private void postException(Message msg, int code, Bundle bundle, BaseException e) {
        msg.what = code;
        bundle.putInt("errCode", e.getErrorCode());
        bundle.putString("errMessage", e.getErrorInfo().description.toString().trim());
        e.printStackTrace();
    }

    public void deviceClearSd(final DeviceListBean deviceBean, final EZStorageStatus storageStatus) {
        if (deviceBean == null || StringUtils.isEmpty(deviceBean.getDeviceSerial()) || storageStatus == null) {
            mView.showMessage(0, "设备存储设置失败！");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                try {
                    MyApplication.getEzSDKInstance().formatStorage(deviceBean.getDeviceSerial(), storageStatus.getIndex());
                    msg.what = AppConfig.CLEAR_EZDEVICE_SD_SUCCESS;
                } catch (BaseException e) {
                    postException(msg, AppConfig.CLEAR_EZDEVICE_SD_FAIL, bundle, e);
                    e.printStackTrace();
                }
                msg.setData(bundle);
                mHander.sendMessage(msg);
            }
        }).start();
    }

    public void getSDInfo(final DeviceListBean deviceBean) {
        if (deviceBean == null || StringUtils.isEmpty(deviceBean.getDeviceSerial()))
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                try {
                    sdList = MyApplication.getEzSDKInstance().getStorageStatus(deviceBean.getDeviceSerial());
                    msg.what = AppConfig.GET_EZDEVICE_SD_SUCCESS;
                } catch (BaseException e) {
                    postException(msg, AppConfig.GET_EZDEVICE_SD_FAIL, bundle, e);
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
            switch (message.what) {
                case AppConfig.GET_EZDEVICE_SUCCESS:
                    mView.getDeviceInfoNext(ezDeviceInfo);
                    break;

                case AppConfig.GET_EZDEVICE_FAIL:
//                    mView.showMessage(message.getData().getInt("errCode"), message.getData().getString("errMessage"));
                    break;

                case AppConfig.GET_EZDEVICE_SD_SUCCESS:
                    mView.getSDInfoNext(sdList);
                    break;

                case AppConfig.GET_EZDEVICE_SD_FAIL:
//                    mView.showMessage(message.getData().getInt("errCode"), message.getData().getString("errMessage"));
                    break;

                case AppConfig.CLEAR_EZDEVICE_SD_SUCCESS:
                    mView.showMessage(0, "格式化设备存储成功！");
                    break;

                case AppConfig.CLEAR_EZDEVICE_SD_FAIL:
                    mView.showMessage(message.getData().getInt("errCode"), "格式化设备存储失败：" + message.getData().getString("errMessage"));
                    break;
            }
        }
    };

}
