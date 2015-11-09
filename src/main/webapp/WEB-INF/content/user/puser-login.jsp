<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <title>Flat UI - Free Bootstrap Framework and Theme</title>
    <meta name="description" content="Flat UI Kit Free is a Twitter Bootstrap Framework design and Theme, this responsive framework includes a PSD and HTML version."/>

    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">

    <!-- Loading Bootstrap -->
    <link href="${pageContext.request.contextPath}/resource/flatui/css/vendor/bootstrap.min.css" rel="stylesheet">

    <!-- Loading Flat UI -->
    <link href="${pageContext.request.contextPath}/resource/flatui/css/flat-ui.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/flatui/css/demo.css" rel="stylesheet">

    <link rel="shortcut icon" href="img/favicon.ico">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements. All other JS at the end of file. -->
    <!--[if lt IE 9]>
      <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/html5shiv.js"></script>
      <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="container">

      <div class="login">
        <div class="login-screen">
          <div class="login-icon">
            <img src="${pageContext.request.contextPath}/sysimg/logo.png" alt="Ground Wide Astronomy Camera" />
            <h4>欢迎登录 <small>GWAC</small></h4>
          </div>

          <div class="login-form">
            <div class="form-group">
              <input type="text" class="form-control login-field" value="" placeholder="Enter your name" id="login-name" />
              <label class="login-field-icon fui-user" for="login-name"></label>
            </div>

            <div class="form-group">
              <input type="password" class="form-control login-field" value="" placeholder="Password" id="login-pass" />
              <label class="login-field-icon fui-lock" for="login-pass"></label>
            </div>

            <a class="btn btn-primary btn-lg btn-block" href="#">Log in</a>
            <a class="login-link" href="#">Lost your password?</a>
          </div>
        </div>
      </div>

    </div> <!-- /container -->


    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/video.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/flat-ui.min.js"></script>

    <script>
      videojs.options.flash.swf = "${pageContext.request.contextPath}/resource/flatui/js/vendors/video-js.swf"
    </script>
  </body>
</html>
