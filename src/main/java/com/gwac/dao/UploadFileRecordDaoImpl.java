/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.UploadFileRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class UploadFileRecordDaoImpl extends BaseHibernateDaoImpl<UploadFileRecord> implements UploadFileRecordDao {

  private static final Log log = LogFactory.getLog(UploadFileRecordDaoImpl.class);

  @Override
  public void save(UploadFileRecord obj) {
    Session session = getCurrentSession();
    //createSQLQuery createQuery
    String sql = "select * from upload_file_record where file_name='"
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
