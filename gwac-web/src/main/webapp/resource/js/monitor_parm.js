
$(function() {
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
    crosshair: {mode: "xy"}
  };

  var parmProperty = {
    fwhm: {name: 'fwhm', min: 0, max: 10, note: '', unit: ''},
    obj_num: {name: 'obj_num', min: 1000, max: 200000, note: '', unit: ''},
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

  initStarCurveShow();
//  loadStarRecords();
  $('#starList').change(loadStarRecords);
  $('#mountList').change(changeMount);
  $('#ccdList').change(changeCCDs);

  function changeMount() {
    $("#ccdList option:selected").prop("selected", false);
    starCurveShow();
  }

  function changeCCDs() {
    $("#mountList option:selected").prop("selected", false);
    starCurveShow();
  }

  function loadStarRecords() {
    var queryUrl = parmUrl;
    parmType = $('#starList').val();
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'queryParm=' + parmType,
      async: false,
      dataType: 'json',
      success: function(data) {
        formateRawData(data, parmType);
        starCurveShow();
      }
    });
  }

  function initStarCurveShow() {

    var otCurveShow = [];
    otCurve = $.plot("#star-light-curve", otCurveShow, plotOption);

    $("#star-light-curve").bind("plothover", function(event, pos, item) {
      if (item) {
        var x = item.datapoint[0].toFixed(4);
        var y = item.datapoint[1].toFixed(2);
        $("#tooltip").html(item.series.label + "(" + x + ", " + y + ")").css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });
  }

  function formateRawData(data, parmType) {

    var minDate = data.minDate;
    var minDateMinute = Date.parse(minDate) / 60000;
    var ccdList = eval(data.parStr);

    $('#startDay').html(minDate);

    if (typeof (ccdList) === "undefined")
    {
      ccdList = [];
    }
    $.each(ccdList, function(i, item) {
      $.each(item.par_detail, function(j, item2) {
        item2['dateObj'] = Date.parse(item2['time_obs_ut']) / 60000;
      });
    });

//    formatedCCDList = [];
    while (formatedCCDList.pop()) {
    }

    $.each(ccdList, function(i, item) {
      var ccdID = 'CCD' + padZero(parseInt(item.dpm_id), 2);
      var parmList = sortJson(item.par_detail, 'time_obs_ut', true);

      var coorShow = [];
      $.each(parmList, function(j, item2) {
        var minute = item2['dateObj'] - minDateMinute;
        coorShow.push([minute, item2[parmType]]);
      });
      formatedCCDList.push({
        ccdId: item.dpm_id,
        label: ccdID,
        data: coorShow,
        points: {radius: 1}
      });
    });
  }

//星表匹配
  function starCurveShow() {

    var tmount = $('#mountList').val();
    var tccds = $('#ccdList').val();

    var showCCDs = [];
    if (tmount === null && tccds === null) {
      showCCDs = formatedCCDList;
    } else if (tmount !== null) {
      $.each(formatedCCDList, function(i, item) {
        if (tmount === item.ccdId.substring(0, 2)) {
          showCCDs.push(item);
        }
      });
    } else if (tmount === null && tccds !== null) {
      $.each(formatedCCDList, function(i, item) {
        if (tccds.indexOf(item.ccdId) > -1) {
          showCCDs.push(item);
        }
      });
    }

    if ("bg_bright" !== parmType && "obj_num" !== parmType) {
      plotOption.yaxis = {
        min: parmProperty[parmType].min,
        max: parmProperty[parmType].max,
        show: true
      };
    }
    otCurve = $.plot("#star-light-curve", showCCDs, plotOption);
    otCurve.setData(showCCDs);
    otCurve.draw();
  }

  function padZero(num, size) {
    var s = num + "";
    while (s.length < size)
      s = "0" + s;
    return s;
  }

  function sortJson(list, prop, asc) {
    list.sort(function(a, b) {
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

});