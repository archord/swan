/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataProcessMachine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class DataProcessMachineDAOImpl extends BaseHibernateDaoImpl<DataProcessMachine> implements DataProcessMachineDAO {

  private static final Log log = LogFactory.getLog(DataProcessMachineDAOImpl.class);

  public void updateByName(DataProcessMachine dpm) {
    Session session = getCurrentSession();
    String sql = "update data_process_machine set cur_process_number=";

//    pStmt = session.connection().prepareStatement(sql);
//    rs = pStmt.executeQuery();
    
//    session.createSQLQuery(sql).executeUpdate();
  }

  public DataProcessMachine getDpmByName(String name) {
    Session session = getCurrentSession();
    String sql = "select * from data_process_machine where name='" + name + "';";
    Query q = session.createSQLQuery(sql).addEntity(DataProcessMachine.class);
    if (!q.list().isEmpty()) {
      return (DataProcessMachine) q.list().get(0);
    } else {
      return null;
    }
  }

  public DataProcessMachine getDpmById(long id) {
    Session session = getCurrentSession();
    String sql = "select * from data_process_machine where dpm_id=" + id;
    Query q = session.createSQLQuery(sql).addEntity(DataProcessMachine.class);
    if (!q.list().isEmpty()) {
      return (DataProcessMachine) q.list().get(0);
    } else {
      return null;
    }
  }

//  public Number count() {
//    String sql = "select count(*) from data_process_machine;";
//    Session curSession = getCurrentSession();
//    Query query = curSession.createSQLQuery(sql);
//    return ((Number) query.uniqueResult()).intValue();
//  }
}
