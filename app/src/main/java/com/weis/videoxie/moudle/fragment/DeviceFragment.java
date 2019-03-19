package com.weis.videoxie.moudle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseTitleFragment;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.WxdepartmentListBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.activity.MainActivity;
import com.weis.videoxie.moudle.activity.RegisterActivity;
import com.weis.videoxie.moudle.adapter.LowPowerAdapter;
import com.weis.videoxie.moudle.adapter.UpPowerAdapter;
import com.weis.videoxie.moudle.custom.LoadingLayout;
import com.weis.videoxie.presenter.DeviceFragmentPresenter;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.DeviceFragmentView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

import butterknife.BindView;

public class DeviceFragment extends BaseTitleFragment<DeviceFragmentView, DeviceFragmentPresenter> implements
        DeviceFragmentView
        , Rationale<List<String>>, View.OnClickListener {
    @BindView(R.id.rv_device)
    RecyclerView rvDevice;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private List<AcacheUserBean.DeviceBean.ProvinceListBean> upPowerDataList = null;
    private UpPowerAdapter upPowerAdapter = null;
    private List<WxdepartmentListBean> lowPowerDataList = null;
    private LowPowerAdapter lowPowerAdapter = null;
    private AcacheUserBean acacheUserBean = null;
    private int count = 0;
    private ACache aCache = null;

    @Override
    protected int getContentView() {
        return R.layout.fargment_device;
    }

    @Override
    protected DeviceFragmentPresenter initPresenter() {
        return new DeviceFragmentPresenter(this.getActivity());
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initPermission();
        initTitle();
        initRecycleView();
        getDevice(false);
        loadingLayout.setOnRefreshListener(this);
    }

    private void getDevice(boolean isGetDevice) {
        loadingLayout.showLoading();
        aCache = ACache.get(getActivity());
        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
        if (acacheUserBean.getDeviceBean() == null || isGetDevice)
            presenter.getDevice();
        else
            getDeviceNext(acacheUserBean.getDeviceBean());
    }

    private void initPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.MICROPHONE)
                .rationale(this)//添加拒绝权限回调
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        showMessage(0, "没有获取录音权限，无法使用对讲功能");
                        if (AndPermission.hasAlwaysDeniedPermission(getActivity(), data)) {
                            //true，弹窗再次向用户索取权限
                            showMessage(0, "请前往设置界面进行权限授予，否则无法使用对讲功能");
                        }
                    }
                }).start();
    }

    private void initRecycleView() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (upPowerDataList != null)
                    upPowerDataList.clear();
                if (lowPowerDataList != null)
                    lowPowerDataList.clear();
                refreshLayout.finishRefresh();
                if (upPowerAdapter != null)
                    upPowerAdapter.notifyDataSetChanged();
                if (lowPowerAdapter != null)
                    lowPowerAdapter.notifyDataSetChanged();
                getDevice(true);
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.finishLoadMore();
    }

    private void initTitle() {
        setTxBack(getActivity(), R.mipmap.icon_person);
        setTxNext(getActivity(), R.mipmap.icon_add);
        setTxTitle(getResources().getString(R.string.activity_main_device));
        setRlColor(R.color.color_main);
    }

    /**
     * getDevice 成功回调
     *
     * @param info
     */
    @Override
    public void getDeviceNext(AcacheUserBean.DeviceBean info) {
        if (info.getWxdepartid() == 1 || info.getWxdepartid() == 16 || info.getWxdepartid() == 17) {//高权限
            upPowerDataList = info.getProvinceList();
            if (upPowerAdapter == null) {
                upPowerAdapter = new UpPowerAdapter(R.layout.item_up_power, upPowerDataList);
                upPowerAdapter.bindToRecyclerView(rvDevice);
                upPowerAdapter.setEnableLoadMore(false);
                upPowerAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                rvDevice.setAdapter(upPowerAdapter);
            }
            upPowerAdapter.setNewData(upPowerDataList);
        } else {//低权限
            lowPowerDataList = info.getProvinceList().get(0).getWxdepartmentList();
            if (lowPowerAdapter == null) {
                lowPowerAdapter = new LowPowerAdapter(R.layout.item_low_power, lowPowerDataList);
                lowPowerAdapter.bindToRecyclerView(rvDevice);
                lowPowerAdapter.setEnableLoadMore(false);
                lowPowerAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                rvDevice.setAdapter(lowPowerAdapter);
            }
            lowPowerAdapter.setNewData(lowPowerDataList);
        }
        rvDevice.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadingLayout.showContent();
    }


    @Override
    public void getAccessTokenNext() {
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        ((MainActivity) getActivity()).operateDrawer(true);
    }

    @Override
    protected void onNext(View view) {
        super.onNext(view);
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }

    @Override
    public void showMessage(final int errCode, final String errMsg) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (errCode) {
                    case 0:
                        ToastUtils.showLong(getActivity(), errMsg);
                        break;

                    default:
                        loadingLayout.showEmpty();
                        ToastUtils.showLong(getActivity(), "errCode:" + errCode + "   errMsg:" + errMsg);
                        break;
                }
            }
        });
    }

    @Override
    public void showRationale(Context context, List<String> data, final RequestExecutor executor) {
        showMessage(0, "无法使用对讲功能");
    }

    /**
     * LoadingLayout回调函数
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        getDevice(true);
    }
}
