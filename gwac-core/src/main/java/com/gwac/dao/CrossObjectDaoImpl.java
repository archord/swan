/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossObject;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
import com.gwac.model4.CrossObjectQueryParameter;
import com.gwac.util.CommonFunction;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
@Repository
public class CrossObjectDaoImpl extends BaseHibernateDaoImpl<CrossObject> implements CrossObjectDao {
  
  private static final Log log = LogFactory.getLog(CrossObjectDaoImpl.class);
    
  @Override
  public CrossObject getCrossObjectById(long coId, Boolean queryHis) {
    
    String sql1 = "select * from cross_object where co_id=" + coId;
    String sql2 = "select * from cross_object_his where co_id=" + coId;
    
    String unionSql = "";
    if (queryHis) {
      unionSql = "(" + sql1 + ") union (" + sql2 + ")";
    } else {
      unionSql = sql1;
    }
    
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(CrossObject.class);
    if (!q.list().isEmpty()) {
      return (CrossObject) q.list().get(0);
    } else {
      return null;
    }
  }
  
  @Override
  public CrossObject getCrossObjectByName(String name, Boolean queryHis) {
    
    String sql1 = "select * from cross_object where name='" + name + "'";
    String sql2 = "select * from cross_object_his where name='" + name + "'";
    
    String unionSql = "";
    if (queryHis) {
      unionSql = "(" + sql1 + ") union (" + sql2 + ")";
    } else {
      unionSql = sql1;
    }
    
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(CrossObject.class);
    if (!q.list().isEmpty()) {
      return (CrossObject) q.list().get(0);
    } else {
      return null;
    }
  }
  
