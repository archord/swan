/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpObservation;
import com.gwac.model.Telescope;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class FollowUpObservationDaoImpl extends BaseHibernateDaoImpl<FollowUpObservation> implements FollowUpObservationDao {


  @Override
  public FollowUpObservation getByName(String name) {

    Session session = getCurrentSession();
    String sql = "select * from follow_up_observation where fo_name='" + name + "'";
    Query q = session.createSQLQuery(sql).addEntity(FollowUpObservation.class);
    if (!q.list().isEmpty()) {
      return (FollowUpObservation) q.list().get(0);
    } else {
      return null;
    }
  }
}
