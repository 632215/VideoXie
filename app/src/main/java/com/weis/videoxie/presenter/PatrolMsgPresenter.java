package com.weis.videoxie.presenter;

import android.content.Context;

import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.presenter.impl.PatrolMsgImpl;
import com.weis.videoxie.presenter.listener.PatrolMsgListener;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.view.PatrolMsgView;

import java.util.HashMap;
import java.util.Map;

public class PatrolMsgPresenter extends BasePresenter<PatrolMsgView> implements PatrolMsgListener {
    private Context mContext = null;
    private PatrolMsgImpl patrolMsgImpl = null;

    public PatrolMsgPresenter(Context mContext) {
        this.mContext = mContext;
        patrolMsgImpl = new PatrolMsgImpl(mContext);
    }

    public void getMsg(PatrolDeviceBean patrolDeviceBean, int page) {
        if (patrolDeviceBean == null || StringUtils.isEmpty(patrolDeviceBean.getId())) {
            mView.showMessage(0, "数据信息错误");
            return;
        }
        Map map = new HashMap();
        switch (patrolDeviceBean.getType()) {
            case "C":
                map.put("deviceSerial", patrolDeviceBean.getId());
                map.put("pageStart", String.valueOf(page));
                map.put("pageSize", "10");
                patrolMsgImpl.getCMsg(map, this);
                break;
            case "V":
                map.put("towerId",patrolDeviceBean.getId());
                patrolMsgImpl.getVMsg(map, this);
                break;
        }
    }

    @Override
    public void getMsgNext(PatrolMsgBean info) {
        if (info == null || info.getLogList() == null || info.getLogList().size() == 0) {
            onError(AppConfig.EZ_DATA_EMPTY, "暂无数据");
            return;
        }
        mView.getMsgNext(info);
    }

    @Override
    public void onError(int i, String msg) {
        mView.showMessage(i, msg);
    }
}
