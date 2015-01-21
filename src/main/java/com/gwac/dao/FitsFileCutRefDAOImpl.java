/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFileCutRef;
import com.gwac.util.CommonFunction;
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
public class FitsFileCutRefDAOImpl extends BaseHibernateDaoImpl<FitsFileCutRef> implements FitsFileCutRefDAO {

  private static final Log log = LogFactory.getLog(FitsFileCutRefDAOImpl.class);

  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM fits_file_cut RETURNING * ) INSERT INTO fits_file_cut_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  public void updateByName(FitsFileCutRef ffcr) {

    Session session = getCurrentSession();
    String sql = "update fits_file_cut_ref set success_cut=true, generate_time='"+
            CommonFunction.getDateTimeString(ffcr.getGenerateTime(), "yyyyMMdd HHmmss")+
            "', file_name='"+
            ffcr.getFileName() +
            "' where file_name='" + ffcr.getFileName().substring(0, ffcr.getFileName().indexOf("_ref") + 4) + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  public String getUnCuttedStarList(int dpmId) {
    Session session = getCurrentSession();
    String sql = "with updated_rows as "
            + "(update fits_file_cut_ref set request_cut=true where request_cut=false and dpm_id=" + dpmId + " returning *) "
            + "select ff.file_name ffname, ot.xtemp, ot.ytemp, ffcr.file_name ffcrname "
            + "from updated_rows ffcr "
            + "inner join fits_file ff on ffcr.ff_id=ff.ff_id "
            + "inner join ot_level2 ot on ot.ot_id=ffcr.ot_id ";
    Query q = session.createSQLQuery(sql);
    List tlst = q.list();
    if (tlst.size() > 0) {
      log.debug("get " + tlst.size() + " cut images.");
    }

    Iterator itor = tlst.iterator();
    StringBuilder rst = new StringBuilder();
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      rst.append(row[0]);
      rst.append(" ");
      rst.append(row[1]);
      rst.append(" ");
      rst.append(row[2]);
      rst.append(" ");
      rst.append(row[3]);
      rst.append("\n");
    }
//    sql = "update fits_file_cut set request_cut=true where request_cut=false and dpm_id=" + dpmId;
//    session.createSQLQuery(sql).executeUpdate();
    return rst.toString();
  }

  /**
   * 通过OT名称，查询该OT所有的切图
   *
   * @param otName
   * @return OT切图路径和名称Map
   */
  public List<FitsFileCutRef> getCutImageByOtName(String otName) {

    Session session = getCurrentSession();
    String sql = "select * "
            + "from fits_file_cut_ref ffcr "
            + "where ffcr.success_cut=true and ffcr.ot_id=(select ot_id from ot_level2 ob where ob.name='" + otName + "');";
    
    Query q = session.createSQLQuery(sql).addEntity(FitsFileCutRef.class);
    return q.list();
  }
  
  public List<FitsFileCutRef> getCutImageByOtNameFromHis(String otName) {

    Session session = getCurrentSession();
    String sql = "select * "
            + "from fits_file_cut_his ffc "
            + "where ffc.success_cut=true and ffc.ot_id=(select ot_id from ot_level2_his ob where ob.name='" + otName + "') "
            + "order by ffc.number;";
    Query q = session.createSQLQuery(sql).addEntity(FitsFileCutRef.class);
    return q.list();
  }
}
