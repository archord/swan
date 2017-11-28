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
public class GetParameter extends ActionSupport implements ApplicationAware {

  private static final long serialVersionUID = 7968754374444173511L;
  private static final Log log = LogFactory.getLog(GetParameter.class);

  private Map<String, Object> appMap;
  private ApplicationParameters appParam;
  private Map msg;

  @Action(value = "/get-app-parameter", results = {
    @Result(name = "json", type = "json", params = {"root", "appParam"})
  })
  public String userLogout() throws Exception {
    appParam = (ApplicationParameters) appMap.get("appParam");
    if (appParam == null) {
      appParam = new ApplicationParameters();
      appParam.setAutoFollowUp(false);
      appMap.put("appParam", appParam);
    }
    return "json";
  }

  /**
   * @return the msg
   */
  public Map getMsg() {
    return msg;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

  /**
   * @return the appParam
   */
  public ApplicationParameters getAppParam() {
    return appParam;
  }

}
