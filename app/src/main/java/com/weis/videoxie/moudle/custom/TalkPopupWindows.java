package com.weis.videoxie.moudle.custom;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.videogo.openapi.EZPlayer;
import com.weis.videoxie.R;

public class TalkPopupWindows extends PopupWindow implements View.OnTouchListener {
    private Context mContext;
    private ImageView imgTalkAnim;
    private ImageButton ibTalk;
    private TextView txTips;
    private ImageView imgExit;
    private TalkPopupListner mListener;
    private EZPlayer mEzPlayer;
    private LayoutInflater inflater;
    private PopupWindow mPopupWindow;
    private AnimationSet animation = null;

    public TalkPopupWindows(Context context, TalkPopupListner listner, EZPlayer ezPlayer) {
        super(context);
        this.mContext = context;
        this.mListener = listner;
        this.mEzPlayer = ezPlayer;
        inflater = LayoutInflater.from(mContext);
        initView();
    }

    private void initView() {
        View rootView = inflater.inflate(R.layout.popupwindows_talk, null);
        mPopupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(false);
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
        ibTalk = rootView.findViewById(R.id.ib_talk);
        imgTalkAnim = rootView.findViewById(R.id.img_talk_anim);
        txTips = rootView.findViewById(R.id.tx_tips);
        imgExit = rootView.findViewById(R.id.img_exit);
        ibTalk.setOnTouchListener(this);
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgTalkAnim.setVisibility(View.GONE);
                mPopupWindow.dismiss();
                mListener.closeTalk();
            }
        });
    }

    public void showPopWindow(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            txTips.setText("松开听对方说话");
            imgTalkAnim.setVisibility(View.VISIBLE);
            animation = (AnimationSet) AnimationUtils.loadAnimation(mContext,
                    R.anim.animation_talking);
            imgTalkAnim.setAnimation(animation);
            imgTalkAnim.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    imgTalkAnim.startAnimation(animation);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mEzPlayer.setVoiceTalkStatus(true);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    imgTalkAnim.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            txTips.setText("正在收听，按住说话");
            mEzPlayer.setVoiceTalkStatus(false);
        }
        return false;
    }

    public interface TalkPopupListner {
        void closeTalk();
    }
}
