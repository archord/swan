<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <style>
      body{background-color: black;}
      path {fill: none;stroke-linejoin: round;}
      .sphere{stroke: #636B62;stroke-width: 1.5px;}
      .graticule{stroke: #a9a9a9;}
      /*.graticule {fill: none;stroke: #636B62;stroke-width: .5px;stroke-dasharray: 2,2;}*/
      .equator {stroke: #636B62;stroke-width: 1.5px;}
      .linetest {stroke: blue;stroke-width: 4px;}
      .pointtest {stroke: green;stroke-width: 2px;}
      .polygontest{fill: yellow;stroke:blue;stroke-width: 4px;}
      #main{width:100%;height: 100%;text-align: center;}
      #header{width: 100%;height: 50px;}
      #sphereDisplay{margin: auto;width:600px;height: 600px}
    </style>
  </head>
  <body>
    <div id="main">
      <div id="header"></div>
      <div id="sphereDisplay"></div>
    </div>

    <!--for f1()--->
<!--<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.js"></script>-->
    <!--for f2()--->
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.v3.min.js"></script>
    <script type="text/javascript">

      f2();

      function f2() {

        var width = 600;
        var height = 600;
        var scale = 300;  //显示的大小,相对于单位长度
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
        var graticule = d3.geo.graticule();//网格
        var svg = d3.select("#sphereDisplay").append("svg").attr("width", width).attr("height", height);
        svg.append("path").datum({type: "Sphere"}).attr("class", "sphere").attr("d", path);
        svg.append("path").datum(graticule).attr("class", "graticule").attr("d", path);
        svg.append("path")
                .datum({type: "LineString", coordinates: [[-180, 0], [-90, 0], [0, 0], [90, 0], [180, 0]]})
                .attr("class", "equator")
                .attr("d", path);
        svg.append("path")
                .datum({type: "LineString", coordinates: [[-60, 30], [-30, 60]]})
                .attr("class", "linetest")
                .attr("d", path);
        svg.append("path")
                .datum({type: "LineString", coordinates: [[-24, 60], [30, 60]]})
                .attr("class", "pointtest")
                .attr("d", path);
        svg.append("path")
                .datum({type: "Point", coordinates: [-22, 60]})
                .attr("class", "pointtest")
                .attr("d", path.pointRadius(10));
        svg.append("path")
                .datum({type: "Point", coordinates: [30, 60]})
                .attr("class", "pointtest")
                .attr("d", path.pointRadius(2));
        svg.append("path")
                .datum({type: "Polygon", coordinates: [[0, 0], [0, 10], [10, 10], [10, 0], [0, 0]]})
                .attr("class", "polygontest")
                .attr("d", path);


        var feature = svg.selectAll("path");
        
//          var dt = Date.now() - time;
//          projection.rotate([rotate[0] + velocity[0] * dt, rotate[1] + velocity[1] * dt, rotate[2] + velocity[2] * dt]);
//          feature.attr("d", path);

        d3.timer(function() {
          var dt = Date.now() - time;
          projection.rotate([rotate[0] + velocity[0] * dt, rotate[1] + velocity[1] * dt, rotate[2] + velocity[2] * dt]);
          feature.attr("d", path);
          return true;
        });
      }


      function f1() {
        var width = 960;
        var height = 960;
        var scale = width / 2 - 4;

        var canvas = d3.select("body").append("canvas")
                .attr("width", width)
                .attr("height", height);

        var context = canvas.node().getContext("2d");

        var sphere = {type: "Sphere"},
        graticule = d3.geo.graticule()();

        d3.timer(function(elapsed) {
          var render = d3.geo.pipeline()
                  .source(d3.geo.jsonSource)
                  .pipe(d3.geo.rotate, 0, -.5, 0)
                  .pipe(d3.geo.rotate, elapsed / 3000, 0, 0)
                  .pipe(d3.geo.clipCircle, Math.PI / 2)
                  .pipe(d3.geo.project, d3.geo.orthographic, .3 / scale)
                  .sink(d3.geom.contextSink, context);

          context.clearRect(0, 0, width, height);
          context.save();
          context.translate(width / 2, height / 2);
          context.scale(scale, -scale);

          context.beginPath();
          render(graticule);
          context.lineWidth = 1 / scale;
          context.stroke();

          context.beginPath();
          render(sphere);
          context.lineWidth = 2.5 / scale;
          context.stroke();

          context.restore();
        });

        d3.select(self.frameElement).style("height", height + "px");
      }
    </script>
  </body>
</html>