package com.gwac.dao;

import java.util.List;

import com.gwac.model.User;
 
public interface UserDAO{
	
	void addUser(User customer);
	
	List<User> listUser();
	
}