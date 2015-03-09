<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/topojson.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.geo.zoom.js"></script>
    <script>

      $(function() {
        var mainHeight = $("#main").height();
        var headerHeight = $("#header").height();
        $("#sphereDisplay").height(mainHeight - headerHeight - 10);
        drawCelestial();
      });

      $(window).resize(function() {
        var winWidth = $(window).width();
        var winHeight = $(window).height();
        $("#main").width(winWidth);
        $("#main").height(winHeight);
        $("#header").width(winWidth);
        $("#sphereDisplay").width(winWidth);
        $("#sphereDisplay").height(winHeight - $("#header").height() - 10);
        drawCelestial();
      });

      function drawCelestial() {

        d3.select("svg").remove();

        var degrees = 180 / Math.PI;
        var ratio = window.devicePixelRatio || 1;
        var p = ratio;

        var width = $("#sphereDisplay").width();
        var height = $("#sphereDisplay").height();
        var scale = height / 3 - 1;  //显示的大小,相对于单位长度
        var clipAngle = 90 - 5;  //投影显示的部分，180显示整个球 1E-6
        var precision = 0.1; //线采样精度
        var rotate = [20, -40, 0]; //初始角度
        var velocity = [.01, 0, 0]; //转速
        var time = Date.now();

        var projection = d3.geo.orthographic()
                .scale(scale)
                .translate([width / 2, height / 2])
                .clipAngle(clipAngle)
                .clipExtent([[-1, -1], [width + 1, height + 1]])
                .precision(precision);

        var path = d3.geo.path().projection(projection);
        var graticule = d3.geo.graticule()();//网格
        var svg = d3.select("#sphereDisplay").append("svg").attr("width", width).attr("height", height);

        var tooltip = d3.select("body")
                .append("div")
                .style("position", "absolute")
                .style("z-index", "10")
                .style("visibility", "hidden")
                .style("color", "white")
                .text("a simple tooltip");

        d3.json("/gwac/json/world-110m.json", function(error, world) {

          var globe = {type: "Sphere"};
          var land = topojson.feature(world, world.objects.land);
          var borders = topojson.mesh(world, world.objects.countries);
          var countries = d3.shuffle(topojson.feature(world, world.objects.countries).features);

          var zoom = d3.geo.zoom()
                  .projection(projection)
                  .duration(function(S) {
                    return 2000 * Math.sqrt(S);
                  }) // assume ease="quad-in-out"
                  .scaleExtent([scale, Infinity])
                  .on("zoom", function() {
                    svg.selectAll("*").remove();
                    svg.append("path").datum({type: "Sphere"}).attr("class", "sphere").attr("d", path);
                    svg.append("path")
                            .datum({type: "LineString", coordinates: [[0, 0], [0, 90], [180, 0], [0, -90], [0, 0]]})
                            .attr("class", "equator")
                            .attr("d", path);
                    var origin = svg.append("path")
                            .datum({type: "Point", coordinates: [0, 0]})
                            .attr("class", "origin")
                            .attr("d", path.pointRadius(5));
                    origin.on("mouseover", function(d,i) {
                      console.log(d.coordinates);
                      console.log(i);
                      tooltip.text("X:" + d3.event.pageX + ", Y:" + d3.event.pageY);
                      return tooltip.style("visibility", "visible").style("top", (d3.event.pageY - 10) + "px").style("left", (d3.event.pageX + 10) + "px");
                    });
                    origin.on("mouseout", function() {
                      return tooltip.style("visibility", "hidden");
                    });
                    svg.append("path")
                            .datum({type: "LineString", coordinates: [[-180, 0], [-90, 0], [0, 0], [90, 0], [180, 0]]})
                            .attr("class", "equator")
                            .attr("d", path);
                    svg.append("path")
                            .datum(graticule)
                            .attr("class", "graticule")
                            .attr("d", path);

                    var landObj = svg.append("path")
                            .datum(land)
                            .attr("class", "land")
                            .attr("d", path);
                    landObj.on("click", landClick);
                    var cc = svg.append("path")
                            .datum(countries[0])
                            .attr("class", "countries")
                            .attr("d", path);
//                    cc.on("click", ccClick);
                    svg.append("path")
                            .datum(borders)
                            .attr("class", "borders")
                            .attr("d", path);
                  });

          svg.call(zoom).call(zoom.event);

          var dt = 0;
          var feature = svg.selectAll("path");
          projection.rotate([rotate[0] + velocity[0] * dt, rotate[1] + velocity[1] * dt, rotate[2] + velocity[2] * dt]);
          feature.attr("d", path);
        });
      }

      function landClick(d, i) {
        console.log(i);
        console.log(d);
      }
      function ccClick(a) {
        console.log(a);
      }

      // Round to integer pixels for speed, and set pixel ratio.
      function roundRatioContext(context) {
        return {
          moveTo: function(x, y) {
            context.moveTo(Math.round(x * ratio), Math.round(y * ratio));
          },
          lineTo: function(x, y) {
            context.lineTo(Math.round(x * ratio), Math.round(y * ratio));
          },
          closePath: function() {
            context.closePath();
          }
        };
      }
    </script>
    <style>
      @import url(/gwac/js/d3/maps.css);

      body{background-color: black;}
      path {fill: none;stroke-linejoin: round;}
      .sphere{stroke: #636B62;stroke-width: 1.5px;}
      .graticule{stroke: #a9a9a9;stroke-width: 0.5px;}
      /*.graticule {fill: none;stroke: #636B62;stroke-width: .5px;stroke-dasharray: 2,2;}*/
      .equator {stroke: #636B62;stroke-width: 1.5px;}
      .linetest {stroke: blue;stroke-width: 4px;}
      .origin{stroke: 636B62;stroke-width: 2px;fill: #636B62;}
      .pointtest {stroke: green;stroke-width: 2px;}
      .polygontest{fill: yellow;stroke:blue;stroke-width: 4px;}
      .land{fill: #69d2e7;}
      .countries{fill: #f00}
      .borders{stroke: #fff; stroke-width: 2px;}

      #main{width:100%;height: 100%;text-align: center;position: absolute; top: 0; left: 0;}
      #header{width: 100%;height: 40px;font-size: 28px;text-align: center; color: white;padding-top: 5px;}
      #sphereDisplay{margin: auto;width:100%;height: 90%}
      #toolbar {position: absolute;bottom: auto;right: 10px;top: 45px;left: auto;width: 120px;z-index: 10;text-align: left;color: white;font-size: 14px;}
      #toolbar label{display: block; margin: 3px 0; padding-left: 15px; text-indent: -15px; cursor: pointer;}
      #toolbar input{width: 14px;height: 14px;padding: 0;margin:0 5px 0 0;vertical-align: bottom;position: relative;top: -1px;*overflow: hidden; cursor: pointer;}
    </style>

  </head>
  <body>
    <div id="main">
      <div id="header">OT分布实时概览图-天球坐标</div>
      <div id="sphereDisplay"></div>
      <div id="toolbar">
        <label for="ot1"><input type="checkbox" checked="" id="ot1">OT1</label>
        <label for="ot2"><input type="checkbox" checked="" id="ot2">OT2</label>
        <label for="ot2cur"><input type="checkbox" checked="" id="ot2cur">OT2-cur</label>
        <label for="templateBorder"><input type="checkbox" id="templateBorder">模板边界</label>
        <label for="galacticPlane"><input type="checkbox" id="galacticPlane">银道面</label>
        <label for="eclipticPlane"><input type="checkbox" id="eclipticPlane">黄道面</label>
        <label for="groundPlane"><input type="checkbox" id="groundPlane">地平面</label>
      </div>
    </div>
  </body>
</html>