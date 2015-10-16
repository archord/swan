<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:url var="otDetail" action="get-ot-detail" namespace="/"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>OT分布实时概览图-XY</title>
    <link href="<%=request.getContextPath()%>/styles/examples.css" rel="stylesheet" type="text/css">
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/excanvas.compiled.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.flot.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.flot.categories.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.flot.resize.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/ot_layout_xy.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
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
        var dataurl = "<%=request.getContextPath()%>/get-ot-xy-list.action?dateStr=" + dateStr;
        //$.ajax({url: dataurl, type: "GET", dataType: "json", success: onDataReceived, error:aa});
        $.ajax({url: dataurl, type: "GET", success: onDataReceived, error:errorLog});
      }
      
      function errorLog(){
        console.log("server error");
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
      <div id="ot-show">
        <div id="ot-show-title">
          <span>OT分布实时概览图</span>
          <input id="showDate" name="showDate" type="text" readOnly="true" style="font-size: 14px;width:80px;height:20px" onClick="WdatePicker()" onchange="onDateChange()"/>
          <div id="ot-legend"></div>
        </div>
        <div id="ot-show-container">
          <div id="placeholder1" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder2" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder3" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder4" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder5" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder6" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder7" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder8" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder9" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder10" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder11" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
          <div id="placeholder12" class="demo-placeholder" style="float:left; width:23.5%; height: 34%; margin:0px;"></div>
        </div>
      </div>
      <div id="sysinfo">
        <div id="sysinfo-title">
          <span>系统磁盘容量监测</span>
        </div>
        <div id="sys-disk-usage"></div>
      </div>
    </div>
    <div id="tooltip" style="position: absolute; display: none; border: 1px solid rgb(255, 221, 221); padding: 2px; opacity: 0.8; background-color: rgb(255, 238, 238);"></div>
    <div id="footer">
      <p style="text-align: center;">
        版权所有 <a href="http://svom.bao.ac.cn" title="OpenSource CSS Layout">NAOC GWAC</a>
      </p>
    </div>
  </body>
</html>