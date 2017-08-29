/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model.OtType;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="otTypeDao")
public class OtTypeDaoImpl extends BaseHibernateDaoImpl<OtType> implements OtTypeDao {

  @Override
  public OtType getOt2Type(OtLevel2 ot2) {

    String sql = "select * from ot_type where ott_id=" + ot2.getOtType() + "";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(OtType.class);
    List<OtType> otts = q.list();
    OtType tobj = null;
    if (otts.size() > 0) {
      tobj = otts.get(0);
    }
    return tobj;
  }

  @Override
  public OtType getOtTypeByTypeName(String typeName) {

    String sql = "select * from ot_type where ott_name='" + typeName + "'";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(OtType.class);
    List<OtType> otts = q.list();
    OtType tobj = null;
    if (otts.size() > 0) {
      tobj = otts.get(0);
    }
    return tobj;
  }

  @Override
  public List<OtType> getOtTypes() {
    String sql = "select * from ot_type order by ott_id asc";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(OtType.class);
    return q.list();
  }

}
