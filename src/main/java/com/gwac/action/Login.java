package com.gwac.action;

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

@InterceptorRef("jsonValidationWorkflowStack")
@Validations(requiredStrings = {
	@RequiredStringValidator(fieldName = "loginuser", type = ValidatorType.FIELD, message = "用户名必须填写。"),
	@RequiredStringValidator(fieldName = "loginpassword", type = ValidatorType.FIELD, message = "密码必须填写。") }, 
        expressions = { @ExpressionValidator(expression = "loginpassword.trim().equals('test') == true", message = "用户名或密码错误。")

})
public class Login extends ActionSupport {

    private static final long serialVersionUID = 7968544374444173511L;
    private static final Log log = LogFactory.getLog(Login.class);

    private String loginuser;
    private String loginpassword;
    private String echo;

    @Action(value = "/register", results = { @Result(location = "simpleecho.jsp", name = "success") })
    public String execute() throws Exception {
	echo = "Welcome " + loginuser;
	log.info(echo);

	return SUCCESS;
    }

    public String getEcho() {
	return echo;
    }

    public String getLoginuser() {
	return loginuser;
    }

    public void setLoginuser(String loginuser) {
	this.loginuser = loginuser;
    }

    public String getLoginpassword() {
	return loginpassword;
    }

    public void setLoginpassword(String loginpassword) {
	this.loginpassword = loginpassword;
    }
}
