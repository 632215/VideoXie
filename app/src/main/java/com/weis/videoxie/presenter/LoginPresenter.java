package com.weis.videoxie.presenter;

import android.content.Context;

import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.EmptyBean;
import com.weis.videoxie.moudle.activity.LoginActivity;
import com.weis.videoxie.presenter.impl.LoginImpl;
import com.weis.videoxie.presenter.listener.LoginListener;
import com.weis.videoxie.view.LoginView;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter extends BasePresenter<LoginView> implements LoginListener {
    private Context mContext;
    private LoginImpl loginImpl;

    public LoginPresenter(Context mContext) {
        this.mContext=mContext;
        loginImpl=new LoginImpl(mContext);
    }

    public void login(String name, String pwd) {
        Map map=new HashMap<>();
        map.put("userName",name);
        map.put("passWord",pwd);
        loginImpl.login(map,this);
    }

    @Override
    public void loginNext(EmptyBean info) {
        if (mView!=null){
            mView.loginNext();
        }
    }

    @Override
    public void onError(String code, String msg) {
        if (mView!=null){
            mView.showMessage(code,"用户名或密码错误");
        }
    }
}
