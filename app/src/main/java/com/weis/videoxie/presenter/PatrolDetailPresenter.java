package com.weis.videoxie.presenter;

import android.content.Context;

import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.JPushBean;
import com.weis.videoxie.bean.LineListBean;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.bean.TowerListBean;
import com.weis.videoxie.bean.WeatherBean;
import com.weis.videoxie.bean.WxdepartmentListBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.presenter.impl.PatrolDetailImpl;
import com.weis.videoxie.presenter.listener.PatrolDetailListener;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.WeatherUtils;
import com.weis.videoxie.view.PatrolDetailView;

import java.util.HashMap;
import java.util.Map;

public class PatrolDetailPresenter extends BasePresenter<PatrolDetailView> implements WeatherUtils.WeatherListener, PatrolDetailListener {
    private Context mContext = null;
    private PatrolDetailImpl patrolDetailImpl = null;

    private AcacheUserBean acacheUserBean = null;
    private ACache aCache = null;
    private DeviceListBean targetBean = null;
    private WeatherUtils weatherUtils = null;

    public PatrolDetailPresenter(Context mContext) {
        this.mContext = mContext;
        patrolDetailImpl = new PatrolDetailImpl(mContext);
    }

    public void getweather(PatrolDeviceBean patrolDeviceBean) {
        aCache = ACache.get(mContext);
        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
        if (acacheUserBean == null || StringUtils.isEmpty(acacheUserBean.getName())) {
            mView.showMessage(0, "用户信息错误，请重新登陆");
            return;
        } else {
            for (AcacheUserBean.DeviceBean.ProvinceListBean provinceListBean : acacheUserBean.getDeviceBean().getProvinceList()) {
                for (WxdepartmentListBean wxdepartmentListBean : provinceListBean.getWxdepartmentList()) {
                    //V
                    for (LineListBean lineListBean : wxdepartmentListBean.getVisualization().getLineList()) {
                        for (TowerListBean towerListBean : lineListBean.getTowerList()) {
                            if (StringUtils.equals(towerListBean.getTowerId(), patrolDeviceBean.getId())
                                    && towerListBean.getDeviceList() != null
                                    && towerListBean.getDeviceList().size() > 0
                                    && towerListBean.getDeviceList().get(0) != null) {
                                targetBean = towerListBean.getDeviceList().get(0);
                                break;
                            }
                        }
                    }
                    //C
                    for (DeviceListBean bean : wxdepartmentListBean.getComprehensive().getDeviceList()) {
                        if (StringUtils.equals(bean.getDeviceSerial(), patrolDeviceBean.getId())) {
                            targetBean = bean;
                            break;
                        }
                    }
                }
            }
        }
        weatherUtils = new WeatherUtils(this);
        weatherUtils.getWeather(mContext, targetBean);
    }

    @Override
    public void getWeatherError(String msg) {
        mView.showMessage(0, msg);
    }

    @Override
    public void getWeatherNext(WeatherBean weatherBean) {
        mView.getWeatherNext(weatherBean);
    }

    /**
     * 获取报警消息推送详情
     *
     * @param jPushBean
     */
    public void getPushInfo(JPushBean jPushBean) {
        Map map = new HashMap();
        map.put("pushTime", String.valueOf(jPushBean.getPushTime()));
        map.put("isVisualizition", String.valueOf(jPushBean.isIsVisualizition()));
        patrolDetailImpl.getPushInfo(map, this);
    }

    @Override
    public void getPushInfoNext(PatrolMsgBean.LogListBean info) {
        mView.getPushInfoNext(info);
    }

    @Override
    public void getPushInfoError(int i, String msg) {
        mView.getPushInfoError(i, msg);
    }
}
