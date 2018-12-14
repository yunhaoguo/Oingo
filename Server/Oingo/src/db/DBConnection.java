package db;


import bean.Comment;
import bean.Filter;
import bean.Note;
import bean.User;

import java.util.List;
import java.util.Map;

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
	public User verifyLogin(String userId, String password);

	/**
	 * Add a new user into database
	 * return whether adding is successful
	 * @param userName
	 * @param password
	 * @param email
	 * @return
	 */
	public boolean addUser(String userName, String password, String email);


	/**
	 * Get all friends of a specific user
	 * return the user list
	 * @param uid
	 * @return
	 */
	public List<User> getFriendList(int uid);


	public List<Note> getAllNoteList();

	public List<User> getRequestsList(int uid);

	public boolean updateRequestsList(int uid, int ruid, int accpet);

	public List<Comment> getCommentsList(int nid);

	public boolean addComment(Comment comment);

	public User getUserInfo(int uid);

	public boolean deleteComment(Comment comment);

	public boolean addFriend(int uid, int fuid);

	public int editInfo(int uid, String uname, String uemail, String ustate);

	public int deleteFriend(int uid, int fuid);

	public int addNote(Note note);


	int createFilter(Filter filter);

	List<Filter> getFilterList(int uid);

	public int deleteNote(int nid);

	List<Note> getFilteredNoteList(int fid);

	int deleteFilter(int fid);

}
