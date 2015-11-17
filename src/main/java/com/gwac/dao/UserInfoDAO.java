package com.gwac.dao;
 
import com.gwac.model.UserInfo;
import java.util.List;

public interface UserInfoDAO  extends BaseHibernateDao<UserInfo>{
	
	public void save(UserInfo user);
	public List<UserInfo> findUser(UserInfo user);
}