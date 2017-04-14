/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObjectIdentity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class ObjectIdentityDaoImpl extends BaseHibernateDaoImpl<ObjectIdentity> implements ObjectIdentityDao {

  private static final Log log = LogFactory.getLog(ObjectIdentityDaoImpl.class);

  @Override
  public String getObjTypeName(int objId) {
    Session session = getCurrentSession();
    String sql = "SELECT ot.obj_type_name "
            + "FROM object_identity oi "
            + "INNER JOIN object_type ot ON ot.obj_type_id=oi.obj_type_id "
            + "WHERE oi.obj_id=?";
    Query q = session.createSQLQuery(sql);
    q.setInteger(0, objId);

    String typeName = "";
    if (!q.list().isEmpty()) {
      typeName = (String) q.list().get(0);
    }
    return typeName;
  }

}
