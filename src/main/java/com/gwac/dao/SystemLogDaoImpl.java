/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.SystemLog;
import org.hibernate.Query;

/**
 *
 * @author xy
 */
public class SystemLogDaoImpl extends BaseHibernateDaoImpl<SystemLog> implements SystemLogDao {

  @Override
  public String findRecord(int start, int length) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT log_id, log_type, log_code, log_date, log_content) r))) "
            + "from(SELECT sl.* FROM system_log sl ORDER BY log_date desc OFFSET " + start + " LIMIT " + length + " )as tmp1";

    System.out.println(sql);
    
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
}
