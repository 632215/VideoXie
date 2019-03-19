package com.weis.videoxie.moudle.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.moudle.custom.LoadingDialog;
import com.weis.videoxie.presenter.SetDevicePresenter;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.SetDeviceView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SetDeviceActivity extends BaseActivity<SetDeviceView, SetDevicePresenter> implements
        SetDeviceView, Rationale<List<String>>, AMapLocationListener {
    @BindView(R.id.tx_current_name)
    TextView txCurrentName;
    @BindView(R.id.tx_commit)
    TextView txCommit;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tx_location_info)
    TextView txLocationInfo;

    private EZDeviceInfo ezDeviceInfo = null;
    private DeviceListBean deviceBean = null;
    private AMapLocation aMapLocation = null;
    private LoadingDialog dialog = null;

    @Override
    protected SetDevicePresenter initPresenter() {
        return new SetDevicePresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_device;
    }

    @Override
    protected void initView() {
        setTxTitle(getString(R.string.activity_device_name_title));
        showBack();
        initData();
    }

    private void initPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.LOCATION)
                .rationale(this)//添加拒绝权限回调
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        startLoaction();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        showMessage(0, "没有获取定位权限，无法定位");
                        if (AndPermission.hasAlwaysDeniedPermission(SetDeviceActivity.this, data)) {
                            //true，弹窗再次向用户索取权限
                            showMessage(0, "请前往设置界面进行权限授予，否则无法使用对讲功能");
                        }
                    }
                }).start();
    }

    //开始定位
    private void startLoaction() {
        AMapLocationClient mlocationClient;
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setInterval(2000);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    private void initData() {
        deviceBean = (DeviceListBean) getIntent().getSerializableExtra("deviceBean");
        ezDeviceInfo = getIntent().getParcelableExtra("ezDeviceInfo");
        if (deviceBean != null) {
            txCurrentName.setText(getString(R.string.activity_device_current_name) + deviceBean.getDeviceName());
            etName.setText(deviceBean.getDeviceName());
        }
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                txCommit.setBackground(editable.toString().length() > 0 ? getResources().getDrawable(R.drawable.shape_blue_radio) :
                        getResources().getDrawable(R.drawable.shape_gray_radio));
            }
        });
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        returnBack("");
    }

    @OnClick({R.id.tx_commit, R.id.tx_get_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_commit:
                if (StringUtils.isEmpty(etName.getText().toString().trim())) {
                    showMessage(0, "请输入新的设备名！");
                    return;
                }
                if (aMapLocation == null || StringUtils.isEmpty(txLocationInfo.getText().toString().trim())) {
                    showMessage(0, "请获取经纬度信息！");
                    return;
                }
                if (dialog == null) {
                    dialog = new LoadingDialog(this);
                }
                dialog.show();
                presenter.setDeviceName(deviceBean, etName.getText().toString().trim(), aMapLocation);
                break;

            case R.id.tx_get_location:
                initPermission();
                break;
        }
    }

    @Override
    public void setDeviceNameNext() {
        if (dialog != null)
            dialog.dismiss();
        showMessage(0, "修改设备名成功！");
        returnBack(etName.getText().toString().trim());
    }

    private void returnBack(String name) {
        Intent intent = new Intent();
        intent.putExtra("name", name);
        setResult(0, intent);
        finish();
    }

    @Override
    public void showMessage(int code, final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null)
                    dialog.dismiss();
                if (!StringUtils.isEmpty(msg))
                    ToastUtils.showShort(SetDeviceActivity.this, msg);
            }
        });
    }

    @Override
    public void showRationale(Context context, List<String> data, RequestExecutor executor) {
        showMessage(0, "定位失败！");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            this.aMapLocation = aMapLocation;
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getLongitude();//获取经度
                txLocationInfo.setText("经度:" + aMapLocation.getLongitude() + ",纬度:" + aMapLocation.getLatitude());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                ToastUtils.showShort(SetDeviceActivity.this, aMapLocation.getErrorInfo());
            }
        }
    }
}
