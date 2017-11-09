/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CameraVacuumMonitor;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CameraVacuumMonitorDao extends BaseHibernateDao<CameraVacuumMonitor> {
  public String getRecords(int days);
  public String getRecords(String camera, int days);
  public List<CameraVacuumMonitor> getRecords(int camId, Date start, Date end);
}
