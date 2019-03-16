package com.bigkoo.billlite.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sai on 2018/5/5.
 */

public class DateUtil {
    public static long DAYINMILLIS = 1440000;
    /** 日期格式：yyyy-MM-dd **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
    /** 日期格式：yyyy-MM **/
    public static final String DF_YYYY_MM = "yyyy-MM";
    /** 日期格式：yyyy-MM **/
    public static final String DF_YYYY = "yyyy";

    public static final String DF_MMDD = "MM/dd";

    /**
     * 格式化
     * @param date
     * @param format
     * @return
     */
    public static String formatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取某个月的时间
     * @param date 参照时间
     * @param distance 距离，上个月为-1，下个月为1
     * @return
     */
    public static Date getMonth(Date date, int distance) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, distance);
        return calendar.getTime();
    }

    /**
     * 字符串日期转换为Date
     * @param strDate
     * @param format
     * @return
     */
    public static Date parseDate(String strDate, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
        }
        return returnDate;

    }
    /**
     * 由毫秒单位转换为天
     * @return
     */
    public static long formatMillisToDay(long time){
        return time/DAYINMILLIS;
    }
    /**
     * 由天单位转换为毫秒
     * @return
     */
    public static long formatDayToMillis(long time){
        return time*DAYINMILLIS;
    }
}
