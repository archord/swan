/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.SyncFile;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class SyncFileDaoImpl extends BaseHibernateDaoImpl<SyncFile> implements SyncFileDao {

  @Override
  public List<SyncFile> getUnSyncFile() {

    String sql = "WITH updated_rows AS "
            + "( update sync_file set is_sync=true where is_sync=false RETURNING * ) "
            + " select * from updated_rows";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(SyncFile.class);
    return q.list();
  }

  /**
   * 如果fileName不存在，则存储；如果不存在，则更新storeTime=new Date()和isSync=false
   *
   * @param sf
   */
  @Override
  public void save(SyncFile sf) {

    String sql = "select * from sync_file where file_name='" + sf.getFileName() + "'";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(SyncFile.class);
    if (q.list().size() > 0) {
      SyncFile tsf = (SyncFile) q.list().get(0);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setStoreTime(new Date());
      super.update(tsf);
    } else {
      super.save(sf);
    }
  }
}
