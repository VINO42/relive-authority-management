package com.relive.utils;

import com.relive.exception.UtilsException;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author ReLive
 * @Date 2021/5/5-17:53
 */
public class DateUtils {

    public static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String dateFormat(Date date) {
        return new SimpleDateFormat(DEFAULT_DATEFORMAT).format(date);
    }

    public static String dateFormat(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date stringToDate(String date) {
        try {
            if (StringUtils.isBlank(date)) {
                return null;
            }
            return new SimpleDateFormat(DEFAULT_DATEFORMAT).parse(date);
        } catch (ParseException e) {
            throw new UtilsException("时间转换异常");
        }
    }

    public static Date addDay(Date date, int num) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        c.add(Calendar.DATE, num);
        return c.getTime();
    }

    public static Date addMinute(Date date, int num) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        c.add(Calendar.MINUTE, num);
        return c.getTime();
    }
}
