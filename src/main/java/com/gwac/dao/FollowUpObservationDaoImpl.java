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

//  public void save(FollowUpObservation fo) {
//
//    Session session = getCurrentSession();
//    String sql = "select * from follow_up_observation where fo_name='" + fo.getFoName() + "' ";
//    Query q = session.createSQLQuery(sql).addEntity(FollowUpObservation.class);
//    if (!q.list().isEmpty()) {
//      FollowUpObservation tfo = (FollowUpObservation) q.list().get(0);
//      fo.setFoId(tfo.getFoId());
//    } else {
//      super.save(fo);
//    }
//  }
}
