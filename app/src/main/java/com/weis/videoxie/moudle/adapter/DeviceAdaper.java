package com.weis.videoxie.moudle.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.moudle.activity.DeviceActivity;
import com.weis.videoxie.moudle.activity.LiveActivity;
import com.weis.videoxie.utils.DrawableUtils;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;

import java.util.List;

public class DeviceAdaper extends BaseQuickAdapter<DeviceListBean, DeviceAdaper.DeviceViewHolder> {

    public DeviceAdaper(int layoutResId, @Nullable List<DeviceListBean> data) {
        super(layoutResId, data);
    }

    public DeviceAdaper(@Nullable List<DeviceListBean> data) {
        super(data);
    }

    public DeviceAdaper(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final DeviceViewHolder helper, final DeviceListBean item) {
        helper.setText(R.id.tx_name, item.getDeviceName())
                .setText((R.id.tx_state), "在线");
        ConstraintLayout clRoot = helper.getView(R.id.cl_root);
        TextView txSee = helper.getView(R.id.tx_see);
        DrawableUtils.setDrawableLeft(mContext, ((TextView) helper.getView(R.id.tx_state)), R.mipmap.icon_online);
        Glide.with(mContext)
                .load(item.getImgUrl())
                .error(R.mipmap.icon_camera_online)
                .placeholder(R.mipmap.icon_camera_online)
                .into((ImageView) helper.getView(R.id.img_video));
        (helper.getView(R.id.tx_see)).setVisibility(View.VISIBLE);
        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, DeviceActivity.class)
                        .putExtra("deviceBean", item)
                );
            }
        });
        txSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (item.getAccessTokenBean() != null)
                        mContext.startActivity(new Intent(mContext, LiveActivity.class).putExtra("deviceBean", item));
                    else
                        ToastUtils.showShort(mContext, "Token不存在");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class DeviceViewHolder extends BaseViewHolder {
        public DeviceViewHolder(View view) {
            super(view);
        }
    }
}
