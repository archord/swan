<%-- 
    Document   : observing
    Created on : 2017-8-18, 10:09:04
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GWAC望远镜观测控制</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css">
    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <style type="text/css">  
      html {
        height: 99%;
      }
      body {
        min-height: 98%;
      }
      .obs_plan_table {width:98%}
      .obs_plan {  
        width:98%;  
        height: 98%;
        overflow:auto;  
        white-space: nowrap;
      }  
      select {
        width: 100px;
        border: 1px solid #555;
        padding: 0.5em;
        font-size: 15px;
        line-height: 1.2em;
        background: #fff;
        -webkit-appearance: none;
        -webkit-box-shadow: 1px 1px 1px #fff;
        -webkit-border-radius: 0.5em;
      }
      .tab_container{
        width:95%; 
        text-align: left;
      }
      .manual_container_tel{
        border: 3px solid #c5c5c5;
        margin: 0 0 10px 0;
        padding: 5px;
      }
      .manual_container{
        border: 3px solid #c5c5c5;
        min-height: 500px;
        padding: 5px;
      }
      .manual_container_col{padding: 0 5px}
      .background {fill: #000;}
      line {stroke: #000;}
    </style>  
  </head>
  <body>

    <div id="tabs" style="width:98%; height:95%;position: absolute">
      <ul>
        <li><a href="#tabs-1">状态监控</a></li>
        <li><a href="#tabs-2">常规模式</a></li>
        <li><a href="#tabs-3">手动模式</a></li>
        <li><a href="#tabs-4">同步模式</a></li>
        <li><a href="<%=request.getContextPath()%>/control/pctl-observing-tab5.action">动态页面</a></li>
      </ul>
      <div id="tabs-1" class="tab_container">
        <%@include file="pctl-observing-tab1-1.jsp"%>
      </div>
      <div id="tabs-2" class="tab_container">
        <%@include file="pctl-observing-tab2.jsp"%>
      </div>
      <div id="tabs-3" class="tab_container">
        <%@include file="pctl-observing-tab3.jsp"%>
      </div>
      <div id="tabs-4" class="tab_container">
        <%@include file="pctl-observing-tab4.jsp"%>
      </div>
    </div>

    <script>
      $(function() {
        $("#tabs").tabs({
          beforeLoad: function(event, ui) {
            ui.jqXHR.fail(function() {
              ui.panel.html(
                      "Couldn't load this tab. We'll try to fix this as soon as possible. " +
                      "If this wouldn't be a demo.");
            });
          }
        });
        $('#tabs').tabs({active: 0});
      });
    </script>

  </body>
</html>
