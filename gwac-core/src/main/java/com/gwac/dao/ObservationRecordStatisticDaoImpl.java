/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObservationRecordStatistic;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class ObservationRecordStatisticDaoImpl extends BaseHibernateDaoImpl<ObservationRecordStatistic> implements ObservationRecordStatisticDao {
  
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
