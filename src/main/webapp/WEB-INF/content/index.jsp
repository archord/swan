<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="ch"> 
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>GWAC信息展示平台 - <s:text name="showcase.version"/></title>

    <link href="${pageContext.request.contextPath}/resource/styles/flexible-grids.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/resource/css/font-oxygen/oxygen.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/resource/css/sliding-menu.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/resource/css/gwac-style.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/resource/mason/mason.css" rel="stylesheet" type="text/css" />

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/mason/app.js"></script>
    <script src="${pageContext.request.contextPath}/resource/mason/mason.js"></script>

  </head>
  <body>
    <!-- Navigation -->
    <nav id="slide-menu">
      <ul>
        <li><a href="gwac/pgwac-system.action" targets="main_content">系统监测信息导航</a></li>
        <li><a href="gwac/pgwac-allot.action" targets="main_content">二级OT查询</a></li>
        <li><a href="manage/pfile-upload.action" targets="main_content">上传文件</a></li>
        <li><a href="gwac/pupload-unstore.action" targets="main_content">未入库文件列表</a></li>
        <li><a href="gwac/pupload-record.action" targets="main_content">上传文件历史记录</a></li>
        <li><a href="gwac/pupload-configfile.action" targets="main_content">上传配置文件历史记录</a></li>
      </ul>
    </nav>
    <!-- Content panel -->
    <div id="main_page_content">
      <div class="menu-trigger"></div>
      <div id="main_content" cssClass="ym-wbox">
        <div id='grid-fixed'>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-allot2.action" target="_blank" ><p>OT列表</p></a>     </div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ccd-pixel-filter.action" target="_blank" ><p>坏像素过滤</p></a>     </div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-pointing.action" target="_blank" ><p>转台跟踪</p></a>     </div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-ccd-image-realtime.action" target="_blank" ><p>图像预览</p></a>     </div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ot-realtimedisplay.action" target="_blank" ><p>OT分布-XY</p></a>          </div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ot-realtimedisplay-sphere.action" target="_blank" ><p>OT分布-RaDec</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ot-realtimedisplay-sphere-d3.action" target="_blank" ><p>OT分布3D</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ot-timesequence-sphere.action" target="_blank" ><p>OT1分布3D</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-mot-timesequence-sphere.action" target="_blank" ><p>移动展示</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-mot-timesequence-sphere-debug.action" target="_blank" ><p>移动展示调试</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-sphere-distribution.action" target="_blank" ><p>模板分布</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-allot.action" target="_blank" ><p>OT2查询</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/manage/pfile-upload.action" target="_blank" ><p>上传文件</p></a></div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-monitor.action" target="_blank" ><p>状态监控</p></a>     </div>
          <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-light-curve.action" target="_blank" ><p>小行星光变查看</p></a></div>
          <div class='mason-block x2'><a href="http://190.168.1.45/Mini_GWAC_Survey/survey_plan_db.php" target="_blank" ><p>观测策略</p></a></div>
          <div class='mason-block x2'><a href="http://190.168.1.45/GWACOC/gcn_trigger.php" target="_blank" ><p>GCN trigger</p></a></div>
          <div class='mason-block x2'><a href="http://www.xinglong-naoc.org/utc/" target="_blank" ><p>兴隆恒星时</p></a></div>
          <div class='mason-block x2'><a href="http://www.xinglong-naoc.org/weather/yuntu.jhtml" target="_blank" ><p>兴隆云图</p></a></div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-fwhm.action" target="_blank" ><p>半高全宽</p></a>                  </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-objnum.action" target="_blank" ><p>星的数目</p></a>                       </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-bgbright.action" target="_blank" ><p>背景亮度</p></a>                     </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-limitmag.action" target="_blank" ><p>极限星等</p></a>                     </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-diffmag.action" target="_blank" ><p>亮星星等差</p></a>                    </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-track.action" target="_blank" ><p>图像偏移量</p></a>                      </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-xyrms.action" target="_blank" ><p>图像对齐精度</p></a>                    </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-timeneed.action" target="_blank" ><p>数据处理时间</p></a>             </div>
          <div class='mason-block'><a href="<%=request.getContextPath()%>/user-logout.action" ><p>退出</p></a>             </div>
        </div>
        <div id='mason-fillers'>
          <div class='mason-fill-filler' style="margin: 5px;"></div>
        </div>
      </div>

    </div>

    <script>
      $(function() {
        $("#grid-fixed").mason({
          itemSelector: '.mason-block',
          ratio: 1,
          columns: [[0, 300, 32]],
//          sizes: [[1, 1], [2, 1]],
          sizes: [[8, 5]],
//      promoted: [['xl', 2, 1]],
          filler: {itemSelector: '.mason-fill-filler', filler_class: 'mason_filler', keepDataAndEvents: true},
          layout: 'fluid',
          gutter: 5
        });

        var $body = document.getElementsByTagName("body")[0];
        var $menu_trigger = $body.getElementsByClassName('menu-trigger')[0];
        if (typeof $menu_trigger !== 'undefined') {
          $menu_trigger.addEventListener('click', function() {
//            $body.className = $body.className == 'menu-active' ? '' : 'menu-active';
          });
        }
      });
    </script>
  </body>
</html>