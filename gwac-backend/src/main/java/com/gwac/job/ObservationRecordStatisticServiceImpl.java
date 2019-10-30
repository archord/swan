/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.ObservationRecordStatisticDao;
import com.gwac.dao.OtLevel2Dao;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service(value = "observationRecordStatisticService")
public class ObservationRecordStatisticServiceImpl  implements BaseService{

  private static final Log log = LogFactory.getLog(ObservationRecordStatisticServiceImpl.class);
  private static boolean running = true;

  @Resource
  private ObservationRecordStatisticDao observationRecordStatisticDao;
  
  @Override
  public void startJob() {
    
    if (running == true) {
      log.debug("start job...");
      running = false;
    } else {
      log.warn("job is running, jump this scheduler.");
      return;
    }

    long startTime = System.nanoTime();
    try {
      observationRecordStatisticDao.createTodayStatistic();
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }
    }
    long endTime = System.nanoTime();
    log.info("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }
  
}
