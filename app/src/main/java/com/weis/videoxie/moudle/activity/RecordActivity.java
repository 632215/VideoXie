package com.weis.videoxie.moudle.activity;

import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.weis.videoxie.R;
import com.weis.videoxie.application.MyApplication;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.bean.DeviceListBean;
import com.weis.videoxie.bean.RecordCloudBean;
import com.weis.videoxie.bean.RecordSDBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.adapter.RecordCloudAdaper;
import com.weis.videoxie.moudle.adapter.RecordSDAdaper;
import com.weis.videoxie.moudle.custom.DatePopupWindows;
import com.weis.videoxie.moudle.custom.LoadingLayout;
import com.weis.videoxie.moudle.custom.LoadingTextView;
import com.weis.videoxie.presenter.RecordPresenter;
import com.weis.videoxie.utils.DrawableUtils;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;
import com.weis.videoxie.view.RecordView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class RecordActivity extends BaseActivity<RecordView, RecordPresenter> implements
        DatePopupWindows.DatePopupListner
        , RecordView
        , RecordCloudAdaper.RecordCloudListener
        , Handler.Callback, RecordSDAdaper.RecordSDListener, View.OnClickListener {
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.cl_record_root)
    ConstraintLayout clRecordRoot;
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.loading_tx_view)
    LoadingTextView loadingTxView;
    @BindView(R.id.cb_play)
    CheckBox cbPlay;
    @BindView(R.id.cb_voice)
    CheckBox cbVoice;
    @BindView(R.id.tx_speed)
    TextView txSpeed;
    @BindView(R.id.cb_size)
    CheckBox cbSize;
    @BindView(R.id.cl_control)
    ConstraintLayout clControl;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.tx_state)
    TextView txState;

    private DeviceListBean deviceBean = null;
    private EZDeviceInfo ezDeviceInfo = null;
    private EZCameraInfo ezCameraInfo = null;
    private Handler mHandler = null;
    private SurfaceHolder surfaceHolder = null;
    private EZPlayer ezPlayer = null;
    private DatePopupWindows datePopupWindows = null;
    private RecordCloudAdaper recordCloudAdaper = null;
    private EZCloudRecordFile mEzCloudRecordFile = null;
    private RecordSDAdaper recordSDAdaper = null;
    private EZDeviceRecordFile mEzSDRecordFile = null;

    //播放状态 0没有选择  1正在播放 2播放完成 3播放失败
    private int playState = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_record;
    }

    @Override
    protected RecordPresenter initPresenter() {
        return new RecordPresenter(this);
    }

    @Override
    protected void initView() {
        deviceBean = (DeviceListBean) getIntent().getSerializableExtra("deviceBean");
        setTxTitle(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        setRlColor(R.color.color_main);
        setTxNextText(AppConfig.RECORD_SOURCE_SD_TX);
        showBack();
        presenter.getDeviceInfo(deviceBean);
        loadingLayout.setOnRefreshListener(this);
    }

    private void initSurfaceView() {
        if (ezDeviceInfo.getCameraNum() > 0 && ezDeviceInfo.getCameraInfoList() != null && ezDeviceInfo.getCameraInfoList().size() > 0) {
            ezCameraInfo = ezDeviceInfo.getCameraInfoList().get(0);
        }
        if (ezDeviceInfo != null && ezCameraInfo != null)
            ezPlayer = MyApplication.getEzSDKInstance().createPlayer(ezDeviceInfo.getDeviceSerial(), ezCameraInfo.getCameraNo());
        else {
            showMessage(0, "EzCameraInfo信息错误！");
            return;
        }
        if (surfaceHolder == null)
            surfaceHolder = surfaceView.getHolder();
        surfaceHolder.removeCallback(surfaceCallBack);
        surfaceHolder.addCallback(surfaceCallBack);
        if (mHandler == null) {
            mHandler = new Handler(this);
        }
        ezPlayer.setHandler(mHandler);//设置Handler, 该handler将被用于从播放器向handler传递消息
        ezPlayer.setSurfaceHold(surfaceHolder);//设置播放器的显示Surface
        ezPlayer.openSound();
    }

    @OnClick({R.id.tx_state, R.id.cb_play, R.id.cb_voice, R.id.tx_speed, R.id.cb_size, R.id.surface_view})
    public void onViewClicked(View view) {
        if (ezPlayer == null)
            return;
        switch (view.getId()) {
            case R.id.tx_state:
                changePlayState(1);
                if (ezPlayer == null)
                    return;
                ezPlayer.resumePlayback();
                cbPlay.setChecked(true);
                break;

            case R.id.cb_play:
                if (cbPlay.isChecked()) {
                    changePlayState(1);
                    ezPlayer.resumePlayback(); //开启直播
                } else {
                    changePlayState(4);
                    ezPlayer.pausePlayback();//关闭
                }
                operateControlLayout(false);
                break;

            case R.id.cb_voice:
                if (cbVoice.isChecked())
                    ezPlayer.openSound(); //开启声音
                else
                    ezPlayer.closeSound();//关闭
                operateControlLayout(false);
                break;

            case R.id.tx_speed:
                if (StringUtils.equals("1.0x", txSpeed.getText())) {
                    txSpeed.setText("2.0x");
                }
                operateControlLayout(false);
                break;

            case R.id.cb_size:
                operateControlLayout(false);
                break;

            case R.id.surface_view:
                operateControlLayout(true);
                break;
        }
    }

    /**
     * 隐藏操作按钮 0-消失 1-出现
     *
     * @param isQuick
     */
    private void operateControlLayout(boolean isQuick) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clControl.setVisibility(clControl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        }, isQuick == true ? 100 : 2000);    //延时1.5s执行
    }

    @Override
    public void getRecordSDSuccess(List<RecordSDBean> list) {
        loadingLayout.showContent();
        if (recordSDAdaper == null)
            recordSDAdaper = new RecordSDAdaper(R.layout.item_record, R.layout.item_record_section, list);
        recordSDAdaper.setEnableLoadMore(true);
        recordSDAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        recordSDAdaper.setListener(this);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));
        rvRecord.setAdapter(recordSDAdaper);
    }

    @Override
    public void getRecordCloudSuccess(List<RecordCloudBean> list) {
        loadingLayout.showContent();
        if (recordCloudAdaper == null)
            recordCloudAdaper = new RecordCloudAdaper(R.layout.item_record, R.layout.item_record_section, list);
        recordCloudAdaper.setEnableLoadMore(true);
        recordCloudAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        recordCloudAdaper.setListener(this);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));
        rvRecord.setAdapter(recordCloudAdaper);
        changePlayState(0);
    }

    @Override
    public void getDeviceInfoSuccess(EZDeviceInfo ezDeviceInfo, EZCameraInfo ezCameraInfo) {
        if (ezDeviceInfo != null && ezCameraInfo != null)
            ezPlayer = EZOpenSDK.getInstance().createPlayer(ezDeviceInfo.getDeviceSerial(), ezCameraInfo.getCameraNo());
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceCallBack);
        mHandler = new Handler(this);
        ezPlayer.setHandler(mHandler);//设置Handler, 该handler将被用于从播放器向handler传递消息
        ezPlayer.setSurfaceHold(surfaceHolder);//设置播放器的显示Surface
        changePlayState(0);
    }

    @Override
    public void getDeviceInfoNext(EZDeviceInfo ezDeviceInfo) {
        this.ezDeviceInfo = ezDeviceInfo;
        changeDate(getTxTitle());
        initSurfaceView();
    }

    //控制txState状态 0没有选择  1正在播放 2播放完成 3播放失败 4暂停播放
    private void changePlayState(int state) {
        playState = state;
        switch (playState) {
            case 0:
                DrawableUtils.setDrawableNone(this, txState);
                txState.setText(R.string.activity_record_state);
                txState.setVisibility(View.VISIBLE);
                break;

            case 1:
                txState.setVisibility(View.GONE);
                loadingTxView.setVisibility(View.GONE);
                break;

            case 2:
                DrawableUtils.setDrawableTop(this, txState, R.mipmap.icon_play_finish);
                txState.setText(R.string.activity_record_finish);
                txState.setVisibility(View.VISIBLE);
                loadingTxView.setVisibility(View.GONE);
                clControl.setVisibility(View.GONE);
                break;

            case 3:
                DrawableUtils.setDrawableTop(this, txState, R.mipmap.icon_play_failed);
                txState.setText(R.string.activity_record_failed);
                txState.setVisibility(View.VISIBLE);
                loadingTxView.setVisibility(View.GONE);
                clControl.setVisibility(View.GONE);
                break;

            case 4:
                DrawableUtils.setDrawableTop(this, txState, R.mipmap.icon_play_stop);
                txState.setText("");
                txState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVideoCloudClick(EZCloudRecordFile ezCloudRecordFile) {
        this.mEzCloudRecordFile = ezCloudRecordFile;
        ezPlayer.release();
        startPlayCloudBack(mEzCloudRecordFile);
    }

    @Override
    public void onVideoSDClick(EZDeviceRecordFile ezDeviceRecordFile) {
        this.mEzSDRecordFile = ezDeviceRecordFile;
        ezPlayer.release();
        startPlaySDBack(mEzSDRecordFile);
    }

    /**
     * 开始回放
     *
     * @param mEzSDRecordFile
     */
    private void startPlaySDBack(EZDeviceRecordFile mEzSDRecordFile) {
        if (ezPlayer != null && mEzSDRecordFile != null) {
            try {
                initSurfaceView();
                ezPlayer.startPlayback(mEzSDRecordFile);
            } catch (Exception e) {
                showMessage(0, e.toString());
                e.printStackTrace();
            }
            updateLoadingProgress(0);
        }
    }

    /**
     * 开始回放
     *
     * @param ezCloudRecordFile
     */
    private void startPlayCloudBack(EZCloudRecordFile ezCloudRecordFile) {
        if (ezPlayer != null && ezCloudRecordFile != null) {
            try {
                initSurfaceView();
                ezPlayer.startPlayback(ezCloudRecordFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateLoadingProgress(0);
        }
    }

    @Override
    protected void onTitle(View view) {
        super.onTitle(view);
        if (datePopupWindows == null) {
            datePopupWindows = new DatePopupWindows(this, this);
        }
        if (datePopupWindows.isShowing()) {
            datePopupWindows.dismiss();
            return;
        }
        datePopupWindows.showPopWindow(clRecordRoot);
    }

    @Override
    public void changeDate(String date) {
        loadingLayout.showLoading();
        setTxTitle(date);
        presenter.getRecordData(ezDeviceInfo, getTxTitle(), StringUtils.equals(AppConfig.RECORD_SOURCE_SD_TX, getTxNextText()) ? AppConfig.RECORD_SOURCE_SD : AppConfig.RECORD_SOURCE_CLOUD);
    }

    SurfaceHolder.Callback surfaceCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ezPlayer != null) {
                ezPlayer.setSurfaceHold(surfaceHolder);
            }
            surfaceHolder = holder;
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (ezPlayer != null) {
                ezPlayer.setSurfaceHold(null);
            }
            surfaceHolder = null;
            ezPlayer.closeSound();
            ezPlayer.stopPlayback();
            ezPlayer.release();
        }
    };

    @Override
    protected void onNext(View view) {
        super.onNext(view);
        changePlayState(1);
        loadingTxView.setVisibility(View.GONE);
        ezPlayer.release();
        setTxNextText(StringUtils.equals(AppConfig.RECORD_SOURCE_SD_TX, getTxNextText()) ? AppConfig.RECORD_SOURCE_CLOUD_TX : AppConfig.RECORD_SOURCE_SD_TX);
        changeDate(getTxTitle());
    }

    /**
     * 更新加载进度动画
     *
     * @param progress
     */
    private void updateLoadingProgress(final int progress) {
        changePlayState(1);
        loadingTxView.setVisibility(View.VISIBLE);
        loadingTxView.setTag(Integer.valueOf(progress));
        loadingTxView.setText(progress + "%");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadingTxView != null) {
                    Integer tag = (Integer) loadingTxView.getTag();
                    if (tag != null && tag.intValue() == progress) {
                        Random r = new Random();
                        loadingTxView.setText((progress + r.nextInt(20)) + "%");
                    }
                }
            }
        }, 500);
    }

    @Override
    protected void onBack(View view) {
        super.onBack(view);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ezPlayer != null) {
            ezPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StringUtils.equals(AppConfig.RECORD_SOURCE_SD_TX, getTxNextText())) {
            startPlaySDBack(mEzSDRecordFile);
        } else if (StringUtils.equals(AppConfig.RECORD_SOURCE_CLOUD_TX, getTxNextText())) {
            startPlayCloudBack(mEzCloudRecordFile);
        } else {
            showMessage(0, "数据错误");
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case EZConstants.EZPlaybackConstants.MSG_REMOTEPLAYBACK_PLAY_START://播放成功
                updateLoadingProgress(30);
                break;

            case EZConstants.EZPlaybackConstants.MSG_REMOTEPLAYBACK_CONNECTION_START://播放成功
                updateLoadingProgress(60);
                break;

            case EZConstants.EZPlaybackConstants.MSG_REMOTEPLAYBACK_CONNECTION_SUCCESS://播放成功
                updateLoadingProgress(90);
                break;

            case EZConstants.EZPlaybackConstants.MSG_REMOTEPLAYBACK_PLAY_SUCCUSS://播放成功
                changePlayState(1);
                break;

            case EZConstants.EZPlaybackConstants.MSG_REMOTEPLAYBACK_PLAY_FAIL://播放失败
                showMessage(0, message.toString());
                ezPlayer.release();
                changePlayState(3);
                break;

            case EZConstants.EZPlaybackConstants.MSG_REMOTEPLAYBACK_PLAY_FINISH://播放结束
                changePlayState(2);
                ezPlayer.release();
                break;
        }
        return false;
    }

    @Override
    public void showMessage(final int errCode, final String errorMsg) {
        RecordActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (errCode) {
                    case 10:
                        if (!StringUtils.isEmpty(errorMsg))
                            ToastUtils.showLong(RecordActivity.this, errorMsg);
                        break;

                    case 1153445:
                    case AppConfig.EZ_DATA_EMPTY:
                        changePlayState(1);
                        loadingLayout.showEmpty();
                        break;

                    default:
                        loadingLayout.showEmpty();
                        if (!StringUtils.isEmpty(errorMsg))
                            ToastUtils.showLong(RecordActivity.this, errorMsg);
                        break;

                }
            }
        });
    }

    /**
     * LoadingLayout回调函数
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        changeDate(getTxTitle());
    }
}
