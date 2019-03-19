package com.weis.videoxie.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.videogo.exception.BaseException;
import com.videogo.openapi.bean.EZProbeDeviceInfoResult;
import com.weis.videoxie.application.MyApplication;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheTokenBean;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.AccessTokenBean;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.LineListBean;
import com.weis.videoxie.bean.TowerListBean;
import com.weis.videoxie.bean.WxdepartmentListBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.presenter.impl.DeviceFragmentImpl;
import com.weis.videoxie.presenter.listener.DeviceFragmentListener;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.view.DeviceFragmentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceFragmentPresenter extends BasePresenter<DeviceFragmentView> implements
        DeviceFragmentListener {
    private Context mContext;
    private DeviceFragmentImpl deviceFragmentImpl;
    private AcacheUserBean acacheUserBean;
    private Map map = new HashMap();
    private ACache aCache = null;

    public DeviceFragmentPresenter(Context mContext) {
        this.mContext = mContext;
        deviceFragmentImpl = new DeviceFragmentImpl(mContext);
        aCache = ACache.get(mContext);
    }

    public void getDevice() {
        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
        Map map = new HashMap();
        map.put("userName", acacheUserBean.getName());
        deviceFragmentImpl.getDevice(map, this);
    }

    private List<AccessTokenBean> needTookenList = new ArrayList();
    private AcacheTokenBean acacheTokenBean = null;
    private boolean isHave = false;

    @Override
    public void getDeviceNext(AcacheUserBean.DeviceBean info) {
        if (mView != null && info != null) {
            mView.getDeviceNext(info);
            acacheUserBean.setDeviceBean(info);
            checkTokenAcache();
            checkToken(info);
        }
    }

    //检查缓存中是否存在相应的token缓存，存在就给userbean赋值
    private void checkTokenAcache() {
        acacheTokenBean = (AcacheTokenBean) aCache.getAsObject(AppConfig.AcacheToken);
        if (acacheTokenBean != null && acacheTokenBean.getList() != null) {
            for (AccessTokenBean accessTokenBean : acacheTokenBean.getList()) {
                for (AcacheUserBean.DeviceBean.ProvinceListBean provinceListBean : acacheUserBean.getDeviceBean().getProvinceList()) {
                    for (WxdepartmentListBean wxdepartmentListBean : provinceListBean.getWxdepartmentList()) {
                        //V
                        for (LineListBean lineListBean : wxdepartmentListBean.getVisualization().getLineList()) {
                            for (TowerListBean towerListBean : lineListBean.getTowerList()) {
                                for (DeviceListBean bean : towerListBean.getDeviceList()) {
                                    if (StringUtils.equals(bean.getAccount(), accessTokenBean.getAccount()))
                                        bean.setAccessTokenBean(accessTokenBean);
                                }
                            }
                        }
                        //C
                        for (DeviceListBean bean : wxdepartmentListBean.getComprehensive().getDeviceList()) {
                            if (StringUtils.equals(bean.getAccount(), accessTokenBean.getAccount()))
                                bean.setAccessTokenBean(accessTokenBean);
                        }

                    }
                }
            }
        }
        aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
    }

    //检查对比过缓存文件后的userBean的token是否都存在，不存在的再进行请求。
    private void checkToken(AcacheUserBean.DeviceBean info) {
        if (needTookenList == null)
            needTookenList = new ArrayList<>();
        for (AcacheUserBean.DeviceBean.ProvinceListBean provinceListBean : info.getProvinceList()) {
            for (WxdepartmentListBean wxdepartmentListBean : provinceListBean.getWxdepartmentList()) {
                //V
                for (LineListBean lineListBean : wxdepartmentListBean.getVisualization().getLineList()) {
                    for (TowerListBean towerListBean : lineListBean.getTowerList()) {
                        for (DeviceListBean bean : towerListBean.getDeviceList()) {
                            checkTokenState(bean);
                        }
                    }
                }
                //C
                for (DeviceListBean bean : wxdepartmentListBean.getComprehensive().getDeviceList()) {
                    checkTokenState(bean);
                }
            }
        }
        if (needTookenList.size() > 0) {
            for (AccessTokenBean accessTokenBean : needTookenList) {
                map = new HashMap();
                map.put("account", accessTokenBean.getAccount());
                deviceFragmentImpl.getAccessToken(map, this);
            }
        } else {
            // TODO: 2019/1/20 放入Adapter中，在展开列表时进行加载
//            getCameraState();
            aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
//            mView.getAccessTokenNext();
        }
    }

    //收集待请求的account对的token
    private void checkTokenState(DeviceListBean bean) {
        if (bean.getAccessTokenBean() == null) {
            if (needTookenList.size() == 0) {
                needTookenList.add(new AccessTokenBean(bean.getAccount()));
            } else {
                for (AccessTokenBean accessTokenBean : needTookenList) {
                    if (StringUtils.equals(accessTokenBean.getAccount(), bean.getAccount()))
                        isHave = true;
                }
                if (!isHave) {
                    needTookenList.add(new AccessTokenBean(bean.getAccount()));
                }
            }
        }
        isHave = false;
    }

    @Override
    public void getAccessTokenNext(AccessTokenBean info) {
        if (info == null || StringUtils.isEmpty(info.getAccessToken())) {
            if (mView != null)
                mView.showMessage(0, "获取AccessToken失败，请重新登陆！");
            return;
        }
        for (AcacheUserBean.DeviceBean.ProvinceListBean provinceListBean : acacheUserBean.getDeviceBean().getProvinceList()) {
            for (WxdepartmentListBean wxdepartmentListBean : provinceListBean.getWxdepartmentList()) {
                //V
                for (LineListBean lineListBean : wxdepartmentListBean.getVisualization().getLineList()) {
                    for (TowerListBean towerListBean : lineListBean.getTowerList()) {
                        for (final DeviceListBean bean : towerListBean.getDeviceList()) {
                            if (StringUtils.equals(info.getAccount(), bean.getAccount()))
                                bean.setAccessTokenBean(info);
                        }
                    }
                }
                //C
                for (DeviceListBean bean : wxdepartmentListBean.getComprehensive().getDeviceList()) {
                    if (StringUtils.equals(info.getAccount(), bean.getAccount()))
                        bean.setAccessTokenBean(info);
                }
            }
        }
        aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
        acacheTokenBean = (AcacheTokenBean) aCache.getAsObject(AppConfig.AcacheToken);
        if (acacheTokenBean == null)
            acacheTokenBean = new AcacheTokenBean(new ArrayList<AccessTokenBean>());
        if (acacheTokenBean.getList() == null || acacheTokenBean.getList().size() == 0) {
            acacheTokenBean.setList(new ArrayList<AccessTokenBean>());
            acacheTokenBean.getList().add(info);
        }
        boolean isExit = true;
        for (AccessTokenBean acacheBean : acacheTokenBean.getList()) {
            if (StringUtils.equals(info.getAccount(), acacheBean.getAccount())) {
                acacheBean.setAccessToken(info.getAccessToken());
                isExit = false;
            }
        }
        if (isExit == true)
            acacheTokenBean.getList().add(info);
        aCache.put(AppConfig.AcacheToken, acacheTokenBean, 2 * ACache.TIME_HOUR);
//        mView.showMessage(0, "获取AccessToken成功");
        //查询在线状态
        // TODO: 2019/1/20 放入Adapter中，在展开列表时进行加载
//        getCameraState();
    }

    private void getCameraState() {
        for (AcacheUserBean.DeviceBean.ProvinceListBean provinceListBean : acacheUserBean.getDeviceBean().getProvinceList()) {
            for (WxdepartmentListBean wxdepartmentListBean : provinceListBean.getWxdepartmentList()) {
                //V
                for (LineListBean lineListBean : wxdepartmentListBean.getVisualization().getLineList()) {
                    for (TowerListBean towerListBean : lineListBean.getTowerList()) {
                        for (final DeviceListBean bean : towerListBean.getDeviceList()) {
                            getState(bean);
                        }
                    }
                }
                //C
                for (DeviceListBean bean : wxdepartmentListBean.getComprehensive().getDeviceList()) {
                    getState(bean);
                }
            }
        }
        aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
    }

    private void getState(final DeviceListBean bean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                if (bean == null) {
                    mView.showMessage(0, "bean数据为空！");
                    return;
                }
                MyApplication.getEzSDKInstance().setAccessToken(bean.getAccessTokenBean().getAccessToken());
                EZProbeDeviceInfoResult result = MyApplication.getEzSDKInstance().probeDeviceInfo(bean.getDeviceSerial(), "");
                if (result == null || result.getEZProbeDeviceInfo() == null)
                    msg.what = 0;
                else
                    bean.setStatus(result.getEZProbeDeviceInfo().getStatus());
                try {
                    bean.setImgUrl(MyApplication.getEzSDKInstance().captureCamera(bean.getDeviceSerial(), AppConfig.CameraNo));
                } catch (BaseException e) {
                    msg.what = 0;
                    e.printStackTrace();
                }
                if (bean.getStatus() == 1 && !StringUtils.isEmpty(bean.getImgUrl())) {
                    msg.what = 1;
                }
                handler.sendMessage(msg);
            }
        }).start();

    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mView != null)
                switch (msg.what) {
                    case 0:
//                    mView.showMessage(0, "获取状态失败");
                        break;
                    case 1:
//                    mView.getAccessTokenNext();
                        aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
                        mView.showMessage(0, "获取状态成功");
                        break;
                }
        }
    };

    @Override
    public void onError(String code, String msg) {
        if (mView != null)
            mView.showMessage(Integer.parseInt(code), msg);
    }

    //token过期
    public void tokenOutTime(String outTimeTokenAccount) {
//        acacheTokenBean = (AcacheTokenBean) aCache.getAsObject(AppConfig.AcacheToken);
//        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
//        //清除缓存token列表中的过期的token
//        Iterator<AcacheUserBean.DeviceBean.DeviceListBean.AccessTokenBean> it = acacheTokenBean.getList().iterator();
//        while (it.hasNext()) {
//            AcacheUserBean.DeviceBean.DeviceListBean.AccessTokenBean bean = it.next();
//            if (StringUtils.equals(bean.getAccount(), outTimeTokenAccount)) {
//                it.remove();
//            }
//        }
//        aCache.put(AppConfig.AcacheToken, acacheTokenBean, ACache.TIME_DAY);
//
//        //清除缓存user列表中的过期的token
//        Iterator<AcacheUserBean.DeviceBean.DeviceListBean> itUserBean = acacheUserBean.getDeviceBean().getDeviceList().iterator();
//        while (itUserBean.hasNext()) {
//            AcacheUserBean.DeviceBean.DeviceListBean bean = itUserBean.next();
//            if (StringUtils.equals(bean.getAccount(), outTimeTokenAccount)) {
//                bean.setAccessTokenBean(null);
//            }
//        }
//        aCache.put(AppConfig.AcacheUserBean, acacheUserBean, ACache.TIME_DAY);
//        //开始请求新的token
//        checkTokenAcache();
//        checkToken(acacheUserBean.getDeviceBean());
    }
}
