package com.gwac.action;

import com.gwac.dao.ScienceObjectDao;
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
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class UpdateScienceObject extends ActionSupport{

  /**
   * @param soId the soId to set
   */
  public void setSoId(int soId) {
    this.soId = soId;
  }

  /**
   * @param isTrue the isTrue to set
   */
  public void setIsTrue(String isTrue) {
    this.isTrue = isTrue;
  }

  private static final long serialVersionUID = 7968592518444173511L;
  private static final Log log = LogFactory.getLog(UpdateScienceObject.class);
  
  @Resource
  private ScienceObjectDao scDao;

  private int soId;
  private String isTrue;
  private String echo;
  private Map msg;


  @Action(value = "/update-science-object", results = {
    @Result(name = "json", type = "json", params = {"root", "msg"})
  })
  public String doUpdate() throws Exception {
    msg = new HashMap<>();
    msg.put("flag", "1");
    scDao.updateIsTrue(soId, isTrue);
    return "json";
  }

  public String getEcho() {
    return echo;
  }

  /**
   * @return the msg
   */
  public Map getMsg() {
    return msg;
  }


}
