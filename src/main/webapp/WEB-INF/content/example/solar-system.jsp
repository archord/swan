<%-- 
    Document   : solar-system
    Created on : 2016-7-26, 14:17:39
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>solar system example</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/topojson.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.geo.zoom.js"></script>
    <link href="<%=request.getContextPath()%>/resource/js/d3/maps.css" rel="stylesheet" type="text/css">

    <style>
      #starcom_map {
        background-color: #333;
        background-image: -webkit-gradient(linear, left top, right bottom, from(#000), to(rgba(34, 34, 34, 0)));
        background-image: -webkit-linear-gradient(left top, #000, rgba(34, 34, 34, 0));
        background-image: -moz-linear-gradient(left top, #000, rgba(34, 34, 34, 0));
        overflow: hidden;
      }

      .star {
        fill: #fff;
        stroke: rgba(255, 255, 255, .5);
        stroke-width: .25px;
      }

      #defs {
        height: 0;
        width:0;
      }

      g.planet:hover > path.orbit {
        stroke-width: 3px;
      }
      g.planet:hover > circle.body {
        stroke: #AFCADF;
        stroke-width: 25px;
      }
    </style>

  </head>
  <body>
    <svg id="defs">
    <linearGradient id="gradeSol" x1="100%" x2="0" y1="100%" y2="0">
    <stop offset="0" stop-color="#F2CA00"></stop>
    <stop offset=".7" stop-color="#FFFED6"></stop>
    <stop offset="1" stop-color="#FDFFEB"></stop>
    </linearGradient>
  <filter id="glowSol">
    <feColorMatrix type="matrix" values="0 0 0 .9 0
                   0 0 0 .9 0
                   0 0 0 0 0
                   0 0 0 1 0" />
    <feGaussianBlur stdDeviation="5.5" result="coloredBlur" />
    <feMerge>
      <feMergeNode in="coloredBlur" />
      <feMergeNode in="SourceGraphic" />
    </feMerge>
  </filter>
  <linearGradient id="gradePlanet" x1="0%" y1="0%" x2="100%" y2="0%">
  <stop offset="0%" style="stop-color:#005C99;stop-opacity:1" />
  <stop offset="100%" style="stop-color:#0099FF;stop-opacity:1" />
  </linearGradient>
  <filter id="glowPlanet">
    <feColorMatrix type="matrix" values="0 0 0 0   0
                   0 0 0 0.9 0
                   0 0 0 0.9 0
                   0 0 0 1   0" />
    <feGaussianBlur stdDeviation="5.5" result="coloredBlur" />
    <feMerge>
      <feMergeNode in="coloredBlur" />
      <feMergeNode in="SourceGraphic" />
    </feMerge>
  </filter>
  </svg>
  <div id="starcom_map"></div>
  <script>

    var width = 940;
    var height = 800;
    var x = d3.scale.linear()
            .domain([-0.5, 0.5])
            .range([0, width]);
    var y = d3.scale.linear()
            .domain([-0.5875, 0.5875])
            .range([height, 0]);
    var planetScale = d3.scale.linear()
            .domain([0.5, 10])
            .range([100, 225]);
    var starts = {};
    var speed = 6;

    var randomNumber = function(min, max, dec) {
      var num = Math.random() * (max - min) + min;
      return Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
    };

// Credit where credit is due: 
// http://marcneuwirth.com/blog/2012/06/24/creating-the-earth-with-d3-js/
    var generateStars = function(number) {
      var stars = [];
      var max = 0.5;
      var min = -0.5;
      for (var i = 0; i < number; i++) {
        stars.push({
          x: x(randomNumber(-0.5, 0.5, 2)),
          y: y(randomNumber(-0.5875, 0.5875, 2)),
          radius: Math.random() * 1.4
        });
      }
      return stars;
    };

    var svg = d3.select("#starcom_map")
            .append("svg")
            .attr("class", "starcom")
            .attr("width", width)
            .attr("height", height)
            .attr("pointer-events", "all");
    svg.append("g").selectAll("circle")
            .data(generateStars(300))
            .enter()
            .append("circle")
            .attr("class", "star")
            .attr("cx", function(d) {
              return d.x;
            })
            .attr("cy", function(d) {
              return d.y;
            })
            .attr("r", function(d) {
              return d.radius;
            });

    var zoomFunc = function(d) {
      vis.attr("transform", "translate(" + d3.event.translate + ")" +
              " scale(" + d3.event.scale + ")");
    };
    var zoom = d3.behavior.zoom().scaleExtent([0.75, 7]).on("zoom", zoomFunc);

    var vis = svg.append('g')
            .call(zoom)
            .append('g');
    vis.append("rect")
            .attr("width", width)
            .attr("height", height)
            .attr("fill", "none")
            .attr("translate", function(d) {
              return "translate(" + x(-0.5) + "," + y(0.5875) + ")";
            });
    vis.append("circle")
            .attr("class", "sun")
            .attr("r", 100)
            .attr("fill", "url(#gradeSol)")
            .attr("filter", "url(#glowSol)")
            .attr("transform", function(d) {
              return "translate(" + x(0) + "," + y(0) + "), scale(.4)";
            });
    var username = "SomePlayer";
    var sol_label = vis.append("g")
            .attr("class", "starcom_label")
            .attr("transform", function() {
              return "translate(" + x(-0.45) + "," + y(0.5) + ")";
            });
    sol_label.append("text")
            .text(function(d) {
              return username + "'s System";
            });

    var planetData = [{
        "id": 7,
        "orbital_radius": 0.13237744727881,
        "radius": 5.22024847014751
      }, {
        "id": 8,
        "orbital_radius": 0.316765600441873,
        "radius": 7.86819859118858
      }, {
        "id": 9,
        "orbital_radius": 0.460161583358368,
        "radius": 4.13240727781911
      }, {
        "id": 10,
        "orbital_radius": 0.421843370415053,
        "radius": 3.23132581798664
      }, {
        "id": 11,
        "orbital_radius": 0.425488589085115,
        "radius": 1.33492278435846
      }, {
        "id": 12,
        "orbital_radius": 0.494398347225706,
        "radius": 4.35501915015778
      }, {
        "id": 13,
        "orbital_radius": 0.317368376158856,
        "radius": 9.3869892039431
      }];

    var deselectOrbit = function(el, model) {
      var transition = d3.select(el).transition().duration(750).delay(5);
      transition.select("circle.body")
              .attr("transform", function(d) {
                return "translate(" + x(d.orbital_radius) + ", " + y(0) + "), scale(.05)";
              });
    };

    var selectOrbit = function(el, model) {
      var transition = d3.select(el).transition().duration(750).delay(250);
      transition.select("circle.body")
              .attr("transform", function(d) {
                return "translate(" + x(d.orbital_radius) + ", " + y(0) + "), scale(.15)";
              });
    };

    var planets = vis.selectAll("g.planet").data(planetData);
    var planetEnter = planets.enter().append("g")
            .attr("class", "planet")
            .on("mouseover", function(d) {
              selectOrbit(this, d);
            })
            .on("mouseout", function(d) {
              deselectOrbit(this, d);
            });

// Magical happenings
    var orbital_arc = d3.svg.arc()
            .startAngle(0)
            .endAngle(Math.PI * 2) // 360 degrees
            .innerRadius(function(d) {
              return x(d.orbital_radius) - width/2;
            }) // Magic number.
            .outerRadius(function(d) {
              return x(d.orbital_radius) - width/2;
            }); // Magic number.

    var selector_arc = d3.svg.arc()
            .startAngle(0)
            .endAngle(Math.PI * 2) // 360 degrees
            .innerRadius(function(d) {
              return x(d.orbital_radius) - (width/2 - 5);
            }) // Magic number.
            .outerRadius(function(d) {
              return x(d.orbital_radius) - (width/2 + 5);
            }); // Magic number.

    planetEnter.append("path")
            .attr("class", "orbit")
            // Arc offset by magic number
            .attr("d", orbital_arc)
            .attr("fill", "#fff")
            .style("stroke", "#fff")
            .attr("transform", function(d) {
              return "translate(" + x(0) + ", " + y(0) + ")";
            });

    planetEnter.append("circle")
            .attr("r", function(d) {
              return planetScale(d.radius);
            })
            .attr("class", "body")
            .attr("fill", "url(#gradePlanet)")
            .attr("filter", "url(#glowPlanet)")
            .attr("transform", function(d) {
              // Position of planet's orbit
              return "translate(" + x(d.orbital_radius) + ", " + y(0) + "), scale(.05)";
            });
// End of magic

    planetEnter.append("path")
            .attr("class", "overlay_orbit")
            .attr("d", selector_arc)
            .attr("fill", "none")
            .attr("transform", function(d) {
              return "translate(" + x(0) + ", " + y(0) + ")";
            });

    var startOrbit = function() {
      var transform = function(d) {
        var start = starts[d.id];
        if (!start) {
          starts[d.id] = Date.now();
          start = starts[d.id];
        }
        var angle = ((Date.now() - start) * speed);
        var degree = angle / x(d.orbital_radius);
        if (degree >= 360) {
          starts[d.id] = Date.now();
          start = starts[d.id];
          degree = ((Date.now() - start) * speed);
        }
        return "rotate(" + degree + ", " + x(0) + ", " + y(0) + ")";
      };

      vis.selectAll("g.planet").attr("transform", transform);
    };

    d3.timer(startOrbit);
  </script>
</body>
</html>
