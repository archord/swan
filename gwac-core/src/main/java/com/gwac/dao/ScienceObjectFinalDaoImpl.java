/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ScienceObjectFinal;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class ScienceObjectFinalDaoImpl extends BaseHibernateDaoImpl<ScienceObjectFinal> implements ScienceObjectFinalDao {
  
  @Override
  public String getById(long sofId) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT tmp1.*) r))) from("
            + "SELECT * from science_object_final where sof_id=" + sofId
            + " )as tmp1";
    System.out.println(sql);
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public String findRecord(int start, int length, int type) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT tmp1.*) r))) from("
            + "SELECT * FROM science_object_final ";
    
    if(type>0){
      sql = sql + " where gwac_type=" + type;
    }
    
    sql = sql + " ORDER BY sof_id desc OFFSET " + start + " LIMIT " + length + " )as tmp1";

    //log.debug(sql);
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public Long findRecordCount(int type) {

    String sql = "SELECT count(*) FROM science_object_final ";
    
    if(type>0){
      sql = sql + " where gwac_type=" + type;
    }
    
    Query q = this.getCurrentSession().createSQLQuery(sql);
    return ((BigInteger) q.list().get(0)).longValue();
  }

}
