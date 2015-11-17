package com.gwac.dao;

import com.gwac.model.UserInfo;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class UserInfoDAOImpl extends BaseHibernateDaoImpl<UserInfo> implements UserInfoDAO {

  @Override
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

  @Override
  public List<UserInfo> findUser(UserInfo user) {
    Session session = getCurrentSession();
    String sql = "select * from user_info where login_name='" + user.getLoginName().trim() + "' and password='" + user.getPassword().trim() + "'";
    Query q = session.createSQLQuery(sql).addEntity(UserInfo.class);
    return q.list();
  }
}
