/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.TimingTaskLog;

/**
 *
 * @author msw
 */
public interface TimingTaskLogDao extends BaseHibernateDao<TimingTaskLog> {
  public String findRecord(int start, int length);
  
}
