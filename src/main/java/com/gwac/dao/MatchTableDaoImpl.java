/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.MatchTable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class MatchTableDaoImpl extends BaseHibernateDaoImpl<MatchTable> implements MatchTableDao {

  @Override
  public MatchTable getMatchTableByTypeName(String typeName) {

    String sql = "select * from match_table where match_type_name='" + typeName + "'";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(MatchTable.class);
    List<MatchTable> otts = q.list();
    MatchTable tobj = null;
    if (otts.size() > 0) {
      tobj = otts.get(0);
    }
    return tobj;
  }

}
