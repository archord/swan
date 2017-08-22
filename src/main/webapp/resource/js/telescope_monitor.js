
(function($) {

  function TelescopeMonitor(placeholder, root, url) {
    this.placeholder = placeholder;
    this.rootUrl = root;
    this.url = root + "/" + url;
    this.pingsUrl = this.rootUrl + "/resource/images/pings.png";
    this.init(placeholder);
  }

  TelescopeMonitor.prototype = {
    reqNum: 1,
    maxNumber: 1,
    playSpeed: 400,
    playInterval: 1,
    telNum: 10,
    gCamNum: 40,
    mCamNum: 10,
    gCCDNum: 40,
    mCCDNum: 10,
    tels: [],
    gCams: [],
    mCams: [],
    gCCDs: [],
    mCCDs: [],
    init: function(placeholder) {
      this.margin = {top: 100, right: 100, bottom: 100, left: 100};
      this.winWidth = $(placeholder).width();
      this.winHeight = $(placeholder).height();
      this.width = this.winWidth - this.margin.left - this.margin.right;
      this.height = this.winHeight - this.margin.top - this.margin.bottom;
      this.grid1x = d3.scale.ordinal().rangeBands([0, this.width]);
      this.grid1y = d3.scale.ordinal().rangeBands([0, this.height]);
      this.grid2x = d3.scale.ordinal().rangeBands([0, this.width]);
      this.grid2y = d3.scale.ordinal().rangeBands([0, this.height]);
      this.colors = d3.scale.linear().domain([0, 1, 2]).range(["#00EE00", "#EEAD0E", "#CD2626"]); //#eee green yellow red "#00EE00", "#EEAD0E", "#CD2626"
      this.grid1W = this.width / 5;
      this.grid1H = this.height / 2;
      this.grid2W = this.width / 10;
      this.grid2H = this.height / 4;

      var rowNum = 2;
      var colNum = 5;
      console.log(this);
      for (r = 0; r < rowNum; r++) {
        for (c = 0; c < colNum; c++) {
          var idx = r * colNum + c + 1;
          this.tels.push({gx: c * this.grid1W, gy: r * this.grid1H, gidx: idx});
          this.mCams.push({gx: c * this.grid1W, gy: r * this.grid1H, idx: 0});
          this.mCCDs.push({gx: c * this.grid1W, gy: r * this.grid1H, idx: 0});
        }
      }
      for (r = 0; r < rowNum; r++) {
        for (c = 0; c < colNum; c++) {
          for (k = 0; k < 4; k++) {
            var idx = (r * colNum + c) * 5 + k + 1;
            var gx = k % 2 + c * 2;
            var gy = k / 2 + r * 2;
            this.gCams.push({gx: gx * this.grid2W, gy: gy * this.grid2H, idx: idx});
            this.gCCDs.push({gx: gx * this.grid2W, gy: gy * this.grid2H, idx: idx});
          }
        }
      }
      this.svg = d3.select(placeholder).append("svg")
              .attr("width", this.winWidth)
              .attr("height", this.winHeight)
              .style("margin-left", "-30px")
              .style("margin-top", "-50px")
              .append("g")
              .attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");

      var background = this.svg.append("rect")
              .attr("class", "background")
              .attr("width", this.width)
              .attr("height", this.height);
    },
    drawTele: function() {
      var tgrid = this.svg.append("g").attr("class", "tele-grid");
      var teles = tgrid.selectAll(".tele").data(this.tels).enter().append("rect");
      teles.attr("class", "tele")
              .attr("x", function(d) {
                return d.gx;
              })
              .attr("y", function(d) {
                return d.gy;
              })
              .attr("width", this.grid1W)
              .attr("height", this.grid1H)
              .attr("value", function(d) {
                return d.idx;
              })
              .style("fill", function(d) {
                return "#FFFFFF";
              });
    }
  };

  $.tmonitor = function(placeholder, root, url) {
    var tmonitor = new TelescopeMonitor(placeholder, root, url);
    return tmonitor;
  };

})(jQuery);
