/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataProcessMachine;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class DataProcessMachineDAOImpl extends BaseHibernateDaoImpl<DataProcessMachine> implements DataProcessMachineDAO {

  private static final Log log = LogFactory.getLog(DataProcessMachineDAOImpl.class);

  @Override
  public List<DataProcessMachine> getAllDpms() {
    Session session = getCurrentSession();
    String sql = "select * from data_process_machine order by dpm_id;";
    Query q = session.createSQLQuery(sql).addEntity(DataProcessMachine.class);
    return q.list();
  }

  @Override
  public void updateLastActiveTime(String dmpName) {
    Session session = getCurrentSession();
    String sql = "update data_process_machine set last_active_time=current_timestamp where name='" + dmpName.toUpperCase() + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public DataProcessMachine getDpmByName(String name) {
    Session session = getCurrentSession();
    String sql = "select * from data_process_machine where name='" + name + "';";
    Query q = session.createSQLQuery(sql).addEntity(DataProcessMachine.class);
    if (!q.list().isEmpty()) {
      return (DataProcessMachine) q.list().get(0);
    } else {
      DataProcessMachine tdpm = new DataProcessMachine();
      tdpm.setName(name);
      tdpm.setUsedStorageSize(new Float(0));
      tdpm.setTotalStorageSize(new Float(1));
      super.save(tdpm);
      return tdpm;
    }
  }

  @Override
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

}
