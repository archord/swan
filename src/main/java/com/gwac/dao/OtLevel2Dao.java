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

  public Boolean exist(OtLevel2 obj);
  
  public Boolean existInLatestN(OtLevel2 obj);

  public OtLevel2 getOtLevel2ByName(String otName);

  public List<OtLevel2> getOtLevel2ByDpmName(String dpmName);

  public List<OtLevel2> queryOtLevel2(String startDate, String endDate, String tsp, float xtemp, float ytemp, float radius, int start, int resultSize);
}
