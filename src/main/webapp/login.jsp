<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC信息展示平台 - 登录页面</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <!-- Loading Bootstrap -->
    <link href="${pageContext.request.contextPath}/resource/flatui/css/vendor/bootstrap.min.css" rel="stylesheet">

    <!-- Loading Flat UI -->
    <link href="${pageContext.request.contextPath}/resource/flatui/css/flat-ui.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/flatui/css/demo.css" rel="stylesheet">

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
            <img src="${pageContext.request.contextPath}/resource/sysimg/logo.png" alt="Ground Wide Astronomy Camera" />
            <h4>欢迎登录 <small>GWAC</small></h4>
          </div>

          <div class="login-form">
            <div style="color:red"><s:fielderror/></div>
            <form action="${pageContext.request.contextPath}/user-login.action" id="loginAction" method="post">
              <div class="form-group">
                <input type="text" class="form-control login-field" value="" placeholder="请输入用户名" id="loginName" name="loginName" />
                <label class="login-field-icon fui-user" for="login-name"></label>
              </div>

              <div class="form-group">
                <input type="password" class="form-control login-field" value="" placeholder="密码" id="loginPass" name="loginPass" />
                <label class="login-field-icon fui-lock" for="login-pass"></label>
              </div>

              <button type="submit" class="btn btn-primary btn-lg btn-block" >登录</button>
              <a class="login-link" href="#">忘记密码？</a>
            </form>
          </div>
        </div>
      </div>

    </div> <!-- /container -->


    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/video.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/flat-ui.min.js"></script>


  </body>
</html>
