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

  public List<OtObserveRecordShow> getRecordByOtName(String otName);

  public Boolean exist(OtObserveRecordTmp obj);
}
