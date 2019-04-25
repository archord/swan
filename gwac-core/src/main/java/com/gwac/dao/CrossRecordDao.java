/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossObject;
import com.gwac.model.CrossRecord;
import com.gwac.model.FitsFileCut;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CrossRecordDao extends BaseHibernateDao<CrossRecord> {
  
  public String getCutImageByOtId(long otId, Boolean queryHis) ;
  
  public String getOtOpticalVaration(CrossObject ot2, Boolean queryHis);
  
  public List<CrossRecord> matchLatestN(CrossRecord obj, float errorBox, int n);
  
}
