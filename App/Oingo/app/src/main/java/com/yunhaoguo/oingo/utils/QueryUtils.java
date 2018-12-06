package com.yunhaoguo.oingo.utils;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.utils
 * 文件名:     QueryUtils
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/6 12:39 AM
 * 描述:      TODO
 */


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class QueryUtils {

    private static OkHttpClient client = new OkHttpClient();

    public static void verify(String name, String password, Callback callback) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("userName", name);
            obj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.LOGIN_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void register(String name, String pass, String email, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("userName", name);
            obj.put("password", pass);
            obj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.REGISTER_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }


    public static void getFriendList(int uid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.GET_FRIENDLIST_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

}
