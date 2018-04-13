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
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT ds_id, ds_path, ds_dir_name, ds_img_num, left_img_num) r))) "
            + "FROM( "
            + "SELECT ds.ds_id, ds.ds_path, ds.ds_dir_name, ds.ds_img_num,  count(img.img_id) left_img_num "
            + "FROM data_set ds "
            + "left join image_record img on ds.ds_id=img.ds_id and img.img_type>-1"
            + "where ds.ds_img_num>0 and ds.dv_id="+dvId +" and ds.ds_default_type="+defaultType+" "
            + "group by ds.ds_id "
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
