<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC目标分类页面</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC系统日志页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>    
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/gallery/css/blueimp-gallery.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/gallery/css/blueimp-gallery-indicator.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/gallery/css/blueimp-gallery-video.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/gallery/css/demo/demo.css">
  </head>
  <body>
    <h2>Carousel image gallery</h2>
    <!-- The Gallery as inline carousel, can be positioned anywhere on the page -->
    <div id="blueimp-image-carousel" class="blueimp-gallery blueimp-gallery-carousel">
      <div class="slides"></div>
      <h3 class="title"></h3>
      <a class="prev">‹</a>
      <a class="next">›</a>
      <a class="play-pause"></a>
    </div>

    
    <script src="${pageContext.request.contextPath}/resource/gallery/js/blueimp-helper.js"></script>
    <script src="${pageContext.request.contextPath}/resource/gallery/js/blueimp-gallery.js"></script>
    <script src="${pageContext.request.contextPath}/resource/gallery/js/blueimp-gallery-fullscreen.js"></script>
    <script src="${pageContext.request.contextPath}/resource/gallery/js/blueimp-gallery-indicator.js"></script>
    <script src="${pageContext.request.contextPath}/resource/gallery/js/vendor/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/resource/gallery/js/jquery.blueimp-gallery.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/obj-classify.js"></script>

  </body>
</html>
