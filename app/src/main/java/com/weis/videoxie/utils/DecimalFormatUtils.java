package com.weis.videoxie.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/1/22.
 */

public class DecimalFormatUtils {
    public static String format(double input, String outPutformat) {
        DecimalFormat decimalFormat = new DecimalFormat(outPutformat);
        return decimalFormat.format(input);
    }
}
