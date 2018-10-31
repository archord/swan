/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ScienceObject;
import java.util.List;

/**
 *
 * @author msw
 */
public interface ScienceObjectDao extends BaseHibernateDao<ScienceObject> {

  public List<ScienceObject> getByStatus(int status);
  
  public void updateFupCount(long sciObjId);
  
  public String findRecord(int start, int length);
  
  public Long findRecordCount();
}
