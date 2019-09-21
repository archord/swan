/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpObservation;
import java.math.BigInteger;
import java.util.List;
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
  public void updateSciObjId(long fupObsId, long sciObjId) {

    String sql = "update follow_up_observation set so_id=" + sciObjId
            + " where fo_id=" + fupObsId;
    getCurrentSession().createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateSciObjId(String objName, long sciObjId) {

    String sql = "update follow_up_observation set so_id=" + sciObjId
            + " where obj_name='" + objName + "'";
    getCurrentSession().createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public List<FollowUpObservation> getBySciObjId(long sciObjId) {

    String sql = "SELECT * "
            + "from follow_up_observation  "
            + "where so_id=" + sciObjId
            + " order by fo_id asc";

    Query q = getCurrentSession().createSQLQuery(sql).addEntity(FollowUpObservation.class);
    return q.list();
  }

  @Override
  public List<FollowUpObservation> getByFoId(long foId) {

    String sql = "SELECT fupObs1.* "
            + "from follow_up_observation fupObs1 "
            + "INNER JOIN follow_up_observation fupObs2 on fupObs1.obj_name=fupObs2.obj_name and fupObs2.fo_id=" + foId
            + " order by fupObs1.fo_id asc";

    Query q = getCurrentSession().createSQLQuery(sql).addEntity(FollowUpObservation.class);
    return q.list();
  }

  @Override
  public List<FollowUpObservation> getUnTriggeredByTime(int seconds) {

    String sql = "SELECT * "
            + "from follow_up_observation "
            + "where EXTRACT(EPOCH FROM begin_time-now())>0 and EXTRACT(EPOCH FROM begin_time-now())<" + seconds;

    Query q = getCurrentSession().createSQLQuery(sql).addEntity(FollowUpObservation.class);
    return q.list();
  }

  @Override
  public void updateLatePlan() {

    String sql = "update follow_up_observation set execute_status='2' "
            + "where execute_status='0' and EXTRACT(EPOCH FROM begin_time-now())<0";

    getCurrentSession().createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void deleteByIds(String foIds) {
    String sql = "update follow_up_observation set execute_status='3' where fo_id in(" + foIds + ")"; //execute_status='3'代表被删除
    this.getCurrentSession().createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String getById(int foId) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT tmp1.*) r))) from("
            + "SELECT fuo.*, ui.name user_name FROM follow_up_observation fuo "
            + "inner join user_info ui on ui.ui_id=fuo.user_id "
            + "where fuo.fo_id=" + foId
            + " )as tmp1";
    System.out.println(sql);
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
  
  @Override
  public void updateExecuteStatus(String followName, char executeStatus) {
    Session session = getCurrentSession();
    String sql = "update follow_up_observation set execute_status='" + executeStatus + "' where fo_name='" + followName + "' ";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateProcessResult(String followName, char result) {
    Session session = getCurrentSession();
    String sql = "update follow_up_observation set process_result='" + result + "' where fo_name='" + followName + "' ";
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
  public String findRecord(int start, int length, char executeStatus, char triggerType, char processResult) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT tmp1.*) r))) from("
            + "SELECT fuo.*, ui.name user_name FROM follow_up_observation fuo "
            + "inner join user_info ui on ui.ui_id=fuo.user_id "
            + "where 1=1 ";
    if (executeStatus != 'a') {
      sql += "and execute_status='" + executeStatus + "' ";
    }
    if (triggerType != 'a') {
      sql += "and trigger_type='" + triggerType + "' ";
    }
    if (processResult != 'a') {
      sql += "and process_result='" + processResult + "' ";
    }
    sql += "ORDER BY fuo.trigger_time desc OFFSET " + start + " LIMIT " + length + " )as tmp1";

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
  public Long findRecordCount(char executeStatus, char triggerType, char processResult) {

    String sql = "SELECT count(*) FROM follow_up_observation where 1=1 ";
    if (executeStatus != 'a') {
      sql += "and execute_status='" + executeStatus + "' ";
    }
    if (triggerType != 'a') {
      sql += "and trigger_type='" + triggerType + "' ";
    }
    if (processResult != 'a') {
      sql += "and process_result='" + processResult + "' ";
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

  @Override
  public int countByObjName(String objName) {

    Session session = getCurrentSession();
    String sql = "select count(*) from follow_up_observation where obj_name='" + objName + "'";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      return ((BigInteger) q.list().get(0)).intValue();
    } else {
      return 0;
    }
  }
}
