/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model3.UsnoCatalog;
import com.gwac.util.SearchBoxSphere;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="usnoCatalogDao")
public class UsnoCatalogDaoImpl extends MysqlUsnoHibernateDaoImpl<UsnoCatalog> implements UsnoCatalogDao {

  @Override
  public List<UsnoCatalog> queryByOt2(OtLevel2 ot2, float searchRadius, float mag, String tableName) {

    SearchBoxSphere sbs = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from " + tableName + " where abs(Rmag)>0.00001 and Rmag < " + mag + " and ";
      if (tflag == 1) {
        sql += "RAdeg between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "DEdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(RAdeg > " + sbs.getMinRa() + " or RAdeg <" + sbs.getMaxRa() + ") and ";
        sql += "DEdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }

      Query q = session.createSQLQuery(sql).addEntity(UsnoCatalog.class);
      return q.list();
    }
    return new ArrayList();
  }

  @Override
  public boolean tableExists(String tableName) {

    String sql = "SELECT count(*) "
            + "FROM information_schema.tables "
            + "WHERE table_schema = 'nomad_catalogue' "
            + "AND table_name = '" + tableName + "'"
            + "LIMIT 1;";

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql);
    return ((BigInteger) q.list().get(0)).intValue() > 0;
  }
}
