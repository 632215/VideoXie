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
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.base.BaseTitleFragment;
import com.weis.videoxie.bean.AcachePoliceBean;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.JPushBean;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.activity.PatrolDetailActivity;
import com.weis.videoxie.moudle.adapter.PoliceAdaper;
import com.weis.videoxie.moudle.custom.LoadingLayout;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PoliceFragment extends BaseTitleFragment implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.rv_police_device)
    RecyclerView rvPoliceDevice;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private ACache aCache = null;
    private AcachePoliceBean acachePoliceBean = null;
    private AcacheUserBean userBean = null;
    private List<JPushBean> dataList = null;
    private PoliceAdaper policeAdaper = null;

    @Override
    protected int getContentView() {
        return R.layout.fargment_police_msg;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
//        setTxBack(getActivity(), R.mipmap.icon_person);
        setRlColor(R.color.color_main);
        setTxTitle(getResources().getString(R.string.activity_main_police));
        initRecycleView();
        initData();
        loadingLayout.setOnRefreshListener(this);
    }

    private void initData() {
        loadingLayout.showLoading();
        aCache = ACache.get(getActivity());
        acachePoliceBean = (AcachePoliceBean) aCache.getAsObject(AppConfig.AcachePoliceBean);
        userBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
        if (userBean == null || acachePoliceBean == null || acachePoliceBean.getList() == null || acachePoliceBean.getList().size() == 0) {
            loadingLayout.showEmpty();
            return;
        }
        if (dataList == null)
            dataList = new ArrayList<>();
        for (JPushBean jPushBean : acachePoliceBean.getList()) {
            if (StringUtils.equals(jPushBean.getName(), userBean.getName()) && !StringUtils.isEmpty(jPushBean.getAlarmTime()))
                dataList.add(jPushBean);
        }
        if (dataList.size() == 0) {
            loadingLayout.showEmpty();
            return;
        } else {
            policeAdaper.setNewData(dataList);
            loadingLayout.showContent();
        }
    }

    private void initRecycleView() {
        if (policeAdaper == null) {
            policeAdaper = new PoliceAdaper(R.layout.item_police_device);
            policeAdaper.bindToRecyclerView(rvPoliceDevice);
            policeAdaper.setEnableLoadMore(false);
            policeAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            policeAdaper.setOnItemClickListener(this);
            rvPoliceDevice.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvPoliceDevice.setAdapter(policeAdaper);
        }
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (dataList != null)
                    dataList.clear();
                initData();
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.finishLoadMore();
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(getContext(), PatrolDetailActivity.class)
                .putExtra("JPushBean", (JPushBean) adapter.getData().get(position)));
    }

    /**
     * LoadingLayout回调函数
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        initData();
    }
}
