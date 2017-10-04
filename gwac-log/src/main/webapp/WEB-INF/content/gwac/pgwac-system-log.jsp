<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC系统日志页面</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC系统日志页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/jquery.dataTables.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/gwac-ui.css" rel="stylesheet">
  </head>
  <body>
    <div class="container-fluid">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>      
      
      <div class="row ot-list-top">
        <form action="${pageContext.request.contextPath}/get-system-log-list.action" id="sysLogQueryAction" method="post">
          <div class="col-xs-4 col-sm-4 col-md-3 ">
            <h4>开始时间</h4>
            <input name="dateStart" id="date_start" type="text" onclick="WdatePicker({doubleCalendar:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" style="width: 200px;height:30px"/>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-3 ">
            <h4>结束时间</h4>
            <input name="dateEnd" id="date_end" type="text" onclick="WdatePicker({doubleCalendar:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" style="width: 200px;height:30px"/>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>日志编码</h4>
            <select name="logCode" id="log_code" multiple="multiple" class="ot2QueryParameter">
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>IP地址</h4>
            <select name="msgIp" id="log_ip" class="ot2QueryParameter"  style="width: 100px;height:30px">
              <option value="">请选择</option>
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>&nbsp;</h4>
            <input type="button" value="查询" class="btn btn-primary" id="sysLogQueryBtn"/>
          </div>
        </form>
      </div>
          
      <div class="row ot-list-body">
        <div id="ot-list">
          <table id="ot-list-table" class="display" cellspacing="0" width="100%">
            <!--<thead><tr><th>ID</th><th>日志日期</th><th>内容</th><th>日志类型</th><th>日志编码</th></tr></thead>-->
            <thead><tr><th>ID</th><th>日志日期</th><th>内容</th><th>日志编码</th><th>IP地址</th></tr></thead>
            <tfoot><tr><th>ID</th><th>日志日期</th><th>内容</th><th>日志编码</th><th>IP地址</th></tr></tfoot>
          </table>
        </div>                                   
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.jplayer.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/system_log.js"></script>

  </body>
</html>
