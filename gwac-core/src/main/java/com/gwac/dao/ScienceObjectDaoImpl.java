/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ScienceObject;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class ScienceObjectDaoImpl extends BaseHibernateDaoImpl<ScienceObject> implements ScienceObjectDao {

  @Override
  public List<ScienceObject> getByStatus(int status) {
    String sql = "SELECT * from science_object where auto_observation=true and status=" + status;

    Query q = getCurrentSession().createSQLQuery(sql).addEntity(ScienceObject.class);
    return q.list();
  }

  @Override
  public void updateFupCount(long sciObjId) {
    
    //"update science_object as so set fup_count=(select count(*) from follow_up_observation as fuo where fuo.so_id=so.so_id)"
    String sql = "update science_object "
            + "set fup_count=(select count(*) from follow_up_observation  where so_id=" + sciObjId + ") "
            + "where so_id=" + sciObjId + "";

    getCurrentSession().createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String findRecord(int start, int length) {

    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT tmp1.*) r))) from("
            + "SELECT * FROM science_object ORDER BY so_id desc OFFSET " + start + " LIMIT " + length + " )as tmp1";

    //log.debug(sql);
    String rst = "";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public Long findRecordCount() {

    String sql = "SELECT count(*) FROM science_object";
    Query q = this.getCurrentSession().createSQLQuery(sql);
    return ((BigInteger) q.list().get(0)).longValue();
  }

}
