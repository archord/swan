/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.dao;

import com.gwac.model.CoordinateShow;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CoordinateShowDao  extends BaseHibernateDao<CoordinateShow>{
  
  List<CoordinateShow> getCoordinateShow(String sql);
}
