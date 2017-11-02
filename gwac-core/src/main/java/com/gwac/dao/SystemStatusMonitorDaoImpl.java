/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.ImageStatusParameter;
import com.gwac.model.SystemStatusMonitor;
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
@Repository(value="ssmDao")
public class SystemStatusMonitorDaoImpl extends BaseHibernateDaoImpl<SystemStatusMonitor> implements SystemStatusMonitorDao {
  
  private static final Log log = LogFactory.getLog(SystemStatusMonitorDaoImpl.class);
  
  @Override
  public List<SystemStatusMonitor> getAllStatus(){
    String sql = "select * from system_status_monitor order by identity asc";
    log.debug(sql);
    
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(SystemStatusMonitor.class);
    return q.list();
  }
  
  @Override
  public void updateCameraStatus(String id, String status) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set camera_status=" + status + ", op_time=current_timestamp where identity in(" + id + ")";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateObservationPlan(String id, Long opSn) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set op_sn=" + opSn + ", op_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgRegister(String id, String imgName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set reg_img_name='" + imgName + "', reg_img_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgParmFile(String id, String imgParmName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set img_parm_name='" + imgParmName + "', img_parm_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOt1List(String id, String ot1ListName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set ot1_list_name='" + ot1ListName + "', ot1_list_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgCut(String id, String imgCutFileName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set img_cut_file_name='" + imgCutFileName + "', img_cut_file_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgCutRef(String id, String imgCutFileName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set img_cut_file_ref_name='" + imgCutFileName + "', img_cut_file_ref_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgCutRequest(String id, String imgCutFileName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set img_cut_request_name='" + imgCutFileName + "', img_cut_request_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgCutRefRequest(String id, String imgCutFileName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set img_cut_request_ref_name='" + imgCutFileName + "', img_cut_request_ref_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOt2LookBack(String id, String ot2Name) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set look_back_ot2_name='" + ot2Name + "', look_back_ot2_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }
  
  @Override
  public void updateOt1ListSub(String id, String ot1ListName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set ot1_list_name_sub='" + ot1ListName + "', ot1_list_time_sub=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgCutSub(String id, String imgCutFileName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set img_cut_file_name_sub='" + imgCutFileName + "', img_cut_file_time_sub=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateOt2LookBackSub(String id, String ot2Name) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set look_back_ot2_name_sub='" + ot2Name + "', look_back_ot2_time_sub=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateThumbnail(String id, String thumbnailName) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set thumbnail_name='" + thumbnailName + "', thumbnail_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLog(String id, String logContent) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set log_content='" + logContent + "', log_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateImgParm(String id, ImageStatusParameter isp) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set img_parm_id=" + isp.getIspId() + 
            ", cam_temperature=" + (isp.getTemperatureActual()-isp.getTemperatureSet()) + 
            " img_fwhm=" + isp.getFwhm() + ", position_error=" + (isp.getImgCenterRa()-isp.getMountRa()) + " where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateProcessMachine(String id, int status, float storage, float cpu) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set process_machine_status=" + status
            + ", process_machine_storage=" + storage
            + ", process_machine_cpu=" + cpu
            + ", process_machine_time=current_timestamp where identity='" + id + "'";
    log.debug(sql);
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateCaptureMachine(String id, int status, float storage, float cpu) {
    Session session = getCurrentSession();
    String sql = "update system_status_monitor set capture_machine_status=" + status
            + ", capture_machine_storage=" + storage
            + ", capture_machine_cpu=" + cpu
            + ", capture_machine_time=current_timestamp where identity='" + id + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

}
