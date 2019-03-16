package com.bigkoo.billlite.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * 自定义的PieChart数字显示格式
 *
 * @author wuhao
 */
public class PieChartValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    private int mDecimalDigits;//小数位数

    /**
     * @param digits 显示几位小数
     * @param unit   数字单位
     */
    public PieChartValueFormatter(int digits, String unit) {
        setup(digits, unit);
    }

    public void setup(int digits, String unit) {

        this.mDecimalDigits = digits;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }
        b.append(unit);

        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }

    public int getmDecimalDigits() {
        return mDecimalDigits;
    }

}
