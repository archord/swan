/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtBase;
import java.math.BigInteger;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtBaseDaoImpl extends BaseHibernateDaoImpl<OtBase> implements OtBaseDao {

  private static final Log log = LogFactory.getLog(OtBaseDaoImpl.class);

  @Override
  public OtBase getOtBaseByName(String otName) {
    Session session = getCurrentSession();
    String sql = "select * from ot_base where name='" + otName + "';";
    Query q = session.createSQLQuery(sql).addEntity(OtBase.class);
    if (!q.list().isEmpty()) {
      return (OtBase) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public Boolean exist(OtBase obj) {
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select ot_id from ot_base where identify='"
            + obj.getIdentify()
            + "' and abs(xtemp-" + obj.getXtemp() + ")<2 "
            + " and abs(ytemp-" + obj.getYtemp() + ")<2 ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger otId = (BigInteger) q.list().get(0);
      obj.setOtId(otId.longValue());
      flag = true;
    }
    return flag;
  }

  public List<OtBase> queryOtBase(String startDate, String endDate, String tsp, float xtemp, float ytemp, float radius, int start, int resultSize) {

    int parNum = 0;
    if (!startDate.isEmpty()) {
      parNum++;
    }
    if (!endDate.isEmpty()) {
      parNum++;
    }
    if (!tsp.equalsIgnoreCase("all")) {
      parNum++;
    }
    if (xtemp != 0.0) {
      parNum++;
    }
    if (ytemp != 0.0) {
      parNum++;
    }
    if(radius < 2.0){
      radius = 2;
    }

    String sql = "select * from ot_base ";

    if (parNum == 1) {
      if (!startDate.isEmpty()) {
        sql += "where found_time_utc>'" + startDate + " 00:00:00' ";
      } else if (!endDate.isEmpty()) {
        sql += "where found_time_utc<'" + endDate + " 23:59:59' ";
      } else if (!tsp.equalsIgnoreCase("all")) {
        sql += "where dpm_name='" + tsp + "' ";
      } else if (xtemp == 0.0) {
        sql += "where abs(xtemp-" + xtemp + ")<" + radius;
      } else if (ytemp == 0.0) {
        sql += "where abs(ytemp-" + ytemp + ")<" + radius;
      }
    } else if (parNum >= 2){
      int tParNum = 1;
      sql += "where ";
      if (!startDate.isEmpty()) {
        sql += "found_time_utc>'" + startDate + " 00:00:00' ";
        if (tParNum < parNum) {
          sql += "and ";
          tParNum++;
        }
      }
      if (!endDate.isEmpty()) {
        sql += "found_time_utc<'" + endDate + " 23:59:59' ";
        if (tParNum < parNum) {
          sql += "and ";
          tParNum++;
        }
      }
      if (!tsp.equalsIgnoreCase("all")) {
        sql += "dpm_name='" + tsp + "' ";
        if (tParNum < parNum) {
          sql += "and ";
          tParNum++;
        }
      }
      if (xtemp != 0.0) {
        sql += "abs(xtemp-" + xtemp + ")<" + radius;
        if (tParNum < parNum) {
          sql += "and ";
          tParNum++;
        }
      }
      if (ytemp != 0.0) {
        sql += "abs(ytemp-" + ytemp + ")<" + radius;
        if (tParNum < parNum) {
          sql += "and ";
          tParNum++;
        }
      }
    }
    sql += "order by dpm_name, found_time_utc";

    Session session = getCurrentSession();
//    sql = "select * from ot_base "
//            + "where found_time_utc>'" + startDate + " 00:00:00' "
//            + "and found_time_utc<'" + endDate + " 23:59:59' "
//            + "and dpm_name='" + tsp + "' "
//            + "and abs(xtemp-" + xtemp + ")<" + radius + " "
//            + "and abs(ytemp-" + ytemp + ")<" + radius + " "
//            + "order by found_time_utc";
    Query q = session.createSQLQuery(sql).addEntity(OtBase.class);
    q.setFirstResult(start);
    q.setMaxResults(resultSize);
    return q.list();
  }
}
