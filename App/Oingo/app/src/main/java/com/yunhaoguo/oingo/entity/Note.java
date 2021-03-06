package com.yunhaoguo.oingo.entity;
/*
 * 项目名:     Oingo
 * 包名:      com.yunhaoguo.oingo.entity
 * 文件名:     Note
 * 创建者:     yunhaoguo
 * 创建时间:    2018/12/6 10:27 PM
 * 描述:      Note
 */


import java.io.Serializable;

public class Note implements Serializable{
    private int nid;
    private int uid;
    private String uname;
    private String ncontent;
    private String imageUrl;
    private int allowComment;
    private String startTime;
    private String endTime;
    private String repeatType;
    private String nlocation;
    private int nradius;

    private String ntag;

    public String getNtag() {
        return ntag;
    }

    public void setNtag(String ntag) {
        this.ntag = ntag;
    }

    public int getNradius() {
        return nradius;
    }

    public void setNradius(int nradius) {
        this.nradius = nradius;
    }

    public int getAllowComment() {
        return allowComment;
    }

    public String getNlocation() {
        return nlocation;
    }

    public void setNlocation(String nlocation) {
        this.nlocation = nlocation;
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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getNcontent() {
        return ncontent;
    }

    public void setNcontent(String ncontent) {
        this.ncontent = ncontent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public void setAllowComment(int allowComment) {
        this.allowComment = allowComment;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }
}
