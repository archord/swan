/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ImageStatusParameter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Repository(value = "ispDao")
public class ImageStatusParameterDaoImpl extends BaseHibernateDaoImpl<ImageStatusParameter> implements ImageStatusParameterDao {

  private static final Log log = LogFactory.getLog(ImageStatusParameterDaoImpl.class);

  @Override
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM image_status_parameter RETURNING * ) INSERT INTO image_status_parameter_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  /**
   * 获取上一帧的参数
   *
   * @param isp
   * @return
   */
  @Override
  public ImageStatusParameter getPreviousStatus(ImageStatusParameter isp) {

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Session session = getCurrentSession();
    String sql = "select * from image_status_parameter where dpm_id=" + isp.getDpmId()
            + " and prc_num=" + (isp.getPrcNum() - 1) + " and date(time_obs_ut)=date('" + df.format(isp.getTimeObsUt()) + "')";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    if (!q.list().isEmpty()) {
      return (ImageStatusParameter) q.list().get(0);
    } else {
      return null;
    }
  }

  /**
   * 获取当前库中所有参数
   *
   * @return
   */
  @Override
  public List<ImageStatusParameter> getCurAllParm() {
    Session session = getCurrentSession();
    String sql = "select * from image_status_parameter";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    return q.list();
  }

  /**
   * 获取当前库中所有参数
   *
   * @return
   */
  @Override
  public String getCurAllParmJson() {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT dpm_id, par_detail) r))) "
            + "FROM( "
            + "SELECT isp.dpm_id, JSON_AGG((SELECT r FROM (SELECT isp.fwhm, isp.obj_num, isp.bg_bright, "
            + "isp.avg_limit, isp.xshift, isp.yshift, isp.xrms, isp.yrms, isp.proc_time, isp.temperature_actual, "
            + "to_char(isp.time_obs_ut, 'YYYY/MM/DD HH:MM:SS') time_obs_ut) r)) as par_detail "
            + "FROM image_status_parameter isp "
            + "GROUP BY isp.dpm_id "
            + ")as moor";

    String rst = "";
    Query q = session.createSQLQuery(sql);
//    q.setString(0, dateStr);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public Date getMinDate() {
    Session session = getCurrentSession();
    String sql = "SELECT min(time_obs_ut) from image_status_parameter ";
    Date rst = null;
    Query q = session.createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (Date) q.list().get(0);
    }
    return rst;
  }

  /**
   * 获取当前库中所有参数
   *
   * @param parmName
   * @return
   */
  @Override
  public String getJsonByParm(List<String> parmName) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT dpm_id, par_detail) r))) "
            + "FROM( "
            + "SELECT oi.name as dpm_id, JSON_AGG((SELECT r FROM (SELECT ";
    for (String tpar : parmName) {
      sql += tpar + ", ";
    }
    sql = sql + "prc_num, isp.time_obs_ut) r)) as par_detail "
            + "FROM image_status_parameter isp inner join camera oi on oi.camera_id=isp.dpm_id WHERE ";
    for (int i = 0; i < parmName.size(); i++) {
      String tpar = parmName.get(i);
      if (i > 0) {
        sql += " and abs(" + tpar + "+99)>0.0001 and " + tpar + " is not null ";
      } else {
        sql += " abs(" + tpar + "+99)>0.0001 and " + tpar + " is not null ";
      }
    }
    sql = sql + "GROUP BY oi.name order by oi.name)as moor";
    log.debug(sql);
    String rst = "";
    Query q = session.createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public String getPointJsonByParm(String dateStr, int dpmId, float ra, float dec) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT mount_ra, mount_dec, img_center_ra, img_center_dec, prc_num, time_obs_ut) r ORDER BY time_obs_ut))) "
            + "FROM image_status_parameter_his "
            + "WHERE dpm_id=" + dpmId 
            + " and time_obs_ut>'" + dateStr + " 8:00:00' and time_obs_ut<'" 
            + dateStr + " 23:59:59' and abs(mount_ra-" + ra + ")<0.1  and abs(mount_dec-" + dec + ")<0.1";
    log.debug(sql);
    String rst = "";
    Query q = session.createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  /**
   * 获取最新一帧的参数
   *
   * @return
   */
  @Override
  public List<ImageStatusParameter> getLatestParmOfAllDpm() {
    Session session = getCurrentSession();
    String sql = "WITH sr AS ( select dpm_id, max(prc_num) maxnum from image_status_parameter group by dpm_id ) "
            + "SELECT isp.* FROM image_status_parameter isp "
            + "inner join sr on isp.dpm_id=sr.dpm_id and isp.prc_num=sr.maxnum "
            + "order by isp.dpm_id;";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    return q.list();
  }

  /**
   * 未实现
   *
   * @param dateStr
   * @return
   */
  @Override
  public List<ImageStatusParameter> getImgStatusParmByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "select isp.* from image_status_parameter isp where isp.is_match=2 and isp.date_str='" + dateStr + "'";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    return q.list();
  }

  @Override
  public List<ImageStatusParameter> getImgStatusParmByDpmId(String dpmId) {
    Session session = getCurrentSession();
    String sql = "select * from image_status_parameter where dpm_id=" + dpmId + ";";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    if (!q.list().isEmpty()) {
      return q.list();
    } else {
      return null;
    }
  }

}
