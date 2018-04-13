/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataSet;

/**
 *
 * @author xy
 */
public interface DataSetDao extends BaseHibernateDao<DataSet> {
  
  public String getRecords(int dvId, int defaultType);
}
