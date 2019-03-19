package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.LineListBean;

import java.util.List;

public class VAdapter extends BaseQuickAdapter<LineListBean, BaseViewHolder> {
    private VDeviceAdapter vDeviceAdapter;

    public VAdapter(int layoutResId, @Nullable List<LineListBean> data) {
        super(layoutResId, data);
    }

    public VAdapter(@Nullable List<LineListBean> data) {
        super(data);
    }

    public VAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LineListBean item) {
        helper.setText(R.id.tx_v_title, item.getLineName());
        final RecyclerView rvVDevice = helper.getView(R.id.rv_v);
        vDeviceAdapter = new VDeviceAdapter(R.layout.item_device_v, item.getTowerList());
        vDeviceAdapter.bindToRecyclerView(rvVDevice);
        vDeviceAdapter.setEnableLoadMore(false);
        vDeviceAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        rvVDevice.setLayoutManager(new LinearLayoutManager(mContext));
        rvVDevice.setAdapter(vDeviceAdapter);
        TextView txVTitle = helper.getView(R.id.tx_v_title);
        txVTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvVDevice.setVisibility(rvVDevice.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }
}
