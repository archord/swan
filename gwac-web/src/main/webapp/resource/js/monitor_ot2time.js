
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var parmUrl = gwacRootURL + "/get-ot2-streamnode-list-json.action";

  var ccdList = [];

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
  loadStarRecords();
  starCurveShow();
  $('#starList').change(starCurveShow);
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
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function(data) {
        formateRawData(data);
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
        $("#tooltip").html(item.series.label + "," + item.series.data[item.dataIndex][2] + ",(" + x + ", " + y + ")").css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });
  }

  function formateRawData(data) {

    var minDate = data.minDate;
    var minDateHour = Date.parse(minDate) / 3600000;
    ccdList = eval(data.parStr);

    $('#startDay').html(minDate);

    if (typeof (ccdList) === "undefined")
    {
      ccdList = [];
    }
    console.log(minDate);
    $.each(ccdList, function(i, item) {
      $.each(item.ot2_list, function(j, item2) {
        if (item2['ot11_gen'] !== 'null') {
          item2['ot11_gen_obj'] = Date.parse(item2['ot11_gen']);
        }
        if (item2['ot11_up'] !== 'null') {
          item2['ot11_up_obj'] = Date.parse(item2['ot11_up']);
        }
        if (item2['ot11_done'] !== 'null') {
          item2['ot11_done_obj'] = Date.parse(item2['ot11_done']);
        }
        if (item2['ot12_gen'] !== 'null') {
          item2['ot12_gen_obj'] = Date.parse(item2['ot12_gen']);
        }
        if (item2['ot12_up'] !== 'null') {
          item2['ot12_up_obj'] = Date.parse(item2['ot12_up']);
        }
        if (item2['ot12_done'] !== 'null') {
          item2['ot12_done_obj'] = Date.parse(item2['ot12_done']);
        }
        if (item2['ot2_gen'] !== 'null') {
          item2['ot2_gen_obj'] = Date.parse(item2['ot2_gen']);
        }
        if (item2['ffc1_gen'] !== 'null') {
          item2['ffc1_gen_obj'] = Date.parse(item2['ffc1_gen']);
        }
        if (item2['ffc1_req'] !== 'null') {
          item2['ffc1_req_obj'] = Date.parse(item2['ffc1_req']);
        }
        if (item2['ffc1_up'] !== 'null') {
          item2['ffc1_up_obj'] = Date.parse(item2['ffc1_up']);
        }
        if (item2['ffc2_gen'] !== 'null') {
          item2['ffc2_gen_obj'] = Date.parse(item2['ffc2_gen']);
        }
        if (item2['ffc2_req'] !== 'null') {
          item2['ffc2_req_obj'] = Date.parse(item2['ffc2_req']);
        }
        if (item2['ffc2_up'] !== 'null') {
          item2['ffc2_up_obj'] = Date.parse(item2['ffc2_up']);
        }
        if (item2['ffcr_gen'] !== 'null') {
          item2['ffcr_gen_obj'] = Date.parse(item2['ffcr_gen']);
        }
        if (item2['ffcr_req'] !== 'null') {
          item2['ffcr_req_obj'] = Date.parse(item2['ffcr_req']);
        }
        if (item2['ffcr_up'] !== 'null') {
          item2['ffcr_up_obj'] = Date.parse(item2['ffcr_up']);
        }
        if (item2['ot2_lb'] !== 'null') {
          item2['ot2_lb_obj'] = Date.parse(item2['ot2_lb']);
        }
        if (item2['ot2_lu'] !== null) {
          item2['ot2_lu_obj'] = Date.parse(item2['ot2_lu']);
        }
        if (item2['ot2_lur'] !== null) {
          item2['ot2_lur_obj'] = Date.parse(item2['ot2_lur']);
        }
      });
    });

    $.each(ccdList, function(i, item) {
      $.each(item.ot2_list, function(j, item2) {
        item2['tidx'] = item2['ot11_gen_obj'] / 3600000 - minDateHour;

        item2['T00'] = (item2['ot2_gen_obj'] - item2['ot11_gen_obj']) / 1000;
        item2['T01'] = (item2['ot11_up_obj'] - item2['ot11_gen_obj']) / 1000;
        item2['T02'] = (item2['ot11_done_obj'] - item2['ot11_up_obj']) / 1000;
        item2['T03'] = (item2['ot12_up_obj'] - item2['ot12_gen_obj']) / 1000;
        item2['T04'] = (item2['ot12_done_obj'] - item2['ot12_up_obj']) / 1000;

        item2['T10'] = (item2['ot2_lb_obj'] - item2['ot2_gen_obj']) / 1000;
        item2['T11'] = (item2['ffc1_req_obj'] - item2['ffc1_gen_obj']) / 1000;
        item2['T12'] = (item2['ffc1_up_obj'] - item2['ffc1_req_obj']) / 1000;
        item2['T13'] = (item2['ffc2_req_obj'] - item2['ffc2_gen_obj']) / 1000;
        item2['T14'] = (item2['ffc2_up_obj'] - item2['ffc2_req_obj']) / 1000;
        item2['T15'] = (item2['ffcr_req_obj'] - item2['ffcr_gen_obj']) / 1000;
        item2['T16'] = (item2['ffcr_up_obj'] - item2['ffcr_req_obj']) / 1000;

        item2['T20'] = (item2['ot2_lur_obj'] - item2['ot2_lb_obj']) / 1000;
        item2['T21'] = (item2['ot2_lur_obj'] - item2['ot2_lu_obj']) / 1000;
      });
    });
    console.log(ccdList);
  }

  function getShowDate(ot2list, parmType) {

    var ccdID = 'CCD' + padZero(parseInt(ot2list.ccd_name), 2);
//      var parmList = sortJson(item.ot2_list, 'ot11_gen', true);

    var coorShow = [];
    $.each(ot2list.ot2_list, function(j, item2) {
      if (parmType === "T20" || parmType === "T21") {
        if (NaN === item2[parmType]) {
          return;
        }
      }
      coorShow.push([item2["tidx"], item2[parmType], item2["ot2_name"]]);
    });
    return {
      label: ccdID,
      data: coorShow,
      points: {radius: 1}
    };
  }

//星表匹配
  function starCurveShow() {

    var tmount = $('#mountList').val();
    var tccds = $('#ccdList').val();
    var parmType = $('#starList').val();

    var showCCDs = [];
    if (tmount === null && tccds === null) {
      $.each(ccdList, function(i, item) {
        showCCDs.push(getShowDate(item, parmType));
      });
    } else if (tmount !== null) {
      $.each(ccdList, function(i, item) {
        if (tmount === item.ccd_name.substring(0, 2)) {
          showCCDs.push(getShowDate(item, parmType));
        }
      });
    } else if (tmount === null && tccds !== null) {
      $.each(ccdList, function(i, item) {
        if (tccds.indexOf(item.ccd_name) > -1) {
          showCCDs.push(getShowDate(item, parmType));
        }
      });
    }
    otCurve = $.plot("#star-light-curve", showCCDs, plotOption);
//    otCurve.setData(showCCDs);
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