/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFile2;
import java.math.BigInteger;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class FitsFile2DAOImpl extends BaseHibernateDaoImpl<FitsFile2> implements FitsFile2DAO {

  private static final Log log = LogFactory.getLog(FitsFile2DAOImpl.class);

  public boolean exist(String ffName) {

    Session session = getCurrentSession();
    String sql = "select * from fits_file2 where img_name='" + ffName + "'";
    Query q = session.createSQLQuery(sql).addEntity(FitsFile2.class);
    return !q.list().isEmpty();
  }

  @Override
  public FitsFile2 getByName(String ffName) {

    Session session = getCurrentSession();
    String sql = "select * from fits_file2 where img_name='" + ffName + "'";
    Query q = session.createSQLQuery(sql).addEntity(FitsFile2.class);

    if (!q.list().isEmpty()) {
      return (FitsFile2) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public void save(FitsFile2 obj) {
    Session session = getCurrentSession();
    //createSQLQuery createQuery
    String sql = "select ff_id from fits_file2 where img_name='"
            + obj.getImgName() + "'";
    Query q = session.createSQLQuery(sql);
    if (q.list().isEmpty()) {
      super.save(obj);
    } else {
      BigInteger ffId = (BigInteger) q.list().get(0);
      obj.setFfId(ffId.longValue());
//      log.debug("source fits file " + obj.getFileName() + " already exist!");
    }
  }
}
