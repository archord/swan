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
    <title>增加后随观测任务</title>
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

    <div class="container-fluid">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>      
      <form action="${pageContext.request.contextPath}/gction/get-ot-level2-list2.action" id="ot2QueryAction" method="post">
        <div class="row followup-form">
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>后随名称</h4>
            <input type="text" name="ot2fp.ra" id="fuRa" value=""  class="form-control"/>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>OT名称</h4>
            <select name="ot2qp.isMatch" id="ot2IsMatch" class="form-control select select-primary">
              <option value="0">未匹配</option>
              <option value="1">匹配不成功</option>
              <option value="2">匹配成功</option>
            </select>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>赤经</h4>
            <input type="text" name="ot2fp.ra" id="fuRa" value=""  class="form-control"/>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>赤纬</h4>
            <input type="text" name="ot2fp.dec" id="fuDec" value="" class="form-control"/>
          </div>
        </div>
        <div class="row followup-form">
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>后随望远镜</h4>
            <select name="ot2fp.telescope" id="telescope" class="form-control select select-primary" data-toggle="select">
              <option value="1" selected>60公分</option>
              <option value="2">30公分</option>
            </select>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>滤光片</h4>
            <select name="ot2fp.filter" id="filter" class="form-control select select-primary" data-toggle="select">
              <option value="0">未匹配</option>
              <option value="1">匹配不成功</option>
              <option value="2">匹配成功</option>
            </select>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>曝光时间</h4>
            <input type="text" name="ot2fp.ra" id="fuRa" value=""  class="form-control"/>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>帧数</h4>
            <input type="text" name="ot2fp.dec" id="fuDec" value="" class="form-control"/>
          </div>
        </div>
        <div class="row followup-form">
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>优先级</h4>
            <input type="text" name="ot2fp.ra" id="fuRa" value=""  class="form-control"/>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>执行方式</h4>
            <input type="text" name="ot2fp.dec" id="fuDec" value="" class="form-control"/>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>开始时间</h4>
            <input type="text" name="ot2fp.ra" id="fuRa" value=""  class="form-control"/>
          </div>
          <div class="col-xs-6 col-sm-3 col-md-3 ">
            <h4>结束时间</h4>
            <input type="text" name="ot2fp.ra" id="fuRa" value=""  class="form-control"/>
          </div>
        </div>
        <div class="row followup-form">
          <div class="col-xs-12 col-sm-12 col-md-12 ">
            <input type="button" value="后随" class="btn btn-primary" id="followBtn"/>
          </div>
        </div>
      </form>
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/pctl-observation-plan.js"></script>


  </body>
</html>
