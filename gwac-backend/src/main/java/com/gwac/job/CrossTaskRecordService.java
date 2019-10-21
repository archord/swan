/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

/**
 *
 * @author xy
 */
public interface CrossTaskRecordService {
  public void parseLevel1Ot(long ufuId, String storePath, String fileName, String taskName, String dateStr);
  
}
