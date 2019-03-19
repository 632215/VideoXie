package com.weis.videoxie.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MyTextWatcher implements TextWatcher {
    private MyTextWatcherListener listener;

    public MyTextWatcher(MyTextWatcherListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (listener != null)
            listener.onTextChanged();
    }

    public interface MyTextWatcherListener {
        void onTextChanged();
    }
}
