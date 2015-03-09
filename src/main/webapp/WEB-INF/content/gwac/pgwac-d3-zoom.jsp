<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>Animated World Zoom</title>
    <style>
      @import url(/gwac/js/d3/maps.css);

      #control {
        width: 960px;
        margin: 0 auto;
        text-align: right;
        font-size: small;
        font-style: italic;
        color: #666;
      }
    </style>

  </head>
  <body>
    <h1>Animated World Zoom</h1>
    <div id="map"></div>
    <div id="control">
      <label for="north-up"><input type="checkbox" checked="" id="north-up"> North is up</label>
    </div>
    <script src="/gwac/js/d3/d3.min.js"></script>
    <script src="/gwac/js/d3/topojson.min.js"></script>
    <script src="/gwac/js/d3/d3.geo.zoom.js"></script>
    <script>

      var degrees = 180 / Math.PI,
              ratio = window.devicePixelRatio || 1,
              width = 960,
              height = 500,
              p = ratio;

      var projection = d3.geo.orthographic()
              .rotate([0, -30])
              .scale(height / 2 - 1)
              .translate([width / 2, height / 2])
              .clipExtent([[-p, -p], [width + p, height + p]])
              .precision(.5);

      var canvas = d3.select("#map").append("canvas")
              .attr("width", width * ratio)
              .attr("height", height * ratio)
              .style("width", width + "px")
              .style("height", height + "px");

      var c = canvas.node().getContext("2d");

      var path = d3.geo.path()
              .projection(projection)
              .context(roundRatioContext(c));

      var northUp = d3.select("#north-up").on("change", function() {
        northUp = this.checked;
      }).property("checked");

      d3.json("/gwac/json/world-110m.json", function(error, world) {

        var globe = {type: "Sphere"};
        var graticule = d3.geo.graticule()();
        console.log(graticule);
        var land = topojson.feature(world, world.objects.land);
        console.log(land);
        var borders = topojson.mesh(world, world.objects.countries);
        var countries = d3.shuffle(topojson.feature(world, world.objects.countries).features);
        var i = -1;
        var i0 = i;

        var zoom = d3.geo.zoom()
                .projection(projection)
                .duration(function(S) {
                  return 2000 * Math.sqrt(S);
                }) // assume ease="quad-in-out"
                .scaleExtent([height / 2 - 1, Infinity])
                .on("zoom", function() {
                  projection.clipAngle(Math.asin(Math.min(1, .5 * Math.sqrt(width * width + height * height) / projection.scale())) * degrees);
                  c.clearRect(0, 0, width * ratio, height * ratio);
                  c.strokeStyle = "#999", c.lineWidth = .25 * ratio, c.beginPath(), path(graticule), c.stroke();
                  c.fillStyle = "#69d2e7", c.beginPath(), path(land), c.fill();
                  c.fillStyle = "#f00", c.beginPath(), path(countries[i0]), c.fill();
//                  c.fillStyle = "#f00", c.beginPath(), path(countries[i]), c.fill();
                  c.strokeStyle = "#fff", c.lineWidth = .5 * ratio, c.beginPath(), path(borders), c.stroke();
                  c.strokeStyle = "#000", c.lineWidth = .5 * ratio, c.beginPath(), path(globe), c.stroke();
                })
                .on("zoomend", transition);

        canvas.call(zoom).call(zoom.event);

        function transition() {
          zoomBounds(projection, countries[i = ((i0 = i) + 1) % countries.length]);
          canvas.transition().ease("quad-in-out")
                  .duration(2000) // see https://github.com/mbostock/d3/pull/2045
                  .call(zoom.projection(projection).event);
        }

        function zoomBounds(projection, o) {
          var centroid = d3.geo.centroid(o),
                  clip = projection.clipExtent();

          projection.rotate(northUp ? [-centroid[0], -centroid[1]] : zoom.rotateTo(centroid))
                  .clipExtent(null)
                  .scale(1)
                  .translate([0, 0]);

          var b = path.bounds(o),
                  k = Math.min(1000, .45 / Math.max(Math.max(Math.abs(b[1][0]), Math.abs(b[0][0])) / width, Math.max(Math.abs(b[1][1]), Math.abs(b[0][1])) / height));

          projection.clipExtent(clip)
                  .scale(k)
                  .translate([width / 2, height / 2]);
        }
      });

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
  </body>
</html>