/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtLevel2Dao extends BaseHibernateDao<OtLevel2> {

  public void moveDataToHisTable();

  public Boolean exist(OtLevel2 obj, float errorBox);

  public OtLevel2 existInLatestN(OtLevel2 obj, float errorBox, int n);

  public OtLevel2 getOtLevel2ByName(String otName);

  public List<OtLevel2> getOtLevel2ByDpmName(String dpmName);

  public List<OtLevel2> queryOtLevel2(String startDate, String endDate, String tsp, float xtemp, float ytemp, float radius, int start, int resultSize);

  public int countOtLevel2(String startDate, String endDate, String tsp, float xtemp, float ytemp, float radius, int start, int resultSize);

  public List<OtLevel2> getCurOccurLv2OT();

  public List<OtLevel2> getCurOccurLv2OTByDate(String dateStr);

  public List<OtLevel2> getNCurOccurLv2OT();

  public List<OtLevel2> getNCurOccurLv2OTByDate(String dateStr);

  public List<OtLevel2> getMissedFFCLv2OT();

  public void updateAllFileCuttedById(long id);

  public OtLevel2 getOtLevel2ByNameFromHis(String otName);
}
