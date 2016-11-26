/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.gwac.model.OtObserveRecordShow;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtObserveRecordDAO extends BaseHibernateDao<OtObserveRecord> {
  
  
  public List<OtObserveRecord> getOt1ByDateDpmSkyId(String dateStr, int dpmId, int skyId);
  
  public void updateFfcId(OtObserveRecord oor);
  
  public List<OtObserveRecord> getLastRecord(OtLevel2 ot2);
  
  public List<OtObserveRecord> getUnCutRecord(long otId, int lastCuttedNum);
  
  public List<OtObserveRecord> searchOT2TmplWrong(OtObserveRecord obj, float searchRadius, float mag);
  
  public List<OtObserveRecord> getOt1ByDate(String dateStr);
  
  public List<String> getAllDateStr();

  public void moveDataToHisTable();

  public List<OtObserveRecordShow> getRecordByOtId(long otId);

  public int countRecordByOtName(String otName, Boolean queryHis);

  public List<OtObserveRecordShow> getRecordByOtName(String otName, int start, int resultSize, Boolean queryHis);

  public Boolean exist(OtObserveRecord obj, float errorBox);
  
  public List<OtObserveRecord> existInAll(OtObserveRecord obj, float errorBox);

  public List<OtObserveRecord> matchLatestN(OtObserveRecord obj, float errorBox, int n);

  public List<OtObserveRecord> getLatestNLv1OT(int n);

  public String getUnCuttedStarList(int dpmId);
  
  public String getOtOpticalVaration(OtLevel2 ot2, Boolean queryHis);
  
  public List<OtObserveRecord> getAllOrderByDate();
}
