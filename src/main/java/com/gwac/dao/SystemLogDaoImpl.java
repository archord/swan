/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.SystemLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="sysLogDao")
public class SystemLogDaoImpl extends BaseHibernateDaoImpl<SystemLog> implements SystemLogDao {
  
  private static final Log log = LogFactory.getLog(SystemLogDaoImpl.class);

  @Override
  public void removeOldRecord(int day) {
    String sql = "DELETE FROM system_log WHERE log_date<(LOCALTIMESTAMP-interval '" + day + " day')";
    log.debug(sql);
    this.getCurrentSession().createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String findRecord(int start, int length) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT log_id, log_type, log_code, log_date, log_content, msg_source, msg_ip) r))) "
            + "from(SELECT sl.* FROM system_log sl ORDER BY log_date desc OFFSET " + start + " LIMIT " + length + " )as tmp1";

    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
}
