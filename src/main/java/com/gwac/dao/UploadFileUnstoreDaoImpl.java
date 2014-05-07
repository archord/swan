/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.UploadFileUnstore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class UploadFileUnstoreDaoImpl extends BaseHibernateDaoImpl<UploadFileUnstore> implements UploadFileUnstoreDao {

  private static final Log log = LogFactory.getLog(UploadFileUnstoreDaoImpl.class);

  @Override
  public void save(UploadFileUnstore obj) {
    Session session = getCurrentSession();
    String sql = "select * from upload_file_unstore where file_name='"
            + obj.getFileName()
            + "' and store_path='"
            + obj.getStorePath() + "'";
    Query q = session.createSQLQuery(sql);
    if (q.list().isEmpty()) {
      super.save(obj);
    } else {
      log.warn("file " + obj.getFileName() + " already exist!");
    }
  }
}
