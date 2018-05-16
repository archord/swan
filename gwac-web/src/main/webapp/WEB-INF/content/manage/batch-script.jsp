<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC系统批处理管理</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC系统批处理管理" />
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

      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12">

          <form action="${pageContext.request.contextPath}/gwebend/observationPlanUpload.action" id="genObsPlanForm">
            <table>
              <tr><td>名称：</td>
                <td><input name="ttName" id="ttName" class="timeinput"/></td>
                <td>执行命令：</td>
                <td><input name="ra" id="ra" style="text-align: right;width: 200px;" value="0"/></td></tr>
              <tr><td>执行机器：</td><td>
                  <select height="30" name="unitId" id="unitId">
                    <option value="">未选择</option>
                    <option value="001">转台01</option>
                    <option value="002">转台02</option>
                  </select></td>
                  <td>执行方式：</td><td>
                  <select height="30" name="groupId" id="groupId">
                    <option value="1" selected>单次立即执行</option>
                    <option value="2">单次定时执行</option>
                    <option value="3">多次定时执行</option>
                  </select></td></tr>
              <tr><td>执行路径：</td>
                <td><input type="file" name="fileUpload" id="fileUpload"class="Wdate"></td>
                <td>执行文件：</td>
                <td><input name="dec" id="dec" style="text-align: right;width: 200px;" value="0"/></td></tr>
              <tr><td width="150px">开始日期：</td><td width="250px"><input name="localTime" id="localTime" class="timeinput"/></td>
                <td width="150px">结束日期：</td><td><input name="utcTime" id="utcTime" class="timeinput"/></td></tr>
              <tr><td width="150px">开始时间：</td><td width="250px"><input name="localTime" id="localTime" class="timeinput"/></td>
                <td width="150px">结束时间：</td><td><input name="utcTime" id="utcTime" class="timeinput"/></td></tr>
              <tr><td>注释：</td>
                <td colspan="3"><input name="ra" id="ra" style="text-align: right;width: 200px;" value="0"/></td></tr>
              <tr><td colspan="4" style="text-align:center;"><button type="button" id="genObsPlanBtn">添加任务</button></td></tr>
            </table>
          </form>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 ">
          <span id="uploadResult"></span>
        </div>
      </div>

      <div class="row ot-list-body">
        <div id="ot-list">
          <table id="ot-list-table" class="display" cellspacing="0" width="100%">
            <thead><tr><th>ID</th><th>文件名</th><th>注释</th><th>日期</th></tr></thead>
            <tfoot><tr><th>ID</th><th>文件名</th><th>注释</th><th>日期</th></tr></tfoot>
          </table>
        </div>                                   
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/manual_upload.js"></script>

  </body>
</html>
