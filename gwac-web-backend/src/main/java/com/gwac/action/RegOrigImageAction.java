/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.activemq.RegOrigImageMessageCreator;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.ApplicationAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class RegOrigImageAction extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(RegOrigImageAction.class);

  private String groupId;
  private String unitId;
  private String camId;
  private String gridId;
  private String fieldId;
  private String imgName;
  private String imgPath;
  private String genTime; //yyyyMMddHHmmssSSS

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "regOrigImageDest")
  private Destination msgDest;

  private Map<String, Object> appMap = null;
  private String dateStr = null;

  private String echo = "";

  @Action(value = "regOrigImg")
  public void upload() {

    String result = SUCCESS;
    echo = "";
    log.debug("groupId:" + groupId);
    log.debug("unitId:" + unitId);
    log.debug("camId:" + camId);
    log.debug("gridId:" + gridId);
    log.debug("fieldId:" + fieldId);
    log.debug("imgName:" + imgName);
    log.debug("imgPath:" + imgPath);
    log.debug("genTime:" + genTime);

    // || genTime.length() != "yyyy-MM-ddTHH:mm:ss.SSSSSS".length()
    if (groupId == null || groupId.isEmpty() || unitId == null || unitId.isEmpty()
            || camId == null || camId.isEmpty() || gridId == null || gridId.isEmpty()
            || fieldId == null || fieldId.isEmpty() || imgName == null || imgName.isEmpty()
            || imgPath == null || imgPath.isEmpty() || genTime == null || genTime.isEmpty()) {
//      echo = "all parameter cannot be empty, and genTime must formated as 'yyyy-MM-ddTHH:mm:ss.SSSSSS'.";
      echo = "all parameter cannot be empty.";
      log.warn(echo);
    } else {
      initObjType();
      MessageCreator tmc = new RegOrigImageMessageCreator(groupId, unitId, camId, gridId, fieldId, imgName, imgPath, genTime, dateStr);
      jmsTemplate.send(msgDest, tmc);
      echo = "receive parameter success.";
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

  public void initObjType() {
    dateStr = (String) appMap.get("datestr");
    if (null == dateStr) {
      dateStr = CommonFunction.getUniqueDateStr();
      appMap.put("datestr", dateStr);
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
   * @param imgName the imgName to set
   */
  public void setImgName(String imgName) {
    this.imgName = imgName;
  }

  /**
   * @param imgPath the imgPath to set
   */
  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  /**
   * @param genTime the genTime to set
   */
  public void setGenTime(String genTime) {
    this.genTime = genTime;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

}
