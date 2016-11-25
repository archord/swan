/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;


import com.gwac.model.Telescope;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="telescopeDAO")
public class TelescopeDAOImpl extends BaseHibernateDaoImpl<Telescope> implements TelescopeDAO {

//  public Number count() {
//    String sql = "select count(*) from data_process_machine;";
//    Session curSession = getCurrentSession();
//    Query query = curSession.createSQLQuery(sql);
//    return ((Number) query.uniqueResult()).intValue();
//  }
}
