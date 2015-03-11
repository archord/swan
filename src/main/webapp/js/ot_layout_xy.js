

Date.prototype.Format = function(fmt) { //author: meizz 
  var o = {
    "M+": this.getMonth() + 1, //月份 
    "d+": this.getDate(), //日 
    "h+": this.getHours(), //小时 
    "m+": this.getMinutes(), //分 
    "s+": this.getSeconds(), //秒 
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
    "S": this.getMilliseconds() //毫秒 
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}

var reqNum = 1;
var plot = [];
var diskUsage = [];
var ot1 = [];
var ot2 = [];
var ot2cur = [];
var ot2mch = [];
var ot1Label = [];
var ot2Label = [];
var ot2curLabel = [];
var ot2mchLabel = [];
var drawData = [];
var ot2curInterval = null;
var uhtime = 50;
var intervalTime = 1500;
//var requestTime = 15000;
for (var m = 0; m < 12; m++) {
  ot1[m] = [];
  ot2[m] = [];
  ot2cur[m] = [];
  ot2mch[m] = [];
  ot1Label[m] = [];
  ot2Label[m] = [];
  ot2curLabel[m] = [];
  ot2mchLabel[m] = [];
}

var option1 = {
  legend: {show: false},
  // Drawing is faster without shadows
  series: {shadowSize: 0},
  points: {show: true},
  grid: {hoverable: true, clickable: true},
  selection: {mode: "xy"},
  xaxis: {show: true, min: 0, max: 3200, tickSize: 1000, tickFormatter: formate1},
  yaxis: {show: true, min: 0, max: 3200, tickSize: 1000, tickFormatter: formate1}
};
var option2 = {
  legend: {show: true, container: "#ot-legend", noColumns: 4},
  // Drawing is faster without shadows
  series: {shadowSize: 0},
  points: {show: true},
  grid: {hoverable: true, clickable: true},
  selection: {mode: "xy"},
  xaxis: {show: true, min: 0, max: 3200, tickSize: 1000, tickFormatter: formate1},
  yaxis: {show: true, min: 0, max: 3200, tickSize: 1000, tickFormatter: formate1}
};

function formate1(val, axis) {
  return (val).toFixed(axis.tickDecimals);
}

function formate2(val, axis) {
  return (val * 100).toFixed(0) + "%";
}

function onDataReceived(result) {

  if (ot2curInterval !== null) {
    unHighlightCurOT2();
    clearInterval(ot2curInterval);
  }

  var otLv1 = result.otLv1;
  var otLv2 = result.otLv2;
  var otLv2Cur = result.otLv2Cur;
  var otLv2Mch = result.otLv2Mch;
  for (var m = 0; m < 12; m++) {
    while (ot1[m].length > 0) {
      ot1[m].pop();
    }
    while (ot1Label[m].length > 0) {
      ot1Label[m].pop();
    }
    while (ot2[m].length > 0) {
      ot2[m].pop();
    }
    while (ot2Label[m].length > 0) {
      ot2Label[m].pop();
    }
    while (ot2cur[m].length > 0) {
      ot2cur[m].pop();
    }
    while (ot2curLabel[m].length > 0) {
      ot2curLabel[m].pop();
    }
    while (ot2cur[m].length > 0) {
      ot2mch[m].pop();
    }
    while (ot2curLabel[m].length > 0) {
      ot2mchLabel[m].pop();
    }
  }
  for (var i = 0; i < otLv1.length; i++) {
    ot1[otLv1[i].dpmId - 1].push([otLv1[i].XTemp, otLv1[i].YTemp]);
    ot1Label[otLv1[i].dpmId - 1].push([otLv1[i].dpmId, otLv1[i].ffNumber]);
  }
  for (var i = 0; i < otLv2.length; i++) {
    ot2[otLv2[i].dpmId - 1].push([otLv2[i].xtemp, otLv2[i].ytemp]);
//            ot2Label[otLv2[i].dpmId - 1].push([otLv2[i].identify, otLv2[i].name]);
    ot2Label[otLv2[i].dpmId - 1].push([otLv2[i].dpmId, otLv2[i].lastFfNumber, otLv2[i].name]);
  }
  for (var i = 0; i < otLv2Cur.length; i++) {
    ot2cur[otLv2Cur[i].dpmId - 1].push([otLv2Cur[i].xtemp, otLv2Cur[i].ytemp]);
//            ot2curLabel[otLv2Cur[i].dpmId - 1].push([otLv2Cur[i].identify, otLv2Cur[i].name]);
    ot2curLabel[otLv2Cur[i].dpmId - 1].push([otLv2Cur[i].dpmId, otLv2Cur[i].lastFfNumber, otLv2Cur[i].name]);
  }
  for (var i = 0; i < otLv2Mch.length; i++) {
    ot2mch[otLv2Mch[i].dpmId - 1].push([otLv2Mch[i].xtemp, otLv2Mch[i].ytemp]);
    ot2mchLabel[otLv2Mch[i].dpmId - 1].push([otLv2Mch[i].dpmId, otLv2Mch[i].lastFfNumber, otLv2Mch[i].name]);
  }

  for (var m = 0; m < 12; m++) {
    drawData[m] = [
      {
        label: "ot1",
        data: ot1[m],
        color: '#71c73e',
        points: {show: true, radius: 0.5}
      },
      {
        label: "ot2-mch",
        data: ot2mch[m],
        color: '#99CCCC',
        points: {show: true, radius: 2}
      },
      {
        label: "ot2",
        data: ot2[m],
        color: '#f59fb4', //#77b7c5
        points: {show: true, radius: 2}
      },
      {
        label: "ot2-cur",
        data: ot2cur[m],
        color: 'purple',
        points: {show: true, radius: 3}
      }
    ];
  }

  if (reqNum === 1) {
    for (var m = 0; m < 12; m++) {
      plotAndBind(m);
    }
    reqNum++;
  } else {
    for (var m = 0; m < 12; m++) {
      plot[m].setData(drawData[m]);//drawData[m]
      plot[m].draw();
    }
    reqNum++;
  }
  ot2curInterval = setInterval(highlightCurOT2, intervalTime);

  var dpms = result.dpms;
  diskUsage[0] = ["M", result.masterUsage];
  for (var m = 0; m < dpms.length; m++) {
    diskUsage[m + 1] = [m + 1, dpms[m].usedStorageSize / dpms[m].totalStorageSize];
//            console.log(dpms[m].usedStorageSize+" "+dpms[m].totalStorageSize+" "+diskUsage[m+1]);
  }

  $.plot("#sys-disk-usage", [diskUsage], {
    series: {
      color: "#77b7c5", //E8E800 77b7c5 AB5800
      bars: {
        show: true,
        barWidth: 0.3,
        align: "center",
        fillColor: {colors: [{opacity: 0.5}, {opacity: 1}]}
      }
    },
    xaxis: {
      mode: "categories"
    },
    yaxis: {show: true, min: 0, max: 0.99, tickFormatter: formate2}
  });
}


//$("<div id='tooltip'></div>").css({
//  position: "absolute",
//  display: "none",
//  border: "1px solid #fdd",
//  padding: "2px",
//  "background-color": "#fee",
//  opacity: 0.80
//}).appendTo("body");

function plotAndBind(number) {
  var id = "#placeholder" + (number + 1);
  if (number === 3) {
    plot[number] = $.plot(id, drawData[number], option2);
  } else {
    plot[number] = $.plot(id, drawData[number], option1);
  }
  $(id).bind("plothover", function(event, pos, item) {
    if (item) {
      var x = item.datapoint[0].toFixed(2);
      var y = item.datapoint[1].toFixed(2);
      if (item.series.label === "ot1") {
        var mName = "";
        if (ot1Label[number][item.dataIndex][0] < 10) {
          mName = "M0" + ot1Label[number][item.dataIndex][0];
        } else {
          mName = "M" + ot1Label[number][item.dataIndex][0];
        }
        $("#tooltip").html(mName + "-" + ot1Label[number][item.dataIndex][1] + " (" + x + ", " + y + ")")
                .css({top: item.pageY - 35, left: item.pageX + 5})
                .fadeIn(200);
      } else if (item.series.label === "ot2") {
        var mName = "";
        if (ot2Label[number][item.dataIndex][0] < 10) {
          mName = "M0" + ot2Label[number][item.dataIndex][0];
        } else {
          mName = "M" + ot2Label[number][item.dataIndex][0];
        }
        $("#tooltip").html(ot2Label[number][item.dataIndex][2] + "<br/>" + mName + "-" + ot2Label[number][item.dataIndex][1] + " (" + x + ", " + y + ")")
                .css({top: item.pageY - 35, left: item.pageX + 5})
                .fadeIn(200);
      } else if (item.series.label === "ot2-mch") {
        var mName = "";
        if (ot2mchLabel[number][item.dataIndex][0] < 10) {
          mName = "M0" + ot2mchLabel[number][item.dataIndex][0];
        } else {
          mName = "M" + ot2mchLabel[number][item.dataIndex][0];
        }
        $("#tooltip").html(ot2mchLabel[number][item.dataIndex][2] + "<br/>" + mName + "-" + ot2mchLabel[number][item.dataIndex][1] + " (" + x + ", " + y + ")")
                .css({top: item.pageY - 35, left: item.pageX + 5})
                .fadeIn(200);
      } else if (item.series.label === "ot2-cur") {
        var mName = "";
        if (ot2curLabel[number][item.dataIndex][0] < 10) {
          mName = "M0" + ot2curLabel[number][item.dataIndex][0];
        } else {
          mName = "M" + ot2curLabel[number][item.dataIndex][0];
        }
        $("#tooltip").html(ot2curLabel[number][item.dataIndex][2] + "<br/>" + mName + "-" + ot2curLabel[number][item.dataIndex][1] + " (" + x + ", " + y + ")")
                .css({top: item.pageY - 35, left: item.pageX + 5})
                .fadeIn(200);
      }
    } else {
      $("#tooltip").hide();
    }
  });
  $(id).bind("plotclick", function(event, pos, item) {
    if (item) {
      if (item.series.label === "ot2") {
        openDialog(ot2Label[number][item.dataIndex][2]);
      } else if (item.series.label === "ot2-cur") {
        openDialog(ot2curLabel[number][item.dataIndex][2]);
      }else if (item.series.label === "ot2-mch") {
        openDialog(ot2mchLabel[number][item.dataIndex][2]);
      }
//              plot[number].highlight(item.series, item.datapoint);
    }
  });
}

function highlightCurOT2() {
  for (var m = 0; m < 12; m++) {
    for (var ii = 0; ii < ot2cur[m].length; ii++) {
      plot[m].highlight(2, ii);
    }
  }
  setTimeout(unHighlightCurOT2, uhtime);
}

function unHighlightCurOT2() {
  for (var m = 0; m < 12; m++) {
    for (var ii = 0; ii < ot2cur[m].length; ii++) {
      plot[m].unhighlight(2, ii);
    }
  }
}

function openDialog(otName) {
  var curDate = new Date().Format("yyyy-MM-dd");
  var dateStr = $("#showDate").val();
  var queryHis = 'true';
  if (dateStr === curDate) {
    queryHis = 'false';
  }
  var dataurl = openUrl+"?otName=" + otName + "&queryHis=" + queryHis;
  openwindow(dataurl, '_blank', 850, 500, 850, 500);
  return false;
}
function openwindow(url, name, width, height, iWidth, iHeight)
{
  var iTop = (window.screen.availHeight - 30 - iHeight) / 2;       //获得窗口的垂直位置;
  var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;           //获得窗口的水平位置;
  window.open(url, name,
          'height=' + height +
          ',innerHeight=' + iHeight +
          ',width=' + width +
          ',innerWidth=' + iWidth +
          ',top=' + iTop +
          ',left=' + iLeft +
          ',toolbar=no,menubar=no,scrollbars=auto,resizeable=yes,location=no,status=yes');
}

function flotclickFunction(event, pos, item) {
  if (item) {
    if (item.series.label === "ot2") {
      openDialog(ot2Label[number][item.dataIndex][1]);
    } else if (item.series.label === "ot2-cur") {
      openDialog(ot2curLabel[number][item.dataIndex][1]);
    }
//              plot[number].highlight(item.series, item.datapoint);
  }
}

function plothoverFunction(event, pos, item) {
  if (item) {
    var x = item.datapoint[0].toFixed(2);
    var y = item.datapoint[1].toFixed(2);
    if (item.series.label === "ot1") {
      var mName = "";
      if (ot1Label[number][item.dataIndex][0] < 10) {
        mName = "M0" + ot1Label[number][item.dataIndex][0];
      } else {
        mName = "M" + ot1Label[number][item.dataIndex][0];
      }
      $("#tooltip").html(mName + "-" + ot1Label[number][item.dataIndex][1] + " (" + x + ", " + y + ")")
              .css({top: item.pageY - 35, left: item.pageX + 5})
              .fadeIn(200);
    } else if (item.series.label === "ot2") {
      $("#tooltip").html(ot2Label[number][item.dataIndex][0] + " (" + x + ", " + y + ")")
              .css({top: item.pageY - 35, left: item.pageX + 5})
              .fadeIn(200);
    } else if (item.series.label === "ot2-cur") {
      $("#tooltip").html(ot2curLabel[number][item.dataIndex][0] + " (" + x + ", " + y + ")")
              .css({top: item.pageY - 35, left: item.pageX + 5})
              .fadeIn(200);
    }
  } else {
    $("#tooltip").hide();
  }
}