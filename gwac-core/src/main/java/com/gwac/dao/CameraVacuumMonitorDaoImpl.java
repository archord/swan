/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CameraVacuumMonitor;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class CameraVacuumMonitorDaoImpl extends BaseHibernateDaoImpl<CameraVacuumMonitor> implements CameraVacuumMonitorDao {

  private static final Log log = LogFactory.getLog(CameraVacuumMonitorDaoImpl.class);
  
  @Override
  public String getRecords(String camera, int days) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT cam_name, voltage, current, pressure, time) r))) "
            + "FROM( "
            + "SELECT cam.name as cam_name, cvm.voltage, cvm.current, cvm.pressure, cvm.time "
            + "FROM camera_vacuum_monitor cvm "
            + "inner join camera cam on cam.camera_id=cvm.cam_id "
            + "where cvm.time>(LOCALTIMESTAMP-interval '" + days + " day') and cam.name='"+camera+"' "
            + "order by cvm.time)as moor ";

    log.debug(sql);
    Query q = session.createSQLQuery(sql);

    String rst = "";
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public String getRecords(int days) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT cam_name, par_detail) r))) "
            + "FROM( "
            + "SELECT cam.name as cam_name, JSON_AGG((SELECT r FROM (SELECT voltage, current, pressure, time) r)) as par_detail "
            + "FROM camera_vacuum_monitor cvm "
            + "inner join camera cam on cam.camera_id=cvm.cam_id "
            + "where cvm.time>(LOCALTIMESTAMP-interval '" + days + " day') "
            + "GROUP BY cam.name "
            + "order by cam.name)as moor ";

    log.debug(sql);
    Query q = session.createSQLQuery(sql);

    String rst = "";
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public List<CameraVacuumMonitor> getRecords(int camId, Date start, Date end) {
    Session session = getCurrentSession();
    String sql = "select * from camera_vacuum_monitor where cam_id=? and time between ? and ?;";
    Query q = session.createSQLQuery(sql).addEntity(CameraVacuumMonitor.class);
    q.setInteger(0, camId);
    q.setDate(1, start);
    q.setDate(2, end);
    return q.list();
  }
}
