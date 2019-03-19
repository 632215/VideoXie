package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.videogo.openapi.EZConstants;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.weis.videoxie.R;
import com.weis.videoxie.application.MyApplication;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.LiveBean;
import com.weis.videoxie.bean.WeatherBean;
import com.weis.videoxie.moudle.adapter.LiveAdaper;
import com.weis.videoxie.moudle.custom.MyGridLayoutManager;
import com.weis.videoxie.moudle.custom.TalkPopupWindows;
import com.weis.videoxie.presenter.LivePresenter;
import com.weis.videoxie.utils.DrawableUtils;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.LiveView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LiveActivity extends BaseActivity<LiveView, LivePresenter>
        implements LiveView
        , View.OnTouchListener
        , TalkPopupWindows.TalkPopupListner
        , LiveAdaper.LiveAdapterListener {
    @BindView(R.id.rv_live)
    RecyclerView rvLive;
    @BindView(R.id.tx_state)
    TextView txState;
    @BindView(R.id.cl_root)
    ConstraintLayout clRoot;
    @BindView(R.id.tx_device_name)
    TextView txDeviceName;
    @BindView(R.id.cb_play)
    CheckBox cbPlay;
    @BindView(R.id.cb_voice)
    CheckBox cbVoice;
    @BindView(R.id.tx_definition)
    TextView txDefinition;
    @BindView(R.id.img_pre)
    ImageView imgPre;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.cl_operate)
    ConstraintLayout clOperate;
    @BindView(R.id.cl_control)
    ConstraintLayout clControl;
    @BindView(R.id.tx_address)
    TextView txAddress;
    @BindView(R.id.tx_environment_temp)
    TextView txEnvironmentTemp;
    @BindView(R.id.tx_hum_temp)
    TextView txHumTemp;
    @BindView(R.id.tx_see_area)
    TextView txSeeArea;
    @BindView(R.id.tx_cond)
    TextView txCond;
    @BindView(R.id.tx_wind_speed)
    TextView txWindSpeed;
    @BindView(R.id.tx_wind_dec)
    TextView txWindDec;
    @BindView(R.id.tx_wind_power)
    TextView txWindPower;
    @BindView(R.id.tx_rain)
    TextView txRain;
    @BindView(R.id.tx_hum)
    TextView txHum;
    @BindView(R.id.tx_pres)
    TextView txPres;
    @BindView(R.id.tx_aqi)
    TextView txAqi;
    @BindView(R.id.tx_aqi_value)
    TextView txAqiValue;
    @BindView(R.id.tx_air_aqi)
    TextView txAirAqi;
    @BindView(R.id.tx_no2)
    TextView txNo2;
    @BindView(R.id.tx_so2)
    TextView txSo2;
    @BindView(R.id.tx_co)
    TextView txCo;
    @BindView(R.id.tx_pm2)
    TextView txPm2;
    @BindView(R.id.tx_o3)
    TextView txO3;
    @BindView(R.id.cl_weather)
    ConstraintLayout clWeather;
    @BindView(R.id.tx_screenshots)
    TextView txScreenshots;
    @BindView(R.id.tx_talk)
    TextView txTalk;
    @BindView(R.id.tx_recording)
    TextView txRecording;
    @BindView(R.id.tx_control)
    TextView txControl;
    @BindView(R.id.ib_control_left)
    ImageButton ibControlLeft;
    @BindView(R.id.rb_control_center)
    RadioButton rbControlCenter;
    @BindView(R.id.ib_control_right)
    ImageButton ibControlRight;
    @BindView(R.id.ib_control_top)
    ImageButton ibControlTop;
    @BindView(R.id.ib_control_bottom)
    ImageButton ibControlBottom;
    @BindView(R.id.cl_cloud_control)
    ConstraintLayout clCloudControl;
    @BindView(R.id.img_recording)
    ImageView imgRecording;

    private DeviceListBean deviceBean = null;
    private EZCameraInfo ezCameraInfo = null;
    private EZDeviceInfo ezDeviceInfo = null;
    //对话
    private TalkPopupWindows talkPopupWindows = null;
    private boolean mIsTalking = false;
    AnimationDrawable animationDrawable = null;

    private LiveAdaper liveAdaper = null;
    private List<LiveBean> liveBeanList = null;
    private AcacheUserBean acacheUserBean = null;
    private int currentAdapterIndex = -1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_live;
    }

    @Override
    protected LivePresenter initPresenter() {
        return new LivePresenter(this);
    }

    @Override
    protected void initView() {
        initTitle();
        initData();
        initAdapter();
    }

    //初始化标题
    private void initTitle() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        setTxBack(this, R.mipmap.icon_back_arrow);
        setTxNextText(getResources().getString(R.string.activity_live_record));
        setTxTitle(getResources().getString(R.string.activity_live_title));
        setRlColor(R.color.color_main);
        ibControlRight.setOnTouchListener(this);
        ibControlTop.setOnTouchListener(this);
        ibControlBottom.setOnTouchListener(this);
        ibControlLeft.setOnTouchListener(this);
    }

    //初始化数据
    private void initData() {
        deviceBean = (DeviceListBean) getIntent().getSerializableExtra("deviceBean");
        if (deviceBean == null)
            return;
        liveBeanList = presenter.initDataList(deviceBean);
        if (liveBeanList != null && liveBeanList.size() > 1) {
            imgNext.setVisibility(View.VISIBLE);
            imgPre.setVisibility(View.GONE);
        }
    }

    //初始化adapter
    private void initAdapter() {
        if (liveBeanList == null || liveBeanList.size() == 0) {
            showMessage(0, "数据为空");
            return;
        }
        if (liveAdaper == null)
            liveAdaper = new LiveAdaper(R.layout.item_live, liveBeanList, this);
        liveAdaper.setActivity(this);
        MyApplication.getEzSDKInstance().setAccessToken(deviceBean.getAccessTokenBean().getAccessToken());
        liveAdaper.bindToRecyclerView(rvLive);
        liveAdaper.setEnableLoadMore(true);
        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(this, 1);
        gridLayoutManager.setScrollEnabled(false);
        rvLive.setLayoutManager(gridLayoutManager);
        rvLive.setAdapter(liveAdaper);
        currentAdapterIndex = 0;
        changeInfo(currentAdapterIndex);
    }

    //改变设备信息，并改变天气信息
    private void changeInfo(int childCount) {
        if (liveBeanList == null || liveBeanList.get(childCount).getDeviceBean() == null) {
            showMessage(0, "数据信息错误");
            return;
        }
        liveAdaper.setCurrnetPosition(childCount);
        if (liveBeanList.get(childCount).getDeviceBean().getStatus() == 0) {
            cbPlay.setVisibility(View.INVISIBLE);
            cbVoice.setVisibility(View.INVISIBLE);
            txDefinition.setVisibility(View.INVISIBLE);
            txTalk.setEnabled(false);
            txTalk.setTextColor(getResources().getColor(R.color.activity_live_gray));
            txControl.setEnabled(false);
            txControl.setTextColor(getResources().getColor(R.color.activity_live_gray));
            txRecording.setEnabled(false);
            txRecording.setTextColor(getResources().getColor(R.color.activity_live_gray));
            txScreenshots.setEnabled(false);
            txScreenshots.setTextColor(getResources().getColor(R.color.activity_live_gray));
            rbControlCenter.setSelected(false);
            changePlayState(1);
        } else {
            cbPlay.setVisibility(View.VISIBLE);
            cbVoice.setVisibility(View.VISIBLE);
            txDefinition.setVisibility(View.VISIBLE);
            presenter.getDeviceInfo(liveBeanList.get(childCount).getDeviceBean());
        }
        txDeviceName.setText(liveBeanList.get(childCount).getDeviceBean().getDeviceName());
        presenter.getWeather(liveBeanList.get(childCount).getDeviceBean());
        if (currentAdapterIndex == 0) {
            imgPre.setVisibility(View.GONE);
            imgNext.setVisibility(View.VISIBLE);
        } else if (currentAdapterIndex == liveBeanList.size() - 1) {
            imgPre.setVisibility(View.VISIBLE);
            imgNext.setVisibility(View.GONE);
        } else {
            imgPre.setVisibility(View.VISIBLE);
            imgNext.setVisibility(View.VISIBLE);
        }
    }

    //获取SDK设备信息后，控制对话、云台等功能的可使用性
    @Override
    public void getDeviceInfoNext(final EZDeviceInfo ezDeviceInfo) {
        if (ezDeviceInfo == null) {
            showMessage(0, "萤石：设备信息错误");
        }
        this.ezDeviceInfo = ezDeviceInfo;
        if (ezDeviceInfo.getCameraNum() > 0 && ezDeviceInfo.getCameraInfoList() != null && ezDeviceInfo.getCameraInfoList().size() > 0) {
            ezCameraInfo = ezDeviceInfo.getCameraInfoList().get(0);
        }
        liveAdaper.setDeviceInfo(ezDeviceInfo, ezCameraInfo);
        LiveActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txTalk.setEnabled(ezDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport ? true : false);
                txTalk.setTextColor(ezDeviceInfo.isSupportTalk() != EZConstants.EZTalkbackCapability.EZTalkbackNoSupport ? getResources().getColor(R.color.color_black) : getResources().getColor(R.color.activity_live_gray));
                txControl.setEnabled(ezDeviceInfo.isSupportPTZ());
                txControl.setTextColor(ezDeviceInfo.isSupportPTZ() ? getResources().getColor(R.color.color_black) : getResources().getColor(R.color.activity_live_gray));
                rbControlCenter.setSelected(false);
            }
        });
    }

    @OnClick({R.id.tx_state, R.id.cb_play, R.id.cb_voice, R.id.tx_definition, R.id.img_pre, R.id.img_next, R.id.tx_control, R.id.tx_screenshots, R.id.tx_recording, R.id.rb_control_center, R.id.tx_talk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tx_state:
                changePlayState(1);
                cbPlay.setChecked(true);
                liveAdaper.playControl(cbPlay.isChecked());
                break;

            case R.id.cb_play:
                if (liveBeanList.get(currentAdapterIndex).getDeviceBean().getStatus() == 1) {
                    changePlayState(cbPlay.isChecked() ? 1 : 4);
                    liveAdaper.playControl(cbPlay.isChecked());
                }
                break;

            case R.id.cb_voice:
                if (liveBeanList.get(currentAdapterIndex).getDeviceBean().getStatus() == 1)
                    liveAdaper.voiceControl(cbVoice.isChecked());
                break;

            case R.id.tx_definition:
                if (liveBeanList.get(currentAdapterIndex).getDeviceBean().getStatus() == 1)
                    liveAdaper.changeDefinition();
                break;

            case R.id.img_pre://前一个
                changeInfo(--currentAdapterIndex);
                break;

            case R.id.img_next://后一个
                changeInfo(++currentAdapterIndex);
                break;

            case R.id.tx_control:
                clCloudControl.setVisibility(clCloudControl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;

            case R.id.tx_screenshots:
                liveAdaper.screenCapture();
                break;

            case R.id.tx_talk:
                talkControl();
                break;

            case R.id.tx_recording:
                liveAdaper.recordingControl();
                break;

            case R.id.rb_control_center:
                break;
        }
    }

    /**
     * 控制语音对话
     */
    private void talkControl() {
        if (mIsTalking) {
            liveAdaper.talkControl(false);
            mIsTalking = false;
        } else {
            liveAdaper.talkControl(true);
            txTalk.setTextColor(getResources().getColor(R.color.color_black));
            if (talkPopupWindows == null)
                talkPopupWindows = new TalkPopupWindows(this, this, liveAdaper.getAdpaterTalkPlay());
            talkPopupWindows.showPopWindow(clRoot);
            mIsTalking = true;
        }
    }

    /**
     * 监听按键操作
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.ib_control_right:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART);
                    break;

                case R.id.ib_control_left:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART);
                    break;

                case R.id.ib_control_top:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART);
                    break;

                case R.id.ib_control_bottom:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART);
                    break;
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            switch (view.getId()) {
                case R.id.ib_control_right:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                    break;

                case R.id.ib_control_left:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                    break;

                case R.id.ib_control_top:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                    break;

                case R.id.ib_control_bottom:
                    liveAdaper.ptzControl(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                    break;
            }
        }
        return false;
    }

    //获取天气成功
    @Override
    public void getWeatherNext(final WeatherBean weatherBean) {
        LiveActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weatherBean == null
                        || weatherBean.getHeWeather5() == null
                        || weatherBean.getHeWeather5().get(0) == null
                        || weatherBean.getHeWeather5().get(0).getBasic() == null
                        || weatherBean.getHeWeather5().get(0).getNow() == null
                        || weatherBean.getHeWeather5().get(0).getBasic() == null
                        || weatherBean.getHeWeather5().get(0).getAqi() == null
                        || weatherBean.getHeWeather5().get(0).getAqi().getCity() == null) {
                    clWeather.setVisibility(View.GONE);
                    return;
                }
                final WeatherBean.HeWeather5Bean.NowBean nowBean = weatherBean.getHeWeather5().get(0).getNow();
                final WeatherBean.HeWeather5Bean.BasicBean basicBean = weatherBean.getHeWeather5().get(0).getBasic();
                final WeatherBean.HeWeather5Bean.AqiBean aqiBean = weatherBean.getHeWeather5().get(0).getAqi();
                clWeather.setVisibility(View.VISIBLE);
                txAddress.setText(basicBean.getCnty() + "-" + basicBean.getCnty() + "-" + basicBean.getCity());
                txEnvironmentTemp.setText("环境温度：" + nowBean.getTmp() + "℃");
                txHumTemp.setText("体感温度：" + nowBean.getFl() + "℃");
                txCond.setText("天气状况：" + nowBean.getCond().getTxt());
                txSeeArea.setText("能见度：" + nowBean.getVis() + "km");
                txWindSpeed.setText("风速：" + nowBean.getWind().getSpd() + "m/s");
                txWindDec.setText("风向：" + nowBean.getWind().getDir());
                txWindPower.setText("风力：" + nowBean.getWind().getDeg());
                txRain.setText("降雨量：" + nowBean.getPcpn() + "mm");
                txHum.setText("湿度：" + nowBean.getHum() + "%");
                txPres.setText("大气压强：" + nowBean.getPres() + "pa");
                txAqiValue.setText("空气质量指数：" + aqiBean.getCity().getAqi());
                txAirAqi.setText("空气质量：" + aqiBean.getCity().getQlty());
                txNo2.setText("二氧化氮：" + aqiBean.getCity().getNo2());
                txSo2.setText("二氧化硫：" + aqiBean.getCity().getSo2());
                txCo.setText("一氧化碳：" + aqiBean.getCity().getCo());
                txPm2.setText("PM2.5：" + aqiBean.getCity().getPm25());
                txO3.setText("臭氧：" + aqiBean.getCity().getO3());
            }
        });
    }

    @Override
    public void showMessage(final int errCode, final String s) {
        LiveActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (errCode) {
                    case 10:
                        liveAdaper.setPlayFailed();
                        changePlayState(3);
                        break;

                    default:
                        break;
                }
                if (s == null || StringUtils.isEmpty(s))
                    return;
                ToastUtils.showLong(LiveActivity.this, s);
            }
        });
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (liveAdaper == null)
            return;
        liveAdaper.notifyDataSetChanged();
    }

    @Override
    protected void onNext(View view) {
        super.onNext(view);
        if (liveBeanList.get(currentAdapterIndex) == null) {
            showMessage(0, "萤石：设备信息错误");
            return;
        }
        startActivity(new Intent(this, RecordActivity.class).putExtra("deviceBean", liveBeanList.get(currentAdapterIndex).getDeviceBean()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        txTalk.setTextColor(getResources().getColor(R.color.color_black));
    }

    //LiveAdapter 操作控制栏回调
    @Override
    public void controlOperateLayout(boolean isVisiable) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clControl.setVisibility(clControl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        }, isVisiable == true ? 100 : 2000);    //延时1.5s执行
    }

    @Override
    public void showMessage(String info) {
        showMessage(0, info);
    }

    //LiveAdapter 设置清晰度回调
    @Override
    public void definitionCallBack(boolean isSuccess, EZConstants.EZVideoLevel mCurrentQulityMode) {
        if (isSuccess) {
            txDefinition.setText(mCurrentQulityMode.getVideoLevel() == 3 ? "超清" :
                    mCurrentQulityMode.getVideoLevel() == 2 ? "高清" :
                            mCurrentQulityMode.getVideoLevel() == 1 ? "标清" : "流畅");
            showMessage(0, "调整清晰度成功");
        } else {
            showMessage(0, "调整清晰度失败");
        }
    }

    //LiveAdapter 录制视频回调
    @Override
    public void recordingCallBack(boolean isRecording) {
        if (isRecording) {
            imgRecording.setVisibility(View.VISIBLE);
            imgRecording.setImageResource(R.drawable.animation_recording);
            animationDrawable = (AnimationDrawable) imgRecording.getDrawable();
            animationDrawable.start();
        } else {
            if (animationDrawable != null)
                animationDrawable.stop();
            imgRecording.setVisibility(View.GONE);
        }
    }

    //LiveAdapter 播放视频回调 true成功 false失败
    @Override
    public void playCallBack(boolean isSuccess) {
        if (isSuccess)
            if (ezCameraInfo != null)
                txDefinition.setText(ezCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET ?
                        "流畅" : ezCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED ?
                        "标清" : ezCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_HD ?
                        "高清" : "超清");

        changePlayState(isSuccess ? 1 : 3);
    }

    @Override
    public void talkCallBack() {
    }

    @Override
    public void closeTalk() {
    }

    //控制txState状态 (0没有选择)  1正在播放 (2播放完成) 3播放失败 4暂停播放
    private void changePlayState(int state) {
        switch (state) {
            case 0:
                DrawableUtils.setDrawableNone(this, txState);
                txState.setText(R.string.activity_record_state);
                txState.setVisibility(View.VISIBLE);
                break;

            case 1:
                txState.setVisibility(View.GONE);
                break;

            case 2:
                DrawableUtils.setDrawableTop(this, txState, R.mipmap.icon_play_finish);
                txState.setText(R.string.activity_record_finish);
                txState.setVisibility(View.VISIBLE);
                clControl.setVisibility(View.GONE);
                break;

            case 3:
                DrawableUtils.setDrawableTop(this, txState, R.mipmap.icon_play_failed);
                txState.setText(R.string.activity_record_failed);
                txState.setVisibility(View.VISIBLE);
                clControl.setVisibility(View.GONE);
                Message msg = new Message();
                msg.what = EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL;
                break;

            case 4:
                DrawableUtils.setDrawableTop(this, txState, R.mipmap.icon_play_stop);
                txState.setText("");
                txState.setVisibility(View.VISIBLE);
        }
    }
}
