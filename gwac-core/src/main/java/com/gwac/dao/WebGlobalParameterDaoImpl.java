/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.WebGlobalParameter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class WebGlobalParameterDaoImpl extends BaseHibernateDaoImpl<WebGlobalParameter> implements WebGlobalParameterDao {

  @Override
  public void updateParameter(String name, String value) {
    Session session = getCurrentSession();
    String sql = "update web_global_parameter set value='" + value + "' where name ='" + name + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String getValueByName(String name) {
    Session session = getCurrentSession();
    String sql = "SELECT value from web_global_parameter where name='" + name + "'";
    Query q = session.createSQLQuery(sql);
    return (String) q.list().get(0);
  }

  @Override
  public String getValueByName(String tag, String name) {
    Session session = getCurrentSession();
    String sql = "SELECT value from web_global_parameter where name='" + name + "' and tag='"+tag+"'";
    Query q = session.createSQLQuery(sql);
    return (String) q.list().get(0);
  }


  @Override
  public void everyDayInit() {
    Session session = getCurrentSession();
    String sql = "update web_global_parameter set value=true where name='AutoFollowUp';";
    session.createSQLQuery(sql).executeUpdate();
  }
}
