
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
      this.timeAlert = 6000;//60秒
      this.parmNames = {
        camTemperature: "相机温度",
        camTemperatureHigh: "散热端温度",
        cameraStatus: "相机状态",
        captureMachineCpu: "图像采集机CPU利用率",
        captureMachineStatus: "图像采集机状态",
        captureMachineStorage: "图像采集机硬盘利用率",
        captureMachineTime: "图像采集机最后活动时间",
        identity: "标识符",
        imgCutFileName: "星表匹配切图文件名",
        imgCutFileNameSub: "图像相减切图文件名",
        imgCutFileRefName: "模板切图文件名",
        imgCutFileRefTime: "模板切图时间",
        imgCutFileTime: "星表匹配切图时间",
        imgCutFileTimeSub: "图像相减切图时间",
        imgCutRequestName: "切图请求文件名",
        imgCutRequestRefName: "模板切图请求文件名",
        imgCutRequestRefTime: "模板切图请求时间",
        imgCutRequestTime: "切图请求时间",
        imgFwhm: "半高全宽",
        imgParmId: "图像参数ID",
        imgParmName: "图像参数文件名",
        imgParmTime: "图像参数时间",
        logContent: "日志内容",
        logTime: "日志时间",
        lookBackOt2Name: "星表匹配回看TO2名",
        lookBackOt2NameSub: "图像相减回看TO2名",
        lookBackOt2Time: "星表匹配回看时间",
        lookBackOt2TimeSub: "图像相减回看时间",
        opSn: "观测计划编号",
        opTime: "观测计划时间",
        ot1ListName: "交叉证认OT1列表文件名",
        ot1ListNameSub: "图像相减OT1列表文件名",
        ot1ListTime: "交叉证认OT1时间",
        ot1ListTimeSub: "图像相减OT1时间",
        positionError: "位置误差",
        processMachineCpu: "数据处理机CPU利用率",
        processMachineStatus: "数据处理机状态",
        processMachineStorage: "数据处理机磁盘利用率",
        processMachineTime: "数据处理机最后活动时间",
        regImgName: "图像注册图像名",
        regImgTime: "图像注册时间",
        thumbnailName: "预览图名称",
        thumbnailTime: "预览图时间"};
      this.showParms = [
        {label: "观测计划上传", min: 0, max: 0, time: "opTime", title: function(d) {
            return d.opSn + "(" + d.opTiime + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "图像注册", min: 0, max: 0, time: "regImgTime", title: function(d) {
            return d.regImgName + "(" + d.regImgTime + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "图像参数上传", min: 0, max: 0, time: "imgParmTime", title: function(d) {
            return d.imgParmName + "(" + d.imgParmTime + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "交叉证认OT1列表上传", min: 0, max: 0, time: "ot1ListTime", title: function(d) {
            return d.ot1ListName + "(" + d.ot1ListTime + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "OT2切图请求", min: 0, max: 0, time: "imgCutRequestTime", title: function(d) {
            return d.imgCutRequestName + "(" + d.imgCutRequestTime + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "OT2模板切图请求", min: 0, max: 0, time: "imgCutRequestRefTime", title: function(d) {
            return d.imgCutRequestRefName + "(" + d.imgCutRequestRefTime + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "交叉证认切图上传", min: 0, max: 0, time: "imgCutFileTime", title: function(d) {
            return d.imgCutFileName + "(" + d.imgCutFileTime + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "交叉证认OT2回看", min: 0, max: 0, time: "lookBackOt2Time", title: function(d) {
            return d.lookBackOt2Name + "(" + d.lookBackOt2Time + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "图像相减OT1列表上传", min: 0, max: 0, time: "ot1ListTimeSub", title: function(d) {
            return d.ot1ListNameSub + "(" + d.ot1ListTimeSub + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "图像相减切图上传", min: 0, max: 0, time: "imgCutFileTimeSub", title: function(d) {
            return d.imgCutFileNameSub + "(" + d.imgCutFileTimeSub + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "图像相减OT2回看", min: 0, max: 0, time: "lookBackOt2TimeSub", title: function(d) {
            return d.lookBackOt2NameSub + "(" + d.lookBackOt2TimeSub + ")";
          }, fillColorIdx: this.fillColorIdx},
        {label: "预览图", min: 0, max: 0, time: "thumbnailTime", title: function(d) {
            return d.thumbnailName + "(" + d.thumbnailTime + ")";
          }, fillColorIdx: this.fillColorIdx}
      ];

      var w1 = this.grid1W > this.grid1H ? this.grid1H : this.grid1W;
      this.grid1W = w1;
      this.grid1H = w1;
      this.realW = w1 * this.colNum;
      this.realH = w1 * this.rowNum;

      var tyInterval = 0;
      for (var r = 0; r < this.rowNum; r++) {
        var unitId = parseInt(r / 5) + 1;
        var camNum = r % 5 + 1;
        var tlable = this.pad0(unitId, 2) + camNum;
        if (r % 5 === 0) {
          tyInterval = parseInt((r + 1) / 5 + 0.5) * this.space * 2;
        }
        var py = r * this.grid1H + tyInterval;
        var txInterval = 0;
        for (var c = 0; c < this.colNum; c++) {
          if (c % 5 === 0) {
            txInterval = parseInt((c + 1) / 5 + 0.5) * this.space * 2;
          }
          var id = "O" + tlable + "_" + (c + 1);
          var px = c * this.grid1W + txInterval;
          this.showObjs.push({px: px, py: py, gx: c, gy: r, id: id, width: this.grid1W, height: this.grid1H});
        }
      }
      var txInterval = 0;
      for (var c = 0; c < this.colNum; c++) {
        if (c % 5 === 0) {
          txInterval = parseInt((c + 1) / 5 + 0.5) * this.space * 2;
        }
        var px = c * this.grid1W + txInterval;
        this.xLabel.push({px: px, py: 0, label: c + 1, id: c, width: this.grid1W, height: this.grid1H});
      }
      tyInterval = 0;
      for (var r = 0; r < this.rowNum; r++) {
        if (r % 5 === 0) {
          tyInterval = parseInt((r + 1) / 5 + 0.5) * this.space * 2;
        }
        var py = r * this.grid1H + tyInterval;
        var unitId = parseInt(r / 5) + 1;
        var camNum = r % 5 + 1;
        var tlable = this.pad0(unitId, 2) + camNum;
        this.yLabel.push({px: 0, py: py, label: tlable, width: this.grid1W, height: this.grid1H});
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
    updateStatus: function() {
      var tmonitor = this;
      var colors = ["#FFF", "#ABABAB", "#00EE00", "#EEAD0E", "#CD2626"]; //白，灰，绿，橙，红
      var url = $("#gwacRootURL").val() + "gction/get-system-status.action?timestamp=" + new Date().getTime();

      $.ajax({
        type: "get",
        url: url,
        async: false,
        dataType: 'json',
        success: function(data) {
          var objs = data.datas;
          $.each(objs, function(i, item) {
            var cameraStatus = parseInt(item.cameraStatus);
            if (item.cameraStatus !== 3) {
              for (var c = 0; c < tmonitor.colNum; c++) {
                var id = "#O" + item.identity + "_" + (c + 1);
                d3.select(id).style("fill", colors[cameraStatus - 1]);
              }
            } else {
              for (var c = 0; c < tmonitor.colNum; c++) {
                if (c < tmonitor.showParms.length) {
                  var id = "#O" + item.identity + "_" + (c + 1);
                  var tobj = d3.select(id);
                  var colorIdx = tmonitor.showParms[c].fillColorIdx(item, tmonitor.showParms[c].time);
                  tobj.style("fill", colors[colorIdx]);
                  if (c < tmonitor.showParms.length) {
                    tobj.append("svg:title").text(tmonitor.showParms[c].title(item));
                  }
                }
              }
            }
          });
        }
      });

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

      var url = $("#gwacRootURL").val() + "gction/get-system-init-status.action?timestamp=" + new Date().getTime();
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
                var tlabel = " ";
//                if (i % 2 === 0) {
//                  tlabel = tmonitor.pad0(d.label, 2);
//                }
                tlabel = tmonitor.pad0(d.label, 2);
                return tlabel;
              }).append("svg:title")
              .text(function(d) {
                var title = "";
                if (d.id < tmonitor.showParms.length) {
                  title = tmonitor.showParms[d.id]["label"];
                }
                return title;
              });
      var ylabel = tgrid.selectAll(".axisY").data(tmonitor.yLabel).enter().append("text");
      ylabel.attr("class", "ylabel")
              .attr("x", -50)
              .attr("y", function(d, i) {
                return d.py + 20;
              })
              .text(function(d, i) {
                return 'G' + d.label;
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
    },
    fillColorIdx: function(d, timeName) {
      var idx = 4;
      var ttime = d[timeName];
      if (ttime !== null) {
        ttime = new Date(ttime.replace('T', ' '));
        var now = new Date();
        var timeDiff = now - ttime;
        if (timeDiff > 60 * 1000 && timeDiff < 600 * 1000) {
          idx = 3;
        } else if (timeDiff < 60 * 1000) {
          idx = 2;
        }
      }
      return idx;
    }
  };

  $.tmonitor = function(placeholder, root, url) {
    var tmonitor = new TelescopeMonitor(placeholder, root, url);
    return tmonitor;
  };

})(jQuery);
