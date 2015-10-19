package com.gwac.dao;
 
import com.gwac.model.UserInfo;

public interface UserInfoDAO  extends BaseHibernateDao<UserInfo>{
	
	public void save(UserInfo user);
}