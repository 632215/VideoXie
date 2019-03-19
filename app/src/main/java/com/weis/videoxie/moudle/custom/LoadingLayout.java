package com.weis.videoxie.moudle.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.weis.videoxie.R;

public class LoadingLayout extends FrameLayout {

    private int emptyView, errorView, loadingView;
    private OnClickListener onRefreshListener;
    private Animation animation;    //向右旋转的360度的动画

    private TextView txEmptyTips;
    private TextView txEmptyTry;

    private ImageView imgRefresh;

    public ImageView imgError;
    private ConstraintLayout loadingLayoutError;

    public OnClickListener getOnRefreshListener() {
        return onRefreshListener;
    }

    public void setOnRefreshListener(OnClickListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadingLayout, 0, 0);
        try {
            emptyView = a.getResourceId(R.styleable.LoadingLayout_emptyView, R.layout.loading_layout_empty); //空数据页面
            errorView = a.getResourceId(R.styleable.LoadingLayout_errorView, R.layout.loading_layout_error);//网络失败页面
            loadingView = a.getResourceId(R.styleable.LoadingLayout_loadingView, R.layout.loading_layout_loading);//加载页面
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(emptyView, this, true);
            inflater.inflate(errorView, this, true);
            inflater.inflate(loadingView, this, true);
            initViews();
        } finally {
            a.recycle();
        }
    }

    //初始化视图
    private void initViews() {
        animation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setFillAfter(true);

        txEmptyTry = findViewById(R.id.tx_empty_try);
        txEmptyTips = findViewById(R.id.tx_empty_tips);

        loadingLayoutError = findViewById(R.id.cl_loading_error);
        imgError = findViewById(R.id.img_error);

        imgRefresh = findViewById(R.id.img_refresh);
        imgRefresh.setAnimation(animation);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount() - 1; i++) {
            getChildAt(i).setVisibility(GONE);
        }

        txEmptyTry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRefreshListener != null) {
                    onRefreshListener.onClick(v);
                }
            }
        });

        loadingLayoutError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onRefreshListener != null) {
                    imgError.startAnimation(animation);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            onRefreshListener.onClick(v);
                        }
                    }, 600);
                }
            }
        });
    }

    //显示空数据页面
    public void showEmpty() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 0) {
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }

    //显示网络失败页面
    public void showError() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 1) {
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }

    //显示加载Loading页面
    public void showLoading() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 2) {
                child.setVisibility(VISIBLE);
                animation.start();
            } else {
                child.setVisibility(GONE);
            }
        }
    }

    //显示想要加载的内容
    public void showContent() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 3) {
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }

    //下面方法，自己添加的，可以不要
    public void setNodataText(String showText) {
        txEmptyTips.setText(showText);
    }

    public void setNodataBt(int visibility, final Activity activity, final Class<?> cls) {
        txEmptyTry.setVisibility(visibility);
        txEmptyTry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, cls));
            }
        });
    }

    public void setNodataBt(int visibility) {
        txEmptyTry.setVisibility(visibility);
    }
}