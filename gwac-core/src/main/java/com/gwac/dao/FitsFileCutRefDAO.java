/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFileCutRef;
import java.util.List;

/**
 *
 * @author xy
 */
public interface FitsFileCutRefDAO extends BaseHibernateDao<FitsFileCutRef> {
  
  public void moveDataToHisTable();
  
  public List<FitsFileCutRef> getByName(String ffcName);
  
  public void updateIsRecvOk(long ffcrId);
  
  public List<FitsFileCutRef> getUnSyncList(int size);

  public void updateByName(FitsFileCutRef ffcr);

  public List<FitsFileCutRef> getCutImageByOtName(String otName);
  
  public List<FitsFileCutRef> getCutImageByOtId(long otId, Boolean queryHis);
  
  public String getUnCuttedStarList(int dpmId, int size);
}
