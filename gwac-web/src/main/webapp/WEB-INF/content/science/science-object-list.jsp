<%-- 
    Document   : observing
    Created on : 2017-8-18, 10:09:04
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC科学目标页面</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC科学目标页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/jquery.dataTables.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/gwac-ui.css" rel="stylesheet">
  </head>
  <body>

    <div style="display: none;">
      <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
    </div>      
    <div id="tabs-monitor-table-div" style="width:100%; height:100%;margin:0 auto;border:0;">
      <div style="width:100%; text-align: center; margin-top: 20px;">
          <span style="font-size: 18px">科学目标列表</span>&nbsp;<img id="setFupParm" title="点击设置后随参数" src="${pageContext.request.contextPath}/resource/images/Settings-icon.png"/>
      </div>
      
      <div id="ot-list" style="margin: 0px 30px">
        <table id="sci-obj-table" class="display" cellspacing="0" width="100%">
          <thead><tr><th></th><th>名称</th><th>发现时间</th><th>后随星等</th><th title="后随第一帧USNO">R2</th><th title="后随第一帧USNO">B2</th><th title="后随第一帧USNO">I</th>
              <th title="(RA-DEC)J2000">目标位置(60)</th><th>后随次数</th><th>类型</th><th>假的</th></tr></thead>

          <tfoot><tr><th></th><th>名称</th><th>发现时间</th><th>后随星等</th><th title="后随第一帧USNO">R2</th><th title="后随第一帧USNO">B2</th><th title="后随第一帧USNO">I</th>
              <th title="(RA-DEC)J2000">目标位置(60)</th><th>后随次数</th><th>类型</th><th>假的</th></tr></tfoot>
        </table>
      </div>  
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/science-object-list.js"></script>


  </body>
</html>