  @Override
  public List<Integer> hisOrCurExist(long coId) {
    
    List result = new ArrayList<>();
    
    String sql = "select 0 his from cross_object where co_id=" + coId
            + " union select 1 his from cross_object_his where co_id=" + coId + ";";
    
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
  public List<Integer> hisOrCurExist(String name) {
    
    List result = new ArrayList<>();
    
    String sql = "select 0 his from cross_object where name='" + name
            + "' union select 1 his from cross_object_his where name='" + name + "';";
    
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
  public void updateIsMatch(CrossObject obj) {
    
    String sql = "update cross_object set is_match=" + obj.getIsMatch() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
    session.flush();
  }
  
  @Override
  public void moveDataToHisTable() {
    
    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM cross_object RETURNING * ) INSERT INTO cross_object_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public CrossObject getCrossObjectById(String coId, Boolean queryHis) {
    
    String sql1 = "select * from cross_object where co_id=" + coId;
    String sql2 = "select * from cross_object_his where co_id=" + coId;
    
    String unionSql = "";
    if (queryHis) {
      unionSql = "(" + sql1 + ") union (" + sql2 + ")";
    } else {
      unionSql = sql1;
    }
    
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(CrossObject.class);
    if (!q.list().isEmpty()) {
      return (CrossObject) q.list().get(0);
    } else {
      return null;
    }
  }
  
  @Override
  public CrossObject exist(CrossObject obj, float errorBox) {
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select * from cross_object where ct_id="
            + obj.getCtId()
            + " and sqrt(power(x_temp-" + obj.getXtemp() + ", 2)+power(y_temp-" + obj.getYtemp() + ", 2))<" + errorBox + " ";
    Query q = session.createSQLQuery(sql).addEntity(CrossObject.class);
    if (!q.list().isEmpty()) {
      return (CrossObject) q.list().get(0);
    }
    return null;
  }
  
  @Override
  public CrossObject existInLatestN(CrossObject obj, float errorBox, int n) {
    Session session = getCurrentSession();
    
    String sql = "select * from cross_object "
            + " where last_ff_number>" + (obj.getLastFfNumber() - n)
            + " and ct_id=" + obj.getCtId()
            + " and sqrt(power(x_temp-" + obj.getXtemp() + ", 2)+power(y_temp-" + obj.getYtemp() + ", 2))<" + errorBox + " ";
    Query q = session.createSQLQuery(sql).addEntity(CrossObject.class);
    if (!q.list().isEmpty()) {
      return (CrossObject) q.list().get(0);
    }
    return null;
  }
  
  @Override
  public List<CrossObject> queryCrossObject(CrossObjectQueryParameter ot2qp) {
    
    double cosd = Math.cos(ot2qp.getDec() * 0.0174532925);
    String sqlprefix1 = "select * from cross_object where 1=1 ";
    String sqlprefix2 = "select * from cross_object_his where 1=1 ";
    StringBuilder sql = new StringBuilder("");
    
    ot2qp.removeEmpty();
//    log.debug(ot2qp.toString());

    Boolean isQueryParameterEmpty = true;
    if (ot2qp.getDateStr()!=null && !ot2qp.getDateStr().isEmpty()) {
      sql.append(" and date_str='").append(ot2qp.getStartDate()).append("' ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getCoId() != null && ot2qp.getCoId()>0) {
      sql.append(" and co_id=").append(ot2qp.getCoId()).append(" ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getCtId() != null && ot2qp.getCtId()>0) {
      sql.append(" and ct_id=").append(ot2qp.getCtId()).append(" ");
      isQueryParameterEmpty = false;
    }
    if (ot2qp.getMagDiff() != null && ot2qp.getMagDiff()>0) {
      sql.append(" and mag_diff>=").append(ot2qp.getMagDiff()).append(" ");
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
      sql.append(" and abs(x_temp-").append(ot2qp.getXtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
      sql.append(" and abs(y_temp-").append(ot2qp.getYtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
      isQueryParameterEmpty = false;
    } else if (Math.abs(ot2qp.getSphereRadius()) > CommonFunction.MINFLOAT) {
      sql.append(" and abs(ra-").append(ot2qp.getRa()).append(")/").append(cosd).append("<").append(ot2qp.getSphereRadius()).append(" ");
      sql.append(" and abs(dec-").append(ot2qp.getDec()).append(")<").append(ot2qp.getSphereRadius()).append(" ");
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
        sql.append("=true or ");
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
    if (ot2qp.getLookBackCnn() != null && !ot2qp.getLookBackCnn().isEmpty()) {
      if (ot2qp.getLookBackCnn().size() > 0) {
        sql.append(" and look_back_cnn >=");
        sql.append(ot2qp.getLookBackCnn().get(0));
        sql.append(" ");
        isQueryParameterEmpty = false;
      }
    }
    
    if (isQueryParameterEmpty && ot2qp.getLength() == 0) {
      ot2qp.setStart(0);
      ot2qp.setLength(30);
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
    log.debug(unionSql);
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(CrossObject.class);
    if (ot2qp.getLength() != 0) {
      q.setFirstResult(ot2qp.getStart());
      q.setMaxResults(ot2qp.getLength());
    }
    return q.list();
  }
  
  @Override
  public int countCrossObject(CrossObjectQueryParameter ot2qp) {
    
    double cosd = Math.cos(ot2qp.getDec() * 0.0174532925);
    String sqlprefix1 = "select count(*) from cross_object where 1=1 ";
    String sqlprefix2 = "select count(*) from cross_object_his where 1=1 ";
    StringBuilder sql = new StringBuilder("");
    
    ot2qp.removeEmpty();
    log.debug(ot2qp.toString());
    
    if (ot2qp.getCoId() != null && ot2qp.getCoId()>0) {
      sql.append(" and co_id=").append(ot2qp.getCoId()).append(" ");
    }
    if (ot2qp.getCtId() != null && ot2qp.getCtId()>0) {
      sql.append(" and ct_id=").append(ot2qp.getCtId()).append(" ");
    }
    if (ot2qp.getStartDate() != null && !ot2qp.getStartDate().isEmpty()) {
      sql.append(" and found_time_utc>'").append(ot2qp.getStartDate()).append(" 00:00:00' ");
    }
    if (ot2qp.getEndDate() != null && !ot2qp.getEndDate().isEmpty()) {
      sql.append(" and found_time_utc<'").append(ot2qp.getEndDate()).append(" 23:59:59' ");
    }
    if (Math.abs(ot2qp.getPlaneRadius()) > CommonFunction.MINFLOAT) {
      sql.append(" and abs(x_temp-").append(ot2qp.getXtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
      sql.append(" and abs(y_temp-").append(ot2qp.getYtemp()).append(")<").append(ot2qp.getPlaneRadius()).append(" ");
    } else if (Math.abs(ot2qp.getSphereRadius()) > CommonFunction.MINFLOAT) {
      sql.append(" and abs(ra-").append(ot2qp.getRa()).append(")/").append(cosd).append("<").append(ot2qp.getSphereRadius()).append(" ");
      sql.append(" and abs(dec-").append(ot2qp.getDec()).append(")<").append(ot2qp.getSphereRadius()).append(" ");
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
    if (ot2qp.getLookBackResult() != null && !ot2qp.getLookBackResult().isEmpty()) {
      sql.append(" and look_back_result in (");
      for (String tstr : ot2qp.getLookBackResult()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
    }
    if (ot2qp.getLookBackCnn() != null && !ot2qp.getLookBackCnn().isEmpty()) {
      sql.append(" and look_back_cnn in (");
      for (String tstr : ot2qp.getLookBackCnn()) {
        sql.append(tstr);
        sql.append(",");
      }
      sql.append(") ");
    }
    String tstr = sql.toString().replace(",)", ")");
    tstr = tstr.replace("or )", ")");
    sqlprefix1 += tstr;
    sqlprefix2 += tstr;
    
    String unionSql = "";
    if (ot2qp.getQueryHis() && ot2qp.getDateStr()!=null && !ot2qp.getDateStr().isEmpty()) {
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
  public void updateCvsMatch(CrossObject obj) {
    String sql = "update cross_object set cvs_match=" + obj.getCvsMatch() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateRc3Match(CrossObject obj) {
    String sql = "update cross_object set rc3_match=" + obj.getRc3Match() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateMinorPlanetMatch(CrossObject obj) {
    String sql = "update cross_object set minor_planet_match=" + obj.getMinorPlanetMatch() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateOtType(CrossObject obj) {
    String sql = "update cross_object set ot_type=" + obj.getOtType() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateHisMatch(CrossObject obj) {
    String sql = "update cross_object set his_match=" + obj.getHisMatch() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateOtherMatch(CrossObject obj) {
    String sql = "update cross_object set other_match=" + obj.getOtherMatch() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateUsnoMatch(CrossObject obj) {
    String sql = "update cross_object set usno_match=" + obj.getUsnoMatch() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public int updateLookBackResult(CrossObject obj) {
    String sql = "update cross_object set look_back_result=" + obj.getLookBackResult() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    int result = session.createSQLQuery(sql).executeUpdate();
    session.flush();
    return result;
  }
  
  @Override
  public void updateFollowUpResult(CrossObject obj) {
    String sql = "update cross_object set follow_up_result=" + obj.getFollowUpResult() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateSomeRealTimeInfo(CrossObject obj) {
    String sql = "update cross_object set first_ff_number=?, found_time_utc=?, last_ff_number=?, x_temp=?, y_temp=?, "
            + "ra=?, dec=?, mag=?, total=?, ot_type=?, min_mag=?, max_mag=?, mag_diff=? where co_id=?";
    Session session = getCurrentSession();
    SQLQuery query = session.createSQLQuery(sql);
    query.setParameter(0, obj.getFirstFfNumber());
    query.setParameter(1, obj.getFoundTimeUtc());
    query.setParameter(2, obj.getLastFfNumber());
    query.setParameter(3, obj.getXtemp());
    query.setParameter(4, obj.getYtemp());
    query.setParameter(5, obj.getRa());
    query.setParameter(6, obj.getDec());
    query.setParameter(7, obj.getMag());
    query.setParameter(8, obj.getTotal());
    query.setParameter(9, obj.getOtType());
    query.setParameter(10, obj.getCoId());
    query.setParameter(8, obj.getMinMag());
    query.setParameter(9, obj.getMaxMag());
    query.setParameter(10, obj.getMagDiff());
    query.executeUpdate();
  }
  
  @Override
  public void updateFoCount(CrossObject obj) {
    String sql = "update cross_object set fo_count=" + obj.getFoCount() + " where co_id=" + obj.getCoId();
    Session session = getCurrentSession();
    session.createSQLQuery(sql).executeUpdate();
  }
}
