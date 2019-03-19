package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.PatrolDeviceBean;
import com.weis.videoxie.utils.StringUtils;

import java.util.List;

public class PatrolDeviceAdapter extends BaseQuickAdapter<PatrolDeviceBean, PatrolDeviceAdapter.DeviceViewHolder> {

    public PatrolDeviceAdapter(int layoutResId, @Nullable List<PatrolDeviceBean> data) {
        super(layoutResId, data);
    }

    public PatrolDeviceAdapter(@Nullable List<PatrolDeviceBean> data) {
        super(data);
    }

    public PatrolDeviceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final DeviceViewHolder helper, final PatrolDeviceBean item) {
        helper.setText(R.id.tx_name, item.getName());
        helper.setTextColor(R.id.tx_name, StringUtils.equals("C", item.getType()) ? mContext.getResources().getColor(R.color.black_two) : mContext.getResources().getColor(R.color.color_black));
    }

    public class DeviceViewHolder extends BaseViewHolder {
        public DeviceViewHolder(View view) {
            super(view);
        }
    }
}
