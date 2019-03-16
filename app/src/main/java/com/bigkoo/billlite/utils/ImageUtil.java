package com.bigkoo.billlite.utils;

import com.bigkoo.billlite.R;

import java.lang.reflect.Field;

/**
 * Created by sai on 2018/4/30.
 */

public class ImageUtil {
    public static int getResId(String variableName) {
        try {
            Field idField = R.mipmap.class.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
