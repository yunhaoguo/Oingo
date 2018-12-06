package db;


import bean.User;

import java.util.List;

public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void close();



	/**
	 * Return whether the credential is correct.
	 * 
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public int verifyLogin(String userId, String password);

	/**
	 * Add a new user into database
	 * return whether adding is successful
	 * @param userName
	 * @param password
	 * @param email
	 * @return
	 */
	public boolean addUser(String userName, String password, String email);

	public List<User> getFriendList(int uid);

}
