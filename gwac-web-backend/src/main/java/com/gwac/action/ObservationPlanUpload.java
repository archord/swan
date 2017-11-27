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
  private static final String dateFormateString = "yyyy-MM-dd HH:mm:ss.SSS";

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
  private String ra;
  private String dec;
  private String epoch;
  private String objRa;
  private String objDec;
  private String objEpoch;
  private String objError;
  private String imgType;
  private String expusoreDuring;
  private String delay;
  private String frameCount;
  private String priority;
  private String beginTime;
  private String endTime;
  private String pairId;
  private String echo = "";

  @Action(value = "observationPlanUpload")
  public void upload() {

    echo = "";
    log.debug("observationPlan:" + getObsPlanString());

    ObservationPlan obsPlan = new ObservationPlan();
    if (null != ra && !ra.isEmpty()) {
      obsPlan.setRa(Float.parseFloat(ra));
    }
    if (null != dec && !dec.isEmpty()) {
      obsPlan.setDec(Float.parseFloat(dec));
    }
    if (null != delay && !delay.isEmpty()) {
      obsPlan.setDelay(Integer.parseInt(delay));
    }
    if (null != epoch && !epoch.isEmpty()) {
      obsPlan.setEpoch(Integer.parseInt(epoch));
    }
    if (null != expusoreDuring && !expusoreDuring.isEmpty()) {
      obsPlan.setExpusoreDuring(Integer.parseInt(expusoreDuring));
    }
    if (null != frameCount && !frameCount.isEmpty()) {
      obsPlan.setFrameCount(Integer.parseInt(frameCount));
    }
    if (null != objDec && !objDec.isEmpty()) {
      obsPlan.setObjDec(Float.parseFloat(objDec));
    }
    if (null != objEpoch && !objEpoch.isEmpty()) {
      obsPlan.setObjEpoch(Integer.parseInt(objEpoch));
    }
    if (null != objRa && !objRa.isEmpty()) {
      obsPlan.setObjRa(Float.parseFloat(objRa));
    }
    if (null != pairId && !pairId.isEmpty()) {
      obsPlan.setPairId(Integer.parseInt(pairId));
    }
    if (null != priority && !priority.isEmpty()) {
      obsPlan.setPriority(Integer.parseInt(priority));
    }
    obsPlan.setFieldId(fieldId);
    obsPlan.setGridId(gridId);
    obsPlan.setGroupId(groupId);
    obsPlan.setImgType(imgType);
    obsPlan.setObjError(objError);
    obsPlan.setObjId(objId);
    obsPlan.setObsType(obsType);
    obsPlan.setOpSn(opSn);
    obsPlan.setOpType(opType); //???
    obsPlan.setUnitId(unitId);

    if (null != beginTime && !beginTime.isEmpty()) {
      beginTime = beginTime.replace("T", " ");
      if (beginTime.length() > dateFormateString.length()) {
        beginTime = beginTime.substring(0, dateFormateString.length());
      }
      Date tdate = CommonFunction.stringToDate(beginTime, dateFormateString);
      obsPlan.setBeginTime(tdate);
    }
    if (null != endTime && !endTime.isEmpty()) {
      endTime = endTime.replace("T", " ");
      if (endTime.length() > dateFormateString.length()) {
        endTime = endTime.substring(0, dateFormateString.length());
      }
      Date tdate = CommonFunction.stringToDate(endTime, "yyyy-MM-dd HH:mm:ss");
      obsPlan.setEndTime(tdate);
    }
    if (null != opTime && !opTime.isEmpty()) {
      opTime = opTime.replace("T", " ");
      if (opTime.length() > dateFormateString.length()) {
        opTime = opTime.substring(0, dateFormateString.length());
      }
      Date tdate = CommonFunction.stringToDate(opTime, "yyyy-MM-dd HH:mm:ss");
      obsPlan.setOpTime(tdate);
    }
    //必须设置传输机器名称
    if (!obsPlan.checkValid() || opTime == null) {
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
  public void setRa(String ra) {
    this.ra = ra;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(String dec) {
    this.dec = dec;
  }

  /**
   * @param epoch the epoch to set
   */
  public void setEpoch(String epoch) {
    this.epoch = epoch;
  }

  /**
   * @param objRa the objRa to set
   */
  public void setObjRa(String objRa) {
    this.objRa = objRa;
  }

  /**
   * @param objDec the objDec to set
   */
  public void setObjDec(String objDec) {
    this.objDec = objDec;
  }

  /**
   * @param objEpoch the objEpoch to set
   */
  public void setObjEpoch(String objEpoch) {
    this.objEpoch = objEpoch;
  }

  /**
   * @param objError the objError to set
   */
  public void setObjError(String objError) {
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
  public void setExpusoreDuring(String expusoreDuring) {
    this.expusoreDuring = expusoreDuring;
  }

  /**
   * @param delay the delay to set
   */
  public void setDelay(String delay) {
    this.delay = delay;
  }

  /**
   * @param frameCount the frameCount to set
   */
  public void setFrameCount(String frameCount) {
    this.frameCount = frameCount;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(String priority) {
    this.priority = priority;
  }

  /**
   * @param pairId the pairId to set
   */
  public void setPairId(String pairId) {
    this.pairId = pairId;
  }

}
