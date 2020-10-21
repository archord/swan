/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model4.OtLevel2QueryParameter;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xy
 */
public interface OtLevel2Dao extends BaseHibernateDao<OtLevel2> {
  public String getOT2CutList(int camId, Boolean queryHis);
  public void updateCutImageRequest(String otName, String cutImageRequest, Boolean queryHis);
  public void updateComments(long otId, String comments);
  public void updateCommentsHis(long otId, String comments);
  
  public List<OtLevel2> getUnFollowOT2();
  
  public List<OtLevel2> getUnCutRecord(int successiveImageNumber);
  
  public void updateOt2HisMatchHis(OtLevel2 ot2);
  
  public void updateOTTypeHis(OtLevel2 ot2) ;
  
  public List<OtLevel2> getTodayOt2(char otClass);
  
  public List<OtLevel2> getOt2ByDate(String dateStr);
  
  public List<String> getAllDateStr();
  
  public List<String> getAllDateStr(boolean history);
  
  public List<OtLevel2> getLv2OTByDateAndOTClass(String dateStr, char otClass);

  public void moveDataToHisTable();

  public List<OtLevel2> searchOT2His(OtLevel2 ot2, float searchRadius, float mag);

  public Boolean exist(OtLevel2 obj, float errorBox);
  
  public List<Integer> hisOrCurExist(String otName);
  public List<Integer> hisOrCurExist(long otId);

  public OtLevel2 existInAll(OtLevel2 obj, float errorBox);

  public OtLevel2 existInLatestN(OtLevel2 obj, float errorBox, int n);

  public OtLevel2 getOtLevel2ByName(String otName, Boolean queryHis);

  public OtLevel2 getOtLevel2ByNameFromHis(String otName);

  public List<OtLevel2> getOtLevel2ByDpmName(String dpmName);

  public List<OtLevel2> queryOtLevel2(OtLevel2QueryParameter ot2qp);

  public int countOtLevel2(OtLevel2QueryParameter ot2qp);

  public List<OtLevel2> getMatchedLv2OT();

  public List<OtLevel2> getMatchedLv2OTByDate(String dateStr);

  public List<OtLevel2> getCurOccurLv2OT();

  public List<OtLevel2> getCurOccurLv2OTByDate(String dateStr);

  public List<OtLevel2> getNCurOccurLv2OT();

  public List<OtLevel2> getNCurOccurLv2OTByDate(String dateStr);

  public List<OtLevel2> getMissedFFCLv2OT();

  public void updateAllFileCuttedById(long id);

  public void updateIsMatch(OtLevel2 ot2);

  public void updateCvsMatch(OtLevel2 ot2);

  public void updateRc3Match(OtLevel2 ot2);

  public void updateMinorPlanetMatch(OtLevel2 ot2);

  public void updateOt2HisMatch(OtLevel2 ot2);
  
  public void updateOTType(OtLevel2 ot2);

  public void updateOtherMatch(OtLevel2 ot2);

  public void updateUsnoMatch(OtLevel2 ot2);
  
  public void updateOtType(int otId, int otTypeId);
  
  public int updateLookBackResult(OtLevel2 ot2);
  
  public void updateFollowUpResult(OtLevel2 ot2);
  
  public void updateCuttedFfNumber(OtLevel2 ot2);
  
  public void updateSomeRealTimeInfo(OtLevel2 ot2);
  
  public void updateFoCount(OtLevel2 ot2);

  public List<OtLevel2> getUnMatched();
  
  public String getOT2FitsFileName(String otName, Boolean queryHis);
  
  public short getIsMatchByName(String otName);
  
}
