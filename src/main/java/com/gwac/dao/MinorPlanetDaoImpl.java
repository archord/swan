/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model2.MinorPlanet;
import com.gwac.util.SearchBoxSphere;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class MinorPlanetDaoImpl extends MysqlHibernateDaoImpl<MinorPlanet> implements MinorPlanetDao {

  @Override
  public List<MinorPlanet> queryByOt2(OtLevel2 ot2, float searchRadius, float mag, String tableName) {
    
    double maxRaSpeed = getMaxAbsValue(tableName, "DLON");
    double maxDecSpeed = getMaxAbsValue(tableName, "DLAT");

    SearchBoxSphere sbs1 = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), maxRaSpeed);
    SearchBoxSphere sbs2 = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), maxDecSpeed);
    int tflag1 = sbs1.calSearchBox();
    int tflag2 = sbs2.calSearchBox();
    if (tflag1 != 0 && tflag2 != 0) {
      Session session = getCurrentSession();
      String sql = "select * from " + tableName + " where ";
      if (tflag1 == 1) {
        sql += "LON between " + sbs1.getMinRa() + " and " + sbs1.getMaxRa() + " and ";
      } else {
        sql += "(LON > " + sbs1.getMinRa() + " or LON <" + sbs1.getMaxRa() + ") and ";
      }
      if (tflag2 == 1) {
        sql += "LAT between " + sbs2.getMinDec() + " and " + sbs2.getMaxDec() + " ";
      } else {
        sql += "LAT between " + sbs2.getMinDec() + " and " + sbs2.getMaxDec() + " ";
      }

      Query q = session.createSQLQuery(sql).addEntity(MinorPlanet.class);
      return q.list();
    }
    return null;
  }

  public Double getMaxAbsValue(String tableName, String name) {

    Session session = getCurrentSession();
    String sql = "select max(abs(" + name + ")) from " + tableName + ";";
    Query q = session.createSQLQuery(sql);
    return (Double) q.list().get(0);
  }
}
