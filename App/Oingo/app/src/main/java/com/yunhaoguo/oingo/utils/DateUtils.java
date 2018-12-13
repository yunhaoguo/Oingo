package com.yunhaoguo.oingo.utils;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.utils
 * 文件名:     DateUtils
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/12 8:05 PM
 * 描述:      TODO
 */


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return date2String(date);

    }

    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

}
