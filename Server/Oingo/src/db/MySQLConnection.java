package db;


import bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
	public int verifyLogin(String userName, String password) {
        if (conn == null) {
            return -1;
        }
        try {
            String sql = "SELECT uid FROM User WHERE binary uname = ? AND binary upassword = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
            System.out.println("影响了多少行" + res);
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
}
