/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtObserveRecordTmp;


/**
 *
 * @author xy
 */
public interface OtObserveRecordDAO extends BaseHibernateDao<OtObserveRecordTmp>{
  
  public void saveOTCopy(final String fname, final String sql);
}
