package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheTokenBean;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.custom.LoadingDialog;
import com.weis.videoxie.moudle.custom.LoadingView;
import com.weis.videoxie.presenter.LoginPresenter;
import com.weis.videoxie.utils.ACache;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.LoginView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tx_line_name)
    TextView txLineName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tx_line_pwd)
    TextView txLinePwd;
    @BindView(R.id.tx_login)
    TextView txLogin;
    @BindView(R.id.tx_register)
    TextView txRegister;

    private ACache aCache;
    private AcacheUserBean acacheUserBean;
    private LoadingDialog dialog = null;

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initView() {
        aCache = ACache.get(this);
        acacheUserBean = (AcacheUserBean) aCache.getAsObject(AppConfig.AcacheUserBean);
        if (acacheUserBean != null && !StringUtils.isEmpty(acacheUserBean.getName())) {
            if (!StringUtils.isEmpty(acacheUserBean.getPwd())) {
                JPushInterface.setAlias(this, 0, acacheUserBean.getName());//注册别名
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else
                etName.setText(acacheUserBean.getName());
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.tx_login, R.id.tx_reset, R.id.tx_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_login:
                if (StringUtils.isEmpty(etName.getText().toString().trim())) {
                    ToastUtils.showLong(this, "请输入用户名！");
                    return;
                }
                if (StringUtils.isEmpty(etPwd.getText().toString().trim())) {
                    ToastUtils.showLong(this, "请输入密码！");
                    return;
                }
                if (dialog == null) {
                    dialog = new LoadingDialog(this);
                }
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                presenter.login(etName.getText().toString().trim(), etPwd.getText().toString().trim());
                break;

            case R.id.tx_reset:
//                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.tx_register:
//                startActivityForResult(new Intent(this, RegisterActivity.class), 1);
                break;
        }
    }


    @Override
    public void showMessage(String code, final String msg) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(LoginActivity.this, msg);
            }
        });
    }

    @Override
    public void loginNext() {
        if (acacheUserBean == null) {
            acacheUserBean = new AcacheUserBean(etName.getText().toString().trim(),etPwd.getText().toString().trim());
        } else if (!StringUtils.equals(acacheUserBean.getName(), etName.getText().toString().trim())) {
            JPushInterface.deleteAlias(this, 1);//删除原有的alias
            acacheUserBean.setName(etName.getText().toString().trim());
            acacheUserBean.setPwd(etPwd.getText().toString().trim());
        }
        aCache.put(AppConfig.AcacheUserBean, acacheUserBean, 2 * ACache.TIME_HOUR);
        JPushInterface.setAlias(this, 0, acacheUserBean.getName());//注册别名
        if (dialog != null)
            dialog.dismiss();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
