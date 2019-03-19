package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;

import java.util.List;

public class PicAdaper extends BaseQuickAdapter<String, BaseViewHolder> {
    public PicAdaper(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext)
                .load(item)
                .error(R.mipmap.icon_camera_offline)
                .placeholder(R.mipmap.icon_camera_offline)
                .into((ImageView) helper.getView(R.id.img_pic));
    }
}
