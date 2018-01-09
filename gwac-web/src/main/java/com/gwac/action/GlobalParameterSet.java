/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.DataProcessMachineDAO;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.util.ServletContextAware;

/*http://host-url/dpmIsAlive.action?dpm=M01*/
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class GlobalParameterSet extends ActionSupport implements ServletContextAware {

  private static final Log log = LogFactory.getLog(GlobalParameterSet.class);

  private ServletContext globalContext;

  @Resource
  private DataProcessMachineDAO dpmDao;

  protected String dpm = "";
  private String echo = "";

  @Action(value = "globalParmSet")
  public void upload() throws Exception {

    String result = SUCCESS;

    if (dpm.isEmpty()) {
      result = ERROR;
      echo += "Error, dpm(name) is empty.\n";
    } else {
      dpmDao.updateLastActiveTime(dpm);
      echo += "success\n";
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
   * @param echo the echo to set
   */
  public void setEcho(String echo) {
    this.echo = echo;
  }

  /**
   * @param dpm the dpm to set
   */
  public void setDpm(String dpm) {
    this.dpm = dpm;
  }

  @Override
  public void setServletContext(ServletContext sc) {
    this.globalContext = sc;
  }

}
