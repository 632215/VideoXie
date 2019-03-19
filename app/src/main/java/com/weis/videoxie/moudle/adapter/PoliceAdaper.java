package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.JPushBean;

import java.util.List;

public class PoliceAdaper extends BaseQuickAdapter<JPushBean, BaseViewHolder> {
    public PoliceAdaper(int layoutResId, @Nullable List<JPushBean> data) {
        super(layoutResId, data);
    }
    public PoliceAdaper(int layoutResId) {
        super(layoutResId);
    }
    @Override
    protected void convert(BaseViewHolder helper, JPushBean item) {
        Glide.with(mContext)
                .load(item.getPicUrl())
                .error(R.mipmap.icon_camera_offline)
                .placeholder(R.mipmap.icon_camera_offline)
                .into((ImageView) helper.getView(R.id.img_pic));
        helper.setText(R.id.tx_name,item.getDeviceName());
        helper.setText(R.id.tx_time,item.getAlarmTime());
    }
}
