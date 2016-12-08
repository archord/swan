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
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value = "uploadFileRecordDao")
public class UploadFileRecordDaoImpl extends BaseHibernateDaoImpl<UploadFileRecord> implements UploadFileRecordDao {

  private static final Log log = LogFactory.getLog(UploadFileRecordDaoImpl.class);

  /**
   * 删除day天以前的所有数据
   * @param day 
   */
  @Override
  public void removeOldRecordByDay(int day) {
    String sql = "DELETE FROM upload_file_record where upload_date<(CURRENT_TIMESTAMP - interval '" + day + "' day);";
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

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
