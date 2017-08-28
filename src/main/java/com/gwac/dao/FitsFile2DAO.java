/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFile2;
import com.gwac.model.FitsFile2Show;

/**
 *
 * @author xy
 */
public interface FitsFile2DAO extends BaseHibernateDao<FitsFile2> {

  public FitsFile2 getByName(String ffName);
  
  public FitsFile2Show getShowByName(String ffName);

  public boolean exist(String ffName);
}
