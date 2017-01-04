/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class CommonLog extends ActionSupport {

  private static final Log log = LogFactory.getLog(CommonLog.class);

  private String logType;
  private String msgType;
  private Integer msgCode;
  private String msgDate;
  private String msgContent;

  private String echo = "";

  @Action(value = "commonLog", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    String result = SUCCESS;
    echo = "";
    if ("logchb".equals(logType)) {
      storeLogMsgChb();
      echo += "success receive logs.";
    } else if ("loglxm".equals(logType)) {
      storeLogMsgLxm();
      echo += "success receive logs.";
    } else {
      echo += "unrecognize logType:" + logType;
    }

    log.debug(echo);
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);

    return result;
  }

  public void storeLogMsgChb() {
    log.debug("msgType=" + msgType);
    log.debug("msgCode=" + msgCode);
    log.debug("msgDate=" + msgDate);
    log.debug("msgContent=" + msgContent);
  }

  public void storeLogMsgLxm() {
    log.debug("msgContent=" + msgContent);
  }

  public String display() {
    return NONE;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param logType the logType to set
   */
  public void setLogType(String logType) {
    this.logType = logType;
  }

  /**
   * @param msgType the msgType to set
   */
  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  /**
   * @param msgCode the msgCode to set
   */
  public void setMsgCode(Integer msgCode) {
    this.msgCode = msgCode;
  }

  /**
   * @param msgDate the msgDate to set
   */
  public void setMsgDate(String msgDate) {
    this.msgDate = msgDate;
  }

  /**
   * @param msgContent the msgContent to set
   */
  public void setMsgContent(String msgContent) {
    this.msgContent = msgContent;
  }

}
