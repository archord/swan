<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>系统配置页面</title>
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

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>

    <script>
      $(function() {
        $('#backupDataButton').click(clickBackup);
//        $('#delDataButton').click(clickDelete);
        function clickDelete() {
          var queryUrl = "<%=request.getContextPath()%>/gction/databaseManage.action";
          var formData = "operation=delete";
          $.post(queryUrl, formData,
                  function(data) {
                    console.log(data);
                    alert(data.echo);
                  }, "json");
        }
        function clickBackup() {
          var queryUrl = "<%=request.getContextPath()%>/gction/databaseManage.action";
          var formData = "operation=backup&dateStr=" + $("#dateStr").val();
          $.post(queryUrl, formData,
                  function(data) {
                    console.log(data);
                    alert(data.echo);
                  }, "json");
        }
      });
    </script>
  </head>
  <body>
    <div class="container-fluid">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>
      <div class="row ot-list-top">
        <div class="col-xs-2 col-sm-2 col-md-2 ">
          <input type="text" value="" class="btn btn-primary"  placeholder="备份名称" name="dateStr" id="dateStr" />
        </div>
        <div class="col-xs-2 col-sm-2 col-md-1 ">
          <input type="button" value="备份数据" class="btn btn-primary" id="backupDataButton"/>
        </div>
        <div class="col-xs-6 col-sm-6 col-md-6 " style="text-align: left">
          1,正式数据，在16:50时错过备份的，手动点击备份。备份名称为空。<br/>
          2,正式数据再次处理，结果备份，必须填写备份名称。默认为日期后六位，如170118。备份请改为其他值，如170118a。
        </div>
      </div>
      <div class="row ot-list-top">
        <div class="col-xs-2 col-sm-2 col-md-2 "></div>
        <div class="col-xs-2 col-sm-2 col-md-1 ">
          <!--<input type="button" value="删除数据" class="btn btn-primary" id="delDataButton"/>-->
        </div>
        <div class="col-xs-6 col-sm-6 col-md-6 " style="text-align: left">
          测试数据，多次重复测试时，需要删除已经入库的测试数据，手动点击备份。
        </div>
      </div>

    </div>

  </body>
</html>
