package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.DepartmentBean;
import com.weis.videoxie.bean.DeviceListBean;

import java.util.List;

public class DepartmentAdaper extends BaseQuickAdapter<DepartmentBean.DepartmentListBean, DepartmentAdaper.DepartmentViewHolder> {

    public DepartmentAdaper(int layoutResId, @Nullable List<DepartmentBean.DepartmentListBean> data) {
        super(layoutResId, data);
    }

    public DepartmentAdaper(@Nullable List<DepartmentBean.DepartmentListBean> data) {
        super(data);
    }

    public DepartmentAdaper(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final DepartmentViewHolder helper, final DepartmentBean.DepartmentListBean item) {
        helper.setText(R.id.tx_department,item.getWxdepartname());
    }

    public class DepartmentViewHolder extends BaseViewHolder {
        public DepartmentViewHolder(View view) {
            super(view);
        }
    }
}
