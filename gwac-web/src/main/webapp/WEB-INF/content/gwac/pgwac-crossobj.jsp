<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>OT实时列表</title>
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
        <input type="hidden" id="otName" value="<%=request.getParameter("otName")%>"/>
      </div>      
      <div class="row ot-list-top">
        <form action="${pageContext.request.contextPath}/get-cross-object-list.action" id="ot2QueryAction" method="post">
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>日期</h4>
            <input type="text" style="width: 85px; height: 34px;" name="ot2qp.dateStr" id="dateStr" onclick="WdatePicker({dateFmt: 'yyMMdd',onpicked: loadCrossTask})" class="Wdate timeinput form-control"/>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>任务名称</h4>
            <select name="ot2qp.crossTaskId" id="crossTaskId"  class="form-control">
              <option value="0">未选择</option></select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>匹配状态</h4>
            <select name="ot2qp.isMatch" id="ot2IsMatch" multiple="multiple" class="ot2QueryParameter">
              <option value="0">未匹配</option>
              <option value="1">匹配不成功</option>
              <option value="2">匹配成功</option>
              <option value="3">OT2数目异常</option>
            </select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>匹配类别</h4>
            <select name="ot2qp.matchType" id="ot2MatchType" multiple="multiple" class="ot2QueryParameter">
              <option value="cvs_match">CVS</option>
              <option value="rc3_match">RC3</option>
              <option value="minor_planet_match">小行星</option>
              <option value="ot2_his_match">OT2历史</option>
              <option value="other_match">其他类型</option>
              <option value="usno_match">USNO</option>
            </select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>分类标识</h4>
            <select name="ot2qp.otType" id="ot2Type" multiple="multiple" class="ot2QueryParameter"></select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>CCD</h4>
            <select name="ot2qp.telscope" id="ot2Ccd" multiple="multiple" class="ot2QueryParameter">
              <option value="1">011</option>
              <option value="2">012</option>
              <option value="3">013</option>
              <option value="4">014</option>
              <option value="5">015</option>
              <option value="6">021</option>
              <option value="7">022</option>
              <option value="8">023</option>
              <option value="9">024</option>
              <option value="10">025</option>
              <option value="11">031</option>
              <option value="12">032</option>
              <option value="13">033</option>
              <option value="14">034</option>
              <option value="15">035</option>
              <option value="16">041</option>
              <option value="17">042</option>
              <option value="18">043</option>
              <option value="19">044</option>
              <option value="20">045</option>
            </select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>回看</h4>
            <select name="ot2qp.lookBackResult" id="lookBackResult" multiple="multiple" class="ot2QueryParameter">
              <option value="0">未处理</option>
              <option value="1">OT</option>
              <option value="2">FOT</option>
              <option value="3">FNBG</option>
              <option value="4">FOBJ</option>
              <option value="5">FSUB</option>
              <option value="6">FRMS</option>
              <option value="7">FWHM</option>
              <option value="8">FMCH</option>
              <option value="9">FHOT</option>
            </select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>回看2</h4>
            <select name="ot2qp.lookBackCnn" id="lookBackCnn" class="form-control">
              <option value="">未处理</option>
              <option value="1">1.0</option>
              <option value="0.9">0.9</option>
              <option value="0.8">0.8</option>
              <option value="0.7">0.7</option>
              <option value="0.6">0.6</option>
              <option value="0.5">0.5</option>
              <option value="0.4">0.4</option>
              <option value="0.3">0.3</option>
              <option value="0.2">0.2</option>
              <option value="0.1">0.1</option>
              <option value="0.05">0.05</option>
            </select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>星等变化</h4>
            <select name="ot2qp.magDiffAbs" id="magDiffAbs" class="form-control">
              <option value="0">ALL</option>
              <option value="3">大于3</option>
              <option value="2">大于2</option>
              <option value="1">大于1</option>
            </select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 " title="匹配不成功且回看为OT的会自动后随">
            <h4>自动后随</h4>
            <input id="autoFollowUp" type="checkbox" />
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>自动刷新</h4>
            <input id="ot2ListTableAutoRefresh" type="checkbox"/>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>&nbsp;</h4>
            <input type="button" value="查询" class="btn btn-primary" id="ot2QueryBtn"/>
          </div>
        </form>
      </div>
      <div class="row ot-list-body">
        <div id="ot-list">
          <table id="ot-list-table" class="display" cellspacing="0" width="100%">
            <thead><tr><th></th><th>ID</th><th>时间</th><th>RA</th><th>DEC</th><th>模板X</th><th>模板Y</th><th>记录总数</th>
                <th>分类标识</th><th>回看</th><th>回看2</th><th>小行星</th><th>OT2</th><th>USNO</th><th>RC3</th><th>后随次数</th>
                <th>CVS</th><th>Other</th></tr></thead>
            <tfoot><tr><th></th><th>ID</th><th>时间</th><th>RA</th><th>DEC</th><th>模板X</th><th>模板Y</th><th>记录总数</th>
                <th>分类标识</th><th>回看</th><th>回看2</th><th>小行星</th><th>OT2</th><th>USNO</th><th>RC3</th><th>后随次数</th>
                <th>CVS</th><th>Other</th></tr></tfoot>
          </table>
        </div>                                   
      </div>
      <div id="alarm-player" ></div>
      <img id='ot2lbcnn' style="position: fixed;display:none;"/>
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.jplayer.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/crossobj_list.js"></script>

  </body>
</html>
