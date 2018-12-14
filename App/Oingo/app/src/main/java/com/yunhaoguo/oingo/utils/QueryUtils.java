package com.yunhaoguo.oingo.utils;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.utils
 * 文件名:     QueryUtils
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/6 12:39 AM
 * 描述:      数据库查询工具类
 */


import com.google.gson.Gson;
import com.yunhaoguo.oingo.entity.Comment;
import com.yunhaoguo.oingo.entity.Filter;
import com.yunhaoguo.oingo.entity.Note;

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
        Request request = new Request.Builder().url(HttpUtils.GET_FRIENDLIST_URL + "?uid=" + uid).build();
        client.newCall(request).enqueue(callback);
    }

    public static void getNotesList(Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("is_filter", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.GET_NOTESLIST_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void getRequestsList(int uid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url(HttpUtils.GET_REQUESTSLIST_URL + "?uid=" + uid).build();
        client.newCall(request).enqueue(callback);
    }


    //update the requests list after user accept or reject the friend requests
    public static void updateRequestsList(int ruid, int accept, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", AccountUtils.getUid());
            obj.put("ruid", ruid);
            obj.put("accept", accept);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.GET_REQUESTSLIST_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void getNoteDetail(int nid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("nid", nid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url(HttpUtils.GET_COMMENTSLIST_URL + "?nid=" + nid).build();
        client.newCall(request).enqueue(callback);
    }


    public static void addComment(Comment comment, int nid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("op", 1);
            obj.put("uid", AccountUtils.getUid());
            obj.put("nid", nid);
            obj.put("ctime", comment.getCtime());
            obj.put("ccontent", comment.getCcontent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.GET_COMMENTSLIST_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteComment(Comment comment, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("op", 0);
            obj.put("uid", comment.getUid());
            obj.put("nid", comment.getNid());
            obj.put("ctime", comment.getCtime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.GET_COMMENTSLIST_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void getUserInfo(int uid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url(HttpUtils.GET_USERINFO_URL + "?uid=" + uid).build();
        client.newCall(request).enqueue(callback);
    }

    public static void addFriend(int fuid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", AccountUtils.getUid());
            obj.put("fuid", fuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.ADD_FRIEND_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    public static void deleteFriend(int fuid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", AccountUtils.getUid());
            obj.put("fuid", fuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.GET_FRIENDLIST_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void editInfo(String uname, String uemail, String ustate, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", AccountUtils.getUid());
            obj.put("uname", uname);
            obj.put("uemail", uemail);
            obj.put("ustate", ustate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
        Request request = new Request.Builder().url(HttpUtils.GET_USERINFO_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void addNote(Note note, Callback callback) {
        Gson gson = new Gson();
        String noteStr = gson.toJson(note, Note.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), noteStr);
        Request request = new Request.Builder().url(HttpUtils.ADD_NOTE_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

<<<<<<< HEAD
    public static void createFilter(Filter filter, Callback callback) {
        Gson gson = new Gson();
        String filterStr = gson.toJson(filter, Filter.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), filterStr);
        Request request = new Request.Builder().url(HttpUtils.CREATE_FILTER_URL).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }


    public static void getFilterList(Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", AccountUtils.getUid());
=======
    public static void deleteNote(int nid, Callback callback) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("nid", nid);
>>>>>>> ff5ca0dc0af9f2ce4f06ad1ddf2f966ad3abbb02
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
<<<<<<< HEAD
        Request request = new Request.Builder().url(HttpUtils.GET_FILTERLIST_URL).post(requestBody).build();
=======
        Request request = new Request.Builder().url(HttpUtils.DELETE_NOTE_URL).post(requestBody).build();
>>>>>>> ff5ca0dc0af9f2ce4f06ad1ddf2f966ad3abbb02
        client.newCall(request).enqueue(callback);
    }
}
