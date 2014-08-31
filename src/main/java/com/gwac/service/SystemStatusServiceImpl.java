/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.OtLevel2;
import java.util.List;

/**
 *
 * @author xy
 */
public class SystemStatusServiceImpl implements SystemStatusService {

  private OtLevel2Dao otbDao;
  private DataProcessMachineDAO dpmDao;
  
  public void updateSystemStatus(){
    
    List<DataProcessMachine> dpms = getDpmDao().findAll();
    for(DataProcessMachine dpm : dpms){
      List<OtLevel2> ot2s = getOtbDao().getOtLevel2ByDpmName(dpm.getName());

      getOtbDao().save(ot2s);
    }
  }

  /**
   * @return the otbDao
   */
  public OtLevel2Dao getOtbDao() {
    return otbDao;
  }

  /**
   * @param otbDao the otbDao to set
   */
  public void setOtbDao(OtLevel2Dao otbDao) {
    this.otbDao = otbDao;
  }

  /**
   * @return the dpmDao
   */
  public DataProcessMachineDAO getDpmDao() {
    return dpmDao;
  }

  /**
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }
}
