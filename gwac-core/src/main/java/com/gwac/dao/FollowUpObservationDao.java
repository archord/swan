/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpObservation;
import java.util.List;

/**
 *
 * @author xy
 */
public interface FollowUpObservationDao extends BaseHibernateDao<FollowUpObservation> {
    
  public List<FollowUpObservation> getBySciObjId(long sciObjId);
  
  public List<FollowUpObservation> getByFoId(long foId);
  
  public void updateSciObjId(long fupObsId, long sciObjId);
  
  public void updateSciObjId(String objName, long sciObjId);
  
  public void deleteByIds(String foIds);
  
  public String getById(int foId);
  
  public void updateExecuteStatus(String followName, char executeStatus);
  
  public void updateProcessResult(String followName, char result);

  public String findRecord(int start, int length, char executeStatus);
  
  public String findRecord(int start, int length, char executeStatus, char triggerType, char processResult);
  
  public Long findRecordCount(char executeStatus);
  
  public Long findRecordCount(char executeStatus, char triggerType, char processResult);

  public FollowUpObservation getByName(String name);
  
  public int countByObjName(String objName);
  
  public List<FollowUpObservation> getUnTriggeredByTime(int seconds);
  
  public void updateLatePlan();
}
