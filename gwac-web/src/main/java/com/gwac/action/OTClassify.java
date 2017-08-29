package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.model.UserInfo;
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
import org.apache.struts2.interceptor.SessionAware;

@ParentPackage("default")
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class OTClassify extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 7968544374444173511L;
  private static final Log log = LogFactory.getLog(OTClassify.class);
  
  @Resource
  private OtLevel2Dao ot2Dao;

  private Map<String, Object> session;

  private int otId;
  private int otTypeId;
  private String echo;
  private Map msg;


  @Action(value = "/ot-classify", results = {
    @Result(name = "json", type = "json", params = {"root", "msg"})
  })
  public String userLogout() throws Exception {
    msg = new HashMap<>();
    UserInfo user = (UserInfo) session.get("userInfo");
    if (user == null) {
      log.debug("user not login.");
      msg.put("login", "false");
    }
    msg.put("flag", "1");
    log.debug("otId="+otId);
    log.debug("otTypeId="+otTypeId);
    ot2Dao.updateOtType(otId, otTypeId);
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

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = map;
  }

  /**
   * @return the otId
   */
  public int getOtId() {
    return otId;
  }

  /**
   * @param otId the otId to set
   */
  public void setOtId(int otId) {
    this.otId = otId;
  }

  /**
   * @return the otTypeId
   */
  public int getOtTypeId() {
    return otTypeId;
  }

  /**
   * @param otTypeId the otTypeId to set
   */
  public void setOtTypeId(int otTypeId) {
    this.otTypeId = otTypeId;
  }

}
