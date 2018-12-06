package com.yunhaoguo.oingo.entity;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.entity
 * 文件名:     User
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/6 12:57 AM
 * 描述:      TODO
 */


public class User {
    private int uid;
    private String uname;
    private String uemail;
    private String ustate;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUstate() {
        return ustate;
    }

    public void setUstate(String ustate) {
        this.ustate = ustate;
    }
}
