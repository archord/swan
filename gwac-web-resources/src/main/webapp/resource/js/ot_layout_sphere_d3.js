
(function($) {

  function maybeCall(thing, ctx) {
    return (typeof thing == 'function') ? (thing.call(ctx)) : thing;
  }

  function Gwac(placeholder, root, url) {
    this.placeholder = placeholder;
    this.rootUrl = root;
    this.url = root + "/" + url;
    this.pingsUrl = this.rootUrl + "/resource/images/pings.png";
  }

  Gwac.prototype = {
    reqNum: 1,
    curSerialNum: 0,
    graticule: {data: d3.geo.graticule()(), class: "graticule"}, //球面网格，经度纬度方向上以10度为间隔
    sphere: {data: {type: "Sphere"}, class: "sphere"}, //投影平面外侧圆
    equator: {data: {type: "LineString", coordinates: [[-180, 0], [-90, 0], [0, 0], [90, 0], [180, 0]]}, class: "equator"}, //赤道
    primemeridian: {data: {type: "LineString", coordinates: [[0, 90], [0, 0], [0, -90]]}, class: "primemeridian"}, //本初子午线
    origin: {data: {type: "Point", coordinates: [0, 0]}, class: "origin"}, //原点
    templateborder: {data: {type: "Polygon", coordinates: []}, class: "templateborder"},
    galacticplane: {data: {type: "LineString", coordinates: []}, class: "galacticplane"},
    eclipticplane: {data: {type: "LineString", coordinates: []}, class: "eclipticplane"},
    horizon: {data: {type: "LineString", coordinates: []}, class: "horizon"},
    ot1Data: {type: "Point", class: "ot1", radius: 1, stars: []},
    ot1Data2: {data: {type: "MultiPoint", coordinates: []}, class: "ot1", radius: 1},
    ot2Data: {type: "Point", class: "ot2", radius: 2, stars: []},
    ot2mchData: {type: "Point", class: "ot2mch", radius: 2, stars: []},
    ot2curData: {type: "Point", class: "ot2cur", radius: 3, stars: []},
    ot2newData: {type: "Point", class: "ot2new", radius: 3, stars: []},
    varstarData: {type: "Point", class: "varstar", radius: 3, stars: []},
    reqData: {},
    ot1: [],
    ot2: [],
    ot2mch: [],
    ot2cur: [],
    ot2new: [],
    varstar: [],
    parseData: function(reqData) {
      this.reqData = reqData;
//      this.ot1Data.stars = reqData.otLv1;
//      this.ot2Data.stars = reqData.otLv2;
//      this.ot2mchData.stars = reqData.otLv2Mch;
//      this.ot2curData.stars = reqData.otLv2Cur;
//      this.ot2newData.stars = reqData.otLv2New;
//      this.varstarData.stars = reqData.otVarStar;
    },
    draw: function() {

      var gwac = this;
      d3.select(gwac.placeholder + " svg").remove();

      var degrees = 180 / Math.PI;
      var time = Date.now();
      var ratio = window.devicePixelRatio || 1;
      var p = ratio;
      var width = $(gwac.placeholder).width();
      var height = $(gwac.placeholder).height();
      var scale = height / 3 - 1;  //显示的大小,相对于单位长度
      var clipAngle = 90 - 5;  //投影显示的部分，180显示整个球 1E-6
      var precision = 0.1; //线采样精度
      var rotate = [20, -40, 0]; //初始角度
      var velocity = [.01, 0, 0]; //转速

      var projection = d3.geo.orthographic()
              .scale(scale)
              .translate([width / 2, height / 2])
              .clipAngle(clipAngle)
              .clipExtent([[-1, -1], [width + 1, height + 1]])
              .precision(precision);

      var path = d3.geo.path().projection(projection);
      var svg = d3.select("#sphereDisplay").append("svg").attr("width", width).attr("height", height);
      var tooltip = d3.select("#tooltip");

      var zoom = d3.geo.zoom().projection(projection)
              .scaleExtent([scale, Infinity])
              .on("zoom", function() {
                svg.selectAll("*").remove();
                svg.append("image").attr("xlink:href", gwac.pingsUrl).attr("width", 40).attr("height", 40).attr("x", 10).attr("y", 10);
                svg.append("path").datum(gwac.graticule.data).attr("class", gwac.graticule.class).attr("d", path);
                svg.append("path").datum(gwac.sphere.data).attr("class", gwac.sphere.class).attr("d", path);
                svg.append("path").datum(gwac.equator.data).attr("class", gwac.equator.class).attr("d", path);
                svg.append("path").datum(gwac.primemeridian.data).attr("class", gwac.primemeridian.class).attr("d", path);

//                for (var i = 0; i < gwac.reqData.otLv1.length; i++) {
//                  var tstar = gwac.reqData.otLv1[i];
//                  var mName = tstar.dpmId < 10 ? "M0" + tstar.dpmId : "M" + tstar.dpmId;
//                  var tnode = svg.append("path").datum({type: "Point", coordinates: [tstar.raD, tstar.decD]}).attr("class", gwac.ot1Data.class).attr("d", path);
//                  tnode.append("title").text(mName + "(" + tstar.raD + "," + tstar.decD + ")");
//                }
                /*ot1使用MultiPoint*/
                var ot1coor = gwac.ot1Data2.data.coordinates;
                while (ot1coor.length > 0) {
                  ot1coor.pop();
                }
                for (var i = 0; i < gwac.reqData.otLv1.length; i++) {
                  var tstar = gwac.reqData.otLv1[i];
                  ot1coor.push([tstar.raD, tstar.decD]);
                }
                var ot1node = svg.append("path").datum(gwac.ot1Data2.data).attr("class", gwac.ot1Data2.class).attr("d", path);

                for (var i = 0; i < gwac.reqData.otLv2.length; i++) {
                  var tstar = gwac.reqData.otLv2[i];
                  var mName = tstar.dpmId < 10 ? "M0" + tstar.dpmId : "M" + tstar.dpmId;
                  var tnode = svg.append("path").datum({type: "Point", coordinates: [tstar.ra, tstar.dec]}).attr("class", gwac.ot2Data.class).attr("d", path.pointRadius(3));
                  tnode.append("title").text(tstar.name + "," + mName + "(" + tstar.ra + "," + tstar.dec + ")");
                  tnode.attr("value", tstar.name);
                  tnode.on("click", gwac.clickStar);
                  tnode.on("mouseover", gwac.clickStar);
                }
                for (var i = 0; i < gwac.reqData.otLv2Mch.length; i++) {
                  var tstar = gwac.reqData.otLv2Mch[i];
                  var mName = tstar.dpmId < 10 ? "M0" + tstar.dpmId : "M" + tstar.dpmId;
                  var tnode = svg.append("path").datum({type: "Point", coordinates: [tstar.ra, tstar.dec]}).attr("class", gwac.ot2mchData.class).attr("d", path.pointRadius(3));
                  tnode.append("title").text(tstar.name + "," + mName + "(" + tstar.ra + "," + tstar.dec + ")");
                  tnode.attr("value", tstar.name);
                  tnode.on("click", gwac.clickStar);
                  tnode.on("mouseover", gwac.clickStar);
                }
                for (var i = 0; i < gwac.reqData.otLv2Cur.length; i++) {
                  var tstar = gwac.reqData.otLv2Cur[i];
                  var mName = tstar.dpmId < 10 ? "M0" + tstar.dpmId : "M" + tstar.dpmId;
                  var tnode = svg.append("path").datum({type: "Point", coordinates: [tstar.ra, tstar.dec]}).attr("class", gwac.ot2curData.class).attr("d", path.pointRadius(3));
                  tnode.append("title").text(tstar.name + "," + mName + "(" + tstar.ra + "," + tstar.dec + ")");
                  tnode.attr("value", tstar.name);
                  tnode.on("click", gwac.clickStar);
                  tnode.on("mouseover", gwac.clickStar);
                }
                svg.append("path").datum(gwac.origin.data).attr("class", gwac.origin.class).attr("d", path.pointRadius(1)).append("title").text("origin(0,0)");
              });

      svg.call(zoom).call(zoom.event);

//      gwac.zoomBounds(projection, zoom, path, gwac.getBounds());
      gwac.zoomBounds(projection, zoom, path, gwac.ot1Data2.data);
      svg.transition().ease("quad-in-out")
              .duration(2000)
              .call(zoom.projection(projection).event);
//      
//      var dt = 0;
//      var feature = svg.selectAll("path");
//      projection.rotate([rotate[0] + velocity[0] * dt, rotate[1] + velocity[1] * dt, rotate[2] + velocity[2] * dt]);
//      feature.attr("d", path);
    },
    getBounds: function() {
      var gwac = this;
      var bounds = {type: "Feature", geometry: {type: "Polygon", coordinates: [[[30, 30], [0, 10], [10, 0], [0, -10], [-10, 0]]]}};
//      for (var i = 0; i < gwac.reqData.otLv1.length; i++) {
//        var tstar = gwac.reqData.otLv1[i];
//      }
      return bounds;
    },
    zoomBounds: function(projection, zoom, path, o) {

      var gwac = this;
      var width = $(gwac.placeholder).width();
      var height = $(gwac.placeholder).height();
      var centroid = d3.geo.centroid(o),
              clip = projection.clipExtent();

      projection.rotate(true ? [-centroid[0], -centroid[1]] : zoom.rotateTo(centroid))
              .clipExtent(null)
              .scale(1)
              .translate([0, 0]);

      var b = path.bounds(o),
              k = Math.min(1000, .45 / Math.max(Math.max(Math.abs(b[1][0]), Math.abs(b[0][0])) / width, Math.max(Math.abs(b[1][1]), Math.abs(b[0][1])) / height));

      projection.clipExtent(clip)
              .scale(k)
              .translate([width / 2, height / 2]);
    },
    clickStar: function() {
      var ot2Name = $(this).attr("value");
      console.log(ot2Name);
      openDialog(ot2Name);
    }
  };

  $.gwac = function(placeholder, root, url) {
    var gwac = new Gwac(placeholder, root, url);
    return gwac;
  };

})(jQuery);


function openDialog(otName) {
  var gwacRootURL = $("#gwacRootURL").val();
  var queryUrl = gwacRootURL + "/gwac/pgwac-ot-detail2.action?otName=" + otName;
  window.open(queryUrl, '_blank');
  return false;
}