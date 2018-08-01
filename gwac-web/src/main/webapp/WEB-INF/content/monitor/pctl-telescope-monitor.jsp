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
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/telescope_monitor.js"></script>
    <style>
      html {
        height: 99%;
        width: 100%;
        position: relative;
      }
      body {
        height: 98%;
        width: 98%;
        background: #FFF;
        position: relative;
      }
      #monitor-header{
        float:left;
        width: 100%;
        display: block;
        position: relative;
        text-align: center;
        font-size: 32px;
      }
      #monitor-footer{
        float:left;
        width: 100%;
        display: block;
        position: relative;
        text-align: center;
      }
      #monitor-center {
        position: relative;
        float:left;
        display: block;
        width: 100%;
        min-height: 80%;
        background: #FFF;
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
        var url = "gction/get-ot-xy-list.action?dateStr=";
        var tmonitor = $.tmonitor("#monitor-center", root, url);
        tmonitor.drawAll();
        setInterval(function() {
          tmonitor.randomEffects();
        }, 2000);
      });
    </script>
  </head>
  <body>
    <div id="monitor-header">GWAC望远镜监控页面</div>
    <div id="monitor-center"></div>
    <div id="monitor-footer">
      <p>每个设备的状态用背景颜色标示：白（上线）、<span style="background-color: #00EE00">绿（正常）</span>、
        <span style="background-color: #EEAD0E">橙（有故障）</span>、
        <span style="background-color: #CD2626">红（损坏）</span>、
        <span style="background-color: #ABABAB">灰（下线）</span></p>
    </div>

  </body>
</html>

