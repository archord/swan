<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/topojson.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.geo.zoom.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/ot_timesequence_sphere.js"></script>
    <script>

      $(function() {
        var root = "<%=request.getContextPath()%>";
        var url = "get-ot1-timesequence-list.action";
        var mainHeight = $("#main").height();
        var headerHeight = $("#header").height();
        $("#sphereDisplay").height(mainHeight - headerHeight - 10);

        var gwac = $.gwac("#sphereDisplay", root, url);
        d3.json(gwac.url, function(errors, reqData) {
          gwac.parseData(reqData);
          gwac.draw();
        });

        $('#dynamicDrawOt1').change(function() {
          if ($(this).is(":checked")) {
            gwac.ot1DrawInterval = setInterval(dynamicDrawOt1, 400);
//            console.log(":checked");
          } else {
            clearInterval(gwac.ot1DrawInterval);
//            console.log(":unchecked");
          }
        });

        function dynamicDrawOt1() {
          gwac.curSerialNum = (gwac.curSerialNum + 1)%gwac.maxNumber;
          if (gwac.curSerialNum > 0) {
            gwac.svg.selectAll(".ot1").remove();
          }
          gwac.ot1Data2.data.coordinates = gwac.ot1[gwac.curSerialNum];
          gwac.curnode = gwac.svg.append("path").datum(gwac.ot1Data2.data).attr("class", gwac.ot1Data2.class).attr("d", gwac.path.pointRadius(1)).attr("d", gwac.path);
          console.log(gwac.curSerialNum);
        }
        
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
      @import url(/gwac/js/d3/maps.css);

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
      .origin{stroke: #636B62;stroke-width: 5px;fill: #636B62;}
      .ot1{stroke: #fff;stroke-width: 1px;fill: #fff;}
      .ot2{stroke: #993399;stroke-width: 3px;fill: #993399;}
      .ot2mch{stroke: #FFFF99;stroke-width: 3px;fill: #FFFF99;}
      .ot2cur{stroke: #FF33CC;stroke-width: 5px;fill: #FF33CC;}
    </style>

  </head>
  <body>
    <div id="main">
      <div id="header">OT分布实时概览图-天球坐标</div>
      <div id="sphereDisplay"></div>
      <div id="toolbar">
        <label for="dynamicDrawOt1"><input type="checkbox" id="dynamicDrawOt1">Start</label>
        <label for="ot1"><input type="checkbox" checked="" id="ot1">OT1</label>
      </div>
    </div>
    <div id="tooltip">a simple tooltip</div>
  </body>
</html>