package com.gwac.dao;

import com.gwac.model.UserInfo;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserInfoDAOImpl extends BaseHibernateDaoImpl<UserInfo> implements UserInfoDAO {

  @Override
  public UserInfo getUserByLoginName(String loginName) {

    Session session = getCurrentSession();
    String sql = "select * from user_info where login_name='" + loginName.trim() + "' ";
    Query q = session.createSQLQuery(sql).addEntity(UserInfo.class);
    List list = q.list();
    if (list.size() > 0) {
      return (UserInfo) list.get(0);
    } else {
      return null;
    }
  }

  @Override
  public List<UserInfo> findUser(UserInfo user) {
    Session session = getCurrentSession();
    String sql = "select * from user_info where login_name='" + user.getLoginName().trim() + "' and password='" + user.getPassword().trim() + "'";
    Query q = session.createSQLQuery(sql).addEntity(UserInfo.class);
    return q.list();
  }
}
