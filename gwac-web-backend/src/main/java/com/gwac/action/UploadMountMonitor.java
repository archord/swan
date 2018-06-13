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
import com.gwac.dao.MountMonitorDao;
import com.gwac.model.Mount;
import com.gwac.model.MountMonitor;
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
public class UploadMountMonitor extends ActionSupport {

  private static final Log log = LogFactory.getLog(UploadMountMonitor.class);

  private String groupId;
  private String unitId;
  private String utc;
  private Character state;
  private Short errcode;
  private Float ra;
  private Float dec;
  private Float objRa;
  private Float objDec;

  @Resource
  private MountDao mountDao;
  @Resource
  private MountMonitorDao mmDao;
  private String echo = "";

  @Action(value = "uploadMountStatus")
  public void upload() {

    echo = "";
    log.debug("groupId:" + groupId);
    log.debug("unitId:" + unitId);
    log.debug("utc:" + utc);
    log.debug("state:" + state);
    log.debug("errcode:" + errcode);
    log.debug("ra:" + ra);
    log.debug("dec:" + dec);
    log.debug("objRa:" + objRa);
    log.debug("objDec:" + objDec);

    if (groupId == null || groupId.isEmpty() || unitId == null || unitId.isEmpty()) {
      echo = "groupId and unitId cannot be empty.";
      log.warn(echo);
    } else {
      Mount tmount = mountDao.getByGroupUnitId(groupId, unitId);
      if (tmount != null) {
        MountMonitor obj = new MountMonitor();
        obj.setMountId(tmount.getMountId());
        obj.setState(state);
        obj.setErrcode(errcode);
        obj.setRa(ra);
        obj.setDec(dec);
        obj.setObjRa(objRa);
        obj.setObjDec(objDec);
        if (null != utc) {
          obj.setTimeUtc(CommonFunction.stringToDate(utc.replace("T", " ")));
        }
        mmDao.save(obj);
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
   * @param ra the ra to set
   */
  public void setRa(Float ra) {
    this.ra = ra;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(Float dec) {
    this.dec = dec;
  }

  /**
   * @param objRa the objRa to set
   */
  public void setObjRa(Float objRa) {
    this.objRa = objRa;
  }

  /**
   * @param objDec the objDec to set
   */
  public void setObjDec(Float objDec) {
    this.objDec = objDec;
  }

  /**
   * @param utc the utc to set
   */
  public void setUtc(String utc) {
    this.utc = utc;
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
