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
    <title>GWAC后随观测任务管理</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC观测计划列表页面" />
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
        <span style="font-size: 18px">后随观测任务列表</span>
      </div>
      <div style="width:100%; text-align: center; margin-top: 20px;">
        <form action="${pageContext.request.contextPath}/get-fllowup-list.action" id="getUnDonePlanForm" method="post">
          <span style="font-size: 18px">&nbsp;&nbsp;&nbsp;&nbsp;</span>
          <select id="executeStatus" name="executeStatus" style="font-size: 14px; height: 30px;">
            <option value="a">全部任务</option>
            <option value="0" selected="selected">未执行任务</option>
            <option value="1">已执行任务</option>
            <option value="2">过时</option>
            <option value="3">删除</option>
          </select>
          <span style="font-size: 18px">&nbsp;&nbsp;&nbsp;&nbsp;</span>
          <select id="executeStatus" name="executeStatus" style="font-size: 14px; height: 30px;">
            <option value="a">分类</option>
            <option value="0" selected="selected">NewOT</option>
            <option value="1">MiniOT</option>
            <option value="2">GWACOT</option>
            <option value="3">CheckOT</option>
          </select>
          <span style="font-size: 18px">&nbsp;&nbsp;&nbsp;&nbsp;</span>
          <input type="button" value="增加" class="btn btn-primary" id="addFuObsBtn"/>
          <span style="font-size: 18px">&nbsp;&nbsp;&nbsp;&nbsp;</span>
          <input type="button" value="修改" class="btn btn-primary" id="editFuObsBtn"/>
          <span style="font-size: 18px">&nbsp;&nbsp;&nbsp;&nbsp;</span>
          <input type="button" value="删除" class="btn btn-primary" id="delFuObsBtn"/>
          <span style="font-size: 18px">&nbsp;&nbsp;&nbsp;&nbsp;</span>
          <input type="button" value="配置默认后随参数" class="btn btn-primary" id="delFuObsBtn"/>
        </form>
      </div>
      <div id="ot-list">
        <table id="obs-plan-table" class="display" cellspacing="0" width="100%">
          <thead><tr><th></th><th>开始时间</th><th>后随名称</th><th>OT2</th><th>望远镜</th><th title="(RA-DEC)J2000">位置</th><th>曝光时间</th><th>帧数</th>
              <th>滤光片</th><th>优先级</th><th>触发类型</th><th>观测者</th><th>状态</th><th>结果</th><th>登记时间</th></tr></thead>

          <tfoot><tr><th></th><th>开始时间</th><th>后随名称</th><th>OT2</th><th>望远镜</th><th title="(RA-DEC)J2000">位置</th><th>曝光时间</th><th>帧数</th>
              <th>滤光片</th><th>优先级</th><th>触发类型</th><th>观测者</th><th>状态</th><th>结果</th><th>登记时间</th></tr></tfoot>
        </table>
      </div>  
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/followup-manage.js"></script>


  </body>
</html>
