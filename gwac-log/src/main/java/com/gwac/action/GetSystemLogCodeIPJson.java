package com.gwac.action;

import com.gwac.dao.SystemLogDao;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetSystemLogCodeIPJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetSystemLogCodeIPJson.class);

  @Resource
  private SystemLogDao systemLogDao;
  /**
   * 返回结果
   */
  private String logCodes;
  private String msgIps;

  @Actions({
    @Action(value = "/get-sys-log-codeip-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
        
    logCodes=systemLogDao.getLogCodes();
    msgIps=systemLogDao.getMsgIPs();
    return "json";
  }

  /**
   * @return the logCodes
   */
  public String getLogCodes() {
    return logCodes;
  }

  /**
   * @return the msgIps
   */
  public String getMsgIps() {
    return msgIps;
  }


}
