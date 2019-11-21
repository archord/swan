/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtHistoryRepeat;
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
@Repository
public class OtHistoryRepeatDaoImpl extends BaseHibernateDaoImpl<OtHistoryRepeat> implements OtHistoryRepeatDao {

  private static final Log log = LogFactory.getLog(OtHistoryRepeatDaoImpl.class);

  @Override
  public List<OtHistoryRepeat> exist(Integer camId, float x, float y) {

    Session session = getCurrentSession();
    String sql = "SELECT * from ot_history_repeat where cam_id=? and x>=? and x<=? and y>=? and y<=?";
    Query q = session.createSQLQuery(sql);
    q.setInteger(0, camId);
    q.setFloat(1, x/4-1);
    q.setFloat(2, x/4+1);
    q.setFloat(3, y/4-1);
    q.setFloat(4, y/4+1);
    return q.list();
  }

}
