<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="tele-status-id">

</div>

<div>
  <p>每个设备的状态用设备的背景颜色标示：白（未开机）、绿（正常）、黄（有故障）、红（损坏）、灰（下线）</p>
</div>

<script>
  //https://github.com/d3/d3-3.x-api-reference/blob/master/API-Reference.md
  var margin = {top: 100, right: 100, bottom: 100, left: 100};
  var conWidth = 200;
  var winWidth = $("#tabs").width();
  var winHeight = $("#tabs").height();
  var width = winWidth - margin.left - margin.right;
  var height = winHeight - margin.top - margin.bottom;
  var x = d3.scale.ordinal().rangeBands([0, width]);
  var y = d3.scale.ordinal().rangeBands([0, height]);
  var x2 = d3.scale.ordinal().rangeBands([0, width]);
  var y2 = d3.scale.ordinal().rangeBands([0, height]);
  var z = d3.scale.linear().domain([0, 4]).clamp(true);
  var c = d3.scale.linear().domain([0, 1, 2]).range(["#00EE00", "#EEAD0E", "#CD2626"]); //#eee green yellow red "#00EE00", "#EEAD0E", "#CD2626"

  var yLabel = [];
  var xLabel = [];
  var colorMap = [];
  var colorMap2 = [];
  var rowNum = 2;
  var colNum = 5;
  var xRange = d3.range(colNum);
  var yRange = d3.range(rowNum);
  var xRange2 = d3.range(colNum * 2);
  var yRange2 = d3.range(rowNum * 2);

  x.domain(xRange);
  y.domain(yRange);
  x2.domain(xRange2);
  y2.domain(yRange2);
  xLabel = xRange;
  yLabel = yRange;
  xLabel2 = xRange2;
  yLabel2 = yRange2;

  randomColorMap();

  var svg = d3.select("#tele-status-id").append("svg")
          .attr("width", winWidth)
          .attr("height", winHeight)
          .style("margin-left", "-30px")
          .style("margin-top", "-50px")
          .append("g")
          .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
console.log(colorMap);
  draw(colorMap);
  // setInterval(updateData, 1000)
//      setTimeout(updateData, 000);
//      updateData();

  function draw(colorMap) {

    var rect = svg.append("rect")
            .attr("class", "background")
            .attr("width", width)
            .attr("height", height);

    var row = svg.selectAll(".tele-row")
            .data(colorMap)
            .enter().append("g")
            .attr("class", "tele-row")
            .attr("transform", function(d, i) {
              return "translate(0," + y(i) + ")";
            })
            .each(rowDrawRect);

    svg.selectAll(".tele-row2")
            .data(colorMap2)
            .enter().append("g")
            .attr("class", "tele-row2")
            .attr("transform", function(d, i) {
              return "translate(0," + y2(i) + ")";
            })
            .each(rowDrawCircle);

    var column = svg.selectAll(".tele-column")
            .data(xLabel)
            .enter().append("g")
            .attr("class", "tele-column")
            .attr("transform", function(d, i) {
              return "translate(" + x(i) + ")rotate(-90)";
            });

    row.append("line")
            .attr("x2", width);
    column.append("line")
            .attr("x1", -height);
    svg.append("line")
            .attr("x1", 0)
            .attr("y1", height)
            .attr("x2", width)
            .attr("y2", height);
    //.attr("style", "stroke:rgb(255,0,0);stroke-width:4");
    svg.append("line")
            .attr("x1", width)
            .attr("y1", 0)
            .attr("x2", width)
            .attr("y2", height);

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
      //return c(d.z);
      return "#FFFFFF";
    });


    cell = d3.select(this).selectAll(".cell-circle")
            .data(row.filter(function(d) {
              return true;
            })).enter().append("circle"); //circle rect
    cell.attr("class", "cell-circle")
            .attr("cx", function(d, i) {
              return x(d.x) + x.rangeBand() / 2.0;
            })
            .attr("cy", function(d, i) {
              return y.rangeBand() / 2.0;
            })
            .attr("r", function(d, i) {
              return x.rangeBand() > y.rangeBand() ? Math.floor(y.rangeBand() / 10) : Math.floor(x.rangeBand() / 10);
            })
            .attr("value", function(d) {
              return d.name;
            });
    cell.attr("style", "stroke:rgb(0,0,0);stroke-width:2;fill:#fff;");
  }

  function rowDrawCircle(row) {
    var g = d3.select(this).selectAll(".cell2")
            .data(row.filter(function(d) {
              return true;
            })).enter().append("g");
    var cell = g.append("circle"); //circle rect
    cell.attr("class", "cell2")
            .attr("cx", function(d, i) {
              return x2(d.x) + x2.rangeBand() / 2.0;
            })
            .attr("cy", function(d, i) {
              return y2.rangeBand() / 2.0;
            })
            .attr("r", function(d, i) {
              return x2.rangeBand() > y2.rangeBand() ? Math.floor(y2.rangeBand() / 3) : Math.floor(x2.rangeBand() / 3);
            });
    cell.attr("value", function(d) {
      return d.name;
    }).attr("style", "stroke:rgb(0,0,0);stroke-width:2;fill:#fff;");
    cell.on('mouseover', function() {
      d3.select(this).style('stroke-width', '4');
    });
    cell.on('mouseout', function() {
      d3.select(this).style('stroke-width', '2');
    });

    var text = g.append("text")
            .attr("x", function(d) {
              //return x2(d.x) + x2.rangeBand() / 2.0-10;
              return x2(d.x) + x2.rangeBand() / 2.0;
            })
            .attr("y", function(d, i) {
              //return y2.rangeBand() / 2.0+10;
              return y2.rangeBand() / 2.0;
            })
            .attr("style", "fill:blue;font-size:14px")
            .attr("text-anchor", "middle")
            .attr("alignment-baseline", "middle")
            .text("GM");
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

    yRange2.forEach(function(d, i) {
      colorMap2[i] = xLabel2.map(function(j) {
        var tmp = 0;
        var ran = Math.random();
        if (ran > 0.999) {
          tmp = 2;
        } else if (ran > 0.99) {
          tmp = 1;
        }
        return {name: yLabel2[i], x: j, y: i, z: tmp};
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