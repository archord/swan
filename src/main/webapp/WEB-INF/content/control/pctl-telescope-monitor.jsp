<%-- 
    Document   : rect
    Created on : 2016-8-9, 18:20:23
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="ocks-org do-not-copy">
  <head>
    <meta charset="utf-8">
    <title>GWAC望远镜监控页面</title>
    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/telescope_monitor.js"></script>
    <style>
      html {
        height: 99%;
      }
      body {
        min-height: 98%;
        background: #000;
      }
      .background {fill: #000;}
      line {stroke: #000;}
      text.active {fill: red;}
      #main{width:100%;padding: 5px 10px 5px 10px;}
      .axisLabel{fill: #fff; stroke: none;}
    </style>
    <script>

      $(function() {
        var root = "<%=request.getContextPath()%>";
        var url = "get-ot-xy-list.action?dateStr=";
        var tmonitor = $.tmonitor("body", root, url);
        tmonitor.drawTele();
      });
    </script>
  </head>
  <body>

  </body>
</html>

