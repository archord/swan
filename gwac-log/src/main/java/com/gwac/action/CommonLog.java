/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.SystemLogDao;
import com.gwac.model.SystemLog;
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
public class CommonLog extends ActionSupport {

  private static final Log log = LogFactory.getLog(CommonLog.class);

  @Resource
  private SystemLogDao sysLogDao;

  private String logType;
  private String msgSource;
  private String msgType;
  private Integer msgCode;
  private String msgDate; //yyyy-MM-ddTHH:mm:ss.SSS
  private String msgContent;

  private String echo = "";

  @Action(value = "commonLog")
  public void upload() {

    echo = "";

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Date tdate = CommonFunction.stringToDate(msgDate.replace('T', ' '), "yyyy-MM-dd HH:mm:ss.SSS");
    SystemLog sysLog = new SystemLog();
    sysLog.setLogDate(tdate);
    sysLog.setLogContent(msgContent);
    sysLog.setMsgIP(ip);
    sysLog.setMsgSource(msgSource);

    if ("logchb".equals(logType)) {
      sysLog.setLogCode(msgCode);
      if (msgType.equals("error")) {
        sysLog.setLogType('1');
      } else if (msgType.equals("state")) {
        sysLog.setLogType('2');
      } else {
        sysLog.setLogType('3');
      }
      echo += "success receive logs.";
    } else if ("loglxm".equals(logType)) {
      sysLog.setLogType('4');
      echo += "success receive logs.";
    } else {
      sysLog.setLogType('0');
      echo += "unrecognize logType:" + logType;
    }
    sysLogDao.save(sysLog);

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

  /**
   * @param sysLogDao the sysLogDao to set
   */
  public void setSysLogDao(SystemLogDao sysLogDao) {
    this.sysLogDao = sysLogDao;
  }

  /**
   * @param msgSource the msgSource to set
   */
  public void setMsgSource(String msgSource) {
    this.msgSource = msgSource;
  }

}
