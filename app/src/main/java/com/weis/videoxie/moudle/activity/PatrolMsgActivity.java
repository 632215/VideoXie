package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.bean.PatrolMsgBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.adapter.PatrolMsgAdapter;
import com.weis.videoxie.moudle.custom.LoadingLayout;
import com.weis.videoxie.presenter.PatrolMsgPresenter;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.PatrolMsgView;

import java.util.List;

import butterknife.BindView;

public class PatrolMsgActivity extends BaseActivity<PatrolMsgView, PatrolMsgPresenter> implements PatrolMsgView, BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    private PatrolDeviceBean patrolDeviceBean = null;
    @BindView(R.id.rv_patrol_msg)
    RecyclerView rvPatrolMsg;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private List<PatrolMsgBean.LogListBean> msgList = null;
    private PatrolMsgAdapter patrolMsgAdapter = null;
    private int page = 0;

    @Override
    protected PatrolMsgPresenter initPresenter() {
        return new PatrolMsgPresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_patrol_msg;
    }

    @Override
    protected void initView() {
        initRefresh();
        initData();
        getMsg();
        loadingLayout.setOnRefreshListener(this);
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (msgList != null)
                    msgList.clear();
                refreshLayout.finishRefresh();
                patrolMsgAdapter.notifyDataSetChanged();
                getMsg();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.finishLoadMore();
    }

    private void initData() {
        patrolDeviceBean = (PatrolDeviceBean) getIntent().getSerializableExtra("DeviceBean");
        if (patrolDeviceBean != null) {
            setTxTitle(patrolDeviceBean.getName());
        }
        showBack();
    }

    private void getMsg() {
        loadingLayout.showLoading();
        presenter.getMsg(patrolDeviceBean, page);
    }

    @Override
    public void getMsgNext(PatrolMsgBean info) {
        this.msgList = info.getLogList();
        if (patrolMsgAdapter == null) {
            patrolMsgAdapter = new PatrolMsgAdapter(R.layout.item_patrol_msg);
            patrolMsgAdapter.bindToRecyclerView(rvPatrolMsg);
            patrolMsgAdapter.setEnableLoadMore(false);
            patrolMsgAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            patrolMsgAdapter.setOnItemClickListener(this);
            rvPatrolMsg.setLayoutManager(new GridLayoutManager(this, 2));
            rvPatrolMsg.setAdapter(patrolMsgAdapter);
        }
        patrolMsgAdapter.setNewData(msgList);
        loadingLayout.showContent();
    }

    @Override
    public void showMessage(final int i, final String msg) {
        this.runOnUiThread(new Runnable() {
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
                ToastUtils.showShort(PatrolMsgActivity.this, msg);
            }
        });
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        finish();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (patrolDeviceBean == null || msgList == null || msgList.get(position) == null) {
            showMessage(0, "数据信息错误");
            return;
        }
        startActivity(new Intent(this, PatrolDetailActivity.class)
                .putExtra("PatrolDeviceBean", patrolDeviceBean)
                .putExtra("MsgBean", msgList.get(position)));
    }


    /**
     * LoadingLayout回调函数
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        getMsg();
    }
}
