package com.weis.videoxie.moudle.adapter;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.videogo.constant.Constant;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.realplay.RealPlayMsg;
import com.videogo.util.LocalInfo;
import com.videogo.util.MediaScanner;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.widget.CustomRect;
import com.videogo.widget.CustomTouchListener;
import com.weis.videoxie.R;
import com.weis.videoxie.application.MyApplication;
import com.weis.videoxie.bean.LiveBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.moudle.activity.LiveActivity;
import com.weis.videoxie.moudle.custom.LoadingTextView;
import com.weis.videoxie.moudle.custom.PasswordDialog;
import com.weis.videoxie.utils.EZUtils;
import com.weis.videoxie.utils.FileNameUtil;
import com.weis.videoxie.utils.LogUtils;

import java.util.List;
import java.util.Random;

public class LiveAdaper extends BaseQuickAdapter<LiveBean, LiveAdaper.LiveViewHolder> implements Handler.Callback, PasswordDialog.PassWordDialogListener {
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private ImageView imageView;
    private LoadingTextView loadingTxView;
    private EZPlayer ezPlayer;
    private EZPlayer ezPlayerTalk;
    private Handler mHandler;
    private LiveAdapterListener listener;
    private EZCameraInfo ezCameraInfo = null;
    private EZDeviceInfo ezDeviceInfo = null;
    private CustomTouchListener mTouchListener = null;
    private LiveActivity activity;
    private float mPlayScale = 1;
    private boolean mIsRecording = false;//是否在录制
    private boolean mIsPlaying = false;//是否在播放
    private EZConstants.EZVideoLevel mCurrentQulityMode = EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET;
    private EZConstants.EZVideoLevel tempMode = null;
    private int currnetPosition = 0;
    private PasswordDialog passwordDialog = null;

    public LiveAdaper(int layoutResId, @Nullable List<LiveBean> data, LiveAdapterListener listener) {
        super(layoutResId, data);
        this.listener = listener;
    }

    public LiveAdaper(@Nullable List<LiveBean> data) {
        super(data);
    }

    public LiveAdaper(int layoutResId) {
        super(layoutResId);
    }

    public void setActivity(LiveActivity liveActivity) {
        this.activity = liveActivity;
    }

    public EZPlayer getAdpaterTalkPlay() {
        return ezPlayerTalk;
    }

    @Override
    protected void convert(final LiveViewHolder helper, LiveBean item) {
        item = mData.get(currnetPosition);
        surfaceView = helper.getView(R.id.surface_view);
        imageView = helper.getView(R.id.img_replace);
        loadingTxView = helper.getView(R.id.loading_tx_view);
        if (item.getDeviceBean().getStatus() == 0) {
            imageView.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            loadingTxView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(item.getDeviceBean().getImgUrl())
                    .into(imageView);
            return;
        } else {
            imageView.setVisibility(View.GONE);
            surfaceView.setVisibility(View.VISIBLE);
            loadingTxView.setVisibility(View.VISIBLE);
        }
        if (item.getDeviceBean() != null) {
            if (item.getDeviceBean().getAccessTokenBean() == null) {
                listener.playCallBack(false);
                return;
            }
            mHandler = new Handler(this);
            MyApplication.getEzSDKInstance().setAccessToken(item.getDeviceBean().getAccessTokenBean().getAccessToken());//注册token
            ezPlayer = MyApplication.getEzSDKInstance().createPlayer(item.getDeviceBean().getDeviceSerial(), AppConfig.CameraNo);
            ezPlayerTalk = MyApplication.getEzSDKInstance().createPlayer(item.getDeviceBean().getDeviceSerial(), AppConfig.CameraNo);
            playControl(true);
            iniTouchListener();//用于放大
        }
    }

