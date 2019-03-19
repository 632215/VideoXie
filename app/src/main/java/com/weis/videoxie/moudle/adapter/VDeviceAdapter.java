package com.weis.videoxie.moudle.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.videogo.exception.BaseException;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.weis.videoxie.R;
import com.weis.videoxie.application.MyApplication;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.DevicePicBean;
import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.LineListBean;
import com.weis.videoxie.bean.TowerListBean;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.WxdepartmentListBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.http.HttpMethods;
import com.weis.videoxie.http.ProgressSubscriber;
import com.weis.videoxie.http.SubscriberOnNextListener;
import com.weis.videoxie.moudle.custom.LoadingDialog;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VDeviceAdapter extends BaseQuickAdapter<TowerListBean, BaseViewHolder> {
    private DeviceAdaper deviceAdaper;
    ACache aCache = null;
    private AcacheUserBean acacheUserBean = null;
    private int count = 0;
    private List deviceList = null;
    private LoadingDialog dialog = null;

    public VDeviceAdapter(int layoutResId, @Nullable List<TowerListBean> data) {
        super(layoutResId, data);
    }

    public VDeviceAdapter(@Nullable List<TowerListBean> data) {
        super(data);
    }

    public VDeviceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TowerListBean item) {
        helper.setText(R.id.tx_device_v_name, item.getTowerName());
        final RecyclerView rvVDevice = helper.getView(R.id.rv_v_device);
        deviceAdaper = new DeviceAdaper(R.layout.item_device, item.getDeviceList());
        deviceAdaper.bindToRecyclerView(rvVDevice);
        deviceAdaper.setEnableLoadMore(true);
        deviceAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        rvVDevice.setLayoutManager(new LinearLayoutManager(mContext));
        rvVDevice.setAdapter(deviceAdaper);
        TextView txDeviceVName = helper.getView(R.id.tx_device_v_name);
        if (item.getDeviceList().size() > 0)
            txDeviceVName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvVDevice.getVisibility() == View.GONE) {
                        count = 0;
                        aCache = ACache.get(mContext);
                        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
                        deviceList = item.getDeviceList();
                        if (dialog == null) {
                            dialog = new LoadingDialog(mContext);
                        }
                        dialog.show();
                        getCameraState(deviceList, rvVDevice);
                    } else
                        rvVDevice.setVisibility(View.GONE);
                }
            });
    }

    private RecyclerView recyclerView;

    public void getCameraState(final List<DeviceListBean> deviceList, final RecyclerView rvVDevice) {
        if (deviceList == null || deviceList.size() == 0)
            return;
        String deviceSerialList = "";
        for (int x = 0; x < deviceList.size(); x++) {
            deviceSerialList += (deviceList.get(x).getDeviceSerial() + ((x == deviceList.size() - 1) ? "" : ","));
        }
        Map map = new HashMap();
        map.put("deviceSerialList", deviceSerialList);
        map.put("account", deviceList.get(0).getAccount());
        SubscriberOnNextListener nextListener = new SubscriberOnNextListener<DevicePicBean>() {
            @Override
            public void onNext(DevicePicBean info) {
                List<DeviceListBean> tempList = deviceList;
                for (DevicePicBean.ListBean picBean : info.getList()) {
                    for (DeviceListBean deviceListBean : tempList) {
                        if (StringUtils.equals(deviceListBean.getDeviceSerial(), picBean.getDeviceSerial())) {
                            deviceListBean.setImgUrl(picBean.getPoster());
                            deviceListBean.setStatus(picBean.getStatus());
                        }
                    }
                    for (AcacheUserBean.DeviceBean.ProvinceListBean provinceListBean : acacheUserBean.getDeviceBean().getProvinceList()) {
                        for (WxdepartmentListBean wxdepartmentListBean : provinceListBean.getWxdepartmentList()) {
                            //V
                            for (LineListBean lineListBean : wxdepartmentListBean.getVisualization().getLineList()) {
                                for (TowerListBean towerListBean : lineListBean.getTowerList()) {
                                    for (DeviceListBean bean : towerListBean.getDeviceList()) {
                                        if (StringUtils.equals(bean.getDeviceSerial(), picBean.getDeviceSerial())) {
                                            bean.setStatus(picBean.getStatus());
                                            bean.setImgUrl(picBean.getPoster());
                                            aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
                                        }
                                    }
                                }
                            }
                            //C
                            for (DeviceListBean bean : wxdepartmentListBean.getComprehensive().getDeviceList()) {
                                if (StringUtils.equals(bean.getDeviceSerial(), picBean.getDeviceSerial())) {
                                    bean.setStatus(picBean.getStatus());
                                    bean.setImgUrl(picBean.getPoster());
                                    aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
                                }
                            }
                        }
                    }
                }
                deviceAdaper.setNewData(tempList);
                rvVDevice.setVisibility(rvVDevice.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                dialog.dismiss();
            }

            @Override
            public void onError(String Code, String Msg) {
                ToastUtils.showShort(mContext, "数据错误");
                dialog.dismiss();
            }
        };
        HttpMethods.getInstance().getDevicePic(new ProgressSubscriber<HttpBean<DevicePicBean>>(nextListener, mContext), map);

    }
}
