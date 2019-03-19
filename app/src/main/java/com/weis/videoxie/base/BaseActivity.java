package com.weis.videoxie.base;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.weis.videoxie.R;
import com.weis.videoxie.utils.NetWorkUtils;
import com.weis.videoxie.utils.StatusBarUtils;
import com.weis.videoxie.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * 基类
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends BaseTitleActivity {
    public T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.color_main, 0);
        ButterKnife.bind(this);
        if (!NetWorkUtils.isAvailable(this))
            ToastUtils.showShort(this, R.string.network_error);
        presenter = initPresenter();
        if (presenter != null)
            presenter.attach((V) this);
        initView();
    }

    protected abstract T initPresenter();

    protected abstract void initView();

    protected abstract int getContentViewId();

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null)
            presenter.attach((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.dettach();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //监听用户点击屏幕事件，进行拦截
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            //判断当前获取的焦点是否在Editext上面
            if (StatusBarUtils.isShouldHideInput(view, ev)) {
                hideSoftInput(view.getWindowToken());//关闭软键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 隐藏软件盘
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
