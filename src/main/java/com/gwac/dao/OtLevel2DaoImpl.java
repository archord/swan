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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value = "otLevel2Dao")
public class OtLevel2DaoImpl extends BaseHibernateDaoImpl<OtLevel2> implements OtLevel2Dao {

  private static final Log log = LogFactory.getLog(OtLevel2DaoImpl.class);

  public Map<Float, Float> getAllCoorByMatchId(String ids) {

    Map<Float, Float> result = new HashMap<>();
    String sql = "select ot2h.ra, ot2h.dec "
            + "from ot_level2_his ot2h "
            + "inner join ot_level2_match ot2m on ot2m.ot_id=ot2h.ot_id and ot2m.mt_id=6 and match_id in (" + ids + ");";

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql);
    List list = q.list();
    Iterator iter = list.iterator();
    while (iter.hasNext()) {
      Object row[] = (Object[]) iter.next();
      result.put((float) row[0], (float) row[1]);
    }
    return result;
  }

  @Override
  public List<OtLevel2> getUnCutRecord(int successiveImageNumber) {

    Session session = getCurrentSession();
    String sql = "select ot2.* "
            + "from ot_level2 ot2 "
            + "inner join ot_type ott on ott.ott_id=ot2.ot_type and ott.ot_class='1' "
            + "inner join data_process_machine dpm on ot2.dpm_id=dpm.dpm_id and ot2.last_ff_number+" + successiveImageNumber + "<dpm.cur_process_number "
            + "where ot2.data_produce_method='1';";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<OtLevel2> getTodayOt2(char otClass) {

    Session session = getCurrentSession();
    String sql = "select ol2.* "
            + " from ot_level2 ol2 "
            + " inner join ot_type ott on ott.ott_id=ol2.ot_type and ott.ot_class='" + otClass + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<OtLevel2> getOt2ByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "select ol2.* from ot_level2_his ol2 where ol2.date_str='" + dateStr + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  /**
   *
   * @param dateStr
   * @param otClass ot_class的取值有5种： 0，未分类 1，真OT 2，动OT 3，错OT 4，假OT。
   * 考虑到大部分假的都未分类，为能建立完善的OT2历史模板，现在将“未分类”的值设置为4
   * @return
   */
  @Override
  public List<OtLevel2> getLv2OTByDateAndOTClass(String dateStr, char otClass) {

    Session session = getCurrentSession();
    String sql = "select ol2.* "
            + " from ot_level2_his ol2 "
            + " inner join ot_type ott on ott.ott_id=ol2.ot_type and ott.ot_class='" + otClass + "'"
            + " where ol2.date_str='" + dateStr + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public List<String> getAllDateStr(boolean history) {

    List<String> result = new ArrayList<>();
    String sql = "select distinct date_str from ot_level2;";
    if (history) {
      sql = "select distinct date_str from ot_level2_his where date_str>'161001' order by date_str;";
    }
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql);
    List list = q.list();
    Iterator iter = list.iterator();
    while (iter.hasNext()) {
      String his = (String) iter.next();
      result.add(his);
    }
    return result;
  }

  @Override
  public List<OtLevel2> searchOT2His(OtLevel2 ot2, float searchRadius, float mag) {

    SearchBoxSphere sbs = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from ot_level2_his where ot_id!=" + ot2.getOtId()
              + " and data_produce_method='" + ot2.getDataProduceMethod() + "' and ";
      if (tflag == 1) {
        sql += "ra between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(ra > " + sbs.getMinRa() + " or ra <" + sbs.getMaxRa() + ") and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }

      sql += " order by ot_id asc";

      Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
      return q.list();
    }
    return new ArrayList();
  }

  @Override
  public void updateIsMatch(OtLevel2 ot2) {

    String sql = "update ot_level2 set is_match=" + ot2.getIsMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public List<OtLevel2> getUnMatched() {

//    String sql = "WITH updated_rows AS "
//            + "( update ot_level2 set is_match=1 where is_match=0 RETURNING * ) "
//            + " select * from updated_rows";
    String sql = " select * from ot_level2 where is_match=0 order by ot_id";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2.class);
    return q.list();
  }

  @Override
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM ot_level2 RETURNING * ) INSERT INTO ot_level2_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateAllFileCuttedById(long id) {

    Session session = getCurrentSession();
    String sql = "update ot_level2 set all_file_cutted=true where ot_id=" + id;
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public List<OtLevel2> getMissedFFCLv2OT() {

    Session session = getCurrentSession();
    String sql = "select ot2.* "
            + "from ot_level2 ot2 "
            + "inner join ot_type ott on ott.ott_id=ot2.ot_type and ott.ot_class in ('1','2') "
            + "where ot2.cutted_ff_number<ot2.last_ff_number and  ot2.data_produce_method='1' "
            + "order by ot2.ot_id;";
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

  /**
   * 查询OT是在历史表还是当前表
   *
   * @param otName
   * @return his=0 OT存在ot_level2表，his=1 OT存在ot_level2_his表
   */
  @Override
  public List<Integer> hisOrCurExist(String otName) {

    List result = new ArrayList<>();

    String sql = "select 0 his from ot_level2 where name='" + otName
            + "' union select 1 his from ot_level2_his where name='" + otName + "';";

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql);
    List list = q.list();
    Iterator iter = list.iterator();
    if (iter.hasNext()) {
      Integer his = (Integer) iter.next();
      result.add(his);
    }
    return result;
  }

  @Override
  public OtLevel2 existInAll(OtLevel2 obj, float errorBox) {
    Boolean flag = false;
    Session session = getCurrentSession();

    String sql = "select * from ot_level2 "
            + " where dpm_id=" + obj.getDpmId()
            + " and sky_id=" + obj.getSkyId()
            //            + " and date_str='" + obj.getDateStr() + "'"
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

    double cosd = Math.cos(ot2qp.getDec() * 0.0174532925);
    String sqlprefix1 = "select * from ot_level2 where 1=1 ";
    String sqlprefix2 = "select * from ot_level2_his where 1=1 ";
    StringBuilder sql = new StringBuilder("");

    ot2qp.removeEmpty();
//    log.debug(ot2qp.toString());

    Boolean isQueryParameterEmpty = true;
    if (ot2qp.getOtName() != null && !ot2qp.getOtName().isEmpty()) {
      sql.append(" and name='").append(ot2qp.getOtName()).append("' ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getStartDate() != null && !ot2qp.getStartDate().isEmpty()) {
      sql.append(" and found_time_utc>'").append(ot2qp.getStartDate()).append(" 00:00:00' ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getEndDate() != null && !ot2qp.getEndDate().isEmpty()) {
      sql.append(" and found_time_utc<'").append(ot2qp.getEndDate()).append(" 23:59:59' ");
      isQueryParameterEmpty = false;
    }
    if (Math.abs(ot2qp.getPlaneRadius()) > CommonFunction.MINFLOAT) {
      sql.append(" and abs(xtemp-").append(ot2qp.getXtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
      sql.append(" and abs(ytemp-").append(ot2qp.getYtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
      isQueryParameterEmpty = false;
    } else if (Math.abs(ot2qp.getSphereRadius()) > CommonFunction.MINFLOAT) {
      sql.append(" and abs(ra-").append(ot2qp.getRa()).append(")/").append(cosd).append("<").append(ot2qp.getSphereRadius()).append(" ");
      sql.append(" and abs(dec-").append(ot2qp.getDec()).append(")<").append(ot2qp.getSphereRadius()).append(" ");
      isQueryParameterEmpty = false;
    }

    if (ot2qp.getProcessType() != null && !ot2qp.getProcessType().isEmpty()) {
      sql.append(" and data_produce_method in (");
      for (String tstr : ot2qp.getProcessType()) {
        sql.append("'");
        sql.append(tstr);
        sql.append("',");
      }
      sql.append(") ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getTelscope() != null && !ot2qp.getTelscope().isEmpty()) {
      sql.append(" and dpm_id in (");
      for (String tstr : ot2qp.getTelscope()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getIsMatch() != null && !ot2qp.getIsMatch().isEmpty()) {
      sql.append(" and is_match in (");
      for (String tstr : ot2qp.getIsMatch()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getOtType() != null && !ot2qp.getOtType().isEmpty()) {
      sql.append(" and ot_type in (");
      for (String tstr : ot2qp.getOtType()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getMatchType() != null && !ot2qp.getMatchType().isEmpty()) {
      sql.append(" and (");
      for (String tstr : ot2qp.getMatchType()) {
        sql.append(tstr);
        sql.append(">0 or ");
      }
      sql.append(") ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getLookBackResult() != null && !ot2qp.getLookBackResult().isEmpty()) {
      sql.append(" and look_back_result in (");
      for (String tstr : ot2qp.getLookBackResult()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
      isQueryParameterEmpty = false;
    }

    if (isQueryParameterEmpty && ot2qp.getSize() == 0) {
      ot2qp.setStart(0);
      ot2qp.setSize(100);
    }

    String tstr = sql.toString().replace(",)", ")");
    tstr = tstr.replace("or )", ")");
    sqlprefix1 += tstr;
    sqlprefix2 += tstr;

    String unionSql = "";
    if (ot2qp.getQueryHis()) {
      unionSql = "(" + sqlprefix1 + ") union (" + sqlprefix2 + ")  order by found_time_utc desc";
    } else {
      unionSql = sqlprefix1 + " order by found_time_utc desc";
    }
//    log.debug(unionSql);
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(OtLevel2.class);
    if (ot2qp.getSize() != 0) {
      q.setFirstResult(ot2qp.getStart());
      q.setMaxResults(ot2qp.getSize());
    }
    return q.list();
  }

  @Override
  public int countOtLevel2(OtLevel2QueryParameter ot2qp) {

    double cosd = Math.cos(ot2qp.getDec() * 0.0174532925);
    String sqlprefix1 = "select count(*) from ot_level2 where 1=1 ";
    String sqlprefix2 = "select count(*) from ot_level2_his where 1=1 ";
    StringBuilder sql = new StringBuilder("");

    ot2qp.removeEmpty();
    log.debug(ot2qp.toString());

    if (ot2qp.getOtName() != null && !ot2qp.getOtName().isEmpty()) {
      sql.append(" and name='").append(ot2qp.getOtName()).append("' ");
    }
    if (ot2qp.getStartDate() != null && !ot2qp.getStartDate().isEmpty()) {
      sql.append(" and found_time_utc>'").append(ot2qp.getStartDate()).append(" 00:00:00' ");
    }
    if (ot2qp.getEndDate() != null && !ot2qp.getEndDate().isEmpty()) {
      sql.append(" and found_time_utc<'").append(ot2qp.getEndDate()).append(" 23:59:59' ");
    }
    if (Math.abs(ot2qp.getPlaneRadius()) > CommonFunction.MINFLOAT) {
      sql.append(" and abs(xtemp-").append(ot2qp.getXtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
      sql.append(" and abs(ytemp-").append(ot2qp.getYtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
    } else if (Math.abs(ot2qp.getSphereRadius()) > CommonFunction.MINFLOAT) {
      sql.append(" and abs(ra-").append(ot2qp.getRa()).append(")/").append(cosd).append("<").append(ot2qp.getSphereRadius()).append(" ");
      sql.append(" and abs(dec-").append(ot2qp.getDec()).append(")<").append(ot2qp.getSphereRadius()).append(" ");
    }

    if (ot2qp.getProcessType() != null && !ot2qp.getProcessType().isEmpty()) {
      sql.append(" and data_produce_method in (");
      for (String tstr : ot2qp.getProcessType()) {
        sql.append("'");
        sql.append(tstr);
        sql.append("',");
      }
      sql.append(") ");
    }
    if (ot2qp.getTelscope() != null && !ot2qp.getTelscope().isEmpty()) {
      sql.append(" and dpm_id in (");
      for (String tstr : ot2qp.getTelscope()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
    }
    if (ot2qp.getIsMatch() != null && !ot2qp.getIsMatch().isEmpty()) {
      sql.append(" and is_match in (");
      for (String tstr : ot2qp.getIsMatch()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
    }
    if (ot2qp.getOtType() != null && !ot2qp.getOtType().isEmpty()) {
      sql.append(" and ot_type in (");
      for (String tstr : ot2qp.getOtType()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
    }
    if (ot2qp.getMatchType() != null && !ot2qp.getMatchType().isEmpty()) {
      sql.append(" and (");
      for (String tstr : ot2qp.getMatchType()) {
        sql.append(tstr);
        sql.append(">0 or ");
      }
      sql.append(") ");
    }
    String tstr = sql.toString().replace(",)", ")");
    tstr = tstr.replace("or )", ")");
    sqlprefix1 += tstr;
    sqlprefix2 += tstr;

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
    String sql = "update ot_level2 set cvs_match=" + ot2.getCvsMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateRc3Match(OtLevel2 ot2) {
    String sql = "update ot_level2 set rc3_match=" + ot2.getRc3Match() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateMinorPlanetMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set minor_planet_match=" + ot2.getMinorPlanetMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOTType(OtLevel2 ot2) {
    String sql = "update ot_level2 set ot_type=" + ot2.getOtType() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOTTypeHis(OtLevel2 ot2) {
    String sql = "update ot_level2_his set ot_type=" + ot2.getOtType() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOt2HisMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set ot2_his_match=" + ot2.getOt2HisMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOt2HisMatchHis(OtLevel2 ot2) {
    String sql = "update ot_level2_his set ot2_his_match=" + ot2.getOt2HisMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOtherMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set other_match=" + ot2.getOtherMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOtType(int otId, int otTypeId) {
    String sql = "update ot_level2 set ot_type=" + otTypeId + " where ot_id=" + otId;
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateUsnoMatch(OtLevel2 ot2) {
    String sql = "update ot_level2 set usno_match=" + ot2.getUsnoMatch() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public int updateLookBackResult(OtLevel2 ot2) {
    String sql = "update ot_level2 set look_back_result=" + ot2.getLookBackResult() + " where name='" + ot2.getName() + "'";
    Session session = getCurrentSession();
    //return The number of entities updated or deleted.
    int result = session.createSQLQuery(sql).executeUpdate();
    session.flush();
    return result;
  }

  @Override
  public void updateFollowUpResult(OtLevel2 ot2) {
    String sql = "update ot_level2 set follow_up_result=" + ot2.getFollowUpResult() + " where name='" + ot2.getName() + "'";
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateCuttedFfNumber(OtLevel2 ot2) {
    String sql = "update ot_level2 set cutted_ff_number=" + ot2.getCuttedFfNumber() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateSomeRealTimeInfo(OtLevel2 ot2) {
    String sql = "update ot_level2 set first_ff_number=?, found_time_utc=?, last_ff_number=?, xtemp=?, ytemp=?, "
            + "ra=?, dec=?, mag=?, total=? where ot_id=?";
    Session session = getCurrentSession();
    SQLQuery query = session.createSQLQuery(sql);
    query.setParameter(0, ot2.getFirstFfNumber());
    query.setParameter(1, ot2.getFoundTimeUtc());
    query.setParameter(2, ot2.getLastFfNumber());
    query.setParameter(3, ot2.getXtemp());
    query.setParameter(4, ot2.getYtemp());
    query.setParameter(5, ot2.getRa());
    query.setParameter(6, ot2.getDec());
    query.setParameter(7, ot2.getMag());
    query.setParameter(8, ot2.getTotal());
    query.setParameter(9, ot2.getOtId());
    query.executeUpdate();
  }

  @Override
  public void updateFoCount(OtLevel2 ot2) {
    String sql = "update ot_level2 set fo_count=" + ot2.getFoCount() + " where ot_id=" + ot2.getOtId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
}
