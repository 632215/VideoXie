package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZStorageStatus;
import com.weis.videoxie.R;
import com.weis.videoxie.application.MyApplication;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.moudle.custom.SDCleanDialog;
import com.weis.videoxie.presenter.DevicePresenter;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.DeviceView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceActivity extends BaseActivity<DeviceView, DevicePresenter> implements DeviceView, SDCleanDialog.SDCleanDialogListener {
    @BindView(R.id.img_device)
    ImageView imgDevice;
    @BindView(R.id.tx_number)
    TextView txNumber;
    @BindView(R.id.tx_name)
    TextView txName;
    @BindView(R.id.tx_real)
    TextView txReal;
    @BindView(R.id.tx_record)
    TextView txRecord;
    private DeviceListBean deviceBean = null;
    private EZDeviceInfo ezDeviceInfo = null;
    private EZStorageStatus storageStatus = null;
    private SDCleanDialog sdCleanDialog = null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_device;
    }

    @Override
    protected DevicePresenter initPresenter() {
        return new DevicePresenter(this);
    }

    @Override
    protected void initView() {
        setTxTitle("设备信息");
        showBack();
        initData();
    }

    private void initData() {
        deviceBean = (DeviceListBean) getIntent().getSerializableExtra("deviceBean");
        if (deviceBean != null) {
            txName.setText("设备:" + deviceBean.getDeviceName());
            txNumber.setText("No.:" + deviceBean.getDeviceSerial());
            txReal.setEnabled(true);
            txReal.setBackgroundResource(R.drawable.shape_blue_radio);
            Glide.with(this)
                    .load(deviceBean.getImgUrl())
                    .placeholder(R.mipmap.icon_camera_online)
                    .error(R.mipmap.icon_camera_online)
                    .into(imgDevice);
            presenter.getDeviceInfo(deviceBean);
            presenter.getSDInfo(deviceBean);
            if (deviceBean.getAccessTokenBean() != null)
                MyApplication.getEzSDKInstance().setAccessToken(deviceBean.getAccessTokenBean().getAccessToken());
        }
    }

    @Override
    public void getDeviceInfoNext(EZDeviceInfo ezDeviceInfo) {
        this.ezDeviceInfo = ezDeviceInfo;
    }

    @Override
    public void getSDInfoNext(List sdList) {
        if (sdList != null && sdList.size() > 0) {
            this.storageStatus = (EZStorageStatus) sdList.get(0);
        }
    }

    @Override
    public void showMessage(final int errCode, final String errMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong(DeviceActivity.this, errMessage);
            }
        });
    }

    @OnClick({R.id.tx_real, R.id.tx_record, R.id.tx_clear_sd, R.id.tx_set_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_real:
                if (deviceBean.getAccessTokenBean() != null)
                    startActivity(new Intent(this, LiveActivity.class).putExtra("deviceBean", deviceBean));
                else
                    showMessage(0, "Token不存在");
                break;

            case R.id.tx_record:
                if (deviceBean == null) {
                    showMessage(0, "设备信息错误");
                    return;
                }
                startActivity(new Intent(this, RecordActivity.class).putExtra("deviceBean", deviceBean));
                break;

            case R.id.tx_clear_sd:
                if (sdCleanDialog == null)
                    sdCleanDialog = new SDCleanDialog(this, this);
                sdCleanDialog.show();
                break;

            case R.id.tx_set_info:
                startActivityForResult(new Intent(this, SetDeviceActivity.class)
                        .putExtra("ezDeviceInfo", ezDeviceInfo)
                        .putExtra("deviceBean", deviceBean), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case 0:
                String name = data.getStringExtra("name");
                if (!StringUtils.isEmpty(name))
                    txName.setText(name);
                break;
        }
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        finish();
    }

    @Override
    public void sDCleanDialogSure() {
        presenter.deviceClearSd(deviceBean, storageStatus);
    }
}
