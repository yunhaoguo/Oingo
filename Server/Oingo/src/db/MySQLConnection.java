package db;


import bean.Comment;
import bean.Note;
import bean.User;
import utils.DateUtils;

import java.sql.*;
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
                user.setUid(rs.getInt(1));
                user.setUemail(rs.getString(2));
                user.setUname(rs.getString(3));
                user.setUstate(rs.getString(5));
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
            String sql = "SELECT * FROM Note NATURAL JOIN User";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Note note = new Note();
                note.setUid(rs.getInt(1));
                note.setUname(rs.getString(12));
                note.setNid(rs.getInt(2));
                note.setNcontent(rs.getString(3));
                note.setAllowComment(rs.getInt(7) == 1);
                note.setStartTime(DateUtils.date2String(rs.getDate(8)));
                note.setEndTime(DateUtils.date2String(rs.getDate(9)));
                note.setRepeatType(rs.getString(10));
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
                comment.setUid(rs.getInt("uid"));
                comment.setUname(rs.getString("uname"));
                comment.setCcontent(rs.getString("ccontent"));
                String date = rs.getDate("ctime").toString();
                String time = rs.getTime("ctime").toString();
                String datetime = date.split(" ")[0] + " " + time;
                String resultdatetime = DateUtils.date2String29(DateUtils.str2Date(datetime));
                comment.setCtime(resultdatetime);
                System.out.println(resultdatetime);
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
                //reject
                //do nothing
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
