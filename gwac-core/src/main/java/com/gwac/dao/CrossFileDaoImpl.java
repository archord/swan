/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossFile;
import java.math.BigInteger;
import java.util.Iterator;
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
public class CrossFileDaoImpl extends BaseHibernateDaoImpl<CrossFile> implements CrossFileDao {

  private static final Log log = LogFactory.getLog(CrossFileDaoImpl.class);

  @Override
  public void save(CrossFile obj) {
    Session session = getCurrentSession();
    String sql = "select cf_id from cross_file where file_name='" + obj.getFileName()+ "'";
    Query q = session.createSQLQuery(sql);
    if (q.list().isEmpty()) {
      super.save(obj);
    } else {
      BigInteger ffId = (BigInteger) q.list().get(0);
      obj.setCfId(ffId.longValue());
    }
  }
  
  @Override
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM cross_file RETURNING * ) INSERT INTO cross_file_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public boolean exist(CrossFile obj) {

    Session session = getCurrentSession();
    String sql = "select ff_id from cross_file where file_name='" + obj.getFileName() + "' and ct_id="+obj.getCtId();
    Query q = session.createSQLQuery(sql).addEntity(CrossFile.class);
    return !q.list().isEmpty();
  }

  @Override
  public CrossFile getByName(String ffName) {

    Session session = getCurrentSession();
    String sql = "select * from cross_file where file_name='" + ffName + "'";
    Query q = session.createSQLQuery(sql).addEntity(CrossFile.class);

    if (!q.list().isEmpty()) {
      return (CrossFile) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public CrossFile getByNameHis(String ffName) {

    Session session = getCurrentSession();
    String sql = "select * from cross_file where file_name='" + ffName + "'";
    Query q = session.createSQLQuery(sql).addEntity(CrossFile.class);

    if (!q.list().isEmpty()) {
      return (CrossFile) q.list().get(0);
    } else {
      return null;
    }
  }
}
