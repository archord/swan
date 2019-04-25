/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossObject;
import com.gwac.model.CrossRecord;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
import java.util.Calendar;
import java.util.Date;
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
public class CrossRecordDaoImpl extends BaseHibernateDaoImpl<CrossRecord> implements CrossRecordDao {

  private static final Log log = LogFactory.getLog(CrossRecordDaoImpl.class);
  
  @Override
  public String getCutImageByOtId(long otId, Boolean queryHis) {

    String sql1 = "SELECT text(JSON_AGG((SELECT r FROM (SELECT stamp_name, stamp_path) r)))  " +
      "FROM(SELECT stamp_name, stamp_path FROM cross_record where co_id="+ otId+" order by cr_id)as obj";

    String sql2 = "SELECT text(JSON_AGG((SELECT r FROM (SELECT stamp_name, stamp_path) r)))  " +
      "FROM(SELECT stamp_name, stamp_path FROM cross_record_his where co_id="+ otId+" order by cr_id)as obj";

    String unionSql = "";
    if (queryHis) {
      unionSql = "(" + sql1 + ") union (" + sql2 + ")";
    } else {
      unionSql = sql1 ;
    }

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql);
    return (String) q.list().get(0);
  }
  
  @Override
  public List<CrossRecord> matchLatestN(CrossRecord obj, float errorBox, int n) {
    Session session = getCurrentSession();
    String sql = "select * from cross_record "
	    + " where ff_number>" + (obj.getFfNumber() - n)
	    + " and co_id=0"
	    + " and ct_id=" + obj.getCtId()
	    + " and sqrt(power(x_temp-" + obj.getXTemp() + ", 2)+power(y_temp-" + obj.getYTemp() + ", 2))<" + errorBox + " "
	    + " order by ff_number asc";
    Query q = session.createSQLQuery(sql).addEntity(CrossRecord.class);
    return q.list();
  }
  
  @Override
  public String getOtOpticalVaration(CrossObject ot2, Boolean queryHis) {
    Session session = getCurrentSession();
    String sql1 = "select oor.date_utc, oor.mag , oor.x, oor.y, oor.x_temp, oor.y_temp, oor.cr_id  from cross_record oor  where oor.co_id=" + ot2.getCoId();
    String sql2 = "select oorh.date_utc, oorh.mag , oorh.x, oorh.y, oorh.x_temp, oorh.y_temp, oorh.cr_id  from cross_record_his oorh  where oorh.co_id=" + ot2.getCoId();

    String unionSql = "";
    if (queryHis) {
      unionSql = sql1 + " union " + sql2 + " order by date_ut asc";
    } else {
      unionSql = sql1 + " order by date_utc asc";
    }

    Query q = session.createSQLQuery(unionSql);
    Iterator itor = q.list().iterator();

//    Date baseDate = CommonFunction.stringToDate("2015-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss.SSS");
    Date baseDate = ot2.getFoundTimeUtc();
    Calendar cal = Calendar.getInstance();
    cal.setTime(baseDate);
    /**
     * 天 86400000.0 小时 3600000.0 分钟 60000.0
     */
    double baseDay = cal.getTimeInMillis() / 60000.0;
//    double baseDay = CommonFunction.dateToJulian(baseDate)*1440;

    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    StringBuilder sb3 = new StringBuilder();
    int i = 0;
    float x0 = 0, y0 = 0, xn, yn;
    float xt0 = 0, yt0 = 0, xtn, ytn;
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      try {
	cal.setTime((Date) row[0]);
	/**
	 * 天 86400000.0 小时 3600000.0 分钟 60000.0
	 */
	double now = cal.getTimeInMillis() / 60000.0;
//        double now = CommonFunction.dateToJulian((Date) row[0])*1440;
	sb.append("[");
	sb.append(now - baseDay);
	sb.append(",");
	sb.append(row[1]);
	sb.append("],");

	//计算后面的记录与首条记录位置偏差
	if (i == 0) {
	  x0 = (Float) row[2];
	  y0 = (Float) row[3];
	  xt0 = (Float) row[4];
	  yt0 = (Float) row[5];
	  i++;
	}
	xn = (Float) row[2];
	yn = (Float) row[3];
	sb2.append("[");
	sb2.append(xn - x0);
	sb2.append(",");
	sb2.append(yn - y0);
	sb2.append("],");

	xtn = (Float) row[4];
	ytn = (Float) row[5];
	sb3.append("[");
	sb3.append(xtn - xt0);
	sb3.append(",");
	sb3.append(ytn - yt0);
	sb3.append("],");
      } catch (Exception e) {
	e.printStackTrace();
      }
    }
    String tStr = sb.toString();
    String tStr2 = sb2.toString();
    String tStr3 = sb3.toString();
    if (tStr.isEmpty()) {
      tStr = "[]";
    } else {
      tStr = "[" + tStr + "]";
    }
    if (tStr2.isEmpty()) {
      tStr2 = "[]";
    } else {
      tStr2 = "[" + tStr2 + "]";
    }
    if (tStr3.isEmpty()) {
      tStr3 = "[]";
    } else {
      tStr3 = "[" + tStr3 + "]";
    }
    return tStr + "=" + tStr2 + "=" + tStr3;
  }

}
