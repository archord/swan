/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtObserveRecordShow;
import com.gwac.model.OtObserveRecord;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtObserveRecordDAO extends BaseHibernateDao<OtObserveRecord> {

  public List<OtObserveRecordShow> getRecordByOtId(long otId);

  public int countRecordByOtName(String otName);

  public List<OtObserveRecordShow> getRecordByOtName(String otName, int start, int resultSize);

  public Boolean exist(OtObserveRecord obj);

  public List<OtObserveRecord> matchLatestN(OtObserveRecord obj);

  public List<OtObserveRecord> getLatestNLv1OT(int n);

  public String getUnCuttedStarList(int dpmId);
}
