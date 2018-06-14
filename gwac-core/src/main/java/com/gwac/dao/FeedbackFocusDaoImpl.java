/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FeedbackFocus;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class FeedbackFocusDaoImpl extends BaseHibernateDaoImpl<FeedbackFocus> implements FeedbackFocusDao {

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
