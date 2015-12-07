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
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>
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
        <form action="${pageContext.request.contextPath}/get-ot-level2-list2.action" id="ot2QueryAction" method="post">
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>数据处理方式</h4>
            <select name="ot2qp.processType" id="ot2ProcessType" multiple="multiple">
              <option value="1">星表匹配</option>
              <option value="8">图像相减</option>
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>匹配状态</h4>
            <select name="ot2qp.isMatch" id="ot2IsMatch" multiple="multiple">
              <option value="0">未匹配</option>
              <option value="1">匹配不成功</option>
              <option value="2">匹配成功</option>
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>匹配类别</h4>
            <select name="ot2qp.matchType" id="ot2MatchType" multiple="multiple">
              <option value="cvs_match">CVS</option>
              <option value="rc3_match">RC3</option>
              <option value="minor_planet_match">小行星</option>
              <option value="ot2_his_match">OT2历史</option>
              <option value="other_match">其他类型</option>
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>分类标识</h4>
            <select name="ot2qp.otType" id="ot2Type" multiple="multiple">
              <option value="1">假OT</option>
              <option value="2">小行星</option>
              <option value="3">移动目标</option>
              <option value="4">鬼像</option>
              <option value="5">坏像素</option>
              <option value="6">坏像列</option>
              <option value="7">热像素</option>
              <option value="8">OT候选体</option>
              <option value="9">超新星</option>
              <option value="10">GRB</option>
              <option value="11">耀发候选体</option>
            </select>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <h4>CDD</h4>
            <select name="ot2qp.telscope" id="ot2Ccd" multiple="multiple">
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
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <h4>自动刷新</h4>
            <input name="autoRefresh" id="ot2ListTableAutoRefresh" type="checkbox"/>
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
            <thead><tr><th>ID</th><th>OT名</th><th>RA</th><th>DEC</th><th>模板X</th><th>模板Y</th><th>首帧标识字符串</th><th>记录总数</th>
                <th>RC3</th><th>小行星</th><th>CVS</th><th>Other</th><th>OT2</th><th>USNO</th><th>前N分钟</th><th>后随次数</th><th>分类标识</th></tr></thead>
            <tfoot><tr><th>ID</th><th>OT名</th><th>RA</th><th>DEC</th><th>模板X</th><th>模板Y</th><th>首帧标识字符串</th><th>记录总数</th>
                <th>RC3</th><th>小行星</th><th>CVS</th><th>Other</th><th>OT2</th><th>USNO</th><th>前N分钟</th><th>后随次数</th><th>分类标识</th></tr></tfoot>
          </table>
        </div>                                   
      </div>

    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/ot_list.js"></script>

  </body>
</html>
