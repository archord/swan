/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossFile;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CrossFileDao extends BaseHibernateDao<CrossFile> {

  public void moveDataToHisTable();
  
  public CrossFile getByName(String ffName);
  
  public CrossFile getByNameHis(String ffName);

  public boolean exist(CrossFile obj);
}
