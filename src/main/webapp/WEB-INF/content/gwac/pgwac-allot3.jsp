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
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>GWAC信息展示平台 - <s:text name="showcase.version"/></title>


    <link href="${pageContext.request.contextPath}/resource/css/sliding-menu2.css" rel="stylesheet" type="text/css" />
    
    <style>
    </style>
    

  </head>
  <body>
    <!-- Navigation -->
    <nav id="slide-menu">
      </nav>
      <!-- Content panel -->
      <div id="main_page_content">
        <div class="menu-trigger"></div>
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