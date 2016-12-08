/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ConfigFile;
import java.util.List;

/**
 *
 * @author xy
 */
public interface ConfigFileDao extends BaseHibernateDao<ConfigFile> {
  
  public void removeOldRecordByDay(String dateStr);
  
  public void moveDataToHisTable();

  public Boolean exist(ConfigFile obj);

  public List<ConfigFile> getTopNUnSync(int topn);
}
