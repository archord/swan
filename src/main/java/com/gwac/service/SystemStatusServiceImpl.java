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
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service
public class SystemStatusServiceImpl implements SystemStatusService {

  @Resource
  private OtLevel2Dao otbDao;
  @Resource
  private DataProcessMachineDAO dpmDao;
  
  public void updateSystemStatus(){
    
    List<DataProcessMachine> dpms = dpmDao.findAll();
    for(DataProcessMachine dpm : dpms){
    }
  }
}
