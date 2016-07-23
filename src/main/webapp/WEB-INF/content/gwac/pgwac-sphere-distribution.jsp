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
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/sphere-distribution.js"></script>
    <link href="<%=request.getContextPath()%>/resource/js/d3/maps.css" rel="stylesheet" type="text/css">
    <script>

      $(function() {
        var root = "<%=request.getContextPath()%>";
        var url = "get-coor-list.action";
        var mainHeight = $("#main").height();
        var headerHeight = $("#header").height();
        $("#sphereDisplay").height(mainHeight - headerHeight - 10);
        $("#queryButton").click(doQuery);

        var gwac = $.gwac("#sphereDisplay", root, "");
        gwac.draw();

        function doQuery() {
          var querySql = $("#querySql").val();
          if (querySql === null || querySql === "") {
            alert("查询语句为空！");
          }
          var queryUrl = url + "?querySql=" + querySql;
          console.log(queryUrl);
          d3.json(queryUrl, function(errors, reqData) {
            gwac.parseData(reqData);
            gwac.draw();
          });
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
      /*@import url(/gwac/resource/js/d3/maps.css);*/

      body{background-color: black;}
      path {fill: none;stroke-linejoin: round;}
      #main{width:100%;height: 100%;text-align: center;position: absolute; top: 0; left: 0;}
      #tooltip{position:absolute;z-index:10;visibility:hidden;color:white;}
      #header{width: 100%;height: 40px;font-size: 28px;text-align: center; color: white;padding-top: 5px;}
      #sphereDisplay{margin: auto;width:100%;height: 90%;}
      #toolbar {width:99%; position: absolute;bottom: auto;left: 10px;bottom: 5px;z-index: 10;text-align: left;color: white;font-size: 14px;}
      #toolbar label{display: block; margin: 3px 0; padding-left: 15px; text-indent: -15px; cursor: pointer;}
      #toolbar input{width: 14px;height: 14px;padding: 0;margin:0 5px 0 0;vertical-align: bottom;position: relative;top: -1px;*overflow: hidden; cursor: pointer;}
      #toolbar textarea{width:80%; border:1;padding: 0;margin:0 5px -5px 0;line-height: 20px;background-color:transparent; scrollbar-arrow-color:yellow; scrollbar-base-color:lightsalmon; color: white;}

      /*.graticule {fill: none;stroke: #636B62;stroke-width: .5px;stroke-dasharray: 2,2;}*/
      .graticule{stroke: #a9a9a9;stroke-width: 0.5px;}
      .sphere{stroke: #636B62;stroke-width: 1.5px;}
      .equator {stroke: #636B62;stroke-width: 1.5px;}
      .primemeridian {stroke: #636B62;stroke-width: 1.5px;}
      .origin{stroke: #636B62;stroke-width: 5px;fill: #636B62;}
      .ot1{stroke: #fff;stroke-width: 1px;fill: #fff;}
      .ot2{stroke: #993399;stroke-width: 1px;fill: #993399;}
      .ot2:hover{stroke: #FFFF00;stroke-width: 4px;fill: #FFFF00;z-index: 1000}
      .ot2mch{stroke: #FFFF99;stroke-width: 3px;fill: #FFFF99;}
      .ot2cur{stroke: #FF33CC;stroke-width: 5px;fill: #FF33CC;}
    </style>

  </head>
  <body>
    <div id="main">
      <div id="header">分布查询-天球坐标</div>
      <div id="sphereDisplay"></div>
      <div id="toolbar">
        <span>sql语句：<textarea rows="1" cols="100" name="querySql" id="querySql">
select ot_id id, name, ra, dec, mag from ot_tmpl_wrong where matched_total>2 and data_produce_method='1';
          </textarea>
          <button type="button" id="queryButton">查询</button></span>
      </div>
    </div>
    <div id="tooltip">a simple tooltip</div>

    <div style="display: none;">
      <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
    </div>      
  </body>
</html>