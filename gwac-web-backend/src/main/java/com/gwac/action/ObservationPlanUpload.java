/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.ObservationPlanDao;
import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.model.ObservationPlan;
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
public class ObservationPlanUpload extends ActionSupport {

  private static final Log log = LogFactory.getLog(ObservationPlanUpload.class);

  @Resource
  private ObservationPlanDao obsPlanDao;
  @Resource
  private SystemStatusMonitorDao ssmDao;

  private Long opSn;
  private String opTime;
  private String opType;
  private String groupId;
  private String unitId;
  private String obsType;
  private String gridId;
  private String fieldId;
  private String objId;
  private Float ra;
  private Float dec;
  private Integer epoch;
  private Float objRa;
  private Float objDec;
  private Integer objEpoch;
  private Float objError;
  private String imgType;
  private Integer expusoreDuring;
  private Integer delay;
  private Integer frameCount;
  private Integer priority;
  private String beginTime;
  private String endTime;
  private Integer pairId;
  private String echo = "";

  @Action(value = "observationPlanUpload")
  public void upload() {

    echo = "";
    log.debug("observationPlan:" + getObsPlanString());

    ObservationPlan obsPlan = new ObservationPlan();
    obsPlan.setDec(dec);
    obsPlan.setDelay(delay);
    obsPlan.setEpoch(epoch);
    obsPlan.setExpusoreDuring(expusoreDuring);
    obsPlan.setFieldId(fieldId);
    obsPlan.setFrameCount(frameCount);
    obsPlan.setGridId(gridId);
    obsPlan.setGroupId(groupId);
    obsPlan.setImgType(imgType);
    obsPlan.setObjDec(objDec);
    obsPlan.setObjEpoch(objEpoch);
    obsPlan.setObjError(objError);
    obsPlan.setObjId(objId);
    obsPlan.setObjRa(objRa);
    obsPlan.setObsType(obsType);
    obsPlan.setOpSn(opSn);
    obsPlan.setOpType(opType); //???
    obsPlan.setPairId(pairId);
    obsPlan.setPriority(priority);
    obsPlan.setRa(ra);
    obsPlan.setUnitId(unitId);

    if (null != beginTime && !beginTime.isEmpty()) {
      Date tdate = CommonFunction.stringToDate(beginTime, "yyyy-MM-dd HH:mm:ss");
      obsPlan.setBeginTime(tdate);
    }
    if (null != endTime && !endTime.isEmpty()) {
      Date tdate = CommonFunction.stringToDate(endTime, "yyyy-MM-dd HH:mm:ss");
      obsPlan.setEndTime(tdate);
    }
    if (null != opTime && !opTime.isEmpty()) {
      Date tdate = CommonFunction.stringToDate(opTime, "yyyy-MM-dd HH:mm:ss");
      obsPlan.setOpTime(tdate);
    }
    //必须设置传输机器名称
    if (null == obsPlan || !obsPlan.checkValid()) {
      echo = echo + "Error, observationPlan is inValid.\n";
    } else {
      obsPlanDao.save(obsPlan);
      echo = "upload observation success!";
    }
    ssmDao.updateObservationPlan(unitId, opSn);
    log.debug(echo);
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

  public String getObsPlanString() {
    StringBuilder sb = new StringBuilder();
    sb.append("opSn=");
    sb.append(opSn);
    sb.append(",opTime=");
    sb.append(opTime);
    sb.append(",opType=");
    sb.append(opType);
    sb.append(",groupId=");
    sb.append(groupId);
    sb.append(",unitId=");
    sb.append(unitId);
    sb.append(",obsType=");
    sb.append(obsType);
    sb.append(",gridId=");
    sb.append(gridId);
    sb.append(",fieldId=");
    sb.append(fieldId);
    sb.append(",ra=");
    sb.append(ra);
    sb.append(",dec=");
    sb.append(dec);
    sb.append(",epoch=");
    sb.append(epoch);
    sb.append(",objRa=");
    sb.append(objRa);
    sb.append(",objDec=");
    sb.append(objDec);
    sb.append(",objEpoch=");
    sb.append(objEpoch);
    sb.append(",objError=");
    sb.append(objError);
    sb.append(",imgType=");
    sb.append(imgType);
    sb.append(",expusoreDuring=");
    sb.append(expusoreDuring);
    sb.append(",delay=");
    sb.append(delay);
    sb.append(",frameCount=");
    sb.append(frameCount);
    sb.append(",priority=");
    sb.append(priority);
    sb.append(",beginTime=");
    sb.append(beginTime);
    sb.append(",endTime=");
    sb.append(endTime);
    sb.append(",pairId=");
    sb.append(pairId);
    return sb.toString();
  }

  /**
   * @param beginTime the beginTime to set
   */
  public void setBeginTime(String beginTime) {
    this.beginTime = beginTime;
  }

  /**
   * @param endTime the endTime to set
   */
  public void setEndTime(String endTime) {
    this.endTime = endTime;
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
   * @param opType the opType to set
   */
  public void setOpType(String opType) {
    this.opType = opType;
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
   * @param obsType the obsType to set
   */
  public void setObsType(String obsType) {
    this.obsType = obsType;
  }

  /**
   * @param gridId the gridId to set
   */
  public void setGridId(String gridId) {
    this.gridId = gridId;
  }

  /**
   * @param fieldId the fieldId to set
   */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * @param objId the objId to set
   */
  public void setObjId(String objId) {
    this.objId = objId;
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
   * @param epoch the epoch to set
   */
  public void setEpoch(Integer epoch) {
    this.epoch = epoch;
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
   * @param objEpoch the objEpoch to set
   */
  public void setObjEpoch(Integer objEpoch) {
    this.objEpoch = objEpoch;
  }

  /**
   * @param objError the objError to set
   */
  public void setObjError(Float objError) {
    this.objError = objError;
  }

  /**
   * @param imgType the imgType to set
   */
  public void setImgType(String imgType) {
    this.imgType = imgType;
  }

  /**
   * @param expusoreDuring the expusoreDuring to set
   */
  public void setExpusoreDuring(Integer expusoreDuring) {
    this.expusoreDuring = expusoreDuring;
  }

  /**
   * @param delay the delay to set
   */
  public void setDelay(Integer delay) {
    this.delay = delay;
  }

  /**
   * @param frameCount the frameCount to set
   */
  public void setFrameCount(Integer frameCount) {
    this.frameCount = frameCount;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  /**
   * @param pairId the pairId to set
   */
  public void setPairId(Integer pairId) {
    this.pairId = pairId;
  }

}
