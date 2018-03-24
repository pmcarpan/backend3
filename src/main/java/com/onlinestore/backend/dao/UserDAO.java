package com.onlinestore.backend.dao;

import java.util.List;

import com.onlinestore.backend.model.User;

public interface UserDAO {

	void saveOrUpdate(User u);
	
	List<User> getAllUsers();
	
	User getUser(String username);
	
	void delete(String username);
	
	boolean validate(String username, String password);
	
}
