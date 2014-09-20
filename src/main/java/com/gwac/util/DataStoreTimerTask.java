/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.util;

import com.gwac.service.OtObserveRecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class DataStoreTimerTask {
  
  private static final Log log = LogFactory.getLog(DataStoreTimerTask.class);
  
  private static boolean running = true;
  private OtObserveRecordService otORService;
  
  public void storeOT(){
    if (running == true) {
      log.info("start job storeOtObserveRecordJob...");
      running = false;
    } else {
      log.info("job storeOtObserveRecordJob is running, jump this scheduler.");
      return;
    }
    
    otORService.storeOTCatalog();
    
    if (running == false) {
      running = true;
      log.info("job storeOtObserveRecordJob is done.");
    }
  }

  /**
   * @return the otORService
   */
  public OtObserveRecordService getOtORService() {
    return otORService;
  }

  /**
   * @param otORService the otORService to set
   */
  public void setOtORService(OtObserveRecordService otORService) {
    this.otORService = otORService;
  }
}
