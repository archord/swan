<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>OT-<s:property value="otName"/>详细页面</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <!-- Loading Bootstrap 说明文档http://v3.bootcss.com/css/ -->
    <link href="${pageContext.request.contextPath}/resource/flatui/css/vendor/bootstrap.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="${pageContext.request.contextPath}/resource/flatui/css/flat-ui.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/flatui/css/demo.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/jquery.dataTables.min.css" rel="stylesheet">
    <!--<link href="${pageContext.request.contextPath}/resource/css/jquery.dataTables.min.css" rel="stylesheet">-->
    <!--<link href="${pageContext.request.contextPath}/resource/css/datatables.css" rel="stylesheet">-->
    <link href="${pageContext.request.contextPath}/resource/css/gwac-ui.css" rel="stylesheet">
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements. All other JS at the end of file. -->
    <!--[if lt IE 9]>
      <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/html5shiv.js"></script>
      <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <div style="display: none;">
      <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      <input type="hidden" id="otId" value=""/>
    </div>
    <div class="container-fluid"> <!--container container-fluid -->
      <div class="row ot-detail-top">
        <div id="user-welcome">
          <span id="ot-classify">小行星编号&nbsp;<select name="mpNumber" id="ot2Classify" title="选择即可查看历史光变"></select></span>
        </div>
        <div id="ot-followup">
        </div>
      </div>
      <div class="row ot-detail-body">
        <div class="col-md-12 col-lg-6 ot-detail-left">
          <div id="ot-curves">
            <div id="ot-curve-show">
              <div id="ot-curve-title">
                <span>OT光变曲线（X轴为时间，单位/分钟，开始于<span id="otFoundTimeUtc"></span>U，Y轴为星等值）</span>
              </div>
              <div id="ot-curve"></div>
            </div>
          </div>
        </div>
      </div>

    </div>
    <!-- /.container -->
    <div id='tooltip'></div>

    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/video.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/flat-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/application.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.carouFredSel-6.2.1-packed.js"></script>
    <script src="<%=request.getContextPath()%>/js/plot/jquery.flot.js"></script>
    <script src="<%=request.getContextPath()%>/js/plot/jquery.flot.categories.js"></script>
    <script src="<%=request.getContextPath()%>/js/plot/jquery.flot.resize.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.validate.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/dataTables.buttons.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/bootbox.min.js"></script>
    <!--<script src="${pageContext.request.contextPath}/resource/js/datatables.js"></script>-->
    <script src="${pageContext.request.contextPath}/resource/js/ot_detail.js"></script>

    <script>
      videojs.options.flash.swf = "${pageContext.request.contextPath}/resource/flatui/js/vendors/video-js.swf";
    </script>
  </body>
</html>
