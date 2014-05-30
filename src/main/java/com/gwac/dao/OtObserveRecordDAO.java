/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtObserveRecordShow;
import com.gwac.model.OtObserveRecordTmp;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtObserveRecordDAO extends BaseHibernateDao<OtObserveRecordTmp> {

  public List<OtObserveRecordShow> getRecordByOtId(long otId);

  public int countRecordByOtName(String otName);

  public List<OtObserveRecordShow> getRecordByOtName(String otName, int start, int resultSize);

  public Boolean exist(OtObserveRecordTmp obj);
}
