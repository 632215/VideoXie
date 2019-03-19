package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.DevicePicBean;
import com.weis.videoxie.bean.HttpBean;
import com.weis.videoxie.bean.LineListBean;
import com.weis.videoxie.bean.TowerListBean;
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

public class LowPowerAdapter extends BaseQuickAdapter<WxdepartmentListBean, BaseViewHolder> {
    private VAdapter vAdapter = null;
    private DeviceAdaper deviceAdaper = null;
    ACache aCache = null;
    private AcacheUserBean acacheUserBean = null;
    private int count = 0;
    private List deviceList = null;
    private LoadingDialog dialog = null;

    public LowPowerAdapter(int layoutResId, @Nullable List<WxdepartmentListBean> data) {
        super(layoutResId, data);
    }

    public LowPowerAdapter(@Nullable List<WxdepartmentListBean> data) {
        super(data);
    }

    public LowPowerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final WxdepartmentListBean item) {
        final TextView txV = helper.getView(R.id.tx_v);
        final RecyclerView rvV = helper.getView(R.id.rv_v);
        final TextView txC = helper.getView(R.id.tx_c);
        final RecyclerView rvC = helper.getView(R.id.rv_c);
        //V
        if (item.getVisualization().getLineList() == null || item.getVisualization().getLineList().size() == 0) {
            rvV.setVisibility(View.GONE);
            txV.setVisibility(View.GONE);
        } else {
            rvV.setVisibility(View.GONE);
            txV.setVisibility(View.VISIBLE);
            txV.setText(item.getVisualization().getName());
            vAdapter = new VAdapter(R.layout.item_v, item.getVisualization().getLineList());
            vAdapter.bindToRecyclerView(rvV);
            vAdapter.setEnableLoadMore(true);
            vAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            rvV.setLayoutManager(new LinearLayoutManager(mContext));
            rvV.setAdapter(vAdapter);
            ConstraintLayout clCompantRoot = helper.getView(R.id.cl_company_root);
            clCompantRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txV.setVisibility(txV.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    rvV.setVisibility(txV.getVisibility());
                }
            });
            txV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rvV.setVisibility(rvV.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            clCompantRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rvV.setVisibility(rvV.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });
        }

        //C
        if (item.getComprehensive() == null || item.getComprehensive().getDeviceList() == null || item.getComprehensive().getDeviceList().size() == 0) {
            rvC.setVisibility(View.GONE);
            txC.setVisibility(View.GONE);
        } else {
            txC.setVisibility(View.VISIBLE);
            rvC.setVisibility(View.GONE);
            txC.setText(item.getComprehensive().getName());
            deviceAdaper = new DeviceAdaper(R.layout.item_device, item.getComprehensive().getDeviceList());
            deviceAdaper.bindToRecyclerView(rvC);
            deviceAdaper.setEnableLoadMore(true);
            deviceAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            rvC.setLayoutManager(new LinearLayoutManager(mContext));
            rvC.setAdapter(deviceAdaper);
            txC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvC.getVisibility() == View.GONE) {
                        count = 0;
                        aCache = ACache.get(mContext);
                        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
                        deviceList = item.getComprehensive().getDeviceList();
                        if (dialog == null) {
                            dialog = new LoadingDialog(mContext);
                        }
                        dialog.show();
                        getCameraState(deviceList, rvC);
                    } else
                        rvC.setVisibility(View.GONE);
                }
            });
        }
    }

    public void getCameraState(final List<DeviceListBean> deviceList, final RecyclerView rvC) {
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
                rvC.setVisibility(rvC.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
