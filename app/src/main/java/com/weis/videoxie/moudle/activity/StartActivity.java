package com.weis.videoxie.moudle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.weis.videoxie.R;
import com.weis.videoxie.base.BaseActivity;
import com.weis.videoxie.base.BasePresenter;
import com.weis.videoxie.bean.AcacheUserBean;
import com.weis.videoxie.config.AppConfig;
import com.weis.videoxie.utils.ACache;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class StartActivity extends BaseActivity {


    @BindView(R.id.img_start)
    ImageView imgStart;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        JPushInterface.setDebugMode(false);
//        JPushInterface.init(this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2500);
        alphaAnimation.setFillAfter(true);
        imgStart.setAnimation(alphaAnimation);
        alphaAnimation.startNow();
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AcacheUserBean userBean = (AcacheUserBean) ACache.get(StartActivity.this).getAsObject(AppConfig.AcacheUserBean);
                if (userBean == null)
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                else
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
