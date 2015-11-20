/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2QueryParameter;
import com.gwac.util.CommonFunction;
import com.gwac.util.SearchBoxSphere;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtLevel2DaoImpl extends BaseHibernateDaoImpl<OtLevel2> implements OtLevel2Dao {

  private static final Log log = LogFactory.getLog(OtLevel2DaoImpl.class);
  
  @Override
  public List<OtLevel2> searchOT2His(OtLevel2 ot2, float searchRadius, float mag){
    
    SearchBoxSphere sbs = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from ot_level2_his where ot_id!=" + ot2.getOtId()+" and ";
      if (tflag == 1) {
        sql += "ra between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(ra > " + sbs.getMinRa() + " or ra <" + sbs.getMaxRa() + ") and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }

      Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
      return q.list();
    }
    return new ArrayList();
  }

  public void updateIsMatch(OtLevel2 ot2) {

    String sql = "update ot_level2 set is_match=" + ot2.getIsMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  public List<OtLevel2> getUnMatched() {

    String sql = "WITH updated_rows AS "
            + "( update ot_level2 set is_match=1 where is_match=0 RETURNING * ) "
            + " select * from updated_rows";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM ot_level2 RETURNING * ) INSERT INTO ot_level2_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  public void updateAllFileCuttedById(long id) {

    Session session = getCurrentSession();
    String sql = "update ot_level2 set all_file_cutted=true where ot_id=" + id;
    session.createSQLQuery(sql).executeUpdate();
  }

  public List<OtLevel2> getMissedFFCLv2OT() {

    Session session = getCurrentSession();
    String sql = "select * from ot_level2 where all_file_cutted=false order by ot_id";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<OtLevel2> getCurOccurLv2OT() {
    Session session = getCurrentSession();
    String sql = "select ol2.* "
            + "from ot_level2 ol2 "
            + "inner join data_process_machine dpm on ol2.dpm_id = dpm.dpm_id and ol2.last_ff_number=dpm.cur_process_number "
            + "where ol2.first_n_mark=false and ol2.is_match!=2 and ol2.data_produce_method='1'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<OtLevel2> getNCurOccurLv2OT() {
    Session session = getCurrentSession();
    String sql = "select ol2.* "
            + "from ot_level2 ol2 "
            + "inner join data_process_machine dpm on ol2.dpm_id = dpm.dpm_id and ol2.last_ff_number!=dpm.cur_process_number "
            + "where ol2.first_n_mark=false and ol2.is_match!=2 and ol2.data_produce_method='1'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<OtLevel2> getMatchedLv2OT() {
    Session session = getCurrentSession();
    String sql = "select ol2.* "
            + "from ot_level2 ol2 "
            + "where ol2.first_n_mark=false and ol2.is_match=2 and ol2.data_produce_method='1'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<OtLevel2> getMatchedLv2OTByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "select ol2.* from ot_level2_his ol2 where ol2.is_match=2 and ol2.date_str='" + dateStr + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<OtLevel2> getCurOccurLv2OTByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "select ol2.* "
            + "from ot_level2_his ol2 "
            + "inner join data_process_machine dpm on ol2.dpm_id = dpm.dpm_id and ol2.last_ff_number=dpm.cur_process_number "
            + "where ol2.first_n_mark=false and ol2.data_produce_method='1' and ol2.date_str='" + dateStr + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  public List<OtLevel2> getNCurOccurLv2OTByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "select ol2.* "
            + "from ot_level2_his ol2 "
            + "inner join data_process_machine dpm on ol2.dpm_id = dpm.dpm_id and ol2.last_ff_number!=dpm.cur_process_number "
            + "where ol2.first_n_mark=false and ol2.date_str='" + dateStr + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public OtLevel2 getOtLevel2ByName(String otName, Boolean queryHis) {

    String sql1 = "select * from ot_level2 where name='" + otName + "'";
    String sql2 = "select * from ot_level2_his where name='" + otName + "'";

    String unionSql = "";
    if (queryHis) {
      unionSql = "(" + sql1 + ") union (" + sql2 + ")";
    } else {
      unionSql = sql1;
    }

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(OtLevel2.class);
    if (!q.list().isEmpty()) {
      return (OtLevel2) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public OtLevel2 getOtLevel2ByNameFromHis(String otName) {
    Session session = getCurrentSession();
    String sql = "select * from ot_level2_his where name='" + otName + "';";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    if (!q.list().isEmpty()) {
      return (OtLevel2) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public Boolean exist(OtLevel2 obj, float errorBox) {
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select ot_id from ot_level2 where identify='"
            + obj.getIdentify()
            + " and sqrt(power(xtemp-" + obj.getXtemp() + ", 2)+power(ytemp-" + obj.getYtemp() + ", 2))<" + errorBox + " ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger otId = (BigInteger) q.list().get(0);
      obj.setOtId(otId.longValue());
      flag = true;
    }
    return flag;
  }

  @Override
  public OtLevel2 existInAll(OtLevel2 obj, float errorBox) {
    Boolean flag = false;
    Session session = getCurrentSession();

    String sql = "select * from ot_level2 "
            + " where dpm_id=" + obj.getDpmId()
            + " and sky_id=" + obj.getSkyId()
            + " and date_str='" + obj.getDateStr() + "'"
            + " and data_produce_method='" + obj.getDataProduceMethod() + "'"
            + " and sqrt(power(xtemp-" + obj.getXtemp() + ", 2)+power(ytemp-" + obj.getYtemp() + ", 2))<" + errorBox + " ";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    if (!q.list().isEmpty()) {
      return (OtLevel2) q.list().get(0);
    }
    return null;
  }

  public OtLevel2 existInLatestN(OtLevel2 obj, float errorBox, int n) {
    Boolean flag = false;
    Session session = getCurrentSession();

    String sql = "select * from ot_level2 "
            + " where last_ff_number>" + (obj.getLastFfNumber() - n)
            + " and dpm_id=" + obj.getDpmId()
            //            + " and date_str='"+obj.getDateStr() + "'"
            + " and sqrt(power(xtemp-" + obj.getXtemp() + ", 2)+power(ytemp-" + obj.getYtemp() + ", 2))<" + errorBox + " ";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    if (!q.list().isEmpty()) {
      return (OtLevel2) q.list().get(0);
    }
    return null;
  }

  public List<OtLevel2> findRecord1(int start, int resultSize, String[] orderNames, int[] sort) {

    String sql = "select ol2 from OtLevel2 ol2 join fetch ol2.otType ";
    if (orderNames != null && sort != null && orderNames.length > 0 && sort.length > 0) {
      sql += "order by ";
      if (orderNames.length == sort.length) {
        for (int i = 0; i < orderNames.length; i++) {
          if (sort[i] == SORT_ASC) {
            sql += "ol2." + orderNames[i] + " asc ";
          } else {
            sql += "ol2." + orderNames[i] + " desc ";
          }
        }
      } else {
        for (String ord : orderNames) {
          sql += "ol2." + ord + " asc ";
        }
      }
    }
    Query q = getCurrentSession().createQuery(sql);
    q.setFirstResult(start);
    q.setMaxResults(resultSize);
    return q.list();
  }

  @Override
  public List<OtLevel2> queryOtLevel2(OtLevel2QueryParameter ot2qp) {

    String sqlprefix1 = "select * from ot_level2 where 1=1 ";
    String sqlprefix2 = "select * from ot_level2_his where 1=1 ";
    String sql = "";

    double cosd = Math.cos(ot2qp.getDec() * 0.0174532925);

    if (!ot2qp.getProcessType().isEmpty()) {
      sql += " and data_produce_method='" + ot2qp.getProcessType() + "' ";
    }
    if (!ot2qp.getStartDate().isEmpty()) {
      sql += " and found_time_utc>'" + ot2qp.getStartDate() + " 00:00:00' ";
    }
    if (!ot2qp.getEndDate().isEmpty()) {
      sql += " and found_time_utc<'" + ot2qp.getEndDate() + " 23:59:59' ";
    }
    if (!ot2qp.getTelscope().equalsIgnoreCase("all")) {
      sql += " and dpm_id='" + ot2qp.getTelscope() + "' ";
    }
    if (Math.abs(ot2qp.getPlaneRadius()) > CommonFunction.MINFLOAT) {
      sql += " and abs(xtemp-" + ot2qp.getXtemp() + ")<" + ot2qp.getPlaneRadius();
      sql += " and abs(ytemp-" + ot2qp.getYtemp() + ")<" + ot2qp.getPlaneRadius();
    } else if (Math.abs(ot2qp.getSphereRadius()) > CommonFunction.MINFLOAT) {
      sql += " and abs(ra-" + ot2qp.getRa() + ")/" + cosd + "<" + ot2qp.getSphereRadius();
      sql += " and abs(dec-" + ot2qp.getDec() + ")<" + ot2qp.getSphereRadius();
    }
    if (null!=ot2qp.getIsMatch()) {
      sql += " and is_match=" + ot2qp.getIsMatch() + " ";
    }
    if (!ot2qp.getOtName().isEmpty()) {
      sql += " and ot_name'" + ot2qp.getOtName() + "' ";
    }

    sqlprefix1 += sql;
    sqlprefix2 += sql;

    String unionSql = "";
    if (ot2qp.getQueryHis()) {
      unionSql = "(" + sqlprefix1 + ") union (" + sqlprefix2 + ")  order by found_time_utc desc";
    } else {
      unionSql = sqlprefix1 + " order by found_time_utc desc";
    }
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(OtLevel2.class);
    q.setFirstResult(ot2qp.getStart());
    q.setMaxResults(ot2qp.getSize());
    return q.list();
  }

  @Override
  public int countOtLevel2(OtLevel2QueryParameter ot2qp) {

    String sqlprefix1 = "select count(*) from ot_level2 where 1=1 ";
    String sqlprefix2 = "select count(*) from ot_level2_his where 1=1 ";
    String sql = "";

    double cosd = Math.cos(ot2qp.getDec() * 0.0174532925);

    if (!ot2qp.getProcessType().isEmpty()) {
      sql += " and data_produce_method='" + ot2qp.getProcessType() + "' ";
    }
    if (!ot2qp.getStartDate().isEmpty()) {
      sql += " and found_time_utc>'" + ot2qp.getStartDate() + " 00:00:00' ";
    }
    if (!ot2qp.getEndDate().isEmpty()) {
      sql += " and found_time_utc<'" + ot2qp.getEndDate() + " 23:59:59' ";
    }
    if (!ot2qp.getTelscope().equalsIgnoreCase("all")) {
      sql += " and dpm_id='" + ot2qp.getTelscope() + "' ";
    }
    if (Math.abs(ot2qp.getPlaneRadius()) > CommonFunction.MINFLOAT) {
      sql += " and abs(xtemp-" + ot2qp.getXtemp() + ")<" + ot2qp.getPlaneRadius();
      sql += " and abs(ytemp-" + ot2qp.getYtemp() + ")<" + ot2qp.getPlaneRadius();
    } else if (Math.abs(ot2qp.getSphereRadius()) > CommonFunction.MINFLOAT) {
      sql += " and abs(ra-" + ot2qp.getRa() + ")/" + cosd + "<" + ot2qp.getSphereRadius();
      sql += " and abs(dec-" + ot2qp.getDec() + ")<" + ot2qp.getSphereRadius();
    }

    sqlprefix1 += sql;
    sqlprefix2 += sql;
    String unionSql = "";
    if (ot2qp.getQueryHis()) {
      unionSql = "(" + sqlprefix1 + ") union (" + sqlprefix2 + ")";
    } else {
      unionSql = sqlprefix1;
    }

    int total = 0;
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql);
    Iterator itor = q.list().iterator();
    while (itor.hasNext()) {
      BigInteger tNum = (BigInteger) itor.next();
      total += tNum.intValue();
    }
    return total;
  }

  @Override
  public List<OtLevel2> getOtLevel2ByDpmName(String dpmName) {
    Session session = getCurrentSession();
    String sql = "select * from ot_level2 where dpm_name='" + dpmName + "';";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    if (!q.list().isEmpty()) {
      return q.list();
    } else {
      return null;
    }
  }

  @Override
  public void updateCvsMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set cvs_match=" + ot2.getCvsMatch()+ " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateRc3Match(OtLevel2 ot2) {
    String sql = "update ot_level2 set rc3_match=" + ot2.getRc3Match()+ " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateMinorPlanetMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set minor_planet_match=" + ot2.getMinorPlanetMatch()+ " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOt2HisMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set ot2_his_match=" + ot2.getOt2HisMatch()+ " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOtherMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set other_match=" + ot2.getOtherMatch()+ " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
}
