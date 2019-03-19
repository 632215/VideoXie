package com.weis.videoxie.moudle.fragment;

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
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.activity.PatrolMsgActivity;
import com.weis.videoxie.moudle.adapter.PatrolDeviceAdapter;
import com.weis.videoxie.moudle.custom.LoadingLayout;
import com.weis.videoxie.presenter.PatrolFragmentPresenter;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.PatrolFragmentView;

import java.util.List;

import butterknife.BindView;

public class PatrolFragment extends BaseTitleFragment<PatrolFragmentView, PatrolFragmentPresenter>
        implements PatrolFragmentView, BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.rv_patrol_device)
    RecyclerView rvPatrolDevice;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<PatrolDeviceBean> deviceList = null;
    private PatrolDeviceAdapter patrolDeviceAdapter = null;

    @Override
    protected int getContentView() {
        return R.layout.fargment_patrol_msg;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
//        setTxBack(getActivity(), R.mipmap.icon_person);
        setRlColor(R.color.color_main);
        setTxTitle(getResources().getString(R.string.activity_main_patrol));
        initRecycleView();
        getPatrol();
        loadingLayout.setOnRefreshListener(this);
    }

    private void getPatrol() {
        loadingLayout.showLoading();
        presenter.getPartol();
    }

    private void initRecycleView() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (deviceList != null)
                    deviceList.clear();
                refreshLayout.finishRefresh();
                patrolDeviceAdapter.notifyDataSetChanged();
                presenter.getPartol();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.finishLoadMore();
    }

    @Override
    protected PatrolFragmentPresenter initPresenter() {
        return new PatrolFragmentPresenter(getActivity());
    }

    @Override
    public void showMessage(final int i, final String s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (i) {
                    case AppConfig.EZ_DATA_EMPTY:
                        loadingLayout.showEmpty();
                        break;
                    default:
                        loadingLayout.showError();
                        break;
                }
                ToastUtils.showShort(getActivity(), s);
            }
        });
    }

    @Override
    public void getPatrolNext(List info) {
        this.deviceList = info;
        if (patrolDeviceAdapter == null) {
            patrolDeviceAdapter = new PatrolDeviceAdapter(R.layout.item_patrol_device);
            patrolDeviceAdapter.bindToRecyclerView(rvPatrolDevice);
            patrolDeviceAdapter.setEnableLoadMore(false);
            patrolDeviceAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            patrolDeviceAdapter.setOnItemClickListener(this);
            rvPatrolDevice.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvPatrolDevice.setAdapter(patrolDeviceAdapter);
        }
        patrolDeviceAdapter.setNewData(deviceList);
        loadingLayout.showContent();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PatrolDeviceBean deviceBean = deviceList.get(position);
        if (deviceBean == null) {
            return;
        }
        startActivity(new Intent(getActivity(), PatrolMsgActivity.class).putExtra("DeviceBean", deviceBean));
    }

    /**
     * LoadingLayout回调函数
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        getPatrol();
    }
}
