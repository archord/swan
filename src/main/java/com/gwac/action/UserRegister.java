package com.gwac.action;

import com.gwac.model.UserInfo;
import com.gwac.service.UserInfoService;
import static com.opensymphony.xwork2.Action.SUCCESS;
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
import java.util.Date;
import java.util.List;

@InterceptorRef("jsonValidationWorkflowStack")
@Validations(requiredStrings = {
  @RequiredStringValidator(fieldName = "loginname", type = ValidatorType.FIELD, message = "用户名必须填写。"),
  @RequiredStringValidator(fieldName = "loginpassword", type = ValidatorType.FIELD, message = "密码必须填写。"),
  @RequiredStringValidator(fieldName = "loginpasswordrep", type = ValidatorType.FIELD, message = "重复密码必须填写。")},
	expressions = {
	  @ExpressionValidator(expression = "loginpassword.trim().equals(loginpasswordrep.trim()) == true", message = "两次输入密码必须相同。")
	})
public class UserRegister extends ActionSupport {

  private static final long serialVersionUID = 7968544374444173479L;
  private static final Log log = LogFactory.getLog(UserRegister.class);

  private String loginname;
  private String loginpassword;
  private String loginpasswordrep;
  private String address;
  private String echo;
  
  private UserInfoService userService;


  // simpleecho.jsp
  @Action(value = "registerAction", results = {
    @Result(location = "user/userlist.jsp", name = SUCCESS),
    @Result(location = "user/register.jsp", name = INPUT),
    @Result(location = "user/userlist.jsp", name = ERROR)})
  public String execute() throws Exception {

    UserInfo ui = new UserInfo();
    ui.setName(getLoginname());
    ui.setPassword(getLoginpassword());
    ui.setAddress(getAddress());
    ui.setRegisterDate(new Date());
    userService.addUser(ui);

    echo = getLoginname() + "注册成功！";
    log.info(echo);
    return SUCCESS;
  }

  public String getEcho() {
    return echo;
  }

  public String getLoginpassword() {
    return loginpassword;
  }

  public void setLoginpassword(String loginpassword) {
    this.loginpassword = loginpassword;
  }

  /**
   * @return the loginpasswordrep
   */
  public String getLoginpasswordrep() {
    return loginpasswordrep;
  }

  /**
   * @param loginpasswordrep the loginpasswordrep to set
   */
  public void setLoginpasswordrep(String loginpasswordrep) {
    this.loginpasswordrep = loginpasswordrep;
  }

  /**
   * @return the userService
   */
  public UserInfoService getUserService() {
    return userService;
  }

  /**
   * @param userService the userService to set
   */
  public void setUserService(UserInfoService userService) {
    this.userService = userService;
  }

  /**
   * @return the loginname
   */
  public String getLoginname() {
    return loginname;
  }

  /**
   * @param loginname the loginname to set
   */
  public void setLoginname(String loginname) {
    this.loginname = loginname;
  }

  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }
}
