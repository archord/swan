/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.CoordinateShow;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class CoordinateShowDaoImpl extends BaseHibernateDaoImpl<CoordinateShow> implements CoordinateShowDao {

  @Override
  public List<CoordinateShow> getCoordinateShow(String sql) {
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(CoordinateShow.class);
    return q.list();
  }

}
