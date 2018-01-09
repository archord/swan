/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.WebGlobalParameter;

/**
 *
 * @author xy
 */
public interface WebGlobalParameterDao extends BaseHibernateDao<WebGlobalParameter> {

  public void updateParameter(String name, String value);

  public String getValueByName(String name);
  
  public void everyDayInit();
}
