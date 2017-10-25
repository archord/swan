
(function($) {

  function TelescopeMonitor(placeholder, root, url) {
    this.placeholder = placeholder;
    this.rootUrl = root;
    this.url = root + "/" + url;
    this.pingsUrl = this.rootUrl + "/resource/images/pings.png";
  }

  TelescopeMonitor.prototype = {
    reqNum: 1,
    maxNumber: 1,
    playSpeed: 400,
    playInterval: 1,
    colNum: 40,
    rowNum: 25,
    showObjs: [],
    showData: [],
    init: function() {
      this.margin = {top: 20, right: 50, bottom: 20, left: 50};
      this.winWidth = $(this.placeholder).width();
      this.winHeight = $(this.placeholder).height();
      this.width = this.winWidth - this.margin.left - this.margin.right;
      this.height = this.winHeight - this.margin.top - this.margin.bottom;
      this.grid1x = d3.scale.ordinal().rangeBands([0, this.width]);
      this.grid1y = d3.scale.ordinal().rangeBands([0, this.height]);
      this.grid2x = d3.scale.ordinal().rangeBands([0, this.width]);
      this.grid2y = d3.scale.ordinal().rangeBands([0, this.height]);
      this.colors = d3.scale.linear().domain([0, 1, 2]).range(["#00EE00", "#EEAD0E", "#CD2626"]); //#eee green yellow red "#00EE00", "#EEAD0E", "#CD2626"
      this.rowNum = 25;
      this.colNum = 40;
      this.grid1W = this.width / this.colNum;
      this.grid1H = this.height / this.rowNum;
      this.space = 1;
      this.xLabel = [];
      this.yLabel = [];

      var w1 = this.grid1W > this.grid1H ? this.grid1H : this.grid1W;
      this.grid1W = w1;
      this.grid1H = w1;
      this.realW = w1 * this.colNum;
      this.realH = w1 * this.rowNum;

      for (var r = 0; r < this.rowNum; r++) {
        for (var c = 0; c < this.colNum; c++) {
          var id = (r+1) + "-" + (c+1);
          this.showObjs.push({px: c * this.grid1W, py: r * this.grid1H, gx: c, gy: r, id: id, width: this.grid1W, height: this.grid1H});
        }
      }
      for (var c = 0; c < this.colNum; c++) {
        this.xLabel.push({px: c * this.grid1W, py: 0, value: c + 1, width: this.grid1W, height: this.grid1H});
      }
      for (var r = 0; r < this.rowNum; r++) {
        this.yLabel.push({px: 0, py: r * this.grid1H, value: r + 1, width: this.grid1W, height: this.grid1H});
      }

      this.svg = d3.select(this.placeholder).append("svg")
              .attr("width", this.winWidth)
              .attr("height", this.winHeight)
              .append("g")
              .attr("transform", "translate(" + (this.winWidth - this.realW) / 2 + "," + (this.winHeight - this.realH) / 2 + ")");
      /**
       var background = this.svg.append("rect")
       .attr("class", "background")
       .attr("width", this.grid1W * 5)
       .attr("height", this.grid1H * 2);
       */
    },
    submitStatus: function() {
      var colors = ["#FFF", "#ABABAB", "#00EE00", "#EEAD0E", "#CD2626"]; //白，灰，绿，橙，红
      var tccd = $("#ccdsSelect").val();
      var tmount = $("#mountsSelect").val();
      var status = $("#setStatus").val();
      var formData = $("#updateSystemInitStatusAction").serialize();
      var url = $("#updateSystemInitStatusAction").attr('action');
      console.log(tccd);
      console.log(tmount);
      console.log(status);
      console.log(formData);

      $.ajax({
        type: "get",
        url: url,
        data: formData,
        async: false,
        dataType: 'json',
        success: function(data) {
          console.log(data);
        }
      });

      if (tccd !== null) {
        $.each(tccd, function(i, item) {
          d3.select("#ccd" + item).style('fill', colors[status - 1]);
        });
      }
      if (tmount !== null) {
        $.each(tmount, function(i, item) {
          d3.select("#mount" + item).style('fill', colors[status - 1]);
        });
      }
    },
    initSelect: function() {
      var option = {
        maxHeight: 200,
        nonSelectedText: '请选择',
        includeSelectAllOption: true,
        allSelectedText: '已全选',
        selectAllText: '全选'
      };
      option.nonSelectedText = '转台选择';
      $('#mountsSelect').multiselect(option);
      option.nonSelectedText = 'CCD选择';
      $('#ccdsSelect').multiselect(option);
      $('#mountsSelect').change(this.selectMount);
      $('#ccdsSelect').change(this.selectCcd);
      $('#setStatusBtn').click(this.submitStatus);
    },
    drawAll: function() {
      this.init();
      this.drawShowObjs();
//      this.addEvents();
//      this.randomEffects();
    },
    initEffects: function() {

      var colors = ["#FFF", "#ABABAB", "#00EE00", "#EEAD0E", "#CD2626"]; //白，灰，绿，橙，红
      var tmonitor = this;
      var gwacObj = tmonitor.svg.selectAll(".gwac");
      gwacObj.style("fill", "#ABABAB"); //"#00EE00"

      var url = $("#gwacRootURL").val() + "get-system-init-status.action?timestamp=" + new Date().getTime();
      $.ajax({
        type: "get",
        url: url,
        data: "p1=1",
        async: false,
        dataType: 'json',
        success: function(data) {
          var tdata = eval(data);
          var ccdStatus = eval(tdata.ccdStatus);
          var mountStatus = eval(tdata.mountStatus);
          $.each(ccdStatus, function(i, item) {
            d3.select("#ccd" + item.name).style('fill', colors[item.status - 1]);
          });
          $.each(mountStatus, function(i, item) {
            d3.select("#mount" + item.name).style('fill', colors[item.status - 1]);
          });
        }
      });
    },
    randomEffects: function() {
      var tmonitor = this;
      var colors = ["#FFF", "#00EE00", "#EEAD0E", "#CD2626", "#ABABAB"];
      var gwacObj = tmonitor.svg.selectAll(".gwac");
      gwacObj.style("fill", function() {
        //console.log(d3.select(this).attr("value"));
        var tidx = 0;
        var tr = Math.random()
        if (tr > 0.95) {
          tidx = 4;
        } else if (tr > 0.9) {
          tidx = 3;
        } else if (tr > 0.8) {
          tidx = 2;
        } else if (tr > 0.1) {
          tidx = 1;
        }
        return colors[tidx];
      });
    },
    addEvents: function() {
      var tmonitor = this;
      var initStyle = "stroke:rgb(0,0,0);stroke-width:1;fill:#fff;";
      var gwacObj = tmonitor.svg.selectAll(".gwac");
      var showLabel = tmonitor.svg.selectAll(".showlabel");
      gwacObj.attr("style", initStyle).on('mouseover', tmonitor.mover1).on('mouseout', tmonitor.mout1);
      showLabel.on('mouseover', tmonitor.mover2);
      gwacObj.on('click', tmonitor.clickEvent);
    },
    drawShowObjs: function() {
      var tmonitor = this;
      var tgrid = tmonitor.svg.append("g").attr("class", "param-grid");
      var cells = tgrid.selectAll(".gcell").data(tmonitor.showObjs).enter().append("rect");
      cells.attr("class", "gcell")
              .attr("x", function(d) {
                return d.px;
              })
              .attr("y", function(d) {
                return d.py;
              })
              .attr("width", tmonitor.grid1W - tmonitor.space)
              .attr("height", tmonitor.grid1H - tmonitor.space)
              .attr("value", function(d) {
                return "G" + d.gy;
              })
              .attr("id", function(d) {
                return d.id;
              });
      var xlabel = tgrid.selectAll(".axisX").data(tmonitor.xLabel).enter().append("text");
      xlabel.attr("class", "xlabel")
              .attr("x", function(d, i) {
                return d.px;
              })
              .attr("y", function(d, i) {
                return -5;
              })
              .text(function(d, i) {
                var tlabel = "";
                if (i % 2 === 0) {
                  tlabel = tmonitor.pad0(d.value, 2);
                }
                return tlabel;
              });
      var ylabel = tgrid.selectAll(".axisY").data(tmonitor.yLabel).enter().append("text");
      ylabel.attr("class", "ylabel")
              .attr("x", -40)
              .attr("y", function(d, i) {
                return d.py + 20;
              })
              .text(function(d, i) {
                return 'G' + tmonitor.pad0(d.value, 2);
              });
    },
    mover1: function() {
      d3.select(this).style('stroke-width', '3');
    },
    mout1: function() {
      d3.select(this).style('stroke-width', '1');
    },
    mover2: function() {
      var ccdId = "#ccd" + d3.select(this).attr("value");
      d3.select(ccdId).style('stroke-width', '3');
//      setTimeout(function() {
//        d3.select(ccdId).style('stroke-width', '3');
//      }, 50);
    },
    mout2: function() {
      var ccdId = "#ccd" + d3.select(this).attr("value");
//      console.log("out" + ccdId);
      d3.select(ccdId).style('stroke-width', '1');
    },
    clickEvent: function() {
      var tval = d3.select(this).attr("value");
      console.log(tval);
    },
    pad0: function(num, size) {
      var s = "000" + num;
      return s.substr(s.length - size);
    }
  };

  $.tmonitor = function(placeholder, root, url) {
    var tmonitor = new TelescopeMonitor(placeholder, root, url);
    return tmonitor;
  };

})(jQuery);
