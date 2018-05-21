/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.TimingTask;

/**
 *
 * @author msw
 */
public interface TimingTaskDao extends BaseHibernateDao<TimingTask> {
  public String findRecord(int start, int length);
  
}
