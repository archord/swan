/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObservationRecordStatistic;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class ObservationRecordStatisticDaoImpl extends BaseHibernateDaoImpl<ObservationRecordStatistic> implements ObservationRecordStatisticDao {
  
  public List<ObservationRecordStatistic> query(String startDate, String endDate, float cRa, float cDec, float radius){
    
    float halfSearchBox = (float) (13.0/2);
    float minDec = cDec-halfSearchBox-radius;
    float maxDec = cDec+halfSearchBox+radius;
    
    Session session = getCurrentSession();
    String sql = "select * from observation_record_statistic " +
      "where center_dec>=? and center_dec<=? " +
      "and (( start_obs_time>=? and start_obs_time<=?)  " +
      "or ( end_obs_time>=? and end_obs_time<=?) " +
      "or ( start_obs_time<=? and end_obs_time>=?) )";
    
    Query q = session.createSQLQuery(sql).addEntity(ObservationRecordStatistic.class);
    q.setFloat(0, minDec);
    q.setFloat(1, maxDec);
    q.setString(2, startDate);
    q.setString(3, endDate);
    q.setString(4, startDate);
    q.setString(5, endDate);
    q.setString(6, startDate);
    q.setString(7, endDate);
    return q.list();
  }
  
  @Override
  public void createTodayStatistic() {

    Session session = getCurrentSession();
    String sql = "INSERT INTO observation_record_statistic(date_str, sky_id, cam_id, img_num) " +
      "select date_str, sky_id, cam_id, count(*) tnum " +
      "from( " +
      "SELECT to_char(gen_time, 'YYMMDD') date_str, sky_id, cam_id, ff_id " +
      "from fits_file2 " +
      "where sky_id>0 " +
      ")as sky " +
      "GROUP BY date_str, sky_id, cam_id " +
      "ORDER BY date_str, sky_id, cam_id;";
    session.createSQLQuery(sql).executeUpdate();
  }
}
