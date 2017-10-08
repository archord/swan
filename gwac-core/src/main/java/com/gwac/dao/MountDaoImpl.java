/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.Mount;
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
  public Mount getByGroupUnitId(String groupId, String unitId) {
    Session session = getCurrentSession();
    String sql = "select * from mount where group_id='" + groupId + "' and unit_id=" + unitId;
    Query q = session.createSQLQuery(sql).addEntity(Mount.class);

    if (!q.list().isEmpty()) {
      return (Mount) q.list().get(0);
    } else {
      return null;
    }
  }

}
