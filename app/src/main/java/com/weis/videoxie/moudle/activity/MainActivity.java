package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.AcachePoliceBean;
import com.weis.videoxie.bean.AcacheTokenBean;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.AccessTokenBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.custom.CustomRadioButton;
import com.weis.videoxie.moudle.fragment.DeviceFragment;
import com.weis.videoxie.moudle.fragment.PatrolFragment;
import com.weis.videoxie.moudle.fragment.PoliceFragment;
import com.weis.videoxie.presenter.MainPersenter;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainView, MainPersenter> implements
        RadioGroup.OnCheckedChangeListener
        , MainView {
    @BindView(R.id.rb_device)
    CustomRadioButton rbDevice;
    @BindView(R.id.rb_patrol)
    CustomRadioButton rbMsg;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.tx_name)
    TextView txName;

    private FragmentTransaction transaction;
    private FragmentManager manager;
    private DeviceFragment deviceFragment;
    private PatrolFragment patrolFragment;
    private PoliceFragment policeFragment;
    private List<Fragment> fragmentArrayList = new ArrayList<>();

    @Override
    protected MainPersenter initPresenter() {
        return new MainPersenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        txName.setText(((AcacheUserBean) ACache.get(this).getAsObject(AppConfig.AcacheUserBean)).getName());
        initFrgment();
        addDrawerListener();
    }

    private void addDrawerListener() {
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO: 2018/8/3 待开发
                drawerView.setClickable(true);
            }
        });
    }

    private void setFragment(int index) {
        transaction = manager.beginTransaction();
        for (int x = 0; x < fragmentArrayList.size(); x++) {
            if (x == index) {
                if (fragmentArrayList.get(x).isAdded()) {
                    transaction.show(fragmentArrayList.get(x));
                } else {
                    transaction.add(R.id.fl_main_content, fragmentArrayList.get(x));
                }
            } else {
                if (fragmentArrayList.get(x).isAdded()) {
                    transaction.hide(fragmentArrayList.get(x));
                }
            }
        }
        transaction.commit();
    }

    private void initFrgment() {
        manager = getSupportFragmentManager();
        fragmentArrayList.add(new DeviceFragment());
        fragmentArrayList.add(new PatrolFragment());
        fragmentArrayList.add(new PoliceFragment());
        rgMain.check(R.id.rg_main);
        rbDevice.setChecked(true);
        rgMain.setOnCheckedChangeListener(this);
        setFragment(0);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        setFragment(i == R.id.rb_device ? 0 : i == R.id.rb_patrol ? 1 : 2);
    }

    public void operateDrawer(boolean open) {
        if (drawer.isDrawerOpen(Gravity.START) && open == true)
            drawer.closeDrawer(Gravity.START);
        else
            drawer.openDrawer(Gravity.START);
    }

    @Override
    public void oError(String s) {
        ToastUtils.showLong(this, s);
    }

    @OnClick({R.id.tx_exit, R.id.tx_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_exit:
                AcacheUserBean userBean = null;
                AcacheTokenBean tokenBean = null;
                AcachePoliceBean policeBean = null;
                ACache.get(this).put(AppConfig.AcacheUserBean, userBean);
                ACache.get(this).put(AppConfig.AcachePoliceBean, policeBean);
                ACache.get(this).put(AppConfig.AcacheToken, tokenBean);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

            case R.id.tx_add:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
