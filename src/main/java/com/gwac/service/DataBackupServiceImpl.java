/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class DataBackupServiceImpl implements DataBackupService{
  
  private static final Log log = LogFactory.getLog(DataBackupServiceImpl.class);
  
  private static boolean running = true;
  
  private OtLevel2Dao otlv2Dao;
  private FitsFileCutDAO ffcDao;
  private OtObserveRecordDAO oorDao;

  public void backupData() {
    
    if (running == true) {
      log.info("start job dataBackupJob...");
      running = false;
    } else {
      log.info("job dataBackupJob is running, jump this scheduler.");
      return;
    }
    
    otlv2Dao.moveDataToHisTable();
    ffcDao.moveDataToHisTable();
    oorDao.moveDataToHisTable();
    
    if (running == false) {
      running = true;
      log.info("job dataBackupJob is done.");
    }
    
  }

  /**
   * @param otlv2Dao the otlv2Dao to set
   */
  public void setOtlv2Dao(OtLevel2Dao otlv2Dao) {
    this.otlv2Dao = otlv2Dao;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @param oorDao the oorDao to set
   */
  public void setOorDao(OtObserveRecordDAO oorDao) {
    this.oorDao = oorDao;
  }
  
}
