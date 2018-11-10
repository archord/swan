
$(function () {
  var gwacRootURL = $("#gwacRootURL").val();
  var parmUrl = gwacRootURL + "/get-parm-list-json.action";

  var parmType = "";
  var formatedCCDList = [];

  var otCurve;
  var plotOption = {
    legend: {show: true},
    series: {shadowSize: 0},
    points: {show: true},
    lines: {show: false, fill: false},
    grid: {hoverable: true, color: '#646464', borderColor: 'transparent', borderWidth: 20, clickable: true},
    selection: {mode: "xy"},
    xaxis: {show: true, tickColor: 'transparent'},
//    yaxis: {show: true, zoomRange: false, panRange: false, tickDecimals: 1, tickFormatter: formate1, transform: formate2, inverseTransform: formate2},
    yaxis: {show: true},
    zoom: {interactive: true},
    pan: {interactive: true},
    crosshair: {mode: "xy"},
    xaxes: [
      {position: 'bottom'}
    ],
    yaxes: [
      {position: 'left'},
      {position: 'right'}
    ]
  };

  var parmProperty = {
    fwhm: {name: 'fwhm', min: 0, max: 10, note: '', unit: ''},
    obj_num: {name: 'obj_num', min: 1000, max: 200000, note: '', unit: ''},
    ot1_num: {name: 'ot1_num', min: 0, max: 1000, note: '', unit: ''},
    bg_bright: {name: 'bg_bright', min: 1000, max: 20000, note: '', unit: ''},
    avg_limit: {name: 'avg_limit', min: 5, max: 20, note: '', unit: ''},
    s2n: {name: 's2n', min: -3, max: 3, note: '', unit: ''},
    xshift: {name: 'xshift', min: -20, max: 20, note: '', unit: ''},
    yshift: {name: 'yshift', min: -20, max: 20, note: '', unit: ''},
    xrms: {name: 'xrms', min: 0, max: 3, note: '', unit: ''},
    yrms: {name: 'yrms', min: 0, max: 3, note: '', unit: ''},
    proc_time: {name: 'proc_time', min: 0, max: 20, note: '', unit: ''},
    temperature_actual: {name: 'temperature_actual', min: -50, max: 10, note: '', unit: ''},
    temperature_set: {name: 'temperature_set', min: -50, max: 10, note: '', unit: ''}
  };

  var parmProperty = [
    {label: '赤经', name: 'ra', min: 0, max: 10, note: '', unit: ''},
    {label: '赤纬', name: 'dec', min: 0, max: 10, note: '', unit: ''}
  ];

  initStarCurveShow();
//  loadStarRecords();
  $('#unitId').change(loadFieldList);
  $('#fieldId').change(loadPointData);

  function initStarCurveShow() {

    var otCurveShow = [];
    otCurve = $.plot("#star-light-curve", otCurveShow, plotOption);

    $("#star-light-curve").bind("plothover", function (event, pos, item) {
      if (item) {
        var x = item.datapoint[0].toFixed(4);
        var y = item.datapoint[1].toFixed(2);
        $("#tooltip").html(item.series.label + "(" + x + ", " + y + ")").css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });
  }

  function curveShow(data) {

    if (typeof (data.parStr) === "undefined" || data.parStr === null)
    {
      alert("该天区没有数据，或者对应的FFOV没有工作,请选择其他天区！");
      return;
    }

    var parmList = eval(data.parStr);
    parmList = sortJson(parmList, 'time_obs_ut', true);
    var minDate = parmList[0]['time_obs_ut'];
    var minDateMinute = Date.parse(minDate) / 60000;

    $('#startDay').html(minDate);

    if (typeof (parmList) === "undefined" || parmList === null)
    {
      parmList = [];
    }
    $.each(parmList, function (i, item) {
      item['dateObj'] = Date.parse(item['time_obs_ut']) / 60000;
    });

    var formatedObjList = [];
    var coorShow1 = [];
    var coorShow2 = [];
    var coorShow3 = [];
    var coorShow4 = [];
    $.each(parmList, function (j, item2) {
      var minute = item2['dateObj'] - minDateMinute;
      var mRa = item2["mount_ra"];
      var mDec = item2["mount_dec"];
      var iRa = item2["img_center_ra"];
      var iDec = item2["img_center_dec"];
      coorShow1.push([minute, mRa]);
      coorShow3.push([minute, mDec]);
      if (Math.abs(iRa) > 0.000001) {
        coorShow2.push([minute, iRa]);
      }
      if (Math.abs(iDec) > 0.000001) {
        coorShow4.push([minute, iDec]);
      }
    });
    formatedObjList.push({
      label: "计划指向RA",
      data: coorShow1,
      points: {radius: 1},
      xaxis: 1,
      yaxis: 1
    });
    formatedObjList.push({
      label: "实际指向RA",
      data: coorShow2,
      points: {radius: 1},
      xaxis: 1,
      yaxis: 1
    });
    formatedObjList.push({
      label: "计划指向DEC",
      data: coorShow3,
      points: {radius: 1},
      xaxis: 1,
      yaxis: 2
    });
    formatedObjList.push({
      label: "实际指向DEC",
      data: coorShow4,
      points: {radius: 1},
      xaxis: 1,
      yaxis: 2
    });
    otCurve = $.plot("#star-light-curve", formatedObjList, plotOption);
    otCurve.setData(formatedObjList);
    otCurve.draw();
  }


  function padZero(num, size) {
    var s = num + "";
    while (s.length < size)
      s = "0" + s;
    return s;
  }

  function sortJson(list, prop, asc) {
    list.sort(function (a, b) {
      if (asc) {
        return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
      } else {
        return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
      }
    });
    return list;
  }

  function formate1(val, axis) {
    return (val).toFixed(axis.tickDecimals);
  }

  function formate2(val) {
    return -val;
  }

  function formate3(val) {
    return val;
  }

  function loadFieldList() {
    var queryUrl = gwacRootURL + "/query-mount-obs-fieldId.action";

    var obsDate = $('#obsDate').val();
    var unitId = $('#unitId').val();
    if (obsDate === "") {
      alert("请选择日期！");
      return;
    }
    if (unitId === "") {
      alert("请选择转台！");
      return;
    }
    $('#fieldId').find('option').remove();
    $.ajax({
      type: "get",
      url: queryUrl,
      data: "obsDate=" + obsDate + "&unitId=" + unitId,
      async: false,
      dataType: 'json',
      success: function (data) {
        var objs = data.fieldIds;
        if (objs.length > 0) {
          $.each(objs, function (i, item) {
            $('#fieldId').append($('<option>', {
              value: item,
              text: item
            }));
          });
        }
      }
    });
  }

  function loadPointData() {
    var queryUrl = gwacRootURL + "/get-point-list-json.action";

    var obsDate = $('#obsDate').val();
    var unitId = $('#unitId').val();
    var fieldId = $('#fieldId').val();
    if (obsDate === "") {
      alert("请选择日期！");
      return;
    }
    if (unitId === "") {
      alert("请选择转台！");
      return;
    }
    if (fieldId === "") {
      alert("请选择天区！");
      return;
    }
    $.ajax({
      type: "get",
      url: queryUrl,
      data: "obsDate=" + obsDate + "&unitId=" + unitId + "&fieldId=" + fieldId,
      async: false,
      dataType: 'json',
      success: function (data) {
        curveShow(data);
      }
    });
  }
});