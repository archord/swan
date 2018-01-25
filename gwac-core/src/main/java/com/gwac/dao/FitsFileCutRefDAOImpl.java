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
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class FitsFileCutRefDAOImpl extends BaseHibernateDaoImpl<FitsFileCutRef> implements FitsFileCutRefDAO {

  private static final Log log = LogFactory.getLog(FitsFileCutRefDAOImpl.class);
  
  @Override
  public List<FitsFileCutRef> getByName(String ffcName) {

    Session session = getCurrentSession();
    //G170110_C00482_0007_ref_20170110T121901664.jpg
    String sql = "select * from fits_file_cut_ref where file_name='" + ffcName.substring(0, ffcName.indexOf("_ref") + 4) + "'";
    Query q = session.createSQLQuery(sql).addEntity(FitsFileCutRef.class);
    return q.list();
  }
  
  @Override
  public void updateIsRecvOk(long ffcrId){
    Session session = getCurrentSession();
    String sql = "update fits_file_cut_ref set is_recv_ok=true where ffcr_id=" + ffcrId;
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public List<FitsFileCutRef> getUnSyncList(int size) {
    Session session = getCurrentSession();

    String sql = "with updated_rows as "
            + "(update fits_file_cut_ref ffc1 "
            + "set is_sync=true "
            + "from (select ffcr_id from fits_file_cut_ref where success_cut=true and (is_sync=false or is_sync is null ) limit " + size + ") ffc2 "
            + "where ffc1.ffcr_id=ffc2.ffcr_id returning *) "
            + "select ffc.* "
            + "from updated_rows ffc ";
    Query q = session.createSQLQuery(sql).addEntity(FitsFileCutRef.class);
    return q.list();
  }

  @Override
  public void updateByName(FitsFileCutRef ffcr) {

    Session session = getCurrentSession();
    String sql = "update fits_file_cut_ref set success_cut=true, upload_time=now(), generate_time='"
            + CommonFunction.getDateTimeString(ffcr.getGenerateTime(), "yyyyMMdd HHmmss")
            + "', file_name='"
            + ffcr.getFileName()
            + "' where file_name='" + ffcr.getFileName().substring(0, ffcr.getFileName().indexOf("_ref") + 4) + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  /**
   * 查询望远镜dpmId所对应的没有请求过的（request_cut=false）所有OT模板切图
   *
   * @param dpmId
   * @return 
   */
  @Override
  public String getUnCuttedStarList(int dpmId, int size) {
    Session session = getCurrentSession();
    
    String sql = "with updated_rows as "
            + "(update fits_file_cut_ref ffc1 "
            + "set request_cut=true, request_time=now() "
            + "from (select ffcr_id from fits_file_cut_ref ffcr0 "
            + "inner join image_status_parameter isp0 on isp0.ff_id=ffcr0.ff_id  "
            + "where ffcr0.request_cut=false and ffcr0.dpm_id=" + dpmId + " order by ffcr0.ffcr_id asc limit " + size + ") ffc2 "
            + "where ffc1.ffcr_id=ffc2.ffcr_id returning *) "
            + "select ff.img_name ffname, ot.xtemp, ot.ytemp, ffcr.file_name ffcrname, isp.template_path "
            + "from updated_rows ffcr "
            + "left join fits_file2 ff on ffcr.ff_id=ff.ff_id "
            + "left join ot_level2 ot on ot.ot_id=ffcr.ot_id "
            + "left join image_status_parameter isp on isp.ff_id=ff.ff_id;";
    
    Query q = session.createSQLQuery(sql);
    List tlst = q.list();

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
      rst.append(" ");
      rst.append(row[4]);
      rst.append("\n");
    }
    if (tlst.size() > 0) {
      log.debug("get " + tlst.size() + " ref cut images.");
      log.debug(rst.toString());
    }
//    sql = "update fits_file_cut set request_cut=true where request_cut=false and dpm_id=" + dpmId;
//    session.createSQLQuery(sql).executeUpdate();
    return rst.toString();
  }
  
  
  public List<FitsFileCutRef> getCutImageByOtId(long otId) {

    Session session = getCurrentSession();
    String sql = "select * "
            + "from fits_file_cut_ref ffcr "
            + "where ffcr.success_cut=true and ffcr.ot_id='" + otId + "';";
    
    Query q = session.createSQLQuery(sql).addEntity(FitsFileCutRef.class);
    return q.list();
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
  
}
