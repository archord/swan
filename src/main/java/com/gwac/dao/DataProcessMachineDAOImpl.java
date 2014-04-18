/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataProcessMachine;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class DataProcessMachineDAOImpl extends BaseHibernateDaoImpl<DataProcessMachine> implements DataProcessMachineDAO {

  public Number count() {
    String sql = "select count(*) from data_process_machine;";
    Session curSession = getCurrentSession();
    Query query = curSession.createSQLQuery(sql);
    return ((Number) query.uniqueResult()).intValue();
  }
}
