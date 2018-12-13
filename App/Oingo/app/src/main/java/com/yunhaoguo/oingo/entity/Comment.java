package com.yunhaoguo.oingo.entity;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.entity
 * 文件名:     Comment
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/12 5:26 PM
 * 描述:      TODO
 */


public class Comment {

    private int uid;
    private int nid;
    private String uname;
    private String ctime;
    private String ccontent;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCcontent() {
        return ccontent;
    }

    public void setCcontent(String ccontent) {
        this.ccontent = ccontent;
    }
}
