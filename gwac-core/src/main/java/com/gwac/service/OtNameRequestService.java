/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.model.OtLevel2;
import java.io.File;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtNameRequestService {
  
  public List<OtLevel2> getOtNames(File paraFile);
}
