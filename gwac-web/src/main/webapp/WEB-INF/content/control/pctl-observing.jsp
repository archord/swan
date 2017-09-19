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
    <style type="text/css"> 
      html, body {
        padding: 0;
        margin: 0;
        height: 100%;
        width:100%;  
        text-align: center;
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
        width:100%; 
        height: 93%;
        text-align: center;
        border:0;
      }
      .manual_container1{
        border: 3px solid #c5c5c5;
        margin: 0 0 10px 0;
        padding: 5px;
        text-align: left;
        //min-width: 430px;
      }
      .manual_container1 span{  
        white-space: nowrap;  /*强制span不换行*/
        display: inline-block;  /*将span当做块级元素对待*/
        overflow: hidden;  /*超出宽度部分隐藏*/
        text-overflow: ellipsis;  /*超出部分以点号代替*/
        line-height: 0.9;  /*数字与之前的文字对齐*/
      }
      .manual_container1 input{  
        margin: 2px;
      }
      .manual_container_title{
        text-align: center;
        font-size: 16px;
        background-color: #eee;
        margin:  -5px 0px 5px 0px;
      }
      .manual_container2{
        border: 3px solid #c5c5c5;
        min-height: 343px;
        padding: 5px;
        margin-bottom: 10px;
      }
      .manual_container1_col{padding: 5px}
      .background {fill: #000;}
      line {stroke: #000;}
      
    </style>  
  </head>
  <body>

    <div id="tabs" style="width:98%; height:100%;margin:0 auto;border:0;">
      <ul style="margin:12px;">
        <li><a href="#tabs-2">常规模式</a></li>
        <li><a href="#tabs-3">手动模式</a></li>
        <li><a href="#tabs-4">同步模式</a></li>
        <li><a href="<%=request.getContextPath()%>/control/pctl-observing-tab5.action">动态页面</a></li>
      </ul>
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

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
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
        $('#tabs').tabs({active: 1});
      });
    </script>

  </body>
</html>
