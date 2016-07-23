/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model.OtTmplWrong;
import com.gwac.util.SearchBoxSphere;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtTmplWrongDaoImpl extends BaseHibernateDaoImpl<OtTmplWrong> implements OtTmplWrongDao {
  
  @Override
  public OtTmplWrong getOtTmplByName(String otName) {

    String sql = "select * from ot_tmpl_wrong where name='" + otName + "'";

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(OtTmplWrong.class);
    if (!q.list().isEmpty()) {
      return (OtTmplWrong) q.list().get(0);
    } else {
      return null;
    }
  }
  
  @Override
  public List<OtTmplWrong> searchOT2TmplWrong(OtLevel2 ot2, float searchRadius, float mag) {

    SearchBoxSphere sbs = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from ot_tmpl_wrong where data_produce_method='" + ot2.getDataProduceMethod() + "' and ";
      if (tflag == 1) {
        sql += "ra between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(ra > " + sbs.getMinRa() + " or ra <" + sbs.getMaxRa() + ") and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }
      
      sql += " order by ot_id asc";

      Query q = session.createSQLQuery(sql).addEntity(OtTmplWrong.class);
      return q.list();
    }
    return new ArrayList();
  }
  
  @Override
  public List<OtTmplWrong> searchOT2TmplWrong2(OtLevel2 ot2, float searchRadius, float mag) {

    SearchBoxSphere sbs = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from ot_tmpl_wrong2 where data_produce_method='" + ot2.getDataProduceMethod() + "' and ";
      if (tflag == 1) {
        sql += "ra between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(ra > " + sbs.getMinRa() + " or ra <" + sbs.getMaxRa() + ") and ";
        sql += "dec between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }
      
      sql += " order by ot_id asc";

      Query q = session.createSQLQuery(sql).addEntity(OtTmplWrong.class);
      return q.list();
    }
    return new ArrayList();
  }
}
