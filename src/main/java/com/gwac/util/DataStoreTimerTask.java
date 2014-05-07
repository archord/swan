/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.util;

import com.gwac.service.OtObserveRecordService;

/**
 *
 * @author xy
 */
public class DataStoreTimerTask {
  
  private OtObserveRecordService otORService;
  
  public void testRun(){
    System.out.println("Spring 3 + Quartz 1.8.6 TimerTask running!");
    otORService.storeOTCatalog();
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
