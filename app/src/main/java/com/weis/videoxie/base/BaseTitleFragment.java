package com.weis.videoxie.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weis.videoxie.R;
import com.weis.videoxie.utils.DrawableUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseTitleFragment<V, T extends BasePresenter<V>> extends Fragment {
    protected BaseActivity mActivity;
    public T presenter;
    private Unbinder unbinder;
    TextView txTitle;
    TextView txBack;
    TextView txNext;
    RelativeLayout rlTitle;
    FrameLayout flContent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        initChild(view, inflater);
        ButterKnife.bind(this, view);
        presenter = initPresenter();
        if (presenter != null) {
            presenter.attach((V) this);
        }
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    private void initChild(View view, LayoutInflater inflater) {
        txTitle = view.findViewById(R.id.tx_title);
        txBack = view.findViewById(R.id.tx_back);
        txNext = view.findViewById(R.id.tx_next);
        rlTitle = view.findViewById(R.id.rl_title);
        flContent = view.findViewById(R.id.fl_content);
        txBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack(view);
            }
        });
        txNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNext(view);
            }
        });
        flContent.removeAllViews();
        View child = inflater.inflate(getContentView(), null);
        flContent.addView(child);
        unbinder = ButterKnife.bind(this, view);
    }

    protected abstract int getContentView();

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract T initPresenter();

    protected void setTxTitle(String title) {
        txTitle.setText(title);
        txTitle.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected void setTxBack(Context context, int resId) {
        DrawableUtils.setDrawableLeft(context, txBack, resId);
        txBack.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected void setTxBack(Context context, int resId, String tx) {
        DrawableUtils.setDrawableLeft(context, txBack, resId);
        txBack.setText(tx);
        txBack.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected void setTxNext(Context context, int resId) {
        DrawableUtils.setDrawableRight(context, txNext, resId);
        txNext.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected void setTxNext(Context context, int resId, String tx) {
        DrawableUtils.setDrawableLeft(context, txNext, resId);
        txNext.setText(tx);
        txNext.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected void setRlColor(int resId) {
        rlTitle.setBackgroundResource(resId);
    }

    protected void onBack(View view) {
    }

    protected void onNext(View view) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != presenter)
            presenter.attach((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != presenter)
            presenter.dettach();
        unbinder.unbind();
    }


}
