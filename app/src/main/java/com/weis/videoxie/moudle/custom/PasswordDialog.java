package com.weis.videoxie.moudle.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.weis.videoxie.R;
import com.weis.videoxie.utils.StringUtils;
import com.weis.videoxie.utils.ToastUtils;


/**
 * Created by root on 17-8-14.
 */

public class PasswordDialog extends AlertDialog {
    private Context context;
    private TextView txCancel;
    private TextView txSure;
    private EditText etPwd;
    private PassWordDialogListener listener;

    public PasswordDialog(Context context, PassWordDialogListener listener) {
        super(context, R.style.inputDialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_password);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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
                if (StringUtils.isEmpty(etPwd.getText().toString().trim())) {
                    ToastUtils.showShort(context, "请输入密码");
                    return;
                }
                listener.passwordDialogSure(etPwd.getText().toString().trim());
                dismiss();
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (StringUtils.isEmpty(etPwd.getText().toString().trim()))
                    listener.passwordDialogCancel();
            }
        });
    }

    public interface PassWordDialogListener {

        void passwordDialogCancel();

        void passwordDialogSure(String trim);
    }
}