    private void iniTouchListener() {
        mTouchListener = new CustomTouchListener() {
            @Override
            public boolean canZoom(float v) {
                return false;
            }

            @Override
            public boolean canDrag(int i) {
                return false;
            }

            @Override
            public void onSingleClick() {
                listener.controlOperateLayout(true);
            }

            @Override
            public void onDoubleClick(MotionEvent motionEvent) {
            }

            @Override
            public void onZoom(float v) {
            }

            @Override
            public void onZoomChange(float scale, CustomRect customRect, CustomRect customRect1) {
                if (mIsPlaying) {
                    if (scale > 1.0f && scale < 1.1f) {
                        scale = 1.1f;
                    }
                    setPlayScaleUI(scale, customRect, customRect1);
                }
            }

            @Override
            public void onDrag(int i, float v, float v1) {
            }

            @Override
            public void onEnd(int i) {
            }
        };
        surfaceView.setOnTouchListener(mTouchListener);
        mLocalInfo = LocalInfo.getInstance();
        // 获取屏幕参数
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        mLocalInfo.setScreenWidthHeight(metric.widthPixels, metric.heightPixels);
        mLocalInfo.setNavigationBarHeight((int) Math.ceil(25 * activity.getResources().getDisplayMetrics().density));
        final int screenWidth = mLocalInfo.getScreenWidth();
        final int screenHeight = (mOrientation == Configuration.ORIENTATION_PORTRAIT) ? (mLocalInfo.getScreenHeight() - mLocalInfo
                .getNavigationBarHeight()) : mLocalInfo.getScreenHeight();
        final RelativeLayout.LayoutParams realPlaySvlp = Utils.getPlayViewLp(mRealRatio, mOrientation,
                mLocalInfo.getScreenWidth(), (int) (mLocalInfo.getScreenWidth() * Constant.LIVE_VIEW_RATIO),
                screenWidth, screenHeight);
        RelativeLayout.LayoutParams svLp = new RelativeLayout.LayoutParams(realPlaySvlp.width, realPlaySvlp.height);
        surfaceView.setLayoutParams(svLp);
        mTouchListener.setSacaleRect(Constant.MAX_SCALE, 0, 0, realPlaySvlp.width, realPlaySvlp.height);
    }

    private float mRealRatio = Constant.LIVE_VIEW_RATIO;
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    private LocalInfo mLocalInfo = null;

    private void setPlayScaleUI(float scale, CustomRect customRect, CustomRect customRect1) {
        Message message = new Message();
        if (scale == 1) {
            if (mPlayScale == scale) {
                return;
            }
            try {
                if (ezPlayer != null) {
                    ezPlayer.setDisplayRegion(false, null, null);
                    message.what = AppConfig.SET_CAMERA_ZOOM_SUCCESS;
                }
            } catch (BaseException e) {
                zoomFail(message, e);
            }
        } else {
            if (mPlayScale == scale) {
                try {
                    if (ezPlayer != null) {
                        ezPlayer.setDisplayRegion(true, customRect, customRect1);
                        message.what = AppConfig.SET_CAMERA_ZOOM_SUCCESS;
                    }
                } catch (BaseException e) {
                    zoomFail(message, e);
                }
                return;
            }
            try {
                if (ezPlayer != null) {
                    ezPlayer.setDisplayRegion(true, customRect, customRect1);
                    message.what = AppConfig.SET_CAMERA_ZOOM_SUCCESS;
                }
            } catch (BaseException e) {
                zoomFail(message, e);
            }
        }
        mPlayScale = scale;
    }

    private void zoomFail(Message message, BaseException e) {
        message.what = AppConfig.SET_CAMERA_ZOOM_FAIL;
        Bundle bundle = new Bundle();
        bundle.putString("message", e.getMessage());
        message.setData(bundle);
        e.printStackTrace();
    }

