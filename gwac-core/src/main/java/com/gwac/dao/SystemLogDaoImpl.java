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
@Repository(value = "sysLogDao")
public class SystemLogDaoImpl extends BaseHibernateDaoImpl<SystemLog> implements SystemLogDao {

  private static final Log log = LogFactory.getLog(SystemLogDaoImpl.class);

  @Override
  public String getLogCodes() {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT log_code) r))) "
            + "from( "
            + "select log_code "
            + "from system_log "
            + "GROUP BY log_code "
            + "ORDER BY log_code "
            + ")as tmp1";

    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public String getMsgIPs() {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT msg_ip) r))) "
            + "from( "
            + "select msg_ip "
            + "from system_log "
            + "GROUP BY msg_ip "
            + "ORDER BY msg_ip "
            + ")as tmp1";

    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public void removeOldRecord(int day) {
    String sql = "DELETE FROM system_log WHERE log_date<(LOCALTIMESTAMP-interval '" + day + " day')";
    log.debug(sql);
    this.getCurrentSession().createSQLQuery(sql).executeUpdate();
  }

  /**
   *
   * @param day
   * @param codes values example "2012,400"
   */
  @Override
  public void removeOldRecord(int day, String codes) {
    String sql = "DELETE FROM system_log WHERE log_date<(LOCALTIMESTAMP-interval '" + day + " day') and log_code in (" + codes + ")";
    log.debug(sql);
    this.getCurrentSession().createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String findRecord(int start, int length, String dateStart, String dateEnd, String logCode, String msgIp) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT log_id, log_type, log_code, log_date, log_content, msg_source, msg_ip) r))) "
            + "from(SELECT sl.* FROM system_log sl where 1=1 ";
    if(dateStart!=null&&!dateStart.isEmpty()){
      sql += "and log_date>='" + dateStart + "' ";
    }
    if(dateEnd!=null&&!dateEnd.isEmpty()){
      sql += "and log_date<='" + dateEnd + "' ";
    }
    if(logCode!=null&&!logCode.isEmpty()){
      sql += "and log_code in (" + logCode + ") ";
    }
    if(msgIp!=null&&!msgIp.isEmpty()){
      sql += "and msg_ip='" + msgIp + "' ";
    }
    sql += "ORDER BY log_date desc OFFSET " + start + " LIMIT " + length + " )as tmp1";

    //log.debug(sql);
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
}
