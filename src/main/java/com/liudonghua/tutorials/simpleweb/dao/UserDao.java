package com.liudonghua.tutorials.simpleweb.dao;

import java.util.List;
import java.util.Map;

import com.liudonghua.tutorials.simpleweb.model.User;

public interface UserDao {

	public abstract int addUser(User user);

	public abstract void deleteUser(int id);

	public abstract void updateUser(User user);

	public abstract List<User> getAllUsers();

	public abstract User getUserById(int id);

	public abstract Map<String, String> getUserStatisticsInfo();

}