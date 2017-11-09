/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CameraTemperatureMonitor;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CameraTemperatureMonitorDao extends BaseHibernateDao<CameraTemperatureMonitor> {
  public String getRecords(int days);
  public String getRecords(String camera, int days);
  public List<CameraTemperatureMonitor> getRecords(int camId, Date start, Date end);
}
