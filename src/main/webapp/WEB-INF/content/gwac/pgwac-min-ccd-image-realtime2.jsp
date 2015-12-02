<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>Mini-GWAC CCD图像实时预览</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/gwac-ui.css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <style type="text/css">
      <!--
      .imgStyle{height:250px;}
      -->
    </style>
  </head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span1">M01</span>
          <a href="/images/realTimeOtDistribution/M01_ccdimg.jpg"><img id="img1" src="/images/realTimeOtDistribution/M01_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span2">M02</span>
          <a href="/images/realTimeOtDistribution/M02_ccdimg.jpg"><img id="img2" src="/images/realTimeOtDistribution/M02_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span3">M03</span>
          <a href="/images/realTimeOtDistribution/M03_ccdimg.jpg"><img id="img3" src="/images/realTimeOtDistribution/M03_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span4">M04</span>
          <a href="/images/realTimeOtDistribution/M04_ccdimg.jpg"><img id="img4" src="/images/realTimeOtDistribution/M04_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
      </div>        
      <div class="row">
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span5">M05</span>
          <a href="/images/realTimeOtDistribution/M05_ccdimg.jpg"><img id="img5" src="/images/realTimeOtDistribution/M05_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span6">M06</span>
          <a href="/images/realTimeOtDistribution/M06_ccdimg.jpg"><img id="img6" src="/images/realTimeOtDistribution/M06_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span7">M07</span>
          <a href="/images/realTimeOtDistribution/M07_ccdimg.jpg"><img id="img7" src="/images/realTimeOtDistribution/M07_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span8">M08</span>
          <a href="/images/realTimeOtDistribution/M08_ccdimg.jpg"><img id="img8" src="/images/realTimeOtDistribution/M08_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
      </div> 
      <div class="row">
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span9">M09</span>
          <a href="/images/realTimeOtDistribution/M09_ccdimg.jpg"><img id="img09" src="/images/realTimeOtDistribution/M09_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span10">M10</span>
          <a href="/images/realTimeOtDistribution/M10_ccdimg.jpg"><img id="img10" src="/images/realTimeOtDistribution/M10_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span11">M11</span>
          <a href="/images/realTimeOtDistribution/M11_ccdimg.jpg"><img id="img11" src="/images/realTimeOtDistribution/M11_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
        <div class="col-xs-3 col-sm-3 col-md-3 ">
          <span id="span12">M12</span>
          <a href="/images/realTimeOtDistribution/M12_ccdimg.jpg"><img id="img12" src="/images/realTimeOtDistribution/M12_ccdimg.jpg" class="imgStyle" border="0"/></a>
        </div>
      </div>                              
    </div>

  </div>

</body>
</html>
