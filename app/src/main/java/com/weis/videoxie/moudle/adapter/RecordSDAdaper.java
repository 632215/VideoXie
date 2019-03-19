package com.weis.videoxie.moudle.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.RecordSDBean;
import com.weis.videoxie.bean.RecordSDBean;

import java.util.List;

public class RecordSDAdaper extends BaseSectionQuickAdapter<RecordSDBean, RecordSDAdaper.RecordHolder> implements
        BaseQuickAdapter.OnItemClickListener {
    private RecordSDListener listener;

    public RecordSDAdaper(int layoutResId, int sectionHeadResId, List<RecordSDBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    public RecordSDListener getListener() {
        return listener;
    }

    public void setListener(RecordSDListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convertHead(RecordHolder helper, RecordSDBean item) {
        helper.setText(R.id.tx_time, item.header + "点");
    }

    @Override
    protected void convert(RecordHolder helper, RecordSDBean item) {
        VoideoSDAdaper voideoSDAdaper = new VoideoSDAdaper(R.layout.item_record_video, item.getData());
        voideoSDAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        voideoSDAdaper.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView recyclerVidoe = helper.getView(R.id.recycle_view);
        recyclerVidoe.setLayoutManager(linearLayoutManager);
        recyclerVidoe.setAdapter(voideoSDAdaper);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        EZDeviceRecordFile file = (EZDeviceRecordFile) adapter.getData().get(position);
        listener.onVideoSDClick(file);
    }

    public class RecordHolder extends BaseViewHolder {
        public RecordHolder(View view) {
            super(view);
        }
    }

    public interface RecordSDListener {
        void onVideoSDClick(EZDeviceRecordFile ezCloudRecordFile);
    }
}
