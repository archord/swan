package com.gwac.action;

import com.gwac.dao.UserInfoDAO;
import com.gwac.model.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.SessionAware;

@ParentPackage("default")
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class Logout extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 7968544374444173511L;
  private static final Log log = LogFactory.getLog(Logout.class);

  private Map<String, Object> session;

  private String loginName;
  private String loginPass;
  private String echo;
  private Map msg;


  @Action(value = "/user-logout", results = {
    @Result(name = "json", type = "json", params = {"root", "msg"})
  })
  public String userLogout() throws Exception {
    UserInfo user = (UserInfo) session.get("userInfo");
    if (user != null) {
      log.debug("user " + user.getLoginName() + " logout.");
    }
    msg = new HashMap<>();
    session.remove("userInfo");
    msg.put("flag", "1");
    return "json";
  }

  public String getEcho() {
    return echo;
  }

  /**
   * @return the loginName
   */
  public String getLoginName() {
    return loginName;
  }

  /**
   * @param loginName the loginName to set
   */
  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  /**
   * @return the loginPass
   */
  public String getLoginPass() {
    return loginPass;
  }

  /**
   * @param loginPass the loginPass to set
   */
  public void setLoginPass(String loginPass) {
    this.loginPass = loginPass;
  }

  /**
   * @return the msg
   */
  public Map getMsg() {
    return msg;
  }

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = map;
  }

}
