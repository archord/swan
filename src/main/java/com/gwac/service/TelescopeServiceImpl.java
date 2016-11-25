/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.TelescopeDAO;
import com.gwac.model.Telescope;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xy
 */
@Service
public class TelescopeServiceImpl implements TelescopeService {

  @Resource
  private TelescopeDAO tspDao;

  @Override
  @Transactional
  public Number count() {
    return tspDao.count();
  }

  public List<Telescope> findAll() {
    return tspDao.findAll();
  }

  @Override
  @Transactional
  public List<Telescope> findAll(int start, int resultSize) {
    String order[] = {"name"};
    int[] sorts = {1};
    return tspDao.findRecord(start, resultSize, order, sorts);
  }

}
