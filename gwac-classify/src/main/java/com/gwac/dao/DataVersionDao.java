/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataVersion;

/**
 *
 * @author xy
 */
public interface DataVersionDao extends BaseHibernateDao<DataVersion> {
  public String getRecords();
}
