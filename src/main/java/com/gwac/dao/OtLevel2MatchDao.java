/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.dao;

import com.gwac.model.OtLevel2Match;

/**
 *
 * @author xy
 */
public interface OtLevel2MatchDao extends BaseHibernateDao<OtLevel2Match> {
  public OtLevel2Match getByOt2Id(long ot2Id);
}