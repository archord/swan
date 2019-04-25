/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossRecord;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class CrossRecordDaoImpl extends BaseHibernateDaoImpl<CrossRecord> implements CrossRecordDao {

  private static final Log log = LogFactory.getLog(CrossRecordDaoImpl.class);
  
  @Override
  public List<CrossRecord> matchLatestN(CrossRecord obj, float errorBox, int n) {
    Session session = getCurrentSession();
    String sql = "select * from cross_record "
	    + " where ff_number>" + (obj.getFfNumber() - n)
	    + " and co_id=0"
	    + " and ct_id=" + obj.getCtId()
	    + " and sqrt(power(x_temp-" + obj.getXTemp() + ", 2)+power(y_temp-" + obj.getYTemp() + ", 2))<" + errorBox + " "
	    + " order by ff_number asc";
    Query q = session.createSQLQuery(sql).addEntity(CrossRecord.class);
    return q.list();
  }

}
