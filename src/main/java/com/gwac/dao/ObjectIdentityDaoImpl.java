/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObjectIdentity;
import java.math.BigInteger;
import java.util.Iterator;
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
  public ObjectIdentity getByType(String typeName, String objName) {

    Session session = getCurrentSession();
    String sql = "select * from fits_file where file_name='" + typeName + "'";
    Query q = session.createSQLQuery(sql).addEntity(ObjectIdentity.class);

    if (!q.list().isEmpty()) {
      return (ObjectIdentity) q.list().get(0);
    } else {
      return null;
    }
  }

}
