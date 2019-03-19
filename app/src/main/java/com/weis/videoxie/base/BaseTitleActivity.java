package com.weis.videoxie.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weis.videoxie.R;
import com.weis.videoxie.utils.DrawableUtils;

public class BaseTitleActivity extends FragmentActivity {
    TextView txTitle;
    TextView txBack;
    TextView txNext;
    RelativeLayout rlTitle;
    FrameLayout flContent;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_title);
        txTitle = findViewById(R.id.tx_title);
        txBack = findViewById(R.id.tx_back);
        txNext = findViewById(R.id.tx_next);
        rlTitle = findViewById(R.id.rl_title);
        flContent = findViewById(R.id.fl_content);
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
        txTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitle(view);
            }
        });
        if (flContent != null)
            flContent.removeAllViews();
        View.inflate(this, layoutResID, flContent);
        onContentChanged();
    }

    protected void setTxTitle(String title) {
        txTitle.setText(title);
        txTitle.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected String getTxTitle() {
        return txTitle.getText().toString().trim();
    }

    protected void showBack() {
        txBack.setVisibility(View.VISIBLE);
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
        DrawableUtils.setDrawableLeft(context, txNext, resId);
        txNext.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected void setTxNext(Context context, int resId, String tx) {
        DrawableUtils.setDrawableRight(context, txNext, resId);
        txNext.setText(tx);
        txNext.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected void setTxNextText(String tx) {
        txNext.setText(tx);
        txNext.setVisibility(View.VISIBLE);
        rlTitle.setVisibility(View.VISIBLE);
    }

    protected String getTxNextText() {
        return txNext.getText().toString().trim();
    }

    protected void setRlColor(int resId) {
        rlTitle.setBackgroundResource(resId);
    }

    protected void onBack(View view) {
    }

    protected void onNext(View view) {
    }

    protected void onTitle(View view) {
    }
}
