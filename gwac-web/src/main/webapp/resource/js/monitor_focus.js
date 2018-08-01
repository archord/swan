
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var parmUrl = gwacRootURL + "/get-focus-list.action";

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
      {position: 'left'}
    ]
  };

  var parmProperty = [
    {label: '调焦值',name: 'focus', min: 0, max: 100, note: '', unit: ''}
  ];

  initStarCurveShow();
//  loadRecords();
  $('#dayList').change(loadRecords);
  $('#ccdList').change(loadRecords);

  function loadRecords() {
    var queryUrl = parmUrl;
    var days = $('#dayList').val();
    var ccd = $('#ccdList').val();
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'days=' + days + "&ccd=" + ccd,
      async: false,
      dataType: 'json',
      success: function(data) {
        curveShow(data);
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

  function curveShow(data) {
    var minDate = data.minDate;
    var minDateMinute = Date.parse(minDate) / (60000*60*24);
    var objList = eval(data.parStr);

    $('#startDay').html(minDate);

    if (typeof (objList) === "undefined" || objList===null)
    {
      objList = [];
    }
    $.each(objList, function(i, item) {
      item['dateObj'] = Date.parse(item['recv_time_utc']) / (60000*60*24);
    });

    var formatedObjList = [];
    var sortObj = sortJson(objList, 'dateObj', true);
    $.each(parmProperty, function(i, item) {
      var coorShow = [];
      $.each(sortObj, function(j, item2) {
        var minute = item2['dateObj'] - minDateMinute;
        coorShow.push([minute, item2[item.name]]);
      });
      formatedObjList.push({
        label: item.label,
        data: coorShow,
        points: {radius: 1},
        xaxis: 1,
        yaxis: i + 1
      });
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