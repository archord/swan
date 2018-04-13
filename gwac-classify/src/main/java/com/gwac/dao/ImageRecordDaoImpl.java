/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ImageRecord;
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
public class ImageRecordDaoImpl extends BaseHibernateDaoImpl<ImageRecord> implements ImageRecordDao {

  private static final Log log = LogFactory.getLog(ImageRecordDaoImpl.class);

  @Override
  public String getRecords(int dsId) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT img_id, img_name, img_type, ds_path, ds_dir_name) r))) "
            + "FROM( "
            + "SELECT img.img_id, img.img_name, img.img_type, ds.ds_path, ds.ds_dir_name "
            + "FROM image_record img "
            + "inner join data_set ds on ds.ds_id=img.ds_id "
            + "where img.img_type=-1 and img.ds_id=" + dsId + " "
            + ")as moor ";

    log.debug(sql);
    Query q = session.createSQLQuery(sql);

    String rst = "";
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public void updateType(int imgId, int type) {
    Session session = getCurrentSession();
    String sql = "update image_record set img_type=" + type + " where img_id=" + imgId;

    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }
}
