package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.weis.videoxie.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class VoideoCloudAdaper extends BaseQuickAdapter<EZCloudRecordFile, VoideoCloudAdaper.VoideoHolder> {

    public VoideoCloudAdaper(int layoutResId, @Nullable List<EZCloudRecordFile> data) {
        super(layoutResId, data);
    }

    public VoideoCloudAdaper(@Nullable List<EZCloudRecordFile> data) {
        super(data);
    }

    public VoideoCloudAdaper(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(VoideoHolder helper, EZCloudRecordFile item) {
        Glide.with(mContext)
                .load(item.getCoverPic())
                .into((ImageView) helper.getView(R.id.img_guide));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        helper.setText(R.id.tx_time, sdf.format(item.getStartTime().getTime()));
    }

    public class VoideoHolder extends BaseViewHolder {
        public VoideoHolder(View view) {
            super(view);
        }
    }
}
