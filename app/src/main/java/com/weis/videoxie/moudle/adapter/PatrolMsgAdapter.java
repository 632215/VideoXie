package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.PatrolMsgBean;

import java.util.List;

public class PatrolMsgAdapter extends BaseQuickAdapter<PatrolMsgBean.LogListBean, PatrolMsgAdapter.DeviceViewHolder> {

    public PatrolMsgAdapter(int layoutResId, @Nullable List<PatrolMsgBean.LogListBean> data) {
        super(layoutResId, data);
    }

    public PatrolMsgAdapter(@Nullable List<PatrolMsgBean.LogListBean> data) {
        super(data);
    }

    public PatrolMsgAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final DeviceViewHolder helper, final PatrolMsgBean.LogListBean item) {
        Glide.with(mContext)
                .load(item.getPicUrlList() != null && item.getPicUrlList().size() > 0 ? item.getPicUrlList().get(0) : R.mipmap.icon_camera_offline)
                .error(R.mipmap.icon_camera_offline)
                .placeholder(R.mipmap.icon_camera_offline)
                .into((ImageView) helper.getView(R.id.img_msg));
        helper.setText(R.id.tx_time, item.getPushTime());
        helper.setText(R.id.tx_type, item.getType());
    }

    public class DeviceViewHolder extends BaseViewHolder {
        public DeviceViewHolder(View view) {
            super(view);
        }
    }
}
