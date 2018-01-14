/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpObservation;
import com.gwac.model.Telescope;

/**
 *
 * @author xy
 */
public interface FollowUpObservationDao extends BaseHibernateDao<FollowUpObservation> {
  
  public void updateExecuteStatus(String followName, char executeStatus);

  public String findRecord(int start, int length, char executeStatus);
  
  public Long findRecordCount(char executeStatus);

  public FollowUpObservation getByName(String name);
}
