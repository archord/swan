/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.Mount;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class MountDaoImpl extends BaseHibernateDaoImpl<Mount> implements MountDao {

  private static final Log log = LogFactory.getLog(MountDaoImpl.class);

  @Override
  public void updateStatus(String mounts, String status) {
    Session session = getCurrentSession();
    String sql = "update mount set status="+status+" where name in("+mounts+")";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String getMountsStatus() {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT name, status) r)))  "
            + "FROM( "
            + "SELECT name, status "
            + "FROM mount "
            + "where mount_id<51 "
            + "ORDER BY mount_id "
            + ")as obj";
    Query q = session.createSQLQuery(sql);
    return (String) q.list().get(0);
  }

  @Override
  public List<Mount> getAll() {
    Session session = getCurrentSession();
    String sql = "select * from mount order by mount_id";
    Query q = session.createSQLQuery(sql).addEntity(Mount.class);
    return q.list();
  }

  @Override
  public Mount getByGroupUnitId(String groupId, String unitId) {
    Session session = getCurrentSession();
    String sql = "select * from mount where group_id='" + groupId + "' and unit_id='" + unitId + "'";
    Query q = session.createSQLQuery(sql).addEntity(Mount.class);

    if (!q.list().isEmpty()) {
      return (Mount) q.list().get(0);
    } else {
      return null;
    }
  }

}
