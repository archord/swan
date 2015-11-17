package com.gwac.action;

import com.gwac.dao.UserInfoDAO;
import com.gwac.model.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage("default")
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class Login extends ActionSupport {

  private static final long serialVersionUID = 7968544374444173511L;
  private static final Log log = LogFactory.getLog(Login.class);
  
  private UserInfoDAO userDao;

  private String loginName;
  private String loginPass;
  private String echo;
  private Map msg;

  @Action(value = "/login", results = {
    @Result(name = "success", location = "index.jsp"),
    @Result(name = "input", location = "user/puser-login.jsp"),
    @Result(name = "json", type = "json", params = {"root", "msg"})
  })
  @Validations(requiredStrings = {
    @RequiredStringValidator(fieldName = "loginName", type = ValidatorType.FIELD, message = "用户名必须填写。"),
    @RequiredStringValidator(fieldName = "loginPass", type = ValidatorType.FIELD, message = "密码必须填写。")},
          expressions = {
            //不满足条件expression，则直接返回跳转到input;满足条件才会执行后面的方法
            @ExpressionValidator(expression = "loginPass.trim().equals('test') != true", message = "用户名或密码错误。")
          })
  public String userLogin() throws Exception {
    
    String result = SUCCESS;
    msg = new HashMap<>();
    
    UserInfo user = new UserInfo();
    user.setLoginName(loginName);
    user.setPassword(loginPass);
    
    List users = userDao.findUser(user); 
    if(users.isEmpty()){
      msg.put("flag", "0");
    }else{
      UserInfo tuser = (UserInfo)users.get(0);
      msg.put("flag", "1");
      msg.put("userName", tuser.getName());
    }
//    msg.put("flag", "success");
//
//    Map<String, String> usert = new HashMap<>();
//    usert.put("name", "张三");
//    usert.put("age", "34");
//    getMsg().put("user", usert);
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

  /**
   * @param userDao the userDao to set
   */
  public void setUserDao(UserInfoDAO userDao) {
    this.userDao = userDao;
  }

}
