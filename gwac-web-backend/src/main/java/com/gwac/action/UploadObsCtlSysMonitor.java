/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.MountDao;
import com.gwac.dao.ObsCtlSysMonitorDao;
import com.gwac.model.Mount;
import com.gwac.model.ObsCtlSysMonitor;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class UploadObsCtlSysMonitor extends ActionSupport {

  private static final Log log = LogFactory.getLog(UploadObsCtlSysMonitor.class);

  private String groupId;
  private String unitId;
  private String utc;
  private String state;
  private Long opSn;
  private String opTime;
  private Integer mountStatus;
  private String cameraStatus;

  @Resource
  private MountDao mountDao;
  @Resource
  private ObsCtlSysMonitorDao ocsmDao;
  private String echo = "";

  @Action(value = "uploadObsCtlSysStatus")
  public void upload() {

    echo = "";
    log.debug("groupId:" + groupId);
    log.debug("unitId:" + unitId);
    log.debug("utc:" + utc);
    log.debug("state:" + state);
    log.debug("opSn:" + opSn);
    log.debug("opTime:" + opTime);
    log.debug("mountStatus:" + mountStatus);
    log.debug("cameraStatus:" + cameraStatus);

    if (groupId == null || groupId.isEmpty() || unitId == null || unitId.isEmpty()) {
      echo = "groupId and unitId cannot be empty.";
      log.warn(echo);
    } else {
      Mount tmount = mountDao.getByGroupUnitId(groupId, unitId);
      if (tmount != null) {
        ObsCtlSysMonitor obj = new ObsCtlSysMonitor();
        obj.setMountId(tmount.getMountId());
        obj.setState(state);
        obj.setOpSn(opSn);
        obj.setMountStatus(mountStatus);
        obj.setCameraStatus(cameraStatus);
        if (null != utc) {
          obj.setTimeUtc(CommonFunction.stringToDate(utc.replace("T", " ")));
        }
        if (null != opTime) {
          obj.setOpRealStartTime(CommonFunction.stringToDate(opTime.replace("T", " ")));
        }
        ocsmDao.save(obj);
        echo = "receive parameter success.";
      } else {
        echo = "can not find mount by groupId=: " + groupId + ", unitId=" + unitId;
      }
      log.debug(echo);
    }

    sendResultMsg(echo);
  }

  public void sendResultMsg(String msg) {

    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out;
    try {
      out = response.getWriter();
      out.print(msg);
    } catch (IOException ex) {
      log.error("response error: ", ex);
    }
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param utc the utc to set
   */
  public void setUtc(String utc) {
    this.utc = utc;
  }

  /**
   * @param state the state to set
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * @param opSn the opSn to set
   */
  public void setOpSn(Long opSn) {
    this.opSn = opSn;
  }

  /**
   * @param opTime the opTime to set
   */
  public void setOpTime(String opTime) {
    this.opTime = opTime;
  }

  /**
   * @param mountStatus the mountStatus to set
   */
  public void setMountStatus(Integer mountStatus) {
    this.mountStatus = mountStatus;
  }

  /**
   * @param cameraStatus the cameraStatus to set
   */
  public void setCameraStatus(String cameraStatus) {
    this.cameraStatus = cameraStatus;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

}
