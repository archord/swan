/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.TelescopeDAO;
import com.gwac.model.Telescope;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xy
 */
public class TelescopeServiceImpl implements TelescopeService {

  private TelescopeDAO tspDao;

  @Override
  @Transactional
  public Number count() {
    return getTspDao().count();
  }

  public List<Telescope> findAll() {
    return tspDao.findAll();
  }

  @Override
  @Transactional
  public List<Telescope> findAll(int start, int resultSize) {
    String order[] = {"name"};
    int[] sorts = {1};
    return getTspDao().findRecord(start, resultSize, order, sorts);
  }

  /**
   * @return the tspDao
   */
  public TelescopeDAO getTspDao() {
    return tspDao;
  }

  /**
   * @param tspDao the tspDao to set
   */
  public void setTspDao(TelescopeDAO tspDao) {
    this.tspDao = tspDao;
  }
}
