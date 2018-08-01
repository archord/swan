<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>移动目标详细下载页面</title>
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
        loadDateStrList();
        $('#motDownloadButton').click(motDownloadClick);
        function motDownloadClick() {
          var dateStr = $("#dateStr").val();
          if (dateStr !== '0') {
            var downloadUrl = "<%=request.getContextPath()%>/gction/downloadmot.action?dateStr=" + dateStr;
            window.open(downloadUrl, '_blank');
          }
        }

        function loadDateStrList() {
          var queryUrl = "<%=request.getContextPath()%>/gction/get-datestr-list-json.action";
          $.ajax({
            type: "get",
            url: queryUrl,
            async: false,
            dataType: 'json',
            success: function(data) {
              var objs = data.objs;
              if (objs.length > 0) {
                $.each(objs, function(i, item) {
                  $('#dateStr').append($('<option>', {
                    value: item,
                    text: item,
                  }));
                });
              }
            }
          });
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
        <form id="motDownloadAction">
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <select name="dateStr" id="dateStr" class="ot1-input" data-toggle="select">
              <option value="0" selected>选择日期</option>
            </select>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <input type="button" value="下载" class="btn btn-primary" id="motDownloadButton"/>
          </div>
          <div class="col-xs-2 col-sm-2 col-md-1 ">
            <a href="${pageContext.request.contextPath}/gction/user-logout.action" title="点击注销">注销</a>
          </div>
        </form>
      </div>
      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 ">
          <div style="padding: 5px 0px 0px 12px">
          下载方法：选择日期，点击下载按钮，即可下载指定日期的移动目标。<br/>
          文件命名格式：选择日期_移动目标个数.zip。<br/>
          压缩文件中每个文件是一个移动目标的记录详细，各数据列之间用“,”隔开，数据列标签如下所示：<br/>
          赤经，赤纬，UTC时间，图像X坐标，图像Y坐标，CCD编号，图像顺序号<br/>
          注：最新的数据（前一天），需要在第二天下午5点以后才能下载
          </div>
        </div>
      </div>

    </div>

  </body>
</html>
