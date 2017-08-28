/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFile2;
import com.gwac.model.FitsFile2Show;
import java.math.BigInteger;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value = "ff2Dao")
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
  public FitsFile2Show getShowByName(String ffName) {

    Session session = getCurrentSession();
    String sql = "select ff2.*, oi1.obj_name group_name, oi2.obj_name unit_name, oi3.obj_name cam_name, oi4.obj_name grid_name, os.sky_name field_name "
            + "from fits_file2 ff2 "
            + "INNER JOIN object_identity oi1 ON oi1.obj_id=ff2.group_id "
            + "INNER JOIN object_identity oi2 ON oi2.obj_id=ff2.unit_id "
            + "INNER JOIN object_identity oi3 ON oi3.obj_id=ff2.cam_id "
            + "INNER JOIN object_identity oi4 ON oi4.obj_id=ff2.grid_id "
            + "INNER JOIN observation_sky os ON os.sky_id=ff2.field_id "
            + "WHERE ff2.img_name=?";
    Query q = session.createSQLQuery(sql).addEntity(FitsFile2Show.class);
    q.setString(0, ffName);

    if (!q.list().isEmpty()) {
      return (FitsFile2Show) q.list().get(0);
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
