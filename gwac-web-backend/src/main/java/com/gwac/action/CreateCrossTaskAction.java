/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.CrossTaskDao;
import com.gwac.model.CrossTask;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.ApplicationAware;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class CreateCrossTaskAction extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(CreateCrossTaskAction.class);

  private String taskName;
  private String crossMethod;

  @Resource
  private CrossTaskDao crossTaskDao;
  private Map<String, Object> appMap = null;
  private String dateStr = null;

  private String echo = "";

  @Action(value = "createCrossTask")
  public void upload() {

    echo = "";

    if (taskName == null || taskName.isEmpty() || crossMethod == null || crossMethod.isEmpty()) {
      echo = "all parameter cannot be empty.";
      log.warn(echo);
      log.warn("taskName:" + taskName + ", crossMethod:" + crossMethod);
    } else {
      initDateStr();
      CrossTask ct = new CrossTask();
      ct.setCreateTime(new Date());
      ct.setCrossMethod(Integer.parseInt(crossMethod));
      ct.setCtName(taskName);
      ct.setDateStr(dateStr);
      ct.setFfCount(0);
      ct.setObjCount(0);
      if(crossTaskDao.exist(ct)){
	echo = taskName + " already exist, please select another name.";
      }else{
	crossTaskDao.save(ct);
	echo = "create CrossTask success.";
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

  public void initDateStr() {
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


  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

  /**
   * @param taskName the taskName to set
   */
  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  /**
   * @param crossMethod the crossMethod to set
   */
  public void setCrossMethod(String crossMethod) {
    this.crossMethod = crossMethod;
  }

}
