/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao2;

import com.gwac.model.OtLevel2;
import com.gwac.model2.MinorPlanet;
import com.gwac.util.SearchBoxSphere;
import java.math.BigInteger;
import java.util.ArrayList;
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
@Repository(value="minorPlanetDao")
public class MinorPlanetDaoImpl extends MysqlHibernateDaoImpl<MinorPlanet> implements MinorPlanetDao {

  private static final Log log = LogFactory.getLog(MinorPlanetDaoImpl.class);

  @Override
  public List<MinorPlanet> queryByOt2(float ra, float dec, float searchRadius, float mag, String tableName) {

    double maxRaSpeed = getMaxAbsValue(tableName, "DLON", mag);
    double maxDecSpeed = getMaxAbsValue(tableName, "DLAT", mag);
    if (maxRaSpeed > 20) {
      log.error("maxRaSpeed is " + maxRaSpeed + ",force set to 19.9");
      maxRaSpeed = 19.9;
    }
    if (maxDecSpeed > 20) {
      log.error("maxDecSpeed is " + maxDecSpeed + ",force set to 19.9");
      maxDecSpeed = 19.9;
    }

    SearchBoxSphere sbs1 = new SearchBoxSphere(ra, dec, maxRaSpeed + searchRadius);
    SearchBoxSphere sbs2 = new SearchBoxSphere(ra, dec, maxDecSpeed + searchRadius);
    int tflag1 = sbs1.calSearchBox();
    int tflag2 = sbs2.calSearchBox();
    if (tflag1 != 0 && tflag2 != 0) {
      Session session = getCurrentSession();
      String sql = "select * from " + tableName + " where VMAG<" + mag;
      if (tflag1 == 1) {
        sql += " and LON between " + sbs1.getMinRa() + " and " + sbs1.getMaxRa() + " ";
      } else {
        sql += " and ( LON > " + sbs1.getMinRa() + " or LON <" + sbs1.getMaxRa() + ") ";
      }
      if (tflag2 == 1) {
        sql += " and LAT between " + sbs2.getMinDec() + " and " + sbs2.getMaxDec() + " ";
      } else {
        sql += " and LAT between " + sbs2.getMinDec() + " and " + sbs2.getMaxDec() + " ";
      }

      Query q = session.createSQLQuery(sql).addEntity(MinorPlanet.class);
      return q.list();
    }
    return new ArrayList();
  }

  public double getMaxAbsValue(String tableName, String name, float maxMag) {

    double val = 19.9;
    Session session = getCurrentSession();
    String sql = "select max(abs(" + name + ")) from " + tableName + " where VMAG<" + maxMag + " and abs(" + name + ")<20;";
    Query q = session.createSQLQuery(sql);
    if (q.list().size() > 0) {
      val = (double) q.list().get(0);
    } else {
      log.error("MinorPlanet table " + tableName + " is empty!");
    }
    return val;
  }

  @Override
  public boolean tableExists(String tableName) {

    String sql = "SELECT count(*) "
            + "FROM information_schema.tables "
            + "WHERE table_schema = 'catalogue' "
            + "AND table_name = '" + tableName + "'"
            + "LIMIT 1;";

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql);
    return ((BigInteger) q.list().get(0)).intValue() > 0;
  }
}
