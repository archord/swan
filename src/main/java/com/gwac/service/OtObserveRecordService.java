/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.model.OtObserveRecord;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtObserveRecordService {
  public List<OtObserveRecord> getOtOR();
  public void storeOTCatalog();
}
