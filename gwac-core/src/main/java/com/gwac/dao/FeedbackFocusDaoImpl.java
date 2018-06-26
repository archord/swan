/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FeedbackFocus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class FeedbackFocusDaoImpl extends BaseHibernateDaoImpl<FeedbackFocus> implements FeedbackFocusDao {

  private static final Log log = LogFactory.getLog(FeedbackFocusDaoImpl.class);
  
  @Override
  public String getRecords(String camera, int days) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT cam_name, focus, recv_time_utc) r))) "
            + "FROM( "
            + "SELECT cam.name as cam_name, fbf.focus, fbf.recv_time_utc "
            + "FROM feedback_focus fbf "
            + "inner join camera cam on cam.camera_id=fbf.camera_id "
            + "where fbf.recv_time_utc>(LOCALTIMESTAMP-interval '" + days + " day') and cam.name='"+camera+"' "
            + "order by fbf.recv_time_utc)as moor ";

    log.debug(sql);
    Query q = session.createSQLQuery(sql);

    String rst = "";
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
  
  @Override
  public FeedbackFocus getByFbfId( Long fbfId) {
    Session session = getCurrentSession();
    String sql = "select * from feedback_focus where fbf_id=" + fbfId + "";
    Query q = session.createSQLQuery(sql).addEntity(FeedbackFocus.class);

    if (!q.list().isEmpty()) {
      return (FeedbackFocus) q.list().get(0);
    } else {
      return null;
    }
  }
  
}
