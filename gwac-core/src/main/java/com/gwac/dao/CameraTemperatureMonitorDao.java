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
  public List<CameraTemperatureMonitor> getRecords(int camId, Date start, Date end);
}
