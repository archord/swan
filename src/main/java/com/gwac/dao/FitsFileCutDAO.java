/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
import java.util.List;

/**
 *
 * @author xy
 */
public interface FitsFileCutDAO extends BaseHibernateDao<FitsFileCut> {
  
  public void updateIsRecvOk(long ffcId);
  
  public List<FitsFileCut> getUnSyncList(int size);
  
  public List<FitsFileCut> getFirstCutFile(OtLevel2 ot2);

  public void moveDataToHisTable();

  public void uploadSuccessCutByName(String fileName);

  public String getUnCuttedStarList(int dpmId, int size, int maxPriority);
  
  public List<FitsFileCut> getCutImageByOtId(long otId, Boolean queryHis);

  public List<FitsFileCut> getCutImageByOtName(String otName);

  public List<FitsFileCut> getUnCutImageByOtId(long otId, int lastCuttedId);
  
  public List<FitsFileCut> getCutImageByOtNameFromHis(String otName);
}
