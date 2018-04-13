/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataVersion;
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
public class DataVersionDaoImpl extends BaseHibernateDaoImpl<DataVersion> implements DataVersionDao {

  private static final Log log = LogFactory.getLog(DataVersionDaoImpl.class);

  @Override
  public String getRecords() {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT dv_id, dv_name) r))) "
            + "FROM( "
            + "SELECT dv.dv_id, dv.dv_name "
            + "FROM data_version dv "
            + ")as moor ";
    
    log.debug(sql);
    Query q = session.createSQLQuery(sql);
    
    String rst = "";
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
}
