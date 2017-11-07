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
