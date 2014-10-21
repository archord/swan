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
      log.debug("start job storeOtObserveRecordJob...");
      running = false;
    } else {
      log.warn("job storeOtObserveRecordJob is running, jump this scheduler.");
      return;
    }
    
    long startTime=System.nanoTime();
    otORService.storeOTCatalog();
    long endTime=System.nanoTime();
    
    if (running == false) {
      running = true;
      log.debug("job storeOtObserveRecordJob is done.");
    }
    log.debug("store ot observe record consume "+ 1.0*(endTime-startTime)/1e9+" seconds.");
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
