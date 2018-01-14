/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpObservation;
import com.gwac.model.Telescope;
import java.math.BigInteger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class FollowUpObservationDaoImpl extends BaseHibernateDaoImpl<FollowUpObservation> implements FollowUpObservationDao {
  
  @Override
  public void updateExecuteStatus(String followName, char executeStatus) {
    Session session = getCurrentSession();
    String sql = "update follow_up_observation set execute_status='" + executeStatus + "' where fo_name='" + followName + "' ";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String findRecord(int start, int length, char executeStatus) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT tmp1.*) r))) from("
            + "SELECT fuo.*, ui.name user_name FROM follow_up_observation fuo "
            + "inner join user_info ui on ui.ui_id=fuo.user_id "
            + "where 1=1 ";
    if (executeStatus != 'a') {
      sql += "and execute_status='" + executeStatus + "' ";
    }
    sql += "ORDER BY fuo.trigger_time desc OFFSET " + start + " LIMIT " + length + " )as tmp1";

    //log.debug(sql);
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public Long findRecordCount(char executeStatus) {

    String sql = "SELECT count(*) FROM follow_up_observation where 1=1 ";
    if (executeStatus != 'a') {
      sql += "and execute_status='" + executeStatus + "' ";
    }
    Query q = this.getCurrentSession().createSQLQuery(sql);
    return ((BigInteger) q.list().get(0)).longValue();
  }

  @Override
  public FollowUpObservation getByName(String name) {

    Session session = getCurrentSession();
    String sql = "select * from follow_up_observation where fo_name='" + name + "'";
    Query q = session.createSQLQuery(sql).addEntity(FollowUpObservation.class);
    if (!q.list().isEmpty()) {
      return (FollowUpObservation) q.list().get(0);
    } else {
      return null;
    }
  }
}
