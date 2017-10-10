/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.ObservationSky;
import java.util.List;

/**
 *
 * @author xy
 */
public interface ObservationSkyDao extends BaseHibernateDao<ObservationSky> {

  public ObservationSky getByName(String name, String gridId);

  public List<String> getAllSkyName();
  public List<String> getAllSkyName(String gridId);
  public List<String> getAllGridId();
}
