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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class DatabaseManage extends ActionSupport{

  private static final Log log = LogFactory.getLog(DatabaseManage.class);

  private String operation;
  private String dateStr;

  @Resource
  private DataBackupService dataBackupService;

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

    return "json";
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
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
