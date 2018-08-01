<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:url var="otDetail" action="get-ot-detail" namespace="/"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <link href="<%=request.getContextPath()%>/resource/styles/examples.css" rel="stylesheet" type="text/css">
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/excanvas.compiled.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/jquery.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/jquery.flot.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/ot_layout_sphere.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">

      var requestTime = 15000;
      var timerId;
      var openUrl = "<s:property value="otDetail"/>";

      function update() {
        var curDate = new Date().Format("yyyy-MM-dd");
        var dateStr = $("#showDate").val();
        if(dateStr===curDate){
          dateStr="";
        }
        var dataurl = "<%=request.getContextPath()%>/gction/get-ot-xy-list.action?dateStr=" + dateStr;
        $.ajax({url: dataurl, type: "GET", dataType: "json", success: onDataReceived});
      }

      function onDateChange() {
        if (timerId) {
          clearInterval(timerId);
        }
        update();
        //timerId = setInterval(update, requestTime);
      }

      $(function() {
        $("#showDate").val(new Date().Format("yyyy-MM-dd"));
        update();
        timerId = setInterval(update, requestTime);
      });
    </script>
  </head>
  <body>

    <div id="content">
        <div id="ot-show-title">
          <span>OT分布实时概览图——天球坐标</span>
          <input id="showDate" name="showDate" type="text" readOnly="true" style="font-size: 14px;width:100px;height:20px" onClick="WdatePicker()" onchange="onDateChange()"/>
        </div>
      <div class="demo-container" style="width:98%; height: 90%;">
        <div id="placeholder1" class="demo-placeholder" style="float:left; width:100%; height: 100%;"></div>
      </div>
    </div>
    
    <div id="tooltip" style="position: absolute; display: none; border: 1px solid rgb(255, 221, 221); padding: 2px; opacity: 0.8; background-color: rgb(255, 238, 238);"></div>
  </body>
</html>