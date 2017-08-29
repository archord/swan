/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.model.DataProcessMachine;
import java.util.List;

/**
 *
 * @author xy
 */
public interface DataProcessMachineService {
  public Number count();
  public List<DataProcessMachine> findAll(int start, int resultSize);
}
