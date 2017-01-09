package com.gwac.dao;
 
import com.gwac.model.UserInfo;
import java.util.List;

public interface UserInfoDAO  extends BaseHibernateDao<UserInfo>{
	
	public UserInfo getUserByLoginName(String loginName);
	public List<UserInfo> findUser(UserInfo user);
}