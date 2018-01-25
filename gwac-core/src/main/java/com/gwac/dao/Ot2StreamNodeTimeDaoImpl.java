/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.Ot2StreamNodeTime;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class Ot2StreamNodeTimeDaoImpl extends BaseHibernateDaoImpl<Ot2StreamNodeTime> implements Ot2StreamNodeTimeDao {

  @Override
  public void save(Ot2StreamNodeTime obj) {
    Session session = getCurrentSession();
    String sql = "insert into ot2_stream_node_time(ot_id,oor_id1,oor_id2)values(?,?,?)";
    Query q = session.createSQLQuery(sql);
    q.setLong(0, obj.getOtId());
    q.setLong(1, obj.getOorId1());
    q.setLong(2, obj.getOorId2());
    q.executeUpdate();
  }

  @Override
  public void updateLookBackTime(long otId) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookback_time=now() where lookback_time is NULL and ot_id=" + otId + "";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookBackTime(String otName) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookback_time=now() where lookback_time is NULL and ot_id=(select ot_id from ot_level2 where name='" + otName.trim() + "')";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookUpTime(long otId) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookup_time=now() where lookup_time is NULL and ot_id=" + otId + "";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookUpResultTime(long otId) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookup_result_time=now() where ot_id=" + otId + " and lookup_result_time is NULL";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookUpResultTime(String otName) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookup_result_time=now() where lookup_result_time is NULL and ot_id=(select ot_id from ot_level2 where name='" + otName.trim() + "')";
    session.createSQLQuery(sql).executeUpdate();
  }
}
