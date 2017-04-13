/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.ObservationSky;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class ObservationSkyDaoImpl extends BaseHibernateDaoImpl<ObservationSky> implements ObservationSkyDao {

  private static final Log log = LogFactory.getLog(ObservationSkyDaoImpl.class);

  @Override
  public List<String> getAllSkyName() {

    String sql = "SELECT sky_name FROM observation_sky order by sky_name asc";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql);
    return q.list();
  }

  @Override
  public ObservationSky getByName(String name, int gridId) {
    Session session = getCurrentSession();
    String sql = "select * from observation_sky where sky_name='" + name + "' and grid_id="+gridId;
    Query q = session.createSQLQuery(sql).addEntity(ObservationSky.class);

    if (!q.list().isEmpty()) {
      return (ObservationSky) q.list().get(0);
    } else {
      ObservationSky sky = new ObservationSky();
      sky.setSkyName(name);
      sky.setGridId(gridId);
      super.save(sky);
      return sky;
    }
  }

  @Override
  public void save(ObservationSky obj) {
    Session session = getCurrentSession();

    String sql = "select sky_id from observation_sky where sky_name='"
            + obj.getSkyName() + "'";
    Query q = session.createSQLQuery(sql);
    if (q.list().isEmpty()) {
      super.save(obj);
    } else {
      Short id = (Short) q.list().get(0);
      obj.setSkyId(id);
    }
  }
}
