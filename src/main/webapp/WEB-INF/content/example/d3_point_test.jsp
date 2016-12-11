<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/topojson.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.geo.zoom.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3_point_test.js"></script>
    <link href="<%=request.getContextPath()%>/resource/js/d3/maps.css" rel="stylesheet" type="text/css">
    <script>

      $(function() {
        var root = "<%=request.getContextPath()%>";
        var url = "get-ot-xy-list.action?dateStr=";
        var mainHeight = $("#main").height();
        var headerHeight = $("#header").height();
        $("#sphereDisplay").height(mainHeight - headerHeight - 10);

        var gwac = $.gwac("#sphereDisplay", root, url);
        d3.json(gwac.url, function(errors, reqData) {
          gwac.parseData(reqData);
          gwac.draw();
        });

        $(window).resize(function() {
          var winWidth = $(window).width();
          var winHeight = $(window).height();
          $("#main").width(winWidth);
          $("#main").height(winHeight);
          $("#header").width(winWidth);
          $("#sphereDisplay").width(winWidth);
          $("#sphereDisplay").height(winHeight - $("#header").height() - 10);
          gwac.draw();
        });
      });

    </script>
    <style>
      /*@import url(/gwac/resource/js/d3/maps.css);*/

      body{background-color: black;}
      path {fill: none;stroke-linejoin: round;}
      #main{width:100%;height: 100%;text-align: center;position: absolute; top: 0; left: 0;}
      #tooltip{position:absolute;z-index:10;visibility:hidden;color:white;}
      #header{width: 100%;height: 40px;font-size: 28px;text-align: center; color: white;padding-top: 5px;}
      #sphereDisplay{margin: auto;width:100%;height: 90%;}
      #toolbar {position: absolute;bottom: auto;right: 10px;top: 45px;left: auto;width: 120px;z-index: 10;text-align: left;color: white;font-size: 14px;}
      #toolbar label{display: block; margin: 3px 0; padding-left: 15px; text-indent: -15px; cursor: pointer;}
      #toolbar input{width: 14px;height: 14px;padding: 0;margin:0 5px 0 0;vertical-align: bottom;position: relative;top: -1px;*overflow: hidden; cursor: pointer;}

      /*.graticule {fill: none;stroke: #636B62;stroke-width: .5px;stroke-dasharray: 2,2;}*/
      .graticule{stroke: #a9a9a9;stroke-width: 0.5px;}
      .sphere{stroke: #636B62;stroke-width: 1.5px;}
      .equator {stroke: #636B62;stroke-width: 1.5px;}
      .primemeridian {stroke: #636B62;stroke-width: 1.5px;}
      .origin{stroke: white;stroke-width: 1px;fill: red;}
      .ot1{stroke: #fff;stroke-width: 1px;fill: #fff;}
      .ot2{stroke: #993399;stroke-width: 4px;fill: #993399;}
      .ot2:hover{stroke: #993399;stroke-width: 6px;fill: #993399;z-index: 1000}
      .ot2mch{stroke: #FFFF99;stroke-width: 4px;fill: #FFFF99;}
      .ot2mch:hover{stroke: #FFFF99;stroke-width: 6px;fill: #FFFF99;z-index: 1000}
      .ot2cur{stroke: #FF33CC;stroke-width: 4px;fill: #FF33CC;}
      .ot2cur:hover{stroke: #FF33CC;stroke-width: 8px;fill: #FF33CC;z-index: 1000}
    </style>

  </head>
  <body>
    <div id="main">
      <div id="header">OT分布实时概览图-天球坐标</div>
      <div id="sphereDisplay"></div>
      <div id="toolbar">
        <label for="ot1"><input type="checkbox" checked="" id="ot1">OT1</label>
        <label for="ot2"><input type="checkbox" checked="" id="ot2">OT2</label>
        <label for="ot2mch"><input type="checkbox" checked="" id="ot2cur">OT2-mch</label>
        <label for="ot2cur"><input type="checkbox" checked="" id="ot2cur">OT2-cur</label>
        <label for="ot2new"><input type="checkbox" checked="" id="ot2cur">OT2-new</label>
        <label for="varstar"><input type="checkbox" checked="" id="ot2cur">varstar</label>
        <label for="templateBorder"><input type="checkbox" id="templateBorder"/>模板边界</label>
        <label for="galacticPlane"><input type="checkbox" id="galacticPlane">银道面</label>
        <label for="eclipticPlane"><input type="checkbox" id="eclipticPlane">黄道面</label>
        <label for="groundPlane"><input type="checkbox" id="groundPlane">地平面</label>
      </div>
    </div>
    <div id="tooltip">a simple tooltip</div>

    <div style="display: none;">
      <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
    </div>      
  </body>
</html>