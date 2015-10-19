package com.gwac.dao;

import com.gwac.model.UserInfo;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserInfoDAOImpl extends BaseHibernateDaoImpl<UserInfo> implements UserInfoDAO {
  
  public void save(UserInfo user) {
    Session session = getCurrentSession();
    String sql = "select * from user_info where name='" + user.getName() + "' ";
    Query q = session.createSQLQuery(sql).addEntity(UserInfo.class);
    if (!q.list().isEmpty()) {
      UserInfo tuser = (UserInfo) q.list().get(0);
      user.setUiId(tuser.getUiId());
    } else {
      super.save(user);
    }
  }
}
