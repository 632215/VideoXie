package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.DepartmentBean;
import com.weis.videoxie.bean.RegisterBean;
import com.weis.videoxie.presenter.RegisterPresenter;
import com.weis.videoxie.utils.GsonUtil;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.RegisterView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<RegisterView, RegisterPresenter> implements RegisterView {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_confirm)
    EditText etPwdConfirm;
    @BindView(R.id.tx_sure)
    TextView txSure;
    @BindView(R.id.image_check)
    ImageView imageCheck;
    @BindView(R.id.tx_login_deal)
    TextView txLoginDeal;
    @BindView(R.id.et_user_id)
    EditText etUserId;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tx_department)
    TextView txDepartment;
    @BindView(R.id.et_position)
    EditText etPosition;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.tx_out_look)
    TextView txOutLook;
    @BindView(R.id.tx_copy_right)
    TextView txCopyRight;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    private String userAgreementState = "true";
    private DepartmentBean.DepartmentListBean departmentListBean = null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected void initView() {
        showBack();
        setTxTitle(getString(R.string.activity_register_sure));
    }

    @OnClick({R.id.tx_sure, R.id.image_check, R.id.tx_login_deal, R.id.tx_department})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_sure:
                checkInfo();
                break;

            case R.id.image_check:
                switch (userAgreementState) {
                    case "true":
                        imageCheck.setImageResource(R.mipmap.icon_register_un_selected);
                        userAgreementState = "false";
                        break;

                    case "false":
                        imageCheck.setImageResource(R.mipmap.icon_register_selected);
                        userAgreementState = "true";
                        break;
                }
                break;

            case R.id.tx_login_deal:
                break;

            case R.id.tx_department://所在部门
                presenter.getDepartment();
                break;
        }
    }

    //检查信息是否填写
    private void checkInfo() {
        if (!StringUtils.equals("true", userAgreementState)) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_protocol));
            return;
        }
        if (StringUtils.isEmpty(etUserId.getText().toString().trim())) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_user_id));
            return;
        }
        if (StringUtils.isEmpty(txDepartment.getText().toString().trim())) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_department));
            return;
        }
        if (StringUtils.isEmpty(etPhone.getText().toString().trim()) || etPhone.getText().toString().trim().length() < 11) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_phone));
            return;
        }
        if (StringUtils.isEmpty(etPwd.getText().toString())
                || etPwd.getText().toString().length() < 6) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_pwd));
            return;
        }
        if (StringUtils.isEmpty(etPwdConfirm.getText().toString())
                || !StringUtils.equals(etPwdConfirm.getText().toString(), etPwd.getText().toString())) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_pwd_confirm));
            return;
        }
        if (StringUtils.isEmpty(etName.getText().toString().trim())) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_user_name));
            return;
        }
        if (!rbMale.isChecked() && !rbFemale.isChecked()) {
            ToastUtils.showShort(this, getResources().getString(R.string.activity_register_please_sex));
            return;
        }
        RegisterBean registerBean=new RegisterBean();
        registerBean.setUserid(etUserId.getText().toString().trim());
        registerBean.setPassword(etPwd.getText().toString().trim());
        registerBean.setName(etName.getText().toString().trim());
        registerBean.setWxdepartid(departmentListBean.getWxdepartid());
        registerBean.setPosition(etPosition.getText().toString().trim());
        registerBean.setSex(rbMale.isChecked()?1:2);
        registerBean.setMobile(etPhone.getText().toString().trim());
        presenter.register(GsonUtil.GsonString(registerBean));
    }

    @Override
    public void registerNext() {
        showMessage(0,"注册成功");
        finish();
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        finish();
    }

    @Override
    public void getDepartmentNext(DepartmentBean info) {
        startActivityForResult(new Intent(this, DepartmentAcitvity.class).putExtra("data", info),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    departmentListBean = (DepartmentBean.DepartmentListBean) data.getExtras().getSerializable("DepartmentListBean");
                    txDepartment.setText(departmentListBean.getWxdepartname());
                }
                break;
        }
    }

    @Override
    public void showMessage(int i, final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(RegisterActivity.this, s);
            }
        });
    }
}
