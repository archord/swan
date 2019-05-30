/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossTask;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CrossTaskDao extends BaseHibernateDao<CrossTask> {
  
  public List<CrossTask> getObjects(String dateStr);
  
  public CrossTask getByName(String ctName);
  
  public void moveDataToHisTable();

  public Boolean exist(CrossTask obj);

}
