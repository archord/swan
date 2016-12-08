/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.UploadFileRecord;

/**
 *
 * @author xy
 */
public interface UploadFileRecordDao extends BaseHibernateDao<UploadFileRecord>{
  public void removeOldRecordByDay(int day);
}
