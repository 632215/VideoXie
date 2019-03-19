package com.weis.videoxie.moudle.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.weis.videoxie.R;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;


/**
 * Created by root on 17-8-14.
 */

public class SDCleanDialog extends AlertDialog {
    private Context context;
    private TextView txCancel;
    private TextView txSure;
    private EditText etPwd;
    private SDCleanDialogListener listener;

    public SDCleanDialog(Context context, SDCleanDialogListener listener) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sd_clean);
        this.setCanceledOnTouchOutside(true);
        txCancel = findViewById(R.id.tx_cancel);
        txSure = findViewById(R.id.tx_sure);
        etPwd = findViewById(R.id.et_pwd);
        txCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        txSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.sDCleanDialogSure();
                dismiss();
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dismiss();
            }
        });
    }

    public interface SDCleanDialogListener {
        void sDCleanDialogSure();
    }
}
