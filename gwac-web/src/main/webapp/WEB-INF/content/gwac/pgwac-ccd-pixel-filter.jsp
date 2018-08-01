<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>CCD坏像素过滤</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
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
        <form action="${pageContext.request.contextPath}/gction/ccd-pixel-filter-add.action" id="ccdFilterAction" method="post">
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>CDD</h4>
            <select name="ccdFilter.dpmId" >
              <option value="1">M01</option>
              <option value="2">M02</option>
              <option value="3">M03</option>
              <option value="4">M04</option>
              <option value="5">M05</option>
              <option value="6">M06</option>
              <option value="7">M07</option>
              <option value="8">M08</option>
              <option value="9">M09</option>
              <option value="10">M10</option>
              <option value="11">M11</option>
              <option value="12">M12</option>
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>图像坐标X最小值</h4>
            <input type="text" style="width: 80px; " name="ccdFilter.minX" value="0.0"/>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>图像坐标Y最小值</h4>
            <input type="text" style="width: 80px; " name="ccdFilter.minY"  value="0.0"/>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>图像坐标X最大值</h4>
            <input type="text" style="width: 80px; " name="ccdFilter.maxX"  value="10.0" />
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>图像坐标Y最大值</h4>
            <input type="text" style="width: 80px; " name="ccdFilter.maxY"  value="10.0"/>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-1 ">
            <h4>OT类型</h4>
            <select name="ccdFilter.otTypeId">
              <option value="12">灰尘</option>
              <option value="5">坏像素</option>
              <option value="6">坏像列</option>
              <option value="7">热像素</option>
              <option value="17">凹槽</option>
              <option value="4">鬼像</option>
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-1 ">
            <h4>&nbsp;</h4>
            <input type="button" value="添加" class="btn btn-primary" id="ccdFilterBtn"/>
          </div>
        </form>
      </div>
      <div class="row ot-list-body">
        <div id="ot-list">
          <table id="ccd-filter-table" class="display" cellspacing="0" width="100%">
            <thead><tr><th>ID</th><th>CCD</th><th>minX</th><th>minY</th><th>maxX</th><th>maxY</th><th>OT类型</th><th>删除</th></tr></thead>
            <tfoot><tr><th>ID</th><th>CCD</th><th>minX</th><th>minY</th><th>maxX</th><th>maxY</th><th>OT类型</th><th>删除</th></tr></tfoot>
          </table>
        </div>                                   
      </div>

    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/ot_filter.js"></script>

  </body>
</html>
