package com.weis.videoxie.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/19.
 */

public class EmTextWatch implements TextWatcher {
    private int editStart;
    private int editEnd;
    private int maxLen = 16; // 6 个汉字 12 个英文字符 (表情 2 个字符)
    private Context context;
    private EditText editText;
    private String beforeString = "";
    //    private static String tips = "请输入小于10字符的汉字/字母/数字";
    private EmTextWatchListener listener;
    private static String tips = "请输入汉字/字母/数字";


    public EmTextWatch(EditText e, Context context) {
        editText = e;
        this.context = context;
    }

    public EmTextWatch(EditText e, Context context, EmTextWatchListener listener) {
        editText = e;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeString = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editStart = editText.getSelectionStart();
        editEnd = editText.getSelectionEnd();
        // 先去掉监听器，否则会出现栈溢出
        boolean flag = false;
        editText.removeTextChangedListener(this);
        if (s != null && !s.toString().equals("")) {
            int index = editText.getSelectionStart() - 1;
            if (index > 0) {
                char cs = s.charAt(index);
                if (isEmojiCharacter(cs)) {
                    Editable edit = editText.getText();
                    s.clear();
                    s.append(beforeString);
                    flag = true;
                    Toast.makeText(context, tips, Toast.LENGTH_SHORT).show();
                }
            }
        }
        editText.setText(s);
        if (flag) {
            editText.setSelection(beforeString.length());
            if (listener != null) {
                listener.EmTextWatchChanged();
            }
        } else
            editText.setSelection(editStart);
        // 恢复监听器
        editText.addTextChangedListener(this);
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint             比较的单个字符
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint >= 0x2600 && codePoint <= 0x27BF) // 杂项符号与符号字体
                || codePoint == 0x303D
                || codePoint == 0x2049
                || codePoint == 0x203C
                || (codePoint >= 0x2000 && codePoint <= 0x200F)//
                || (codePoint >= 0x2028 && codePoint <= 0x202F)//
                || codePoint == 0x205F //
                || (codePoint >= 0x2065 && codePoint <= 0x206F)//
                /* 标点符号占用区域 */
                || (codePoint >= 0x2100 && codePoint <= 0x214F)// 字母符号
                || (codePoint >= 0x2300 && codePoint <= 0x23FF)// 各种技术符号
                || (codePoint >= 0x2B00 && codePoint <= 0x2BFF)// 箭头A
                || (codePoint >= 0x2900 && codePoint <= 0x297F)// 箭头B
                || (codePoint >= 0x3200 && codePoint <= 0x32FF)// 中文符号
                || (codePoint >= 0xD800 && codePoint <= 0xDFFF)// 高低位替代符保留区域
                || (codePoint >= 0xE000 && codePoint <= 0xF8FF)// 私有保留区域
                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)// 变异选择器
                || codePoint >= 0x10000; // Plane在第二平面以上的，char都不可以存，全部都转
    }

    public interface EmTextWatchListener {
        void EmTextWatchChanged();
    }
}
