package com.weis.videoxie.moudle.adapter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weis.videoxie.R;
import com.weis.videoxie.bean.AcacheUserBean;

import java.util.List;

public class UpPowerAdapter extends BaseQuickAdapter<AcacheUserBean.DeviceBean.ProvinceListBean, BaseViewHolder> {
    private CompanyAdapter companyAdapter;

    public UpPowerAdapter(int layoutResId, @Nullable List<AcacheUserBean.DeviceBean.ProvinceListBean> data) {
        super(layoutResId, data);
    }

    public UpPowerAdapter(@Nullable List<AcacheUserBean.DeviceBean.ProvinceListBean> data) {
        super(data);
    }

    public UpPowerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AcacheUserBean.DeviceBean.ProvinceListBean item) {
        helper.setText(R.id.tx_province_title, item.getProvinceName());
        final RecyclerView rVCompany = helper.getView(R.id.rv_company);
        companyAdapter = new CompanyAdapter(R.layout.item_company_title, item.getWxdepartmentList());
        companyAdapter.bindToRecyclerView(rVCompany);
        companyAdapter.setEnableLoadMore(true);
        companyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        rVCompany.setLayoutManager(new LinearLayoutManager(mContext));
        rVCompany.setAdapter(companyAdapter);
        ConstraintLayout clProvinceRoot = helper.getView(R.id.cl_province_root);
        clProvinceRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rVCompany.setVisibility(rVCompany.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }
}
