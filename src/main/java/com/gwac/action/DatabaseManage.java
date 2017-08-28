/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.service.DataBackupService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import javax.annotation.Resource;
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
public class DatabaseManage extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(DatabaseManage.class);

  private String operation;
  private String dateStr;

  @Resource
  private DataBackupService dataBackupService;
  private Map<String, Object> appMap = null;

  private String echo = "";

  @Action(value = "databaseManage", results = {
    @Result(name = "json", type = "json")})
  public String upload() {

    log.debug("operation=" + operation);
    log.debug("dateStr=" + dateStr);

    echo = "";

    if ("delete".equals(operation)) {
      dataBackupService.deleteData();
      echo = "delete data success!";
    } else if ("backup".equals(operation)) {
      dataBackupService.backupData();
      echo = "backup data success!";
    } else {
      echo = "wrong operation!";
    }

    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);
    return "json";
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

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

  /**
   * @param dataBackupService the dataBackupService to set
   */
  public void setDataBackupService(DataBackupService dataBackupService) {
    this.dataBackupService = dataBackupService;
  }

  /**
   * @param operation the operation to set
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

}
