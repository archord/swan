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
import com.gwac.dao.CameraMonitorDao;
import com.gwac.model.Camera;
import com.gwac.model.CameraMonitor;
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
public class UploadCameraMonitor extends ActionSupport {

  private static final Log log = LogFactory.getLog(UploadCameraMonitor.class);

  private String groupId;
  private String unitId;
  private String camId;
  private String utc;
  private Character mcState;
  private Integer focus;
  private Float coolget;
  private String filter;
  private Character state;
  private Short errcode;
  private String imgType;
  private String objName;
  private Integer frmNo;
  private String fileName;

  @Resource
  private CameraDao cameraDao;
  @Resource
  private CameraMonitorDao cmDao;
  private String echo = "";

  @Action(value = "uploadCameraInfo")
  public void upload() {

    echo = "";
    log.debug("groupId:" + groupId);
    log.debug("unitId:" + unitId);
    log.debug("camId:" + camId);
    log.debug("utc:" + utc);
    log.debug("mcState:" + mcState);
    log.debug("focus:" + focus);
    log.debug("coolget:" + coolget);
    log.debug("filter:" + filter);
    log.debug("state:" + state);
    log.debug("errcode:" + errcode);
    log.debug("imgType:" + imgType);
    log.debug("objName:" + objName);
    log.debug("frmNo:" + frmNo);
    log.debug("fileName:" + fileName);

    if (groupId == null || groupId.isEmpty() || unitId == null || unitId.isEmpty()
             || camId == null || camId.isEmpty()) {
      echo = "groupId, unitId and camId cannot be empty.";
      log.warn(echo);
    } else {
      Camera tcamera = cameraDao.getByName(camId);
      if (tcamera != null) {
        CameraMonitor obj = new CameraMonitor();
        obj.setCameraId(tcamera.getCameraId());
        if (null != utc) {
          obj.setTimeUtc(CommonFunction.stringToDate(utc.replace("T", " ")));
        }
        obj.setMcState(mcState);
        obj.setFocus(focus);
        obj.setCoolget(coolget);
        obj.setFilter(filter);
        obj.setCamState(state);
        obj.setErrcode(errcode);
        obj.setImgType(imgType);
        obj.setObjName(objName);
        obj.setFrmNo(frmNo);
        obj.setFileName(fileName);
        cmDao.save(obj);
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
   * @param camId the camId to set
   */
  public void setCamId(String camId) {
    this.camId = camId;
  }

  /**
   * @param utc the utc to set
   */
  public void setUtc(String utc) {
    this.utc = utc;
  }

  /**
   * @param mcState the mcState to set
   */
  public void setMcState(Character mcState) {
    this.mcState = mcState;
  }

  /**
   * @param focus the focus to set
   */
  public void setFocus(Integer focus) {
    this.focus = focus;
  }

  /**
   * @param coolget the coolget to set
   */
  public void setCoolget(Float coolget) {
    this.coolget = coolget;
  }

  /**
   * @param filter the filter to set
   */
  public void setFilter(String filter) {
    this.filter = filter;
  }

  /**
   * @param state the state to set
   */
  public void setState(Character state) {
    this.state = state;
  }

  /**
   * @param errcode the errcode to set
   */
  public void setErrcode(Short errcode) {
    this.errcode = errcode;
  }

  /**
   * @param imgType the imgType to set
   */
  public void setImgType(String imgType) {
    this.imgType = imgType;
  }

  /**
   * @param objName the objName to set
   */
  public void setObjName(String objName) {
    this.objName = objName;
  }

  /**
   * @param frmNo the frmNo to set
   */
  public void setFrmNo(Integer frmNo) {
    this.frmNo = frmNo;
  }

  /**
   * @param fileName the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
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
