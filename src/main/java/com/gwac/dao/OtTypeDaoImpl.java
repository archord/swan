/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtType;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtTypeDaoImpl extends BaseHibernateDaoImpl<OtType> implements OtTypeDao {

  @Override
  public OtType getOtTypeByTypeName(String typeName) {

    String sql = "select * from ot_type where ot_type_name='" + typeName + "'";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(OtType.class);
    List<OtType> otts = q.list();
    OtType tobj = null;
    if (otts.size() > 0) {
      tobj = otts.get(0);
    }
    return tobj;
  }

}
