package com.bigkoo.billlite.utils;

import java.text.DecimalFormat;

/**
 * Created by sai on 2018/5/12.
 */

public class StringUtil {
    public static String doubleTwoDecimal(Double d) {
        return new DecimalFormat("0.00").format(d);
    }
}
