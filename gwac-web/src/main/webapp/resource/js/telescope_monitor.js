
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
      this.grid1W = this.width / 5;
      this.grid1H = this.height / 2;
      this.grid2W = this.width / 10;
      this.grid2H = this.height / 4;

      var w1 = this.grid1W > this.grid1H ? this.grid1H : this.grid1W;
      var w2 = this.grid2W > this.grid2H ? this.grid2H : this.grid2W;
      this.grid1W = w1;
      this.grid1H = w1;
      this.grid2W = w2;
      this.grid2H = w2;

      var rowNum = 2;
      var colNum = 5;
      for (r = 0; r < rowNum; r++) {
        for (c = 0; c < colNum; c++) {
          var idx = r * colNum + c + 1;
          this.tels.push({gx: c * this.grid1W, gy: r * this.grid1H, idx: this.pad0(idx, 2)});
          this.mCams.push({gx: c * this.grid1W, gy: r * this.grid1H, idx: this.pad0(idx, 2) + '5'});
          this.mCCDs.push({gx: c * this.grid1W, gy: r * this.grid1H, idx: this.pad0(idx, 2) + '5'});
        }
      }
      for (r = 0; r < rowNum; r++) {
        for (c = 0; c < colNum; c++) {
          for (k = 0; k < 4; k++) {
            var idx = r * colNum + c + 1;
            var gx = k % 2 + c * 2;
            var gy = Math.floor(k / 2) + r * 2;
            this.gCams.push({gx: gx * this.grid2W, gy: gy * this.grid2H, idx: this.pad0(idx, 2) + (k + 1)});
            this.gCCDs.push({gx: gx * this.grid2W, gy: gy * this.grid2H, idx: this.pad0(idx, 2) + (k + 1)});
          }
        }
      }

      this.svg = d3.select(this.placeholder).append("svg")
              .attr("width", this.winWidth)
              .attr("height", this.winHeight)
              .append("g")
              .attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");
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
    selectMount: function() {
      var allmount = new Array();
      $("#mountsSelect option").each(function() {
        var txt = $(this).val();
        if (txt !== '') {
          allmount.push(txt);
        }
      });
      $.each(allmount, function(i, item) {
        d3.select("#mount" + item).style('stroke-width', '1');
      });
      var tmount = $("#mountsSelect").val();
      if (tmount !== null) {
        $.each(tmount, function(i, item) {
          d3.select("#mount" + item).style('stroke-width', '3');
        });
      }
    },
    selectCcd: function() {
      var allccd = new Array();
      $("#ccdsSelect option").each(function() {
        var txt = $(this).val();
        if (txt !== '') {
          allccd.push(txt);
        }
      });
      $.each(allccd, function(i, item) {
        d3.select("#ccd" + item).style('stroke-width', '1');
      });
      var tccd = $("#ccdsSelect").val();
      if (tccd !== null) {
        $.each(tccd, function(i, item) {
          d3.select("#ccd" + item).style('stroke-width', '3');
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
      this.drawTelescope();
      this.drawMCamera();
      this.drawGCamera();
      this.drawMCCD();
      this.drawGCCD();
      this.addEvents();
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
    drawTelescope: function() {
      var tmonitor = this;
      var tgrid = tmonitor.svg.append("g").attr("class", "tele-grid");
      var teles = tgrid.selectAll(".tele").data(tmonitor.tels).enter().append("rect");
      teles.attr("class", "tele gwac")
              .attr("x", function(d) {
                return d.gx;
              })
              .attr("y", function(d) {
                return d.gy;
              })
              .attr("width", tmonitor.grid1W)
              .attr("height", tmonitor.grid1H)
              .attr("value", function(d) {
                return "mount" + d.idx;
              })
              .attr("id", function(d) {
                return "mount" + d.idx;
              });
    },
    drawMCCD: function() {
      var tmonitor = this;
      var mcamRadius = tmonitor.grid1W > tmonitor.grid1H ? Math.floor(tmonitor.grid1H / 8) : Math.floor(tmonitor.grid1W / 8);
      var mccdWidth = (mcamRadius * 2 / 1.414) * 0.8; //内切正方形，缩放系数0.8
      var tgrid = tmonitor.svg.append("g").attr("class", "mccd-grid");
      var teleg = tgrid.selectAll(".mccd").data(tmonitor.mCCDs).enter().append("g");
      var teles = teleg.append("rect");
      teles.attr("class", "mccd gwac")
              .attr("x", function(d) {
                return d.gx + tmonitor.grid1W / 2.0 - mccdWidth / 2;
              })
              .attr("y", function(d) {
                return d.gy + tmonitor.grid1H / 2.0 - mccdWidth / 2;
              })
              .attr("width", mccdWidth)
              .attr("height", mccdWidth)
              .attr("value", function(d) {
                return "ccd" + d.idx;
              })
              .attr("id", function(d) {
                return "ccd" + d.idx;
              });

      var teleLabel = teleg.append("text")
              .attr("class", "mlabel showlabel")
              .attr("x", function(d) {
                return d.gx + tmonitor.grid1W / 2.0;
              })
              .attr("y", function(d) {
                return d.gy + tmonitor.grid1H / 2.0;
              })
              .attr("style", "fill:#000;font-size:12px")
              .attr("text-anchor", "middle")
              .attr("alignment-baseline", "middle")
              .attr("value", function(d) {
                return d.idx;
              })
              .text(function(d) {
                return d.idx;
              });
    },
    drawMCamera: function() {
      var tmonitor = this;
      var tgrid = tmonitor.svg.append("g").attr("class", "mcam-grid");
      var teles = tgrid.selectAll(".mcam").data(tmonitor.mCams).enter().append("circle");
      var camRadius = tmonitor.grid1W > tmonitor.grid1H ? Math.floor(tmonitor.grid1H / 8) : Math.floor(tmonitor.grid1W / 8);
      teles.attr("class", "mcam gwac")
              .attr("cx", function(d) {
                return d.gx + tmonitor.grid1W / 2.0;
              })
              .attr("cy", function(d) {
                return d.gy + tmonitor.grid1H / 2.0;
              })
              .attr("r", function(d, i) {
                return camRadius;
              })
              .attr("value", function(d) {
                return "cam" + d.idx;
              })
              .attr("id", function(d) {
                return "cam" + d.idx;
              });
    },
    drawGCCD: function() {
      var tmonitor = this;
      var tgrid = tmonitor.svg.append("g").attr("class", "gccd-grid");
      var teleg = tgrid.selectAll(".gccd").data(tmonitor.gCCDs).enter().append("g");
      var teles = teleg.append("rect");
      var camRadius = tmonitor.grid2W > tmonitor.grid2H ? Math.floor(tmonitor.grid2H / 3) : Math.floor(tmonitor.grid2W / 3);
      var gccdWidth = (camRadius * 2 / 1.414) * 0.8; //内切正方形，缩放系数0.8
      teles.attr("class", "gccd gwac")
              .attr("x", function(d) {
                return d.gx + tmonitor.grid2W / 2.0 - gccdWidth / 2;
              })
              .attr("y", function(d) {
                return d.gy + tmonitor.grid2H / 2.0 - gccdWidth / 2;
              })
              .attr("width", gccdWidth)
              .attr("height", gccdWidth)
              .attr("value", function(d) {
                return "ccd" + d.idx;
              })
              .attr("id", function(d) {
                return "ccd" + d.idx;
              });


      var teleLabel = teleg.append("text")
              .attr("class", "glabel showlabel")
              .attr("x", function(d) {
                return d.gx + tmonitor.grid2W / 2.0;
              })
              .attr("y", function(d) {
                return d.gy + tmonitor.grid2H / 2.0;
              })
              .attr("style", "fill:#000;font-size:14px")
              .attr("text-anchor", "middle")
              .attr("alignment-baseline", "middle")
              .attr("value", function(d) {
                return d.idx;
              })
              .text(function(d) {
                return d.idx;
              });
    },
    drawGCamera: function() {
      var tmonitor = this;
      var tgrid = tmonitor.svg.append("g").attr("class", "gcam-grid");
      var teles = tgrid.selectAll(".gcam").data(tmonitor.gCams).enter().append("circle");
      var camRadius = tmonitor.grid2W > tmonitor.grid2H ? Math.floor(tmonitor.grid2H / 3) : Math.floor(tmonitor.grid2W / 3);
      teles.attr("class", "gcam gwac")
              .attr("cx", function(d) {
                return d.gx + tmonitor.grid2W / 2.0;
              })
              .attr("cy", function(d) {
                return d.gy + tmonitor.grid2H / 2.0;
              })
              .attr("r", function(d, i) {
                return camRadius;
              })
              .attr("value", function(d) {
                return "cam" + d.idx;
              })
              .attr("id", function(d) {
                return "cam" + d.idx;
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
