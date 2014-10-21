/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFile;
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
public class FitsFileDAOImpl extends BaseHibernateDaoImpl<FitsFile> implements FitsFileDAO {

  private static final Log log = LogFactory.getLog(FitsFileDAOImpl.class);

  public FitsFile getByName(String ffName) {

    Session session = getCurrentSession();
    String sql = "select * from fits_file where file_name='" + ffName + "'";
    Query q = session.createSQLQuery(sql).addEntity(FitsFile.class);

    if (!q.list().isEmpty()) {
      return (FitsFile) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public void save(FitsFile obj) {
    Session session = getCurrentSession();
    //createSQLQuery createQuery
    String sql = "select ff_id from fits_file where file_name='"
            + obj.getFileName() + "'";
    Query q = session.createSQLQuery(sql);
    if (q.list().isEmpty()) {
      super.save(obj);
    } else {
      BigInteger ffId = (BigInteger) q.list().get(0);
      obj.setFfId(ffId.longValue());
      log.debug("source fits file " + obj.getFileName() + " already exist!");
    }
  }
}
