package com.gwac.action;

import com.gwac.model.ApplicationParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ApplicationAware;

@ParentPackage("default")
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class SetParameter extends ActionSupport implements ApplicationAware {

  private static final long serialVersionUID = 7968754374444173511L;
  private static final Log log = LogFactory.getLog(SetParameter.class);

  private Map<String, Object> appMap;
  private Boolean autoFollowUp;
  private String echo;

  @Action(value = "/set-app-parameter", results = {
    @Result(name = "json", type = "json", params = {"root", "echo"})
  })
  public String userLogout() throws Exception {
    ApplicationParameters appParam = (ApplicationParameters) appMap.get("appParam");
    if (appParam == null) {
      appParam = new ApplicationParameters();
      appParam.setAutoFollowUp(autoFollowUp);
      appMap.put("appParam", appParam);
    } else {
      appParam.setAutoFollowUp(autoFollowUp);
    }
    
    echo = "success.";
    return "json";
  }

  public String getEcho() {
    return echo;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

  /**
   * @param autoFollowUp the autoFollowUp to set
   */
  public void setAutoFollowUp(Boolean autoFollowUp) {
    this.autoFollowUp = autoFollowUp;
  }
}
