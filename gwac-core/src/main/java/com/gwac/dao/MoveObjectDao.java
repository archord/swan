/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.MoveObject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xy
 */
public interface MoveObjectDao extends BaseHibernateDao<MoveObject> {
  
  public Map<String, Float[]> getMotFitsList(int motId);
  
  public List<String> getAllDateStr();

  public String getMoveObjsByDate(String dateStr);

  public String getNotMatchOTByDate(String dateStr);
  
  public Map<Long, String> getMoveObjsInfoByDate(String dateStr, char moveType, int minFrameNumber);
  
}
