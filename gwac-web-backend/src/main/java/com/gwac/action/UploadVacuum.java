/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.CameraDao;
import com.gwac.dao.CameraVacuumMonitorDao;
import com.gwac.model.Camera;
import com.gwac.model.CameraVacuumMonitor;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
public class UploadVacuum extends ActionSupport {

  private static final Log log = LogFactory.getLog(UploadVacuum.class);

  private String groupId;
  private String unitId;
  private String camId;
  private Boolean online;
  private Float voltage;
  private Float current;
  private Float pressure;
  private String time;

  @Resource
  private CameraDao camDao;
  @Resource
  private CameraVacuumMonitorDao cvmDao;
  private String echo = "";

  @Action(value = "uploadVacuum")
  public void upload() {

    echo = "";
    log.debug("groupId:" + groupId);
    log.debug("unitId:" + unitId);
    log.debug("camId:" + camId);
    log.debug("online:" + online);
    log.debug("voltage:" + voltage);
    log.debug("current:" + current);
    log.debug("pressure:" + pressure);
    log.debug("time:" + time);

    if (groupId == null || groupId.isEmpty() || unitId == null || unitId.isEmpty()
            || camId == null || camId.isEmpty()) {
      echo = "groupId, unitId, camId cannot be empty.";
      log.warn(echo);
    } else {
      Camera tcam = camDao.getByName(camId);
      if (tcam != null) {
        CameraVacuumMonitor obj = new CameraVacuumMonitor();
        obj.setCamId(tcam.getCameraId());
        obj.setCurrent(current);
        obj.setOnline(online);
        obj.setPressure(pressure);
        obj.setVoltage(voltage);
        if (null == time) {
          obj.setTime(new Date());
        } else {
          obj.setTime(CommonFunction.stringToDate(time.replace("T", " ")));
        }
        cvmDao.save(obj);
        echo = "receive parameter success.";
      } else {
        echo = "can not find camera: " + camId;
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

  /**
   * @param camId the camId to set
   */
  public void setCamId(String camId) {
    this.camId = camId;
  }

  /**
   * @param online the online to set
   */
  public void setOnline(Boolean online) {
    this.online = online;
  }

  /**
   * @param voltage the voltage to set
   */
  public void setVoltage(Float voltage) {
    this.voltage = voltage;
  }

  /**
   * @param current the current to set
   */
  public void setCurrent(Float current) {
    this.current = current;
  }

  /**
   * @param pressure the pressure to set
   */
  public void setPressure(Float pressure) {
    this.pressure = pressure;
  }

  /**
   * @param time the time to set
   */
  public void setTime(String time) {
    this.time = time;
  }


}
