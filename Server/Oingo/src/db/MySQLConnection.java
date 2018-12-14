package db;


import bean.Comment;
import bean.Filter;
import bean.Note;
import bean.User;
import utils.DateUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySQLConnection implements DBConnection{

	private Connection conn;
    
    public MySQLConnection() {
   	 try {
   		 Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
   		 conn = DriverManager.getConnection(MySQLDBUtil.URL);
   		 
   	 } catch (Exception e) {
   		 e.printStackTrace();
   	 }
    }

	@Override
	public void close() {
		 if (conn != null) {
	   		 try {
	   			 conn.close();
	   		 } catch (Exception e) {
	   			 e.printStackTrace();
	   		 }
	   	 }
	}


	@Override
	public User verifyLogin(String userName, String password) {
        if (conn == null) {
            return new User();
        }
        try {
            String sql = "SELECT * FROM User WHERE binary uname = ? AND binary upassword = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUname(rs.getString("uname"));
                user.setUstate(rs.getString("ustate"));
                user.setUemail(rs.getString("uemail"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User();
    }

    @Override
    public boolean addUser(String userName, String password, String email) {
        if (conn == null) {
            return false;
        }
        String ustate = null;
        try {
            String sql = "INSERT INTO User(uemail, uname, upassword, ustate) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, userName);
            stmt.setString(3, password);
            stmt.setString(4, ustate);
            int res = stmt.executeUpdate();
            if (res > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> getFriendList(int uid) {
        List<User> friendList = new ArrayList<>();
        if (conn == null) {
            return friendList;
        }
        try {
            String sql = "SELECT * FROM User, Friendship WHERE User.uid = Friendship.fuid and Friendship.uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUemail(rs.getString("uemail"));
                user.setUname(rs.getString("uname"));
                user.setUstate(rs.getString("ustate"));
                friendList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendList;
    }

    @Override
    public List<Note> getAllNoteList() {
        List<Note> noteList = new ArrayList<>();
        if (conn == null) {
            return noteList;
        }
        try {
            String sql = "SELECT uid, uname, nid, ncontent, allow_comment, nstarttime, nrepeat_type, nendtime, ST_AsText(nlocation) as loc FROM Note NATURAL JOIN User ORDER BY nstarttime desc";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Note note = new Note();
                note.setUid(rs.getInt("uid"));
                note.setUname(rs.getString("uname"));
                note.setNid(rs.getInt("nid"));
                note.setNcontent(rs.getString("ncontent"));
                note.setAllowComment(rs.getInt("allow_comment"));

                Date time1=new Date(rs.getTimestamp("nstarttime").getTime());
                SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ctime=formattime.format(time1);
                String resultdatetimeS = DateUtils.date2String5(DateUtils.str2Date(ctime));
                note.setStartTime(resultdatetimeS);

                Date time2=new Date(rs.getTimestamp("nendtime").getTime());
                SimpleDateFormat formattime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ctime2=formattime2.format(time2);
                String resultdatetimeE = DateUtils.date2String5(DateUtils.str2Date(ctime2));
                note.setEndTime(resultdatetimeE);

                String point = rs.getString("loc");
                int pointLen = point.length();
                note.setNlocation(point.substring(6,pointLen - 1));
                note.setRepeatType(rs.getString("nrepeat_type"));
                noteList.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteList;
    }

    @Override
    public List<User> getRequestsList(int uid) {
        List<User> requestsList = new ArrayList<>();
        if (conn == null) {
            return requestsList;
        }
        try {
            String sql = "SELECT * FROM User, Request WHERE User.uid = Request.from_uid and Request.to_uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUid(rs.getInt("uid"));
                user.setUemail(rs.getString("uemail"));
                user.setUname(rs.getString("uname"));
                user.setUstate(rs.getString("ustate"));
                requestsList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestsList;
    }

    @Override
    public boolean updateRequestsList(int uid, int ruid, int accept) {
        if (conn == null) {
            return true;
        }
        try {
            String sql = "DELETE FROM Request WHERE from_uid = ? and to_uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ruid);
            stmt.setInt(2, uid);
            int rows = stmt.executeUpdate();
            if (rows != 1) {
                return false;
            }
            if (accept == 1) {
                String insertSql = "INSERT INTO Friendship(uid, fuid) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, uid);
                insertStmt.setInt(2, ruid);
                int insertRow = insertStmt.executeUpdate();
                if (insertRow != 1) {
                    return false;
                }
                insertStmt.setInt(1, ruid);
                insertStmt.setInt(2, uid);
                if (insertStmt.executeUpdate() != 1) {
                    return false;
                }
            } else {
                //reject
                //do nothing
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<Comment> getCommentsList(int nid) {
        List<Comment> commentsList = new ArrayList<>();
        if (conn == null) {
            return commentsList;
        }
        try {
            String sql = "SELECT * FROM Comment NATURAL JOIN  User WHERE nid = ? ORDER BY ctime asc";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setNid(rs.getInt("nid"));
                comment.setUid(rs.getInt("uid"));
                comment.setUname(rs.getString("uname"));
                comment.setCcontent(rs.getString("ccontent"));
                Date time1=new Date(rs.getTimestamp("ctime").getTime());
                SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ctime=formattime.format(time1);

                String resultdatetime = DateUtils.date2String5(DateUtils.str2Date(ctime));
                System.out.println("结束" + resultdatetime);
                comment.setCtime(resultdatetime);
                commentsList.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentsList;
    }

    @Override
    public boolean addComment(Comment comment) {
        if (conn == null) {
            return false;
        }
        try {
            if (comment != null) {
                String sql = "SELECT allow_comment FROM Note WHERE nid = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, comment.getNid());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("allow_comment") == 1) {
                        String insertSql = "INSERT INTO Comment(uid, nid, ctime, ccontent) VALUES (?, ?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setInt(1, comment.getUid());
                        insertStmt.setInt(2, comment.getNid());
                        insertStmt.setString(3, comment.getCtime());
                        insertStmt.setString(4, comment.getCcontent());
                        int insertRow = insertStmt.executeUpdate();
                        if (insertRow != 1) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                //do nothing
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public User getUserInfo(int uid) {
        User user = new User();
        if (conn == null) {
            return user;
        }
        try {
            String sql = "SELECT * FROM User WHERE uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setUid(rs.getInt("uid"));
                user.setUemail(rs.getString("uemail"));
                user.setUname(rs.getString("uname"));
                user.setUstate(rs.getString("ustate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean deleteComment(Comment comment) {
        if (conn == null) {
            return false;
        }
        try {
            if (comment != null) {
                String deleteSql = "DELETE FROM Comment WHERE uid = ? and nid = ? and ctime = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, comment.getUid());
                deleteStmt.setInt(2, comment.getNid());
                System.out.println("come" + comment.getCtime());
                deleteStmt.setString(3, comment.getCtime());
                int deleteRow = deleteStmt.executeUpdate();
                if (deleteRow != 1) {
                    return false;
                }
            } else {
                //reject
                //do nothing
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean addFriend(int uid, int fuid) {
        if (conn == null) {
            return false;
        }
        try {
            String sql = "SELECT * FROM Request WHERE from_uid = ? and to_uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            stmt.setInt(2, fuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            } else {
                String sql2 = "SELECT * FROM Request WHERE from_uid = ? and to_uid = ?";
                PreparedStatement stmt2 = conn.prepareStatement(sql2);
                stmt2.setInt(1, fuid);
                stmt2.setInt(2, uid);
                ResultSet rs2 = stmt.executeQuery();
                if (rs.next()) {
                    return false;
                } else {
                    String insertSql = "INSERT INTO Request(from_uid, to_uid) VALUES (?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setInt(1, uid);
                    insertStmt.setInt(2, fuid);
                    int insertRow = insertStmt.executeUpdate();
                    if (insertRow != 1) {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public int editInfo(int uid, String uname, String uemail, String ustate) {
        if (conn == null) {
            return 0;
        }
        try {
            String sql = "UPDATE User SET uname = ?, uemail = ?, ustate = ? WHERE uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uname);
            stmt.setString(2, uemail);
            stmt.setString(3, ustate);
            stmt.setInt(4, uid);
            int editRow = stmt.executeUpdate();
            if (editRow != 1) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public int deleteFriend(int uid, int fuid) {
        if (conn == null) {
            return 0;
        }
        try {
            String deleteSql = "DELETE FROM Friendship WHERE uid = ? and fuid = ?";
            PreparedStatement deleteStmt1 = conn.prepareStatement(deleteSql);
            deleteStmt1.setInt(1, uid);
            deleteStmt1.setInt(2, fuid);
            int deleteRow = deleteStmt1.executeUpdate();
            if (deleteRow != 1) {
                return 0;
            }
            String deleteSql2 = "DELETE FROM Friendship WHERE fuid = ? and uid = ?";
            PreparedStatement deleteStmt2 = conn.prepareStatement(deleteSql2);
            deleteStmt2.setInt(1, uid);
            deleteStmt2.setInt(2, fuid);
            int deleteRow2 = deleteStmt2.executeUpdate();
            if (deleteRow2 != 1) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public int addNote(Note note) {
        if (conn == null) {
            return 0;
        }
        try {
            if (note != null) {
                String insertSql = "INSERT INTO Note(uid, ncontent, nlocation, nradius, nimage, allow_comment, nstarttime, nendtime, nrepeat_type)" +
                        " VALUES (?, ?, point(?, ?), ?, ?, ?, ? ,? ,?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, note.getUid());
                insertStmt.setString(2, note.getNcontent());
                insertStmt.setDouble(3, Double.parseDouble(note.getNlocation().split(",")[0]));
                insertStmt.setDouble(4, Double.parseDouble(note.getNlocation().split(",")[1]));
                insertStmt.setInt(5, note.getNradius());
                insertStmt.setString(6, null);
                insertStmt.setInt(7, note.getAllowComment());
                insertStmt.setString(8, note.getStartTime());
                insertStmt.setString(9, note.getEndTime());
                insertStmt.setString(10, note.getRepeatType());
                int insertRow = insertStmt.executeUpdate();
                if (insertRow != 1) {
                    return 0;
                }
            } else {
                return 0;
                //do nothing
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public int createFilter(Filter filter) {
        if (conn == null) {
            return 0;
        }
        try {
            if (filter != null) {
                String insertSql = "INSERT INTO filter(uid, fname, fstarttime, fendtime, flocation, fradius, fstate, from_friend)" +
                        " VALUES (?, ?, ?, ?, point(?, ?), ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, filter.getUid());
                insertStmt.setString(2, filter.getFname());
                insertStmt.setString(3, filter.getFstarttime());
                insertStmt.setString(4, filter.getFendtime());
                insertStmt.setDouble(5, Double.parseDouble(filter.getFlocation().split(",")[0]));
                insertStmt.setDouble(6, Double.parseDouble(filter.getFlocation().split(",")[1]));
                insertStmt.setInt(7, filter.getFradius());
                insertStmt.setString(8, null); // filter.getFstate();
                insertStmt.setInt(9, filter.getFrom_friend());

                int insertRow = insertStmt.executeUpdate();
                if (insertRow != 1) {
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public int deleteNote(int nid) {
        if (conn == null) {
            return 0;
        }
        try {
            String deleteSql = "DELETE FROM Note WHERE nid = ?";
            PreparedStatement deleteStmt1 = conn.prepareStatement(deleteSql);
            deleteStmt1.setInt(1, nid);
            int deleteRow = deleteStmt1.executeUpdate();
            if (deleteRow != 1) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public int deleteFilter(int fid) {
        if (conn == null) {
            return 0;
        }
        try {
            System.out.println("delete" + fid);
            String deleteSql = "DELETE FROM Filter WHERE fid = ?";
            PreparedStatement deleteStmt1 = conn.prepareStatement(deleteSql);
            deleteStmt1.setInt(1, fid);
            int deleteRow = deleteStmt1.executeUpdate();
            if (deleteRow != 1) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }


    @Override
    public List<Filter> getFilterList(int uid) {
        List<Filter> filterList = new ArrayList<>();
        if (conn == null) {
            return new ArrayList<>();
        }
        try {
            String sql = "SELECT * FROM filter WHERE uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Filter filter = new Filter();
                filter.setFid(rs.getInt("fid"));
                filter.setUid(rs.getInt("uid"));
                filter.setFname(rs.getString("fname"));

                Date starttime = new Date(rs.getTimestamp("fstarttime").getTime());
                Date endtime = new Date(rs.getTimestamp("fendtime").getTime());
                SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String starttime_s = formattime.format(starttime);
                String endtime_s = formattime.format(endtime);
                String fstarttime = DateUtils.date2String5(DateUtils.str2Date(starttime_s));
                String fendtime = DateUtils.date2String5(DateUtils.str2Date(endtime_s));

                
                filter.setFstarttime(fstarttime);
                filter.setFendtime(fendtime);
                filter.setFradius(rs.getInt("fradius"));
                filter.setFstate(rs.getString("fstate") == null ? "" : rs.getString("fstate")); // Can be null
                filter.setFrom_friend(rs.getInt("from_friend"));


                filterList.add(filter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filterList;
    }

    @Override
    public List<Note> getFilteredNoteList(int fid) {
        List<Note> noteList = new ArrayList<>();
        if (conn == null) {
            return noteList;
        }
        try {
            // Original get
            // String sql = "SELECT uid, uname, nid, ncontent, allow_comment, nstarttime, nrepeat_type, nendtime, ST_AsText(nlocation) as loc FROM Note NATURAL JOIN User ORDER BY nstarttime desc";
            String sql = "SELECT Note.uid as uid, User.uname as uname, Note.nid as nid, Note.ncontent as ncontent, Note.allow_comment as allow_comment, " +
                         "Note.nstarttime as nstarttime, Note.nrepeat_type as nrepeat_type, Note.nendtime as nendtime, ST_AsText(nlocation) as loc " +
                         "FROM Note, Filter, User WHERE Note.uid=Filter.uid AND Filter.fid = ? AND Note.uid=User.uid" +
                         "checkDoubleRadiusDistance(Filter.flocation, Note.nlocation, Filter.fradius, Note.nradius) AND" +
                         "checkTimeOverlapping(Filter.fstarttime, Filter.fendtime, Note.nstarttime, Note.nendtime, Note.nrepeat_type)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Note note = new Note();
                note.setUid(rs.getInt("uid"));
                note.setUname(rs.getString("uname"));
                note.setNid(rs.getInt("nid"));
                note.setNcontent(rs.getString("ncontent"));
                note.setAllowComment(rs.getInt("allow_comment"));

                Date time1=new Date(rs.getTimestamp("nstarttime").getTime());
                SimpleDateFormat formattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ctime=formattime.format(time1);
                String resultdatetimeS = DateUtils.date2String5(DateUtils.str2Date(ctime));
                note.setStartTime(resultdatetimeS);

                Date time2=new Date(rs.getTimestamp("nendtime").getTime());
                SimpleDateFormat formattime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ctime2=formattime2.format(time2);
                String resultdatetimeE = DateUtils.date2String5(DateUtils.str2Date(ctime2));
                note.setEndTime(resultdatetimeE);

                String point = rs.getString("loc");
                int pointLen = point.length();
                note.setNlocation(point.substring(6,pointLen - 1));
                note.setRepeatType(rs.getString("nrepeat_type"));
                noteList.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteList;
    }
}
