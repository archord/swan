
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var starTypes = gwacRootURL + "/get-star-type-json.action";
  var starList = gwacRootURL + "/get-star-list-json.action";
  var starRecords = gwacRootURL + "/get-star-records-json.action";

  var otCurve, otCurve2;
  var plotOption = {
    legend: {show: false},
    series: {shadowSize: 0},
    points: {show: true},
    lines: {show: false, fill: false},
    grid: {hoverable: true, color: '#646464', borderColor: 'transparent', borderWidth: 20, clickable: true},
    selection: {mode: "xy"},
    xaxis: {show: true, tickColor: 'transparent'},
    yaxis: {show: true, zoomRange: false, panRange: false, tickDecimals: 1, tickFormatter: formate1, transform: formate2, inverseTransform: formate2},
    zoom: {interactive: true},
    pan: {interactive: true},
    crosshair: {mode: "xy"}
  };

  initStarCurveShow();
//  loadStarType();
  loadStarList();
  $('#starList').change(showStarInfo);

  function showStarInfo() {
    showStarClassifyInfo();
    loadStarRecords();
  }

  function showStarClassifyInfo() {

  }

  function loadStarRecords() {
    var queryUrl = starRecords;
    var starid = $('#starList').val();
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'starType=5&starId=' + starid,
      async: false,
      dataType: 'json',
      success: function(data) {
        starCurveShow(data);
        starCurveShow2(data);
      }
    });
  }

  function loadStarList() {
    var queryUrl = starList;
    $('#starList').find('option').remove();

    var starTypeId = $('#starType').val();

    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'starType=5',
      async: false,
      dataType: 'json',
      success: function(data) {
        var objs = data.objs;
        $.each(objs, function(i, item) {
          $('#starList').append($('<option>', {
            value: item,
            text: item
          }));
        });
      }
    });
  }


  function loadStarType() {
    var queryUrl = starTypes;
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function(data) {
        var objs = data.objs;
        var tdiv = $("#star-classify");
        $.each(objs, function(i, item) {
          var radioBtn = $('<input type="radio" value="' + item.stId + '" name="starType" >' + item.stName + '</input>');
          tdiv.append(radioBtn);
        });

      }
    });
  }

  function initStarCurveShow() {

    var otCurveShow = [];
    otCurve = $.plot("#star-light-curve", otCurveShow, plotOption);
    otCurve2 = $.plot("#star-light-curve2", otCurveShow, plotOption);

    $("#star-light-curve").bind("plothover", function(event, pos, item) {
      if (item) {
        var x = item.datapoint[0].toFixed(4);
        var y = item.datapoint[1].toFixed(2);
        $("#tooltip").html(item.series.label + "(" + x + ", " + y + ")").css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });

    $("#star-light-curve").bind("plotclick", function(event, pos, item) {
      if (item) {
        openDialog(item.series.label);
      }
    });

    $("#star-light-curve2").bind("plothover", function(event, pos, item) {
      if (item) {
        var x = item.datapoint[0].toFixed(4);
        var y = item.datapoint[1].toFixed(2);
        $("#tooltip").html(item.series.label + "(" + x + ", " + y + ")").css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });

    $("#star-light-curve2").bind("plotclick", function(event, pos, item) {
      if (item) {
        openDialog(item.series.label);
      }
    });
  }

//星表匹配
  function starCurveShow(data) {
    var ot2s = eval(data.starRecords);
    if (typeof (ot2s) === "undefined")
    {
      ot2s = [];
    }
    var otCurveShow = [];

    $.each(ot2s, function(i, item) {
      var otName = item.otName;
      var mag = item.mag;
      var dateUt = item.dateUt;
      var coorShow = [];
      $.each(mag, function(j, item) {
        coorShow[j] = [dateUt[j] / 86400, mag[j]];
      });
      otCurveShow[i] = {
        label: otName,
        data: coorShow,
        points: {radius: 1}
      };
    });

    otCurve = $.plot("#star-light-curve", otCurveShow, plotOption);
//    otCurve.setData(otCurveShow);
//    otCurve.draw();

  }

//图像相减
  function starCurveShow2(data) {
    var ot2s = eval(data.starRecords2);
    if (typeof (ot2s) === "undefined")
    {
      ot2s = [];
    }
    var otCurveShow = [];

    $.each(ot2s, function(i, item) {
      var otName = item.otName;
      var mag = item.mag;
      var dateUt = item.dateUt;
      var coorShow = [];
      $.each(mag, function(j, item) {
        coorShow[j] = [dateUt[j] / 86400, mag[j]];
      });
      otCurveShow[i] = {
        label: otName,
        data: coorShow,
        points: {radius: 1}
      };
    });

    otCurve2 = $.plot("#star-light-curve2", otCurveShow, plotOption);
//    otCurve2.setData(otCurveShow);
//    otCurve2.draw();

  }


  function openDialog(otName) {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gwac/pgwac-ot-detail2.action?otName=" + otName;
    window.open(queryUrl, '_blank');
    return false;
  }

  function setNavi($c, $i) {
    var title = $i.attr('alt');
    $('#title span').text(title);
    var current = $c.triggerHandler('currentPosition');
    $('#pagenumber span').text(current + 1);
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

  function openwindow(url, name, width, height, iWidth, iHeight)
  {
    var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
    window.open(url, name,
            'height=' + height +
            ',innerHeight=' + iHeight +
            ',width=' + width +
            ',innerWidth=' + iWidth +
            ',top=' + iTop +
            ',left=' + iLeft +
            ',toolbar=no,menubar=no,scrollbars=auto,resizeable=yes,location=no,status=yes');
  }

  function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

    for (i = 0; i < sURLVariables.length; i++) {
      sParameterName = sURLVariables[i].split('=');

      if (sParameterName[0] === sParam) {
        return sParameterName[1] === undefined ? true : sParameterName[1];
      }
    }
  }

  function degreeToDMS(degree) {
    var second = degree * 3600;
    var d = Math.floor(degree);
    var m = Math.floor((second % 3600) / 60);
    var s = (second % 60).toFixed(3);
    var result = "";
    if (d < 10) {
      result = result + "0";
    }
    result = result + d + ":";
    if (m < 10) {
      result = result + "0";
    }
    result = result + m + ":";
    if (s < 10) {
      result = result + "0";
    }
    result = result + s;
    return result;
  }

  function degreeToHMS(degree) {
    var second = degree * 3600 * 24 / 360;
    var h = Math.floor(second / 3600);
    var m = Math.floor((second % 3600) / 60);
    var s = (second % 60).toFixed(3);
    var result = "";
    if (h < 10) {
      result = result + "0";
    }
    result = result + h + ":";
    if (m < 10) {
      result = result + "0";
    }
    result = result + m + ":";
    if (s < 10) {
      result = result + "0";
    }
    result = result + s;
    return result;
  }

});