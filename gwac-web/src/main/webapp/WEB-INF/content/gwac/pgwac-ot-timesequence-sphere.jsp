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
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/ot_timesequence_sphere.js"></script>
    <link href="<%=request.getContextPath()%>/resource/js/d3/maps.css" rel="stylesheet" type="text/css">
    <script>

      $(function() {
        var root = "<%=request.getContextPath()%>";
        var url = "gction/get-ot1-timesequence-list.action";
        var curl = "resource/celestial-d3/data/constellations.lines.json";
        var mainHeight = $("#main").height();
        var headerHeight = $("#header").height();
        $("#sphereDisplay").height(mainHeight - headerHeight - 10);

        var gwac = $.gwac("#sphereDisplay", root, url, curl);
        gwac.getConstellations();
        d3.json(gwac.url, function(errors, reqData) {
          gwac.parseData(reqData);
          gwac.draw();
          setTimeout(function() {
            gwac.ot1DrawInterval = setInterval(dynamicDrawOt1, gwac.playSpeed);
          }, gwac.startAnimationDuration);
        });

        $('#dynamicDrawOt1').change(function() {
          if ($(this).is(":checked")) {
            gwac.reParseData();
            gwac.ot1DrawInterval = setInterval(dynamicDrawOt1, gwac.playSpeed);
          } else {
            clearInterval(gwac.ot1DrawInterval);
          }
        });
        
        $('#changeView').click(function(){gwac.changeView(gwac);});

        function dynamicDrawOt1() {
          gwac.currentFrame = gwac.startFrame + (gwac.currentFrame - gwac.startFrame + 1) % (gwac.endFrame - gwac.startFrame + 1);
          $('#currentFrame').val(gwac.currentFrame);
          if (gwac.currentFrame > 0) {
            gwac.svg.selectAll(".ot1").remove();
          }
          gwac.ot1Data2.data.coordinates = gwac.ot1[gwac.currentFrame];
          gwac.curnode = gwac.svg.append("path").datum(gwac.ot1Data2.data).attr("class", gwac.ot1Data2.class).attr("d", gwac.path.pointRadius(1)).attr("d", gwac.path);
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
      /*@import url(<%=request.getContextPath()%>/resource/js/d3/maps.css);*/

      body{background-color: black;}
      path {fill: none;stroke-linejoin: round;}
      #main{width:100%;height: 100%;text-align: center;position: absolute; top: 0; left: 0;}
      #tooltip{position:absolute;z-index:10;visibility:hidden;color:white;}
      #header{width: 100%;height: 40px;font-size: 28px;text-align: center; color: white;padding-top: 5px;}
      #sphereDisplay{margin: auto;width:100%;height: 90%;}
      #toolbar {position: absolute;bottom: auto;right: 10px;top: 45px;left: auto;width: 150px;z-index: 10;text-align: left;color: white;font-size: 14px;}
      #toolbar label{display: block; margin: 3px 0; padding-left: 15px; text-indent: -15px; cursor: pointer;}
      #toolbar input[type='checkbox']{width: 14px;height: 14px;padding: 0;margin:0 5px 0 0;vertical-align: bottom;position: relative;top: -1px;*overflow: hidden; cursor: pointer;}
      .ot1-input{width: 50px;height: 20px; padding:0;margin:0 0 0 10px;border:none;background-color: transparent;outline: none;color:#fff;}


      /*.graticule {fill: none;stroke: #636B62;stroke-width: .5px;stroke-dasharray: 2,2;}*/
      .graticule{stroke: #888888;stroke-width: 0.3px;}
      .sphere{stroke: #636B62;stroke-width: 1.5px;}
      .equator {stroke: #636B62;stroke-width: 1.5px;}
      .primemeridian {stroke: #636B62;stroke-width: 1.5px;}
      .origin{stroke: #636B62;stroke-width: 5px;fill: #636B62;}
      .ot1{stroke: #fff;stroke-width: 1px;fill: #fff;}
      .ot2{stroke: #993399;stroke-width: 3px;fill: #993399;}
      .ot2mch{stroke: #FFFF99;stroke-width: 3px;fill: #FFFF99;}
      .ot2cur{stroke: #FF33CC;stroke-width: 5px;fill: #FF33CC;}
      .constellation{stroke: #999999;stroke-width: 0.5px;}

    </style>

  </head>
  <body>
    <div id="main">
      <div id="header">OT分布实时概览图-天球坐标</div>
      <div id="sphereDisplay"></div>
      <div id="toolbar">
        <table style="border:0">
          <tr><td colspan="2"><input type="checkbox" checked="" id="dynamicDrawOt1"><span id="playot2">播放</span></td></tr>
          <tr><td>播放速度:</td><td><input type="text" id="playSpeed" class="ot1-input" value="400"/></td></tr>
          <tr><td>每次间隔:</td><td><input type="text" id="playInterval" class="ot1-input" value="5"/></td></tr>
          <tr><td>开始帧数:</td><td><input type="text" id="startFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>当前帧数:</td><td><input type="text" id="currentFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>结束帧数:</td><td><input type="text" id="endFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>总帧数:</td><td><input type="text" id="totalFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>边界左上:</td><td><input type="text" id="leftTopBound" class="ot1-input" value="60,60"/></td></tr>
          <tr><td>边界右下:</td><td><input type="text" id="rightBottomBound" class="ot1-input" value="70,70"/></td></tr>
          <tr><td colspan="2"><a href="javascript:void(0);" id="changeView" style="text-decoration:none;color:#fff;">切换视角</a></td></tr>
        </table>
      </div>
    </div>
    <div id="tooltip">a simple tooltip</div>
  </body>
</html>