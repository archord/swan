/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtObserveRecordShow;
import com.gwac.model.OtObserveRecordLevel1;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtObserveRecordLevel1DAO extends BaseHibernateDao<OtObserveRecordLevel1> {

  public List<OtObserveRecordShow> getRecordByOtId(long otId);

  public int countRecordByOtName(String otName);

  public List<OtObserveRecordShow> getRecordByOtName(String otName, int start, int resultSize);

  public Boolean exist(OtObserveRecordLevel1 obj);
}
