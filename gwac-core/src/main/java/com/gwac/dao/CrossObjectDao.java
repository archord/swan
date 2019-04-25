/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossObject;
import com.gwac.model.FitsFileCut;
import com.gwac.model4.CrossObjectQueryParameter;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CrossObjectDao extends BaseHibernateDao<CrossObject> {
  
  public CrossObject getCrossObjectById(long coId, Boolean queryHis);
  
  public List<Integer> hisOrCurExist(long coId);

  public void moveDataToHisTable();

  public CrossObject exist(CrossObject obj, float errorBox);

  public CrossObject existInLatestN(CrossObject obj, float errorBox, int n);

  public CrossObject getCrossObjectById(String coId, Boolean queryHis);

  public List<CrossObject> queryCrossObject(CrossObjectQueryParameter ot2qp);

  public int countCrossObject(CrossObjectQueryParameter ot2qp);

  public void updateIsMatch(CrossObject ot2);

  public void updateCvsMatch(CrossObject ot2);

  public void updateRc3Match(CrossObject ot2);

  public void updateMinorPlanetMatch(CrossObject ot2);

  public void updateHisMatch(CrossObject ot2);

  public void updateOtType(CrossObject ot2);

  public void updateOtherMatch(CrossObject ot2);

  public void updateUsnoMatch(CrossObject ot2);

  public int updateLookBackResult(CrossObject ot2);

  public void updateFollowUpResult(CrossObject ot2);

  public void updateSomeRealTimeInfo(CrossObject ot2);

  public void updateFoCount(CrossObject ot2);

}
