package com.weis.videoxie.presenter;

import android.content.Context;

import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.PatrolBean;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.presenter.impl.PatrolFragmentImpl;
import com.weis.videoxie.presenter.listener.PatrolFragmentListener;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.view.PatrolFragmentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatrolFragmentPresenter extends BasePresenter<PatrolFragmentView> implements
        PatrolFragmentListener {
    private Context mContext;
    private PatrolFragmentImpl patrolImpl;
    private AcacheUserBean acacheUserBean = null;
    private ACache aCache = null;

    public PatrolFragmentPresenter(Context mContext) {
        this.mContext = mContext;
        this.patrolImpl = new PatrolFragmentImpl(mContext);
    }

    public void getPartol() {
        aCache = ACache.get(mContext);
        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
        if (acacheUserBean == null || StringUtils.isEmpty(acacheUserBean.getName())) {
            mView.showMessage(0, "用户信息错误，请重新登陆");
            return;
        }
        Map map = new HashMap();
        map.put("userName", acacheUserBean.getName());
        patrolImpl.getPatrol(map, this);
    }

    @Override
    public void getPatrolNext(PatrolBean info) {
        if (info == null ||
                info.getDeviceList() == null && info.getTowerList() == null ||
                info.getTowerList().size() == 0 && info.getDeviceList().size() == 0) {
            onError(AppConfig.EZ_DATA_EMPTY, "暂无数据");
        } else {
            List targetList = new ArrayList();
            for (PatrolBean.DeviceListBean deviceListBean : info.getDeviceList()) {
                targetList.add(new PatrolDeviceBean(deviceListBean.getDeviceName(), deviceListBean.getDeviceSerial(), "C"));
            }
            for (PatrolBean.TowerListBean deviceListBean : info.getTowerList()) {
                targetList.add(new PatrolDeviceBean(deviceListBean.getTowerName(), deviceListBean.getTowerId(), "V"));
            }
            mView.getPatrolNext(targetList);
        }
    }

    @Override
    public void onError(int code, String msg) {
    }
}
