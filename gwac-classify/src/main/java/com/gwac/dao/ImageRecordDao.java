/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ImageRecord;

/**
 *
 * @author xy
 */
public interface ImageRecordDao extends BaseHibernateDao<ImageRecord> {
  
  public String getRecords(int dsId);
  
  public void updateType(int imgId, int type);
}
