/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model.OtTmplWrong;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtTmplWrongDao extends BaseHibernateDao<OtTmplWrong> {
  public List<OtTmplWrong> searchOT2TmplWrong(OtLevel2 ot2, float searchRadius, float mag);
  public List<OtTmplWrong> searchOT2TmplWrong2(OtLevel2 ot2, float searchRadius, float mag);
}
