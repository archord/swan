<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>Animated World Zoom</title>
    <style>
      @import url(/gwac/js/d3/maps.css);

      body{background-color: black;}
      #control {
        width: 960px;
        margin: 0 auto;
        text-align: right;
        font-size: small;
        font-style: italic;
        color: #666;
      }

      .buffer {
        visibility: hidden;
      }
    </style>

  </head>
  <body>
    <h1>Animated World Zoom</h1>
    <div id="map"></div>
    <div id="control">
      <label for="north-up"><input type="checkbox" checked="" id="north-up"> North is up</label>
    </div>
    <div id="content"></div>
    <script src="/gwac/js/d3/d3.min.js"></script>
    <script src="/gwac/js/d3/topojson.min.js"></script>
    <script src="/gwac/js/d3/d3.geo.zoom.js"></script>
    <script>

      var particler = function() {
        // Returns a function that will generate colored particles
        var particle = new Image(),
                tempFileCanvas = d3.select("#content")
                .append("canvas")
                .attr("class", "buffer")
                .node();

        particle.src = "data:image/png;base64,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";

        tempFileCanvas.width = 64;
        tempFileCanvas.height = 64;

        return function(r, g, b, a) {
          var imgCtx = tempFileCanvas.getContext("2d"),
                  imgData, i;

          imgCtx.drawImage(particle, 0, 0);

          //if(particle.width > self.innerWidth){particle.width=self.innerWidth;} if(particle.width < 1){particle.width=1;}
          //if(particle.height > self.innerHeight){particle.height=self.innerHeight;} if(particle.height < 1){particle.height=1;}
          imgData = imgCtx.getImageData(2, 2, 64, 64);

          //imgData = imgCtx.getImageData(0, 0, particle.width, particle.height);

          i = imgData.data.length;
          while ((i -= 4) > -1) {
            imgData.data[i + 3] = imgData.data[i] * a;
            if (imgData.data[i + 3]) {
              imgData.data[i] = r;
              imgData.data[i + 1] = g;
              imgData.data[i + 2] = b;
            }
          }

          imgCtx.putImageData(imgData, 0, 0);
          return tempFileCanvas;
        }
      }();

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
              .precision(.2);

      var canvas = d3.select("#map").append("canvas")
              .attr("width", width * ratio)
              .attr("height", height * ratio)
              .style("width", width + "px")
              .style("height", height + "px");

      var c = canvas.node().getContext("2d");

      var path = d3.geo.path()
              .projection(projection)
              .context(c);
//              .context(roundRatioContext(c));

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
                  c.strokeStyle = "#fff", c.lineWidth = .5 * ratio, c.beginPath(), path(globe), c.stroke();
                  c.drawImage(particler(255, 0, 0, 1),0, 90, 10, 10);
                });

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