package com.gwac.service.impl;

import java.util.List;

import com.gwac.service.UserService;
import com.gwac.dao.UserDAO;
import com.gwac.model.User;
 
public class UserServiceImpl implements UserService{
	
	UserDAO userDAO;

	//DI via Spring
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	//call DAO to save user
	public void addUser(User user){
		
		userDAO.addUser(user);
		
	}
	
	//call DAO to return users
	public List<User> listUser(){
		
		return userDAO.listUser();
		
	}
	
}