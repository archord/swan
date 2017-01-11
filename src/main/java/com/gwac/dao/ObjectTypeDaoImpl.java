/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class ObjectTypeDaoImpl extends BaseHibernateDaoImpl<ObjectType> implements ObjectTypeDao {

  private static final Log log = LogFactory.getLog(ObjectTypeDaoImpl.class);

  @Override
  public ObjectType getByName(String name) {

    Session session = getCurrentSession();
    String sql = "select * from object_type where obj_type_name=?";
    Query q = session.createSQLQuery(sql).addEntity(ObjectType.class);
    q.setString(0, name);

    if (!q.list().isEmpty()) {
      return (ObjectType) q.list().get(0);
    } else {
      return null;
    }
  }

}
