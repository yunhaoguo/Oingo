package bean;

/**
 * project: Oingo
 *
 * @author Yubai Tao on 12/14/2018
 */
public class Filter {
    private int fid;
    private int uid;
    private String fname;
    private String fstarttime;
    private String fendtime;
    private String flocation;
    private int fradius;
    private String fstate;
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

    public void setFstarttime(String fstartTime) {
        this.fstarttime = fstartTime;
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

    public String getFstate() {
        return fstate;
    }

    public void setFstate(String fstate) {
        this.fstate = fstate;
    }

    public int getFrom_friend() {
        return from_friend;
    }

    public void setFrom_friend(int from_friend) {
        this.from_friend = from_friend;
    }
}
