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
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>
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
          <div id="user-login-div">您好！<a href="#" id="user-login" title="点击登录">请登录</a></div>
          <div id="user-logout-div" style='display:none;'>欢迎您，<span id="user-welcome-username"></span>！<a href='#' id='user-logout' title="点击注销">注销</a></div>
        </div>
        <div id="ot-followup">
          <s:form id="otFollowUp"  action="otFollowUp" theme="simple" cssClass="yform" namespace="/">
            <table style="width:100%;">
              <tr>
                <th width="100px">观测者</th><th>后随名称</th><th>RA(度)</th><th>DEC(度)</th><th>曝光时间(S)</th><th>曝光帧数</th><th>后随望远镜</th><th>滤光片</th><th></th>
              </tr>
              <tr>
                <td><input type="text" style="border-color: #bdc3c7;color: #34495e;" name="ot2fp.userName" readonly="true" id="userName" value="mini-GWAC" class="form-control"/></td>
                <td><input type="text" style="width: 150px; border-color: #bdc3c7;color: #34495e;" name="ot2fp.otName" readonly="true" id="otName" value="" class="form-control"/></td>
                <td><input type="text" style="width: 80px; " name="ot2fp.ra" id="fuRa" value=""  class="form-control"/></td>
                <td><input type="text" style="width: 80px; " name="ot2fp.dec" id="fuDec" value="" class="form-control"/></td>
                <td><input type="text" style="width: 80px; " name="ot2fp.expTime" id="expTime" value="2" class="form-control"/></td>
                <td><input type="text" style="width: 80px; " name="ot2fp.frameCount" id="frameCount" value="10" class="form-control"/></td>
                <td>
                  <select name="ot2fp.telescope" id="telescope" class="form-control select select-primary" data-toggle="select">
                    <option value="1" selected>60公分</option>
                    <option value="2">30公分</option>
                  </select>
                </td>
                <td>
                  <select name="ot2fp.filter" id="filter" class="form-control select select-primary" data-toggle="select">
                    <!--option value="Lum" selected>Lum</option>
                    <option value="Green">Green</option>
                    <option value="R">R</option>
                    <option value="Blue">Blue</option>
                    <option value="V">V</option>
                    <option value="I">I</option>
                    <option value="B">B</option>
                    <option value="Red">Red</option>
                    <option value="U">U</option>
                    <option value="null">null</option-->
                  </select>
                </td>
                <td>
                  <input type="button" value="后随" class="btn btn-primary" id="followBtn"/>
                </td>
              </tr>
            </table>
          </s:form>
        </div>
      </div>
      <div class="row ot-detail-body">
        <div class="col-md-12 col-lg-6 ot-detail-left">
          <div id="cut-image-show">
          </div>
          <div id="skyCoordinate">
            <span id="ot-classify">OT分类&nbsp;<select name="ot2Classify" id="ot2Classify" title="选择即可分类"></select>
              <a id="ot2ClassifyBtn" href='#' title='点击对OT2进行分类' style="display:none">OT分类</a></span>
            <span id="skyCordDetail"></span>
            <span style="display:inline-block"><a id="showOt2Fits" href='#' title='点击查看fits切图' style="display: none;">查看fits切图</a></span>
          </div>
          <div id="ot-description">
            <div><textarea id="ot-description-content" name="ot-description-content" class="form-control" placeholder="添加OT2的说明，官方对该OT的定性描述。"></textarea></div>
            <div><input type="button" value="提交说明" class="btn btn-primary"  id="ot-description-button"/></div>
          </div>
          <div id="ot-curves">
            <div id="ot-curve-show">
              <div id="ot-curve-title">
                <span>OT光变曲线（X轴为时间，单位/分钟，开始于<span id="otFoundTimeUtc"></span>U，Y轴为星等值）</span>
              </div>
              <div id="ot-curve"></div>
            </div>
            <div id="ot-position-show">
              <div id="ot-position-show-title">
                <span>OT坐标变化（与首帧差值，单位/像素，xy轴分别为模板XY坐标）</span>
              </div>
              <div id="ot-position-curve"></div>
            </div>
          </div>
        </div>
        <div class="col-md-12 col-lg-6 ot-detail-right">
          <div id="ot2-match" class="ot2-table-show">
            <table id="ot2-match-table" class="display" cellspacing="0" width="100%">
              <thead><tr><th>ID</th><th>匹配星表</th><th>匹配ID</th><th>OT2名称</th><th>RA</th><th>DEC</th><th>匹配距离(角分)</th><th>星等</th><th>D25</th></tr></thead>
            </table>
          </div>
          <div id="ot2-record" class="ot2-table-show">
            <table id="ot2-record-table" class="display" cellspacing="0" width="100%">
              <thead><tr><th>ID</th><th>时间(UTC)</th><th>RA</th><th>DEC</th><th>模板X</th><th>模板Y</th><th>星等</th><th>星等误差</th><th>原FITS图</th><th>X</th><th>Y</th>
                  <th>流量</th><th>背景</th><th>半高全宽</th><th>椭率</th><th>分类星</th></tr></thead>
            </table>
          </div>
          <div id="ot2-followup-object" class="ot2-table-show">
            <table id="ot2-followup-object-table" class="display" cellspacing="0" width="100%">
              <thead><tr><th>ID</th><th>目标名</th><th>时间(UTC)</th><th>RA</th><th>DEC</th><th>X</th><th>Y</th></tr></thead>
            </table>
          </div>
          <div id="ot-followup-mag-show">
            <div id="ot-followup-mag-title">
              <span>OT后随光变曲线（X轴为时间，单位/分钟，开始于<span id="otFollowupStartTimeUtc"></span>U，Y轴为星等值）</span>
              <span style="display:inline-block"><a id="showOt2FollowupFits" href='#' title='点击查看后随fits切图'>查看后随fits切图</a></span>
            </div>
            <div id="ot-followup-mag-curve"></div>
          </div>
          <!--div id="ot-comment">
            <div id="ot-comment-list">
              <div id="ot-comment-list-title">
                <span style="font-weight: bold;font-size: 14px;">OT评论</span>&nbsp;&nbsp;
                共<span id="comment-number">0</span>条评论&nbsp;&nbsp;
                <a href="#" id="add-comment">添加评论</a></div>
              <div id="ot-comment-list-content">
                <div id="ot-comment-list-content-each">
                  <div id="ot-comment-list-content-each-title">1楼 徐洋 2015年12月7日14点25分</div>
                  <div id="ot-comment-list-content-each-content">小说讲述了人族孤儿千夜自困苦中崛起成为主宰之王的玄幻传奇。人族孤儿千夜，被帝国元帅林熙棠收为义子，加入黄泉训练营，后进入红蝎特种部队，一次执行任务时，作战情报被出卖，导致整个小队被灭，千夜也被血族所伤，变成血奴，却还有自己的神智。后千夜回归永夜大陆，当上一名雇佣兵，开始了自己的探索真相，走向巅峰之旅。</div>
                </div>
              </div>
            </div>
            <div><textarea id="ot-comment-content" name="ot-comment-content" class="form-control" placeholder="对该OT发表个人意见。"></textarea></div>
            <div><input type="button" value="提交评论" class="btn btn-primary"  id="ot-comment-button"/></div>
          </div-->
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
