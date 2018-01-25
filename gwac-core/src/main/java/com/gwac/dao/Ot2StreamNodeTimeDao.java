/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.dao;

import com.gwac.model.Ot2StreamNodeTime;

/**
 *
 * @author msw
 */
public interface Ot2StreamNodeTimeDao extends BaseHibernateDao<Ot2StreamNodeTime> {
  public void updateLookBackTime(long otId);
  public void updateLookUpTime(long otId);
  public void updateLookBackTime(String otName);
  public void updateLookUpResultTime(long otId);
  public void updateLookUpResultTime(String otName);
}
