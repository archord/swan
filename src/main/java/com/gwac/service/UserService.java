package com.gwac.service;

import java.util.List;

import com.gwac.model.User;
 
public interface UserService{
	
	void addUser(User customer);
	
	List<User> listUser();
	
}