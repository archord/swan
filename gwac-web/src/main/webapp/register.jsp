<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div id="result" class="result ui-widget-content ui-corner-all">用户名和密码为必填项！</div>
<ul id="formerrors" class="errorMessage"  style="color: red"></ul>
<s:form id="formUserRegister" action="registerAction" theme="simple" cssClass="ym-form">
  <fieldset>
    <legend>新用户注册，<span style="color: red">*</span>为必填</legend>
    <div class="ym-fbox-text">
      <label for="echo"><span style="color: red">*</span>登陆名: <span id="loginnameError" style="color: red"></span></label>
      <s:textfield id="loginname" name="loginname" />
    </div>
    <div class="ym-fbox-text">
      <label for="echo"><span style="color: red">*</span>密码： <span id="loginpasswordError" style="color: red"></span></label>
      <s:password id="loginpassword" name="loginpassword" /> 
    </div>
    <div class="ym-fbox-text">
      <label for="echo"><span style="color: red">*</span>重复密码： <span id="loginpasswordrepError" style="color: red"></span></label>
      <s:password id="loginpasswordrep" name="loginpasswordrep" />
    </div>
    <div class="ym-fbox-button">
      <!--  targets="result"  content -->
      <sj:submit
        targets="content" button="true" validate="true"
        validateFunction="customeValidation"
        onBeforeTopics="removeErrors"
        onSuccessTopics="removeErrors"
        value="Submit" indicator="indicator" />
    </div>
  </fieldset>
</s:form>
<img id="indicator" src="gwac_images/indicator.gif" alt="Loading..." style="display:none"/>

