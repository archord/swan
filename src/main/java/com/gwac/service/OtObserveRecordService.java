/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.model.OtObserveRecordTmp;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtObserveRecordService {
  public List<OtObserveRecordTmp> getOtOR();
  public void storeOTCatalog();
}
