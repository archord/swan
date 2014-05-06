/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.model.DataProcessMachine;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xy
 */
public class DataProcessMachineServiceImpl implements DataProcessMachineService {

  private DataProcessMachineDAO dpmDao;

  @Override
  @Transactional
  public Number count() {
    return dpmDao.count();
  }

  @Override
  @Transactional
  public List<DataProcessMachine> findAll(int start, int resultSize) {
    String order[] = {"name"};
    int[] sorts = {1};
    return dpmDao.findRecord(start, resultSize, order,sorts);
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
