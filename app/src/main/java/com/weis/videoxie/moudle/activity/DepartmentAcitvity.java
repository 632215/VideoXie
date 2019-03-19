package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.DepartmentBean;
import com.weis.videoxie.moudle.adapter.DepartmentAdaper;

import butterknife.BindView;

public class DepartmentAcitvity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rv_department)
    RecyclerView rvDepartment;

    private DepartmentBean departmentBean = null;
    private DepartmentAdaper departmentAdaper = null;


    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        setTxTitle("选择部门");
        showBack();
        initData();
    }

    private void initData() {
        departmentBean = (DepartmentBean) getIntent().getSerializableExtra("data");
        if (departmentBean == null)
            return;
        departmentAdaper = new DepartmentAdaper(R.layout.item_department, departmentBean.getDepartmentList());
        rvDepartment.setLayoutManager(new LinearLayoutManager(this));
        rvDepartment.setAdapter(departmentAdaper);
        departmentAdaper.setOnItemClickListener(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_department;
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        finish();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent=new Intent();
        intent.putExtra("DepartmentListBean", departmentBean.getDepartmentList().get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
