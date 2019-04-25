/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossObjectMatch;
import com.gwac.model.OtLevel2MatchShow;
import java.util.List;

/**
 *
 * @author xy
 */
public interface CrossObjectMatchDao extends BaseHibernateDao<CrossObjectMatch> {
  
  public List<Long> getIdsByStarType(int starType);

  public void updateOt2HisMatchId(long curId, long fromId, long toId);

  public CrossObjectMatch getByOt2Id(long ot2Id);

}
