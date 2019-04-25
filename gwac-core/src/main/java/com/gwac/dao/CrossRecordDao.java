/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossRecord;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CrossRecordDao extends BaseHibernateDao<CrossRecord> {
  
  public List<CrossRecord> matchLatestN(CrossRecord obj, float errorBox, int n);
  
}
