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
    <title>GWAC系统监控</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/pgwac-monitor.js"></script>
    <style>
      html {
        height: 99%;
        width: 100%;
        position: relative;
      }
      body {
        height: 98%;
        width: 98%;
        background: #000;
        position: relative;
      }
      #monitor-center {
        position: relative;
        float:left;
        display: block;
        width: 100%;
        height: 98%;
        background: #000;
      }
      #monitor-footer p{
        padding:0;
        margin: 0;
      }

      #monitor-footer{
        float:left;
        width: 100%;
        height:20px;
        display: block;
        position: relative;
        text-align: center;
        color:#FFF;
        padding:0;
        margin: 0;
        font-size: 12px;
      }
      .xlabel,.ylabel{
        font-size: 18px;
        fill: #FFF;
      }
      .gcell{
        background: #fff;
        fill:#ABABAB;
      }
      text.active {fill: red;}
    </style>
    <script>

      $(function() {
        var root = "<%=request.getContextPath()%>";
        var url = "";
        var tmonitor = $.tmonitor("#monitor-center", root, url);
        tmonitor.drawAll();
        tmonitor.updateStatus();
        setInterval(function() {
          tmonitor.updateStatus();
        }, 15*1000);
      });
    </script>
  </head>
  <body>
    <div style="display: none;">
      <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
    </div>   
    <div id="monitor-center"></div>
    <div id="monitor-footer">
      <p>每个设备的状态用背景颜色标示：
        <span style="background-color: #FFF;color:#000;">白（上线）</span>、
        <span style="background-color: #00EE00">绿（正常，1分钟以内）</span>、
        <span style="background-color: #EEAD0E">橙（有故障，1分钟到10分钟）</span>、
        <span style="background-color: #CD2626">红（损坏，大于10分钟）</span>、
        <span style="background-color: #ABABAB">灰（下线）</span></p>
    </div>
  </body>
</html>

