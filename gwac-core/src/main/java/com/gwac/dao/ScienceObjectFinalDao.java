/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ScienceObjectFinal;
import java.util.List;

/**
 *
 * @author msw
 */
public interface ScienceObjectFinalDao extends BaseHibernateDao<ScienceObjectFinal> {
  
  public String getById(long sofId);
  
  public String findRecord(int start, int length, int type);
  
  public Long findRecordCount(int type);
}
