package com.weis.videoxie.presenter;

import android.content.Context;

import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.DepartmentBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.presenter.impl.RegisterImpl;
import com.weis.videoxie.presenter.listener.RegisterListener;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.view.RegisterView;

import java.util.HashMap;
import java.util.Map;

public class RegisterPresenter extends BasePresenter<RegisterView> implements RegisterListener {
    private Context mContext;
    private RegisterImpl registerImpl = null;
    private ACache aCache = null;
    private AcacheUserBean userBean = null;

    public RegisterPresenter(Context mContext) {
        this.mContext = mContext;
        registerImpl = new RegisterImpl(mContext);
    }

    //获取部门信息
    public void getDepartment() {
        aCache = ACache.get(mContext);
        userBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
        if (userBean == null || StringUtils.isEmpty(userBean.getName())) {
            if (mView != null) {
                mView.showMessage(0, "用户信息错误，请重新登录");
            }
            return;
        }
        Map map = new HashMap();
        map.put("userName", userBean.getName());
        registerImpl.getDepartment(map, this);
    }

    @Override
    public void getDepartmentNext(DepartmentBean info) {
        if (info == null || info.getDepartmentList() == null || info.getDepartmentList().size() == 0) {
            if (mView != null) {
                mView.showMessage(0, "用户暂无权限");
            }
            return;
        }
        if (mView != null) {
            mView.getDepartmentNext(info);
        }
    }

    @Override
    public void onError(int i, String msg) {
        if (mView!=null){
            mView.showMessage(0,"用户暂无权限");
        }
    }

    public void register(String s) {
        Map map=new HashMap();
        map.put("data",s);
        registerImpl.register(map,this);
    }

    @Override
    public void registerNext() {
        if (mView!=null)
            mView.registerNext();
    }

    @Override
    public void registerError(int i, String msg) {
        if (mView!=null){
            mView.showMessage(0,msg);
        }
    }
}
