/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.CcdPixFilter;
import com.gwac.model.OtLevel2;

/**
 *
 * @author xy
 */
public interface CcdPixFilterDao extends BaseHibernateDao<CcdPixFilter> {

  public void removeAll();

  public Short filterOT2(OtLevel2 ot2);
}
