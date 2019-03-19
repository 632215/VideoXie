package com.weis.videoxie.presenter;

import android.content.Context;

import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.presenter.impl.MainImpl;
import com.weis.videoxie.presenter.listener.MainListener;
import com.weis.videoxie.view.MainView;

public class MainPersenter extends BasePresenter<MainView> implements MainListener {
    private Context mContext;
    private MainImpl mainImpl;
    private AcacheUserBean bean;

    public MainPersenter(Context mContext) {
        this.mContext = mContext;
        this.mainImpl = new MainImpl(mContext);
    }

    @Override
    public void onError(String code, String msg) {
        if (mView != null)
            mView.oError(msg);
    }
}
