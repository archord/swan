<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<!DOCTYPE html>
<html lang="ch"> 
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>

    <title>GWAC信息展示平台 - <s:text name="showcase.version"/></title>

    <s:if test="%{theme == 'showcase' || theme == null}">
      <sj:head debug="true" compressed="true" jquerytheme="showcase" customBasepath="themes" 
               loadFromGoogle="%{google}" ajaxhistory="%{ajaxhistory}" 
               defaultIndicator="myDefaultIndicator" defaultLoadingText="Please wait ..."/>
    </s:if>
    <s:else>
      <sj:head debug="true" compressed="true" jquerytheme="%{theme}" loadFromGoogle="%{google}" 
               ajaxhistory="%{ajaxhistory}" defaultIndicator="myDefaultIndicator" 
               defaultLoadingText="Please wait ..."/>
    </s:else>

    <link href="<s:url value="/styles/flexible-grids.css" />" rel="stylesheet" type="text/css" />
    <!--[if lte IE 7]>
    <link href="<s:url value="/yaml/core/iehacks.min.css" />" rel="stylesheet" type="text/css" />
    <![endif]-->

    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- This files are needed for AJAX Validation of XHTML Forms -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/struts/utils.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/struts/xhtml/validation.js"></script>

    <!-- This file includes necessary functions/topics for validation and all topic examples -->
    <script type="text/javascript" src="<s:url value="/js/showcase.js" />"></script>
    <!-- Extend the Struts2 jQuery Plugin with an richtext editor -->
    <script type="text/javascript" src="<s:url value="/js/extendplugin.js" />"></script>

    <link href="${pageContext.request.contextPath}/resource/css/font-oxygen/oxygen.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/resource/css/sliding-menu.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/resource/css/gwac-style.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/resource/mason/mason.css" rel="stylesheet" type="text/css" />

  </head>
  <body>
    <!-- Navigation -->
    <nav id="slide-menu">
      <ul>
        <li class="events"><s:url var="url1" action="gwac/pgwac-system"/>
          <sj:a id="gwacsystemlink" href="%{url1}" targets="main_content">系统监测信息导航</sj:a></li>
        <li class="timeline"><s:url var="url6" action="gwac/pgwac-allot"/>
          <sj:a id="gwacallotlink" href="%{url6}" targets="main_content">二级OT查询</sj:a></li>
<!--        <li class="calendar"><s:url var="url8" action="manage/pfile-upload"/>
          <sj:a id="gwacuploadfilelink" href="%{url8}" targets="main_content">上传文件</sj:a></li>-->
        <li class="sep settings"><s:url var="url11" action="gwac/pupload-unstore"/>
          <sj:a id="puploadunstorelink" href="%{url11}" targets="main_content">未入库文件列表</sj:a></li>
        <li class="logout"><s:url var="url12" action="gwac/pupload-record"/>
          <sj:a id="puploadrecordlink" href="%{url12}" targets="main_content">上传文件历史记录</sj:a></li>
        </ul>
      </nav>
      <!-- Content panel -->
      <div id="main_page_content">
        <div class="menu-trigger"></div>
      <s:url var="remotelinkurl" action="gwac/pgwac-system" namespace="/" />
      <sj:div id="main_content" href="%{remotelinkurl}" cssClass="ym-wbox">
      </sj:div>
    </div>

    <script>
      (function() {
        var $body = document.getElementsByTagName("body")[0];
        var $menu_trigger = $body.getElementsByClassName('menu-trigger')[0];
        if (typeof $menu_trigger !== 'undefined') {
          $menu_trigger.addEventListener('click', function() {
            $body.className = $body.className == 'menu-active' ? '' : 'menu-active';
          });
        }
      }.call(this));
    </script>
  </body>
</html>