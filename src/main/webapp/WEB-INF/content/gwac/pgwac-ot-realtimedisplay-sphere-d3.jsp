<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <style>

      path {
        fill: none;
        stroke-linejoin: round;
      }

      .sphere,
      .graticule {
        stroke: #aaa;
      }

      .equator {
        stroke: red;
        stroke-width: 2px;
      }

      .linetest {
        stroke: blue;
        stroke-width: 4px;
      }

      .pointtest {
        stroke: green;
        stroke-width: 2px;
      }

    </style>
  </head>
  <body>
    <!--<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.js"></script>-->
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/d3/d3.v3.min.js"></script>
    <script type="text/javascript">

      f2();

      function f1() {
        var width = 960,
                height = 960,
                scale = width / 2 - 4;

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

      function f2() {

        var width = 960,
                height = 700,
                rotate = [10, -10],
                velocity = [.003, -.001],
                time = Date.now();

        var projection = d3.geo.orthographic()
                .scale(240)
                .translate([width / 2, height / 2])
                .clipAngle(90 + 1e-6)
                .precision(.3);

        var path = d3.geo.path()
                .projection(projection);

        var graticule = d3.geo.graticule();

        var svg = d3.select("body").append("svg")
                .attr("width", width)
                .attr("height", height);

        svg.append("path")
                .datum({type: "Sphere"})
                .attr("class", "sphere")
                .attr("d", path);

        svg.append("path")
                .datum(graticule)
                .attr("class", "graticule")
                .attr("d", path);

        svg.append("path")
                .datum({type: "LineString", coordinates: [[-180, 0], [-90, 0], [0, 0], [90, 0], [180, 0]]})
                .attr("class", "equator")
                .attr("d", path);

        svg.append("path")
                .datum({type: "LineString", coordinates: [[-60, 30], [-30, 60]]})
                .attr("class", "linetest")
                .attr("d", path);

        svg.append("path")
                .datum({type: "LineString", coordinates: [[-24, 60],[30, 60]]})
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


        var feature = svg.selectAll("path");

        d3.timer(function() {
          var dt = Date.now() - time;
          projection.rotate([rotate[0] + velocity[0] * dt, rotate[1] + velocity[1] * dt]);
          feature.attr("d", path);
        });
      }

    </script>
  </body>
</html>