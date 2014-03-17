<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<sj:menu id="subMenuAjaxLink">
  <s:url var="url1" action="main/main-intro"/>
  <sj:menuItem id="remotesimplelink" href="%{url1}" targets="content" title="SVOM项目简介" />

  <s:url var="url2" action="#"/>
  <sj:menuItem id="remoteeventlink" href="%{url2}" targets="content" title="团队介绍" />

  <s:url var="url3" action="#"/>
  <sj:menuItem id="remotetargetslink" href="%{url3}" targets="content" title="最新进展" />

  <s:url var="url4" action="user/grid"/>
  <sj:menuItem id="remoteformlink" href="%{url4}" targets="content" title="成果展示" />
  
  <s:url var="urlregisterlink" action="user/register"/>
  <sj:menuItem id="registerlink" href="%{urlregisterlink}" targets="content" title="用户注册" />

  <s:url var="url6" action="#"/>
  <sj:menuItem id="remotehighlightlink" href="%{url6}" targets="content" title="联系我们" />

  <s:url var="url7" action="user/userlist"/>
  <sj:menuItem id="userList" href="%{url7}" targets="content" title="用户列表" />
</sj:menu>
