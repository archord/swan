/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.SystemLog;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class SystemLogDaoImpl extends BaseHibernateDaoImpl<SystemLog> implements SystemLogDao {
  
  public List<SystemLog> findRecord1(int start, int resultSize, String[] orderNames, int[] sort) {

    String sql = "select * from system_log where  ";
    Query q = getCurrentSession().createQuery(sql);
    q.setFirstResult(start);
    q.setMaxResults(resultSize);
    return q.list();
  }
}
