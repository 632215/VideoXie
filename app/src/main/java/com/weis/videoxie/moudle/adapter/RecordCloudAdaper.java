package com.weis.videoxie.moudle.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.RecordCloudBean;

import java.util.List;

public class RecordCloudAdaper extends BaseSectionQuickAdapter<RecordCloudBean, RecordCloudAdaper.RecordHolder> implements
        BaseQuickAdapter.OnItemClickListener {
    private RecordCloudListener listener;

    public RecordCloudAdaper(int layoutResId, int sectionHeadResId, List<RecordCloudBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    public RecordCloudListener getListener() {
        return listener;
    }

    public void setListener(RecordCloudListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convertHead(RecordHolder helper, RecordCloudBean item) {
        helper.setText(R.id.tx_time, item.header + "点");
    }

    @Override
    protected void convert(RecordHolder helper, RecordCloudBean item) {
        VoideoCloudAdaper voideoCloudAdaper = new VoideoCloudAdaper(R.layout.item_record_video, item.getData());
        voideoCloudAdaper.setEnableLoadMore(true);
        voideoCloudAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        voideoCloudAdaper.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView recyclerVidoe = helper.getView(R.id.recycle_view);
        recyclerVidoe.setLayoutManager(linearLayoutManager);
        recyclerVidoe.setAdapter(voideoCloudAdaper);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        EZCloudRecordFile file = (EZCloudRecordFile) adapter.getData().get(position);
        listener.onVideoCloudClick(file);
    }

    public class RecordHolder extends BaseViewHolder {
        public RecordHolder(View view) {
            super(view);
        }
    }

    public interface RecordCloudListener {
        void onVideoCloudClick(EZCloudRecordFile ezCloudRecordFile);
    }
}
