package com.yunhaoguo.oingo.utils;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.utils
 * 文件名:     AccountUtils
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/7 1:26 AM
 * 描述:      TODO
 */


public class AccountUtils {

    private static int uid = -1;
    private static String uname = "";

    public static String getUname() {
        return uname;
    }

    public static void setUname(String uname) {
        AccountUtils.uname = uname;
    }

    public static int getUid() {
        return uid;
    }

    public static void setUid(int uid) {
        AccountUtils.uid = uid;
    }
}
