/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpFitsfile;
import com.gwac.model.Telescope;
import java.math.BigInteger;
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
public class FollowUpFitsfileDaoImpl extends BaseHibernateDaoImpl<FollowUpFitsfile> implements FollowUpFitsfileDao {

  private static final Log log = LogFactory.getLog(FollowUpFitsfileDaoImpl.class);

  @Override
  public void updateIsUpload(String ffName) {

    Session session = getCurrentSession();
    String sql = "update follow_up_fitsfile set is_upload=true where ff_name='" + ffName.trim() + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public List<FollowUpFitsfile> getByOtId(long otId) {

    Session session = getCurrentSession();
    String sql = "select fuf.* "
            + " from follow_up_fitsfile fuf "
            + " inner join follow_up_observation fuo on fuo.fo_id=fuf.fo_id and fuo.ot_id=" + otId + " "
            + " where fuf.is_upload=true;";
    Query q = session.createSQLQuery(sql).addEntity(FollowUpFitsfile.class);

    return q.list();
  }
  

  @Override
  public List<FollowUpFitsfile> getByFoName(String foName) {

    Session session = getCurrentSession();
    String sql = "select fuf.* "
            + " from follow_up_fitsfile fuf "
            + " inner join follow_up_observation fuo on fuo.fo_id=fuf.fo_id and fuo.fo_name='" + foName + "' "
            + " where fuf.is_upload=true;";
    Query q = session.createSQLQuery(sql).addEntity(FollowUpFitsfile.class);

    return q.list();
  }

  /**
   *
   * @param ffName
   * @return
   */
  @Override
  public FollowUpFitsfile getByName(String ffName) {

    Session session = getCurrentSession();
    String sql = "select * from follow_up_fitsfile where ff_name='" + ffName + "'";
    Query q = session.createSQLQuery(sql).addEntity(FollowUpFitsfile.class);

    if (!q.list().isEmpty()) {
      return (FollowUpFitsfile) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public void save(FollowUpFitsfile obj) {
    Session session = getCurrentSession();
    String sql = "select fuf_id from follow_up_fitsfile where ff_name='"
            + obj.getFfName() + "'";
    Query q = session.createSQLQuery(sql);
    if (q.list().isEmpty()) {
      super.save(obj);
    } else {
      BigInteger ffId = (BigInteger) q.list().get(0);
      obj.setFufId(ffId.longValue());
      super.update(obj);
    }
  }
}
