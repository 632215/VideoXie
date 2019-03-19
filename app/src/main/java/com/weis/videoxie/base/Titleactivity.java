package com.weis.videoxie.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weis.videoxie.R;

public class Titleactivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
    }
}
