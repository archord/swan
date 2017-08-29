<%-- 
    Document   : rect
    Created on : 2016-8-9, 18:20:23
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="ocks-org do-not-copy">
  <head>
    <meta charset="utf-8">
    <title>GWAC监控页面</title>
    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <style>
      body{background: #000}
      .background {fill: #000;}
      line {stroke: #000;}
      text.active {fill: red;}
      #main{width:100%;padding: 5px 10px 5px 10px;}
      .axisLabel{fill: #fff; stroke: none;}
    </style>
  </head>
  <body>
    <script>
      //https://github.com/d3/d3-3.x-api-reference/blob/master/API-Reference.md
      var margin = {top: 80, right: 80, bottom: 10, left: 80};
      var conWidth = 200;
      var winWidth = $(window).width();
      var winHeight = $(window).height();
      var width = winWidth - margin.left - margin.right;
      var height = winHeight - margin.top - margin.bottom;
      var x = d3.scale.ordinal().rangeBands([0, width]);
      var y = d3.scale.ordinal().rangeBands([0, height]);
      var z = d3.scale.linear().domain([0, 4]).clamp(true);
      var c = d3.scale.linear().domain([0, 1, 2]).range(["#00EE00", "#EEAD0E", "#CD2626"]); //#eee green yellow red "#00EE00", "#EEAD0E", "#CD2626"

      var yLabel = [];
      var xLabel = [];
      var colorMap = [];
      var mNum = 12;
      var gNum = 20;
      var pNum = 60;
      var xRange = d3.range(pNum);
      var yRange = d3.range(mNum + gNum);

      x.domain(xRange);
      y.domain(yRange);
      xLabel = xRange;
      yRange.forEach(function(d, i) {
        yLabel[i] = i < gNum ? 'G' + numPadding(i + 1, 2) : 'M' + numPadding(i + 1 - gNum, 2);
      });

      randomColorMap();

      var svg = d3.select("body").append("svg")
              .attr("width", winWidth)
              .attr("height", winHeight)
              .style("margin-left", "-30px")
              .style("margin-top", "-50px")
              .append("g")
              .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

      draw(colorMap);
      setInterval(updateData, 1000)
//      setTimeout(updateData, 000);
//      updateData();

      function draw(colorMap) {

        svg.append("rect")
                .attr("class", "background")
                .attr("width", width)
                .attr("height", height);

        var row = svg.selectAll(".row")
                .data(colorMap)
                .enter().append("g")
                .attr("class", "row")
                .attr("transform", function(d, i) {
                  return "translate(0," + y(i) + ")";
                })
                .each(rowDrawRect);

        row.append("line")
                .attr("x2", width);

        row.append("text")
                .attr("class", "axisLabel")
                .attr("x", -6)
                .attr("y", x.rangeBand() / 2)
                .attr("dy", ".32em")
                .attr("text-anchor", "end")
                .text(function(d, i) {
                  var text = "";
                  if (i % 2 === 0) {
                    text = yLabel[i]
                  }
                  return yLabel[i];
                });

        var column = svg.selectAll(".column")
                .data(xLabel)
                .enter().append("g")
                .attr("class", "column")
                .attr("transform", function(d, i) {
                  return "translate(" + x(i) + ")rotate(-90)";
                });

        column.append("line")
                .attr("x1", -height);

        column.append("text")
                .attr("class", "axisLabel")
                .attr("x", function(d, i) {
                  var x = 0;
                  if (i < 10) {
                    x = y.rangeBand() / 2;
                  }
                  return x;
                })
                .attr("y", -8)
                .attr("dy", ".32em")
                .attr("text-anchor", "start")
                .attr("transform", function(d, i) {
                  return "rotate(90)";
                })
                .text(function(d, i) {
                  var text = "";
                  if (i % 2 === 0) {
                    text = xLabel[i] + 1
                  }
                  return text;
                });

      }

      function rowDraw(row) {
        var cell = d3.select(this).selectAll(".cell")
                .data(row.filter(function(d) {
                  return true;
                })).enter().append("circle"); //circle rect
        cell.attr("class", "cell")
                .attr("cx", function(d, i) {
                  return x(d.x) + x.rangeBand() / 2.0;
                })
                .attr("cy", function(d, i) {
                  return y.rangeBand() / 2.0;
                })
                .attr("r", function(d, i) {
                  return x.rangeBand() > y.rangeBand() ? Math.floor(y.rangeBand() / 3) : Math.floor(x.rangeBand() / 3);
                })
                .attr("value", function(d) {
                  return d.name;
                });
        cell.style("fill", function(d) {
          return c(d.z);
        });
        cell.on("click", cellClick);
        cell.append("title").text(function(d, i) {
          return d.name + "-" + (d.x + 1);
        });
      }


      function rowDrawRect(row) {
        var cell = d3.select(this).selectAll(".cell")
                .data(row.filter(function(d) {
                  return true;
                })).enter().append("rect"); //circle rect
        cell.attr("class", "cell")
                .attr("x", function(d) {
                  return x(d.x);
                })
                .attr("width", x.rangeBand())
                .attr("height", y.rangeBand())
                .attr("value", function(d) {
                  return d.name;
                });
        cell.style("fill", function(d) {
          return c(d.z);
        });
        cell.on("click", cellClick)
        cell.append("title").text(function(d, i) {
          return d.name + "-" + (d.x + 1);
        });

      }


      function updateData() {
        var cell = d3.selectAll(".cell")
                .filter(function(d) {
                  return d.z;
                })
                .style("fill", function(d) {
                  return c(0);
                });

        randomColorMap();
        d3.selectAll(".cell")
                .filter(function(d) {
                  d.z = colorMap[d.y][d.x].z;
                  return d.z;
                })
                .style("fill", function(d) {
                  return c(d.z);
                });
      }

      function randomColorMap() {

        yRange.forEach(function(d, i) {
          colorMap[i] = xLabel.map(function(j) {
            var tmp = 0;
            var ran = Math.random();
            if (ran > 0.999) {
              tmp = 2;
            } else if (ran > 0.99) {
              tmp = 1;
            }
            return {name: yLabel[i], x: j, y: i, z: tmp};
          });
        });
      }

      function cellClick(d) {
        console.log("cx " + $(this).attr("cx") + " ,cy " + $(this).attr("cy"));
        console.log(d);
      }

      function numPadding(n, width, z) {
        z = z || '0';
        n = n + '';
        return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
      }
    </script>

  </body>
</html>