    //    public void screenZoom(boolean isZoom) {
//        Message message = new Message();
//        if (ezPlayer != null) {
//            try {
//                ezPlayer.setDisplayRegion(isZoom, null, null);
//                message.what = AppConfig.SET_CAMERA_ZOOM_SUCCESS;
//            } catch (Exception e) {
//                message.what = AppConfig.SET_CAMERA_ZOOM_FAIL;
//                Bundle bundle = new Bundle();
//                bundle.putString("message", e.getMessage());
//                message.setData(bundle);
//                e.printStackTrace();
//            }
//        }
//    }

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
                ezPlayer.stopRealPlay();
                ezPlayer.closeSound();
                ezPlayer.release();
            }
        }
    };

    /**
     * 更新加载进度动画
     *
     * @param ezPlayer
     * @param mHandler
     * @param loadingTxView
     * @param progress
     */
    private void updateLoadingProgress(EZPlayer ezPlayer, Handler mHandler, final LoadingTextView loadingTxView, final int progress) {
        if (progress == 100) {
            mIsPlaying = true;
            if (ezPlayer != null)
                ezPlayer.openSound();
            if (ezCameraInfo != null)
                ezCameraInfo.setVideoLevel(mCurrentQulityMode.getVideoLevel());
            loadingTxView.setVisibility(View.GONE);
            listener.playCallBack(true);
            listener.controlOperateLayout(false);
            return;
        }
        mIsPlaying = false;
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
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case EZConstants.EZRealPlayConstants.MSG_GET_CAMERA_INFO_SUCCESS:
                updateLoadingProgress(ezPlayer, mHandler, loadingTxView, 20);
                break;

            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_START:
                updateLoadingProgress(ezPlayer, mHandler, loadingTxView, 40);
                break;

            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_CONNECTION_START:
                updateLoadingProgress(ezPlayer, mHandler, loadingTxView, 60);
                break;

            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_CONNECTION_SUCCESS:
                updateLoadingProgress(ezPlayer, mHandler, loadingTxView, 80);
                break;

            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                updateLoadingProgress(ezPlayer, mHandler, loadingTxView, 100);
                break;

            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                ErrorInfo errorInfo = (ErrorInfo) message.obj;
                if (errorInfo == null || errorInfo.errorCode == 110018) {
                    listener.playCallBack(false);
                    mIsPlaying = false;
                    loadingTxView.setVisibility(View.GONE);
                    listener.showMessage("播放失败：" + message.toString());
                } else if (errorInfo.errorCode == 400036 || errorInfo.errorCode == 400035) {
                    if (passwordDialog == null)
                        passwordDialog = new PasswordDialog(mContext, this);
                    passwordDialog.show();
                }
                break;

            case AppConfig.MSG_SET_VEDIOMODE_SUCCESS: //调节清晰度成功
                setDefinitionResult(true);
                break;

            case AppConfig.MSG_SET_VEDIOMODE_FAIL://调节清晰度失败
                setDefinitionResult(false);
                break;

            case EZConstants.EZRealPlayConstants.MSG_PTZ_SET_FAIL://调整摄像头失败
                listener.showMessage(message.getData().getString("message"));
                break;

            case EZConstants.EZRealPlayConstants.MSG_PTZ_SET_SUCCESS://调整摄像头成功
                listener.showMessage("调整成功");
                break;

            case AppConfig.SET_CAMERA_ZOOM_SUCCESS://调整画面缩放成功
                listener.showMessage("画面缩放成功");
                break;

            case AppConfig.SET_CAMERA_ZOOM_FAIL://调整画面缩放失败
                listener.showMessage("画面缩放失败：" + message.getData().getString("message"));
                break;

            case RealPlayMsg.MSG_REALPLAY_VOICETALK_SUCCESS:
                LogUtils.w("32ss", "对讲开启成功");
                break;

            case RealPlayMsg.MSG_REALPLAY_VOICETALK_FAIL:
                LogUtils.w("32ss", "对讲开失败");
                break;
        }
        return false;
    }

    public void setPlayFailed() {
        loadingTxView.setVisibility(View.GONE);
    }

    //设置清晰度
    private void setDefinitionResult(boolean isSuccess) {
        if (isSuccess) {
            if (ezPlayer != null)
                ezPlayer.stopRealPlay();
            listener.definitionCallBack(isSuccess, mCurrentQulityMode);
            playControl(true);
        } else {
            mCurrentQulityMode = tempMode;
        }
    }

    //控制音量
    public void voiceControl(boolean checked) {
        if (ezPlayer != null)
            if (checked)
                ezPlayer.openSound(); //开启声音
            else
                ezPlayer.closeSound();//关闭
    }

    //播放控制
    public void playControl(boolean isPlay) {
        if (ezPlayer == null || ezPlayerTalk == null)
            return;
        if (isPlay) {
            ezPlayerTalk.setHandler(mHandler);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(surfaceCallBack);
            ezPlayer.setHandler(mHandler);//设置Handler, 该handler将被用于从播放器向handler传递消息
            ezPlayer.setSurfaceHold(surfaceHolder);//设置播放器的显示Surface
            ezPlayer.startRealPlay(); //开启直播
            updateLoadingProgress(ezPlayer, mHandler, loadingTxView, 0);
        } else {
            ezPlayer.stopRealPlay();
        }
    }

    public void setDeviceInfo(EZDeviceInfo ezDeviceInfo, EZCameraInfo ezCameraInfo) {
        this.ezDeviceInfo = ezDeviceInfo;
        this.ezCameraInfo = ezCameraInfo;
    }

    //调整清晰度
    public void changeDefinition() {
        tempMode = mCurrentQulityMode;
        mCurrentQulityMode = mCurrentQulityMode.getVideoLevel() == 3 ? EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET :
                mCurrentQulityMode.getVideoLevel() == 2 ? EZConstants.EZVideoLevel.VIDEO_LEVEL_SUPERCLEAR :
                        mCurrentQulityMode.getVideoLevel() == 1 ? EZConstants.EZVideoLevel.VIDEO_LEVEL_HD : EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED;
        if (ezPlayer != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String deviceSerial = ezCameraInfo.getDeviceSerial();
                        int cameraNo = ezCameraInfo.getCameraNo();
                        EZOpenSDK.getInstance().setVideoLevel(deviceSerial, cameraNo, mCurrentQulityMode.getVideoLevel());
                        Message msg = Message.obtain();
                        msg.what = AppConfig.MSG_SET_VEDIOMODE_SUCCESS;
                        mHandler.sendMessage(msg);
                    } catch (BaseException e) {
                        e.printStackTrace();
                        Message msg = Message.obtain();
                        msg.what = AppConfig.MSG_SET_VEDIOMODE_FAIL;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }

    //抓取屏幕
    public void screenCapture() {
        if (!mIsPlaying) {
            listener.showMessage("视频还未播放！");
            return;
        }
        if (!SDCardUtil.isSDCardUseable()) {
            listener.showMessage("SD卡不可用");
            return;
        }
        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            listener.showMessage("内存不足");
            return;
        }
        if (ezPlayer != null) {
            Thread thr = new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = ezPlayer.capturePicture();
                    if (bmp != null) {
                        try {
                            String path = FileNameUtil.getCaptureName();
                            if (TextUtils.isEmpty(path)) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                            EZUtils.saveCapturePictrue(path, bmp);
                            MediaScanner mMediaScanner = new MediaScanner(mContext);
                            mMediaScanner.scanFile(path, "jpg");
                            listener.showMessage("已保存至相册" + path);
                        } catch (InnerException e) {
                            e.printStackTrace();
                        } finally {
                            if (bmp != null) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                        }
                    }
                    super.run();
                }
            };
            thr.start();
        }
    }

    //录制屏幕
    public void recordingControl() {
        if (!mIsPlaying) {
            listener.showMessage("视频还未播放！");
            return;
        }
        if (mIsRecording) {
            listener.recordingCallBack(false);
            if (ezPlayer == null || !mIsRecording) {
                return;
            }
            ezPlayer.stopLocalRecord();
            mIsRecording = false;
            listener.showMessage("录像存储成功");
        } else {
            if (!SDCardUtil.isSDCardUseable()) {
                // 提示SD卡不可用
                listener.showMessage("SD卡不可用");
                return;
            }
            if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
                // 提示内存不足
                listener.showMessage("内存不足");
                return;
            }
            if (ezPlayer != null) {
                mIsRecording = true;
                //时间作为文件命名
                if (ezPlayer.startLocalRecordWithFile(FileNameUtil.getRecordName())) {
                    listener.recordingCallBack(true);
                    listener.showMessage("开始录像");
                } else {
                    listener.showMessage("开启录像失败");
                }
            }
        }
    }

    //对话控制
    public void talkControl(boolean isTalking) {
        if (isTalking) {
            if (ezPlayer != null)
                ezPlayer.closeSound();
            if (ezPlayerTalk == null) {
                listener.showMessage("开启对讲失败");
                return;
            }
            ezPlayerTalk.closeSound();
            ezPlayerTalk.startVoiceTalk();
            listener.talkCallBack();
        } else {
            if (ezPlayer != null)
                ezPlayer.openSound();
            if (ezPlayerTalk != null)
                ezPlayerTalk.stopVoiceTalk();
        }
    }

    /**
     * 调整摄像头
     *
     * @param ezptzCommd
     * @param ezptzAction
     */
    public void ptzControl(final EZConstants.EZPTZCommand ezptzCommd,
                           final EZConstants.EZPTZAction ezptzAction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ptz_result = false;
                Message message = new Message();
                try {
                    ptz_result = MyApplication.getEzSDKInstance().controlPTZ(ezCameraInfo.getDeviceSerial(), ezCameraInfo.getCameraNo(), ezptzCommd,
                            ezptzAction, EZConstants.PTZ_SPEED_DEFAULT);
                    message.what = EZConstants.EZRealPlayConstants.MSG_PTZ_SET_SUCCESS;
                } catch (BaseException e) {
                    message.what = EZConstants.EZRealPlayConstants.MSG_PTZ_SET_FAIL;
                    Bundle bundle = new Bundle();
                    bundle.putString("message", e.getMessage());
                    message.setData(bundle);
                    e.printStackTrace();
                }
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void setCurrnetPosition(int currnetPosition) {
        this.currnetPosition = currnetPosition;
        notifyDataSetChanged();
    }

    @Override
    public void passwordDialogCancel() {
        Message msg = new Message();
        msg.what = EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL;
        handleMessage(msg);
    }

    @Override
    public void passwordDialogSure(String trim) {
        ezPlayer.setPlayVerifyCode(trim);
        ezPlayer.startRealPlay();
    }

    public class LiveViewHolder extends BaseViewHolder {
        public LiveViewHolder(View view) {
            super(view);
        }
    }

    public interface LiveAdapterListener {
        void controlOperateLayout(boolean isVisiable);

        void showMessage(String info);

        void definitionCallBack(boolean isSuccess, EZConstants.EZVideoLevel mCurrentQulityMode);

        void recordingCallBack(boolean isRecording);

        void playCallBack(boolean b);

        void talkCallBack();
    }
}
