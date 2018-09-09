/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFile2;
import com.gwac.model.FitsFile2Show;
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
@Repository(value = "ff2Dao")
public class FitsFile2DAOImpl extends BaseHibernateDaoImpl<FitsFile2> implements FitsFile2DAO {

  private static final Log log = LogFactory.getLog(FitsFile2DAOImpl.class);
  
  @Override
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM fits_file2 RETURNING * ) INSERT INTO fits_file2_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public FitsFile2 getByOt2ForCut(long ot2Id, int ffNum) {

    Session session = getCurrentSession();
    String sql = "select ff2.* "
            + "from fits_file2 ff2 "
            + "INNER JOIN ot_level2 ot2 on ot2.dpm_id=ff2.cam_id and ot2.sky_id=ff2.sky_id and ff2.gen_time+ INTERVAL '1 minute'>=ot2.found_time_utc "
            + "WHERE ot2.ot_id=" + ot2Id + " and ff2.ff_number=" + ffNum + " "
            + "ORDER BY ff_number";
    Query q = session.createSQLQuery(sql).addEntity(FitsFile2.class);

    if (!q.list().isEmpty()) {
      return (FitsFile2) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public List<FitsFile2> getByOt2ForCut(long ot2Id) {

    Session session = getCurrentSession();
    String sql = "select ff2.* "
            + "from fits_file2 ff2 "
            + "INNER JOIN ot_level2 ot2 on ot2.dpm_id=ff2.cam_id and ot2.sky_id=ff2.sky_id and ff2.gen_time+ INTERVAL '1 minute'>=ot2.found_time_utc and ff2.ff_number<ot2.first_ff_number+3 and ff2.ff_number>ot2.first_ff_number-2 "
            + "where ot2.ot_id=" + ot2Id + " "
            + "ORDER BY ff_number";
    Query q = session.createSQLQuery(sql).addEntity(FitsFile2.class);

    return q.list();
  }

  @Override
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
    String sql = "select ff2.*, mt.group_id group_name, mt.unit_id unit_name, cm.name cam_name, os.grid_id grid_name, os.sky_name field_name "
            + "from fits_file2 ff2 "
            + "INNER JOIN mount mt ON mt.mount_id=ff2.mount_id "
            + "INNER JOIN camera cm ON cm.camera_id=ff2.cam_id "
            + "INNER JOIN observation_sky os ON os.sky_id=ff2.sky_id "
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
