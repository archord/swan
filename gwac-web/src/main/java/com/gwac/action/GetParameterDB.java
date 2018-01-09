package com.gwac.action;

import com.gwac.dao.WebGlobalParameterDao;
import com.gwac.model.ApplicationParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage("default")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetParameterDB extends ActionSupport {

  private static final long serialVersionUID = 7968754374444173511L;
  private static final Log log = LogFactory.getLog(GetParameterDB.class);

  private String parmName;
  private Map appParam = new HashMap();

  @Resource
  private WebGlobalParameterDao webGlobalParameterDao;

  @Action(value = "/get-app-parameter2", results = {
    @Result(name = "json", type = "json", params = {"root", "appParam"})
  })
  @Override
  public String execute() throws Exception {

    log.debug("parmName=" + parmName);
    String tval = webGlobalParameterDao.getValueByName(parmName);
    appParam.put(parmName, tval);
    log.debug("parmValue=" + tval);

    return "json";
  }

  /**
   * @return the appParam
   */
  public Map getAppParam() {
    return appParam;
  }

  /**
   * @param parmName the parmName to set
   */
  public void setParmName(String parmName) {
    this.parmName = parmName;
  }

}
