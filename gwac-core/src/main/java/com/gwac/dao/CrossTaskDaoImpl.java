/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossTask;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class CrossTaskDaoImpl extends BaseHibernateDaoImpl<CrossTask> implements CrossTaskDao {
  
  @Override
  public List<CrossTask> getObjects(String dateStr) {
    String sql = "select * from cross_task where date_str='"+dateStr+"' order by ct_id desc";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(CrossTask.class);
    return q.list();
  }
  
  @Override
  public CrossTask getByName(String ctName) {

    Session session = getCurrentSession();
    String sql = "select * from cross_task where ct_name='" + ctName + "'";
    Query q = session.createSQLQuery(sql).addEntity(CrossTask.class);

    if (!q.list().isEmpty()) {
      return (CrossTask) q.list().get(0);
    } else {
      return null;
    }
  }
  
  @Override
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM cross_task RETURNING * ) INSERT INTO cross_task_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public Boolean exist(CrossTask obj) {
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select ct_id from cross_task where ct_name='" + obj.getCtName()+ "' ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger cfId = (BigInteger) q.list().get(0);
      obj.setCtId(cfId.longValue());
      flag = true;
    }
    return flag;
  }


}
