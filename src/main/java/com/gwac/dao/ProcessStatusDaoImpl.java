/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ProcessStatus;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class ProcessStatusDaoImpl extends BaseHibernateDaoImpl<ProcessStatus> implements ProcessStatusDao {

  @Override
  public void save(ProcessStatus obj) {
    Session session = getCurrentSession();
    //createSQLQuery createQuery
    String sql = "select ps_id from process_status where lower(ps_name)=lower('" + obj.getPsName() + "')";
    Query q = session.createSQLQuery(sql);
    if (q.list().isEmpty()) {
      super.save(obj);
    } else {
      Short psId = (Short) q.list().get(0);
      obj.setPsId(psId);
    }
  }

  @Override
  public ProcessStatus getByPsId(short id) {
    Session session = getCurrentSession();
    String sql = "select * from process_status where ps_id=" + id + ";";
    Query q = session.createSQLQuery(sql).addEntity(ProcessStatus.class);
    if (!q.list().isEmpty()) {
      return (ProcessStatus) q.list().get(0);
    } else {
      return null;
    }
  }

}
