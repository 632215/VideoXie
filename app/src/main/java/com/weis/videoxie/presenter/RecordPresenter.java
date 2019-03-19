package com.weis.videoxie.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.RecordCloudBean;
import com.weis.videoxie.bean.RecordSDBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.view.RecordView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.weis.videoxie.config.AppConfig.RECORD_SOURCE_CLOUD;
import static com.weis.videoxie.config.AppConfig.RECORD_SOURCE_SD;

public class RecordPresenter extends BasePresenter<RecordView> {
    private Context mContext;
    private EZDeviceInfo ezDeviceInfo = null;
    private EZCameraInfo ezCameraInfo = null;
    private String date = "";

    public RecordPresenter(Context mContext) {
        this.mContext = mContext;
    }

    //获取指定日期的录像信息 source 0--SD  1--云存储
    public void getRecordData(EZDeviceInfo ezDeviceInfo, String date, int source) {
        this.ezDeviceInfo = ezDeviceInfo;
        if (ezDeviceInfo.getCameraNum() > 0 && ezDeviceInfo.getCameraInfoList() != null && ezDeviceInfo.getCameraInfoList().size() > 0) {
            ezCameraInfo = ezDeviceInfo.getCameraInfoList().get(0);
        }
        final Calendar calendarStart = Calendar.getInstance();
        final Calendar calendarEnd = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = date + " 00:00:00";
        String endTime = date + " 23:59:59";
        try {
            calendarStart.setTime(format.parse(startTime));
            calendarEnd.setTime(format.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
            mView.showMessage(0, "时间转换出错");
        }
        getRecord(calendarStart, calendarEnd, source);
    }

    //获取数据 0--SD  1--云存储
    private void getRecord(final Calendar calendarStart, final Calendar calendarEnd, final int source) {
        if (ezDeviceInfo == null || ezCameraInfo == null) {
            mView.showMessage(0, "设备信息出错");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                switch (source) {
                    case RECORD_SOURCE_SD:
                        try {
                            List<EZDeviceRecordFile> list = EZOpenSDK.getInstance().searchRecordFileFromDevice(ezDeviceInfo.getDeviceSerial(), ezCameraInfo.getCameraNo(), calendarStart, calendarEnd);
                            bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                            message.what = AppConfig.EZ_GET_SD_RECORD_SUCCESS;
                        } catch (BaseException e) {
                            bundle.putInt("errCode", e.getErrorCode());
//                            bundle.putString("errMsg", e.getMessage());
                            bundle.putString("errMsg", "");
                            message.what = AppConfig.EZ_GET_SD_RECORD_FAIL;
                            e.printStackTrace();
                        }
                        break;

                    case RECORD_SOURCE_CLOUD:
                        try {
                            List<EZCloudRecordFile> list = EZOpenSDK.getInstance().searchRecordFileFromCloud(ezDeviceInfo.getDeviceSerial(), ezCameraInfo.getCameraNo(), calendarStart, calendarEnd);
                            bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                            message.what = AppConfig.EZ_GET_CLOUD_RECORD_SUCCESS;
                        } catch (BaseException e) {
                            bundle.putInt("errCode", e.getErrorCode());
//                            bundle.putString("errMsg", e.getMessage());
                            bundle.putString("errMsg", "");
                            message.what = AppConfig.EZ_GET_CLUOD_RECORD_FAIL;
                            e.printStackTrace();
                        }
                        break;
                }
                message.setData(bundle);
                mHander.sendMessage(message);
            }
        }).start();
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case AppConfig.EZ_GET_CLOUD_RECORD_SUCCESS://获取云存储视频成功
                    getCloudRecordSuccess(message);
                    break;

                case AppConfig.EZ_GET_CLUOD_RECORD_FAIL://获取云存储视频失败
                    getRecordFail(message);
                    break;

                case AppConfig.EZ_GET_SD_RECORD_SUCCESS://获取SD存储视频成功
                    getSDRecordSuccess(message);
                    break;

