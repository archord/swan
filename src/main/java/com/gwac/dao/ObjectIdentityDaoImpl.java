/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObjectIdentity;
import com.gwac.model.ObjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value = "objIdDao")
public class ObjectIdentityDaoImpl extends BaseHibernateDaoImpl<ObjectIdentity> implements ObjectIdentityDao {

  private static final Log log = LogFactory.getLog(ObjectIdentityDaoImpl.class);

  @Override
  public ObjectIdentity getByName(ObjectType objType, String objName) {
    Session session = getCurrentSession();
    String sql = "select * from object_identity where obj_type_id=? and obj_name=?";
    Query q = session.createSQLQuery(sql).addEntity(ObjectIdentity.class);
    q.setShort(0, objType.getObjTypeId());
    q.setString(1, objName);

    if (!q.list().isEmpty()) {
      return (ObjectIdentity) q.list().get(0);
    } else {
      log.debug("add obj: obj_type_id=" + objType.getObjTypeId() + ", obj_name=" + objName);
//      String sql2 = "insert into object_identity(obj_type_id, obj_name)values(?,?)";
//      Query q2 = session.createSQLQuery(sql2);
//      q2.setShort(0, objType.getObjTypeId());
//      q2.setString(1, objName);
//      q2.executeUpdate();
      ObjectIdentity obj = new ObjectIdentity();
      obj.setObjName(objName);
      obj.setObjTypeId(objType.getObjTypeId());
      super.save(obj);
      return obj;
    }
  }
  
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
