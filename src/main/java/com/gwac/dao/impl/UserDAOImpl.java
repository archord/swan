package com.gwac.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport; 

import com.gwac.dao.UserDAO;
import com.gwac.model.User;
 
public class UserDAOImpl extends HibernateDaoSupport implements UserDAO{
	
	//add the user
	public void addUser(User user){
		
		getHibernateTemplate().save(user);
		
	}
	
	//return all the users in list
	public List<User> listUser(){
		
		return getHibernateTemplate().find("from User");
		
	}
	
}