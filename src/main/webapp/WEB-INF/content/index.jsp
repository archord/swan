<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%> 

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<!DOCTYPE html>
<html lang="ch"> 
  <head>  
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content=",国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />

    <title>GWAC - <s:text name="showcase.version"/></title>

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
    <link href="<s:url value="/styles/gwac-style.css" />" rel="stylesheet" type="text/css" />
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
  </head>
  <body>

    <header class="ui-widget-header">
      <div class="ym-wrapper">
        <div class="ym-wbox" style="padding: 5px 0 0 0;">
          <div class="ym-grid linearize-level-1">
            <div class="ym-g75 ym-gl" >
              <div style="padding-left: 9px;">
              <h1 class="ui-state-default" style="background: none; border: none; margin: 0;">GWAC信息展示平台</h1>
              <h5 class="ui-state-default" style="background: none; border: none;"> <s:text name="showcase.version"/></h5>
              </div>
            </div>
<!--            <div id="themebox" class="ym-g25 ym-gr">
              <s:form id="themeform" action="index" theme="simple">
                <div>
                  <s:select id="selected_theme" name="theme" list="themes" emptyOption="true" onchange="changeTheme(this.value);"/><br/>
                  <s:checkbox id="google" name="google" onclick="$.publish('themeformTopic');"/>
                  <label for="google" style="padding: 3px;">Load jQuery from Google CDN</label><br/>
                  <sj:submit id="submitThemeform" formIds="themeform" listenTopics="themeformTopic" cssStyle="display: none;"/>
                </div>
              </s:form>
              <img id="myDefaultIndicator" src="gwac_images/ajax-loader.gif" alt="Loading..." style="display:none"/>
            </div>-->
          </div>
        </div>
      </div>
    </header>

    <nav id="nav" class="ui-widget-header">
      <div class="ym-wrapper">
        <div class="ym-hlist ui-widget-header">
          <ul id="navlist" style="font-size: 1.5em">
            <li><s:url var="mainurl" action="menu-main" namespace="/" />
              <sj:a id="menumain" href="%{mainurl}" targets="menu">首页</sj:a></li>
            <li><s:url var="gwacurl" action="menu-gwac" namespace="/" />
              <sj:a id="menugwac" href="%{gwacurl}" targets="menu">GWAC数据</sj:a></li>
            <li><s:url var="questionurl" action="menu-question" namespace="/" />
              <sj:a id="menuquestion" href="%{questionurl}" targets="menu">问题反馈</sj:a></li>
            <li><a href="http://svom.bao.ac.cn:3000/">项目管理</a></li>
            <li><a href="#">数据下载</a></li>
            <li><a href="#">网站导航</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div id="main">
      <div class="ym-wrapper">
        <div class="ym-wbox">
          <section class="ym-grid linearize-level-1">
            <aside class="ym-g25 ym-gl">
              <s:url var="menuurl" action="menu-gwac" namespace="/" />
              <sj:div id="menu" href="%{menuurl}" cssClass="ym-wbox">
              </sj:div>
            </aside>
            <article class="ym-g75 ym-gr content">
              <s:url var="remotelinkurl" action="gwac/pgwac-system" namespace="/" />
              <sj:div id="content" href="%{remotelinkurl}" cssClass="ym-wbox">
              </sj:div>
            </article>
          </section>
        </div>
      </div>
    </div>

    <footer>
      <div class="ym-wrapper">
        <div class="ym-wbox">
          <p style="text-align: center;">
            版权所有 <a href="http://svom.bao.ac.cn" title="OpenSource CSS Layout">NAOC GWAC</a>
          </p>
        </div>
      </div>
    </footer>

  </body>
</html>
