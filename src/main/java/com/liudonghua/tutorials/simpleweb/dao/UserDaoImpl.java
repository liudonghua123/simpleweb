package com.liudonghua.tutorials.simpleweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.liudonghua.tutorials.simpleweb.model.User;
import com.liudonghua.tutorials.simpleweb.util.DatabaseUtil;

public class UserDaoImpl implements UserDao {
	private Connection connection;

	public UserDaoImpl() {
		connection = DatabaseUtil.getConnection();
	}

	/* (non-Javadoc)
	 * @see com.liudonghua.tutorials.simpleweb.dao.UserDao#addUser(com.liudonghua.tutorials.simpleweb.model.User)
	 */
	public int addUser(User user) {
		int id = -1;
		try {
			// http://stackoverflow.com/questions/4246646/mysql-java-get-id-of-the-last-inserted-value-jdbc
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into users(username,age,salary) values (?, ?, ? )", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setInt(2, user.getAge());
			preparedStatement.setDouble(3, user.getSalary());
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: The last packet successfully received from the server was 140,287,058 milliseconds ago.  The last packet sent successfully to the server was 140,287,058 milliseconds ago. is longer than the server configured value of 'wait_timeout'. You should consider either expiring and/or testing connection validity before use in your application, increasing the server configured values for client timeouts, or using the Connector/J connection property 'autoReconnect=true' to avoid this problem.
			// http://stackoverflow.com/questions/667289/why-does-autoreconnect-true-not-seem-to-work
			// try re-connect is possible
			tryReconnectConnectionIfNeeded();
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see com.liudonghua.tutorials.simpleweb.dao.UserDao#deleteUser(int)
	 */
	public void deleteUser(int id) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("delete from users where id=?");
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			tryReconnectConnectionIfNeeded();
		}
	}

	/* (non-Javadoc)
	 * @see com.liudonghua.tutorials.simpleweb.dao.UserDao#updateUser(com.liudonghua.tutorials.simpleweb.model.User)
	 */
	public void updateUser(User user) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("update users set username=?, age=?, salary=?"
							+ "where id=?");
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setInt(2, user.getAge());
			preparedStatement.setDouble(3, user.getSalary());
			preparedStatement.setInt(4, user.getId());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			tryReconnectConnectionIfNeeded();
		}
	}

	/* (non-Javadoc)
	 * @see com.liudonghua.tutorials.simpleweb.dao.UserDao#getAllUsers()
	 */
	public List<User> getAllUsers() {
		User user;
		List<User> users = new ArrayList<User>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from users");
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setAge(rs.getInt("age"));
				user.setSalary(rs.getDouble("salary"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			tryReconnectConnectionIfNeeded();
		}

		return users;
	}

	/* (non-Javadoc)
	 * @see com.liudonghua.tutorials.simpleweb.dao.UserDao#getUserById(int)
	 */
	public User getUserById(int id) {
		User user = new User();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from users where id=?");
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setAge(rs.getInt("age"));
				user.setSalary(rs.getDouble("salary"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			tryReconnectConnectionIfNeeded();
		}

		return user;
	}

	public Map<String, String> getUserStatisticsInfo() {
		Map<String, String> userStatistics = new HashMap<String, String>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select round(min(salary), 2) as min_salary, round(max(salary), 2) as max_salary , round(avg(salary), 2) as avg_salary from users;");

			if (rs.next()) {
				userStatistics.put("min_salary", rs.getString("min_salary"));
				userStatistics.put("max_salary", rs.getString("max_salary"));
				userStatistics.put("avg_salary", rs.getString("avg_salary"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			tryReconnectConnectionIfNeeded();
		}

		return userStatistics;
	}

	private void tryReconnectConnectionIfNeeded() {
		try {
			// given 5 seconds timeout
			if(!connection.isValid(5)) {
				connection = DatabaseUtil.reconnect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