                case AppConfig.EZ_GET_SD_RECORD_FAIL://获取SD存储视频失败
                    getRecordFail(message);
                    break;

                case AppConfig.GET_EZDEVICE_SUCCESS:
                    mView.getDeviceInfoNext(ezDeviceInfo);
                    break;

                case AppConfig.GET_EZDEVICE_FAIL:
                    mView.showMessage(10, message.getData().getString("errMessage"));
                    break;
            }
        }
    };

    //获取SD存储成功
    private void getSDRecordSuccess(Message message) {
        if (mView != null) {
            List<EZDeviceRecordFile> list = message.getData().getParcelableArrayList("list");
            if (list == null || list.size() == 0 && mView != null)
                mView.showMessage(AppConfig.EZ_DATA_EMPTY, "数据为空");
            String time = "";
            List<RecordSDBean> recordSDList = new ArrayList<>();
            for (int x = 0; x < 24; x++) {
                List<EZDeviceRecordFile> deviceRecordFiles = new ArrayList<>();
                for (EZDeviceRecordFile deviceRecordFile : list) {
                    if (deviceRecordFile.getStartTime().get(Calendar.HOUR_OF_DAY) == x) {
                        deviceRecordFiles.add(deviceRecordFile);
                    }
                }
                recordSDList.add(new RecordSDBean(true, String.valueOf(x)));
                if (deviceRecordFiles.size() != 0) {
                    RecordSDBean recordSDBean = new RecordSDBean(false, "");
                    recordSDBean.setData(deviceRecordFiles);
                    recordSDList.add(recordSDBean);
                }
                if (deviceRecordFiles.size() == 0) {
                    recordSDList.remove(recordSDList.size() - 1);
                }
            }
            if (recordSDList == null || recordSDList.size() == 0) {
                mView.showMessage(AppConfig.EZ_DATA_EMPTY, "暂无数据");
                return;
            }
            mView.getRecordSDSuccess(recordSDList);
        }
    }

    //获取云存储成功
    private void getCloudRecordSuccess(Message message) {
        if (mView != null) {
            List<EZCloudRecordFile> list = message.getData().getParcelableArrayList("list");
            if (list == null || list.size() == 0 && mView != null)
                mView.showMessage(AppConfig.EZ_DATA_EMPTY, "数据为空");
            String time = "";
            List<RecordCloudBean> recordList = new ArrayList<>();
            for (int x = 0; x < 24; x++) {
                List<EZCloudRecordFile> cloudRecordFiles = new ArrayList<>();
                for (EZCloudRecordFile cloudRecordFile : list) {
                    if (cloudRecordFile.getStartTime().get(Calendar.HOUR_OF_DAY) == x) {
                        cloudRecordFiles.add(cloudRecordFile);
                    }
                }
                recordList.add(new RecordCloudBean(true, String.valueOf(x)));
                if (cloudRecordFiles.size() != 0) {
                    RecordCloudBean recordCloudBean = new RecordCloudBean(false, "");
                    recordCloudBean.setData(cloudRecordFiles);
                    recordList.add(recordCloudBean);
                }
                if (cloudRecordFiles.size() == 0) {
                    recordList.remove(recordList.size() - 1);
                }
            }
            if (recordList == null || recordList.size() == 0) {
                mView.showMessage(AppConfig.EZ_DATA_EMPTY, "暂无数据");
                return;
            }
            mView.getRecordCloudSuccess(recordList);
        }
    }

    //获取存储失败
    private void getRecordFail(Message message) {
        if (mView != null) {
            mView.showMessage(message.getData().getInt("errCode"), message.getData().getString("errMsg"));
        }
    }

    //获取萤石设备信息
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
//                    bundle.putString("errMessage", e.getErrorInfo().description.toString().trim());
                    bundle.putString("errMessage", "");
                    e.printStackTrace();
                }
                msg.setData(bundle);
                mHander.sendMessage(msg);
            }
        }).start();
    }
}
