package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.weis.videoxie.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class VoideoSDAdaper extends BaseQuickAdapter<EZDeviceRecordFile, VoideoSDAdaper.VoideoHolder> {

    public VoideoSDAdaper(int layoutResId, @Nullable List<EZDeviceRecordFile> data) {
        super(layoutResId, data);
    }

    public VoideoSDAdaper(@Nullable List<EZDeviceRecordFile> data) {
        super(data);
    }

    public VoideoSDAdaper(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(VoideoHolder helper, EZDeviceRecordFile item) {
        Glide.with(mContext)
                .load(R.mipmap.icon_camera_online)
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
