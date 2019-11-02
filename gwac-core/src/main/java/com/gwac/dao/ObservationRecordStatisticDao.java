/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObservationRecordStatistic;
import java.util.List;

/**
 *
 * @author xy
 */
public interface ObservationRecordStatisticDao  extends BaseHibernateDao<ObservationRecordStatistic>{
  
  public void createTodayStatistic();
  
  public List<ObservationRecordStatistic> query(String startDate, String endDate, float cRa, float cDec, float radius);
  
}
