package cs636.music.dao;

import static cs636.music.dao.DBConstants.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs636.music.domain.User;


public class UserDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;

	public UserDAO(DbDAO db) {
		connection = db.getConnection();
	}

	/**
	 * Increase user_id by 1 in the system table
	 * 
	 * @throws SQLException
	 */
	private void advanceUserID() throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			stmt.executeUpdate(" update " + SYS_TABLE + " set user_id = user_id + 1");
		} finally {
			stmt.close();
		}
	}

	/**
	 * Get the available user id
	 * 
	 * @return the user_id available
	 * @throws SQLException
	 */
	private int getNextUserID() throws SQLException {
		int nextUserID;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select user_id from " + SYS_TABLE);
			set.next();
			nextUserID = set.getInt("user_id");
		} finally {
			stmt.close();
		}
		advanceUserID(); // the id has been taken, so set +1 for next one
		return nextUserID;
	}

	/**
	 * insert a User to site_user table
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void insertUser(User user) throws SQLException {
		int user_id = getNextUserID();
		user.setId(user_id);
		try {
			String query = "insert into site_user (user_id,firstname,lastname,email_address,company_name,address1,address2,"
					+ "city,state,zip,country,creditcard_type,creditcard_number,creditcard_expirationdate) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, user.getId());
			preparedStatement.setString(2, user.getFirstname());
			preparedStatement.setString(3, user.getLastname());
			preparedStatement.setString(4, user.getEmailAddress());
			preparedStatement.setString(5, null);
			preparedStatement.setString(6, null);
			preparedStatement.setString(7, null);
			preparedStatement.setString(8, null);
			preparedStatement.setString(9, null);
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, null);
			preparedStatement.setString(12, null);
			preparedStatement.setString(13, null);
			preparedStatement.setString(14, null);

			preparedStatement.execute();

		} finally {
			preparedStatement.close();
		}
	}
	/**
	 * Search User using given user id. 
	 * @return User Object
	 * @param long user id
	 * @throws SQLException
	 */
	public User findUserByID(long userid) throws SQLException {
		User user = null;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery("select * from " + USER_TABLE + " where user_id = " + userid);
			if (set.next()){
				user = new User(set.getString("firstname"), set.getString("lastname"),
						set.getString("email_address"));
				user.setId(set.getLong("user_id"));
			}
		} finally {
			stmt.close();
		}
		return user;
	}
	/**
	 * Search User using given user email_address. 
	 * @return User Object
	 * @param String emailid
	 * @throws SQLException
	 */
	public User findUserByEmailId(String emailid) throws SQLException {
		User user = null;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery("select * from " + USER_TABLE + " where email_address = '" + emailid +"'");
			if (set.next()){
				user = new User(set.getString("firstname"), set.getString("lastname"),
						set.getString("email_address"));
				user.setId(set.getLong("user_id"));
			}
		} finally {
			stmt.close();
		}
		return user;
	}

}
