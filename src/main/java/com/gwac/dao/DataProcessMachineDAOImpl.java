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

/**
 *
 * @author xy
 */
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
  public void updateMonitorImageTime(int dpmId) {
    Session session = getCurrentSession();
    String sql = "update data_process_machine set monitor_image_time=current_timestamp where dpm_id=" + dpmId;
    session.createSQLQuery(sql).executeUpdate();
  }

  public void updateByName(DataProcessMachine dpm) {
    Session session = getCurrentSession();
    String sql = "update data_process_machine set cur_process_number=";

//    pStmt = session.connection().prepareStatement(sql);
//    rs = pStmt.executeQuery();
//    session.createSQLQuery(sql).executeUpdate();
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
      tdpm.setCurProcessNumber(0);
      tdpm.setFirstRecordNumber(0);
      tdpm.setUsedStorageSize(new Float(0));
      tdpm.setTotalStorageSize(new Float(1));
      super.save(tdpm);
      return tdpm;
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
  @Override
  public void updateFirstRecordNumber(String dpmName, int number) {
    Session session = getCurrentSession();
    String sql = "update data_process_machine set first_record_number=" + number + " where name='" + dpmName + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public int getFirstRecordNumber(String dpmName) {
    int number = 0;
    Session session = getCurrentSession();
    String sql = "select first_record_number from data_process_machine where name='" + dpmName + "'";
    Query q = session.createSQLQuery(sql);
    List tlist = q.list();
    if (!tlist.isEmpty()) {
      number = (Integer) tlist.get(0);
    }
    return number;
  }

  @Override
  public void everyDayInit() {
    Session session = getCurrentSession();
    String sql = "update data_process_machine set first_record_number=0, cur_process_number=0;";
    session.createSQLQuery(sql).executeUpdate();
  }
}
