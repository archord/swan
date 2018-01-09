package com.gwac.action;

import com.gwac.dao.WebGlobalParameterDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage("default")
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class SetParameterDB extends ActionSupport {

  private static final long serialVersionUID = 7968754374444173511L;
  private static final Log log = LogFactory.getLog(SetParameterDB.class);

  private String parmName;
  private String parmValue;
  private String echo;
  
  @Resource
  private WebGlobalParameterDao webGlobalParameterDao;

  @Action(value = "/set-app-parameter2", results = {
    @Result(name = "json", type = "json", params = {"root", "echo"})
  })
  @Override
  public String execute() throws Exception {
    log.debug("set value: " + parmName+"="+parmValue);
    webGlobalParameterDao.updateParameter(parmName, parmValue);
    
    echo = "success.";
    return "json";
  }

  public String getEcho() {
    return echo;
  }

  /**
   * @param parmName the parmName to set
   */
  public void setParmName(String parmName) {
    this.parmName = parmName;
  }

  /**
   * @param parmValue the parmValue to set
   */
  public void setParmValue(String parmValue) {
    this.parmValue = parmValue;
  }
}
