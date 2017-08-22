<!DOCTYPE html>
<html lang="en"><head>
    <meta charset="utf-8">
    <title>D3-Celestial Starmap</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/celestial-d3/d3.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/celestial-d3/d3.geo.projection.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resource/celestial-d3/celestial.min.js"></script>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/celestial-d3/celestial.css">
  </head>
  <body style="text-align:center">
    <div style="overflow:hidden;text-align:center"><div id="celestial-map" style="text-align:left; "></div></div>
    <div id="celestial-form" style="margin:0 auto"></div>
    <script type="text/javascript">
      var theight  =$(document).height();
      var config = {
        width:theight,
        projection: "airy",
        transform: "equatorial",
        center: [117.58, 40.39, 0],
        background: {fill: "#000000", stroke: "#000000", width: 1},
        adaptable: true,
        interactive: true,
        form: true,
        location: true,
        fullwidth: false,
        controls: true,
        lang: "en",
        container: "celestial-map",
        datapath: "<%=request.getContextPath()%>/resource/celestial-d3/data",
        stars: {show: false},
        constellations: {
          show: true, //Show constellations 
          names: false, //Show constellation names 
          desig: true, //Show short constellation names (3 letter designations)
          lines: true, //Show constellation lines 
          linestyle: {stroke: "#eeeeee", width: 1, opacity: 0.4},
          bounds: false, //Show constellation boundaries 
          boundstyle: {stroke: "#ccff00", width: 1, opacity: 0.4, dash: [4, 8]}
        },
        dsos: {
          show: true, //Show Deep Space Objects
          limit: 16, //up to maximum magnitude
          names: false, //Show DSO names
          data: "dsos.bright.json"
        },
        lines: {
          graticule: {show: true, stroke: "#cccccc", width: 1, opacity: .2, // Show graticule lines 
            lon: {pos: [0], fill: "#eee", font: "12px 'Lucida Sans Unicode', Trebuchet, Helvetica, Arial, sans-serif"}
          },
          equatorial: {show: true, stroke: "#aaaaaa", width: 1.5, opacity: .5}, // Show equatorial plane 
          ecliptic: {show: true, stroke: "#66cc66", width: 1.5, opacity: .5}, // Show ecliptic plane 
          galactic: {show: true, stroke: "#cc6666", width: 1.5, opacity: .5}, // Show galactic plane 
          supergalactic: {show: false, stroke: "#cc66cc", width: 1.5, opacity: .5} // Show supergalactic plane 
        }
      };
      Celestial.display(config);
      //Celestial.rotate({center:[117.58, 40.39]});
    </script>
    <footer>
      <p><a href="https://github.com/ofrohn/d3-celestial"><b>D3-Celestial</b></a> released under <a href="http://opensource.org/licenses/BSD-3-Clause">BSD license</a>. Copyright 2015-17 <a href="http://armchairastronautics.blogspot.com/" rel="author">Olaf Frohn</a>.
      </p></footer>
  </body>
</html>