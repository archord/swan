
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var parmUrl = gwacRootURL + "/get-parm-list-json.action";

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

  var parmProperty = [
    {name: 'fwhm', min: 1, max: 3, note: '', unit: ''},
    {name: 'obj_num', min: 1000, max: 200000, note: '', unit: ''},
    {name: 'bg_bright', min: 1000, max: 10000, note: '', unit: ''},
    {name: 'avg_limit', min: 8, max: 17, note: '', unit: ''},
    {name: 's2n', min: -3, max: 3, note: '', unit: ''},
    {name: 'posshift', min: -10, max: 10, note: '', unit: ''},
    {name: 'posrms', min: -10, max: 10, note: '', unit: ''},
    {name: 'proc_time', min: 0, max: 20, note: '', unit: ''},
    {name: 'temperature_actual', min: 0, max: 10, note: '', unit: ''}
  ];

  initStarCurveShow();
  $('#starList').change(loadStarRecords);

  function loadStarRecords() {
    var queryUrl = parmUrl;
    var parmType = $('#starList').val();
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'queryParm=' + parmType,
      async: false,
      dataType: 'json',
      success: function(data) {
        starCurveShow(data, parmType);
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

//星表匹配
  function starCurveShow(data, parmType) {
    var ccdList = eval(data.parStr);
    var minDate = data.minDate;
    var minDateMinute = Date.parse(minDate) / 60000;
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

    $.each(ccdList, function(i, item) {
      $.each(item.par_detail, function(j, item2) {
        if (j > 0 && j < item.par_detail.length - 1) {
          var before = Math.abs(item.par_detail[j - 1][parmType]);
          var after = Math.abs(item.par_detail[j + 1][parmType]);
          var cur = Math.abs(item2[parmType]);
          if (cur > 10*(before + after)) {
            console.log(before + "," + cur + "," + after);
            item2[parmType] = (before + after) / 2;
          }
        }
      });
    });

    var otCurveShow = [];
    $.each(ccdList, function(i, item) {
      var ccdID = 'CCD' + padZero(parseInt(item.dpm_id), 2);
      var parmList = sortJson(item.par_detail, 'time_obs_ut', true);

      var coorShow = [];
      $.each(parmList, function(j, item) {
        var minute = item['dateObj'] - minDateMinute;
        coorShow[j] = [minute, item[parmType]];
      });
      otCurveShow[i] = {
        label: ccdID,
        data: coorShow,
        points: {radius: 1}
      };
    });

    otCurve = $.plot("#star-light-curve", otCurveShow, plotOption);
    otCurve.setData(otCurveShow);
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