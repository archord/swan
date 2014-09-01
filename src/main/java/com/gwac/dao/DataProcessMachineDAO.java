/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataProcessMachine;

/**
 *
 * @author xy
 */
public interface DataProcessMachineDAO extends BaseHibernateDao<DataProcessMachine> {

  public DataProcessMachine getDpmByName(String name);

  public void updateByName(DataProcessMachine dpm);
}
