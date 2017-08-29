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
    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>

  </head>
  <body>
        <select name="ot2fp.isMatch" id="ot2IsMatch" multiple="multiple">
          <option value="">All</option>
          <option value="99">不成功</option>
          <option value="0">未匹配</option>
          <option value="4">RC3</option>
          <option value="2">CVS</option>
          <option value="5">小行星</option>
          <option value="6">OT2历史</option>
          <option value="3">其他</option>
        </select>
    <script type="text/javascript">
      $('#ot2IsMatch').multiselect();
    </script>
  </body>
</html>
