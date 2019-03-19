package com.weis.videoxie.moudle.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.weis.videoxie.R;


/**
 * Created by root on 17-8-14.
 */

public class LoadingDialog extends AlertDialog {
    private LoadingTextView loadingTextView;
    private Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        loadingTextView = findViewById(R.id.loading_tx_view);
    }
}
