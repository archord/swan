/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2Match;
import com.gwac.model.OtLevel2MatchShow;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtLevel2MatchDao extends BaseHibernateDao<OtLevel2Match> {
  
  public List<Long> getIdsByStarType(int starType);

  public void updateOt2HisMatchId(long curId, long fromId, long toId);

  public OtLevel2Match getByOt2Id(long ot2Id);

  public List<OtLevel2MatchShow> getByOt2Name(String otName, Boolean queryHis);
}
