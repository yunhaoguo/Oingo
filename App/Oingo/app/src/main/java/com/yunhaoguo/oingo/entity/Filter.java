package com.yunhaoguo.oingo.entity;

public class Filter {
    private String fname;
    private String fstarttime;
    private String fendtime;
    private Point flocation;
    private int fradius;
    private boolean from_friend;

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

    public Point getFlocation() {
        return flocation;
    }

    public void setFlocation(Point flocation) {
        this.flocation = flocation;
    }

    public int getFradius() {
        return fradius;
    }

    public void setFradius(int fradius) {
        this.fradius = fradius;
    }

    public boolean isFrom_friend() {
        return from_friend;
    }

    public void setFrom_friend(boolean from_friend) {
        this.from_friend = from_friend;
    }
}
