package com.yunhaoguo.oingo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Filter implements Serializable {
    // Primary Key; Auto_increment by DB
    private int fid;
    private int uid;
    private String fname;
    private String fstarttime;
    private String fendtime;
    private String flocation;
    private int fradius;
    private int from_friend;


    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFstarttime() {
        return fstarttime;
    }

    public void setFstarttime(String fstarttime) {
        this.fstarttime = fstarttime;
    }

    public String getFendtime() {
        return fendtime;
    }

    public void setFendtime(String fendtime) {
        this.fendtime = fendtime;
    }

    public String getFlocation() {
        return flocation;
    }

    public void setFlocation(String flocation) {
        this.flocation = flocation;
    }

    public int getFradius() {
        return fradius;
    }

    public void setFradius(int fradius) {
        this.fradius = fradius;
    }

    public int getFrom_friend() {
        return from_friend;
    }

    public void setFrom_friend(int from_friend) {
        this.from_friend = from_friend;
    }

    public List<String> getAttributes() {
        List<String> attrs = new ArrayList<>();
        attrs.add("Filter Name: " +fname);
        attrs.add("Filter Location: " + flocation);
        attrs.add("Filter Radius: " + String.valueOf(fradius));
        attrs.add("Filter starttime: " +fstarttime);
        attrs.add("Filter endtime: " + fendtime);
        String fromfriend = from_friend == 0 ? "No" : "Yes";
        attrs.add("From friend: " + fromfriend);

        return attrs;
    }
}
