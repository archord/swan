/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.TimingTaskLog;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class TimingTaskLogDaoImpl extends BaseHibernateDaoImpl<TimingTaskLog> implements TimingTaskLogDao {
  
  @Override
  public String findRecord(int start, int length) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT muf_id, name, ui_id, status, comments, time) r))) "
            + "from(select * from manual_upload_file ORDER BY time desc OFFSET " 
            + start + " LIMIT " + length + " )as tmp1";
    
    //log.debug(sql);
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
}
