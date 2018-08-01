<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC系统文件上传页面</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC系统文件上传页面" />
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
        <form action="${pageContext.request.contextPath}/gction/manualFileUpload.action" id="uploadFileAction" method="post" enctype="multipart/form-data">
          <div class="col-xs-4 col-sm-4 col-md-4 ">
            <input type="file" name="fileUpload" id="fileUpload"class="Wdate">
          </div>
          <div class="col-xs-4 col-sm-4 col-md-6 ">
            <span>
              注释&nbsp;<input name="comments" id="comments" type="text"  class="Wdate" style="width: 300px;height:30px"/>
            </span>
          </div>
          <div class="col-xs-4 col-sm-4 col-md-2 ">
            <input type="button" value="上传" class="btn btn-primary" id="uploadFileBtn"/>
          </div>
        </form>
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
