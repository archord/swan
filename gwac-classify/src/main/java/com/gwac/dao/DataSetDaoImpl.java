/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.DataSet;
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
public class DataSetDaoImpl extends BaseHibernateDaoImpl<DataSet> implements DataSetDao {

  private static final Log log = LogFactory.getLog(DataSetDaoImpl.class);

  @Override
  public String getRecords(int dvId, int defaultType) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT ds_id, ds_path, ds_dir_name) r))) "
            + "FROM( "
            + "SELECT ds_id, ds_path, ds_dir_name "
            + "FROM data_set ds "
            + "where ds.dv_id="+dvId +" and ds.ds_default_type="+defaultType+" "
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
