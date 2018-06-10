/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.TimingTask;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class TimingTaskDaoImpl extends BaseHibernateDaoImpl<TimingTask> implements TimingTaskDao {
  
  @Override
  public String findRecord(int start, int length) {
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (select tmp1.*) r))) "
            + "from(select tt.* from timing_task tt "
            + " ORDER BY tt.tt_id desc OFFSET " 
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
