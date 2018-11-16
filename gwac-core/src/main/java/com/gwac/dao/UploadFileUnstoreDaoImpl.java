/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.UploadFileUnstore;
import java.util.Date;
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
@Repository(value="uploadFileUnstoreDao")
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

  @Override
  public List<UploadFileUnstore> getOTLevel1File() {

    String sql = "WITH moved_rows AS ( DELETE FROM upload_file_unstore where file_type='1' and upload_success=true RETURNING * ) SELECT * FROM moved_rows;";
//    String sql = "select * from upload_file_unstore where file_type='1' and upload_success=true  order by ufu_id;";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(UploadFileUnstore.class);
    return q.list();
  }

  @Override
  public List<UploadFileUnstore> getSubOTLevel1File() {

//    String sql = "WITH moved_rows AS ( DELETE FROM upload_file_unstore where file_type='1' and upload_success=true RETURNING * ) SELECT * FROM moved_rows;";
    String sql = "select * from upload_file_unstore where file_type='8' and upload_success=true  order by ufu_id;";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(UploadFileUnstore.class);
    return q.list();
  }

  @Override
  public List<UploadFileUnstore> getImgStatusFile() {

    String sql = "WITH moved_rows AS ( DELETE FROM upload_file_unstore where file_type='7' and upload_success=true RETURNING * ) SELECT * FROM moved_rows;";
//    String sql = "select * FROM upload_file_unstore where file_type='7' and upload_success=true";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(UploadFileUnstore.class);
    return q.list();
  }

  @Override
  public List<UploadFileUnstore> getVarStarListFile() {

    String sql = "WITH moved_rows AS "
            + "( DELETE FROM upload_file_unstore where file_type='6' and upload_success=true RETURNING * ) "
            + "SELECT * FROM moved_rows order by upload_date;";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(UploadFileUnstore.class);
    return q.list();
  }

  @Override
  public void updateProcessDoneTime(long ufuId) {
    Session session = getCurrentSession();
//    String sql = "update upload_file_unstore set process_done_time=current_timestamp where ufu_id=" + ufuId;
    String sql = "update upload_file_unstore set process_done_time=? where ufu_id=" + ufuId;
    Query q = session.createSQLQuery(sql);
    q.setTimestamp(0, new Date());
    q.executeUpdate();
  }

  @Override
  public List<UploadFileUnstore> getFollowUpFile() {

    String sql = "WITH moved_rows AS "
            + "( DELETE FROM upload_file_unstore where file_type='9' and upload_success=true RETURNING * ) "
            + "SELECT * FROM moved_rows order by upload_date;";
//    String sql = "WITH moved_rows AS "
//            + "( update upload_file_unstore set upload_success=false where file_type='9' and upload_success=true RETURNING * ) "
//            + "SELECT * FROM moved_rows order by upload_date;";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(UploadFileUnstore.class);
    return q.list();
  }

  @Override
  public void removeAll() {
    Session session = getCurrentSession();
    String sql = "delete from upload_file_unstore";
    session.createSQLQuery(sql).executeUpdate();
  }
}
