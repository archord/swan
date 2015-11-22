
$(function() {
  var option1 = {
    legend: {show: false},
    series: {shadowSize: 0},
    points: {show: true},
    lines: {show: true, fill: false},
    grid: {hoverable: true, color: '#646464', borderColor: 'transparent', borderWidth: 20, clickable: true},
    selection: {mode: "xy"},
    xaxis: {show: true, tickColor: 'transparent'},
    yaxis: {show: true, tickDecimals: 2, tickFormatter: formate1, transform: formate2, inverseTransform: formate2}
  };
  cutImgShow();
  otCurveShow();
  otPositionShow();
  loadOT2Record();
  loadOT2Match();
  $("#followBtn").click(function() {
    if (window.confirm('确定发送后随信息？')) {
      var formData = $("#otFollowUp").serialize();
      console.log(formData);
      $.post("otFollowUp.action", formData,
              function(data) {
                console.log(data);
                alert(data.result);
              }, "json");
    }
  });

  $(window).resize(function() {
    cutImgShow();
    otCurveShow();
    otPositionShow();
  });

  function loadOT2Match() {
    var otName = $("#otName").val();
    var queryHis = $("#queryHis").val();
    var queryUrl = "/gwac/get-ot-match-list.action?otName=" + otName + "&queryHis=" + queryHis;
    $('#ot2-match-table').DataTable({
      "deferRender": true,
      "processing": true,
      "searching": true,
      "lengthChange": true,
      "pageLength": 5,
      "scrollX": true,
      "ajax": {
        url: queryUrl,
        dataSrc: 'gridModel'
      },
      "columns": [
        {"data": "oorId"},
        {"data": "matchTableName"},
        {"data": "matchId"},
        {"data": "ot2Name"},
        {"data": "ra"},
        {"data": "dec"},
        {"data": "distance"},
        {"data": "mag"},
        {"data": "d25"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "dont know",
          "render": formateRowNumber
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="5">5</option>' +
                '<option value="10">10</option>' +
                '<option value="50">50</option>' +
                '<option value="100">100</option>' +
                '<option value="-1">All</option>' +
                '</select> 条',
        "info": "显示第 _START_ 到 _END_ ，共 _TOTAL_ 条",
        "search": "模糊查询:",
        "paginate": {
          "first": "首页",
          "last": "尾页",
          "next": "下一页",
          "previous": "上一页"
        }
      }
      ,
      dom: '<"ot2-load-button"B><"ot2-record-table-top"lf>tr<"ot2-record-table-bottom"ip>',
      buttons: [
        {
          text: 'OT匹配记录列表',
          action: ot2LoadButtonClick
        }
      ]
    });
  }

  function loadOT2Record() {
    var otName = $("#otName").val();
    var queryHis = $("#queryHis").val();
    var queryUrl = "/gwac/ot-observe-record.action?otName=" + otName + "&queryHis=" + queryHis;
//    var queryUrl = "/gwac/gwac/data/jquery-datatable-test.txt";
    $('#ot2-record-table').DataTable({
      "deferRender": true,
      "processing": true,
      "searching": true,
      "lengthChange": true,
      "pageLength": 5,
      "scrollX": true,
      "ajax": {
        url: queryUrl,
        dataSrc: 'gridModel'
      },
      "columns": [
        {"data": "oorId"},
        {"data": "ffName"},
        {"data": "dateUt"},
        {"data": "raD"},
        {"data": "decD"},
        {"data": "XTemp"},
        {"data": "YTemp"},
        {"data": "x"},
        {"data": "y"},
        {"data": "flux"},
        {"data": "background"},
        {"data": "threshold"},
        {"data": "magAper"},
        {"data": "magerrAper"},
        {"data": "ellipticity"},
        {"data": "classStar"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "dont know",
          "render": formateRowNumber
        }, {
          "targets": [3, 4],
          "data": "dont know",
          "render": formateRaDec
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="5">5</option>' +
                '<option value="10">10</option>' +
                '<option value="50">50</option>' +
                '<option value="100">100</option>' +
                '<option value="-1">All</option>' +
                '</select> 条',
        "info": "显示第 _START_ 到 _END_ ，共 _TOTAL_ 条",
        "search": "模糊查询:",
        "paginate": {
          "first": "首页",
          "last": "尾页",
          "next": "下一页",
          "previous": "上一页"
        }
      }
      ,
      dom: '<"ot2-load-button"B><"ot2-record-table-top"lf>tr<"ot2-record-table-bottom"ip>',
      buttons: [
        {
          text: 'OT详细记录列表',
          action: ot2LoadButtonClick
        }
      ]
    });
  }

  function ot2LoadButtonClick() {
//    $('#ot2-record .dataTables_scroll, .ot2-record-table-bottom, .ot2-record-table-top, .ot2-record-table-bottom').css("display", "block")
  }

  function formateRaDec(data, type, full, meta) {
    var ra = $('#ra').val();
    var dec = $('#dec').val();
    var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=2&Radius.unit=arcmin&submit=submit%20query&Coord=";
    searchUrl += ra + "%20" + dec;
    return "<a href='" + searchUrl + "' title='点击在simbad搜寻OT对应坐标' target='_blank'>" + data + "</a>";
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

  function otCurveShow() {
    var otCurveData = eval($("#otCurveData").val());
    if (typeof (otCurveData) === "undefined")
    {
      return;
    }
    var otCurveShow = [{
        data: otCurveData,
        color: '#71c73e',
        points: {radius: 2} //fillColor: '#77b7c5'
      }
    ];
    option1.lines.show = true;
    option1.yaxis.transform = formate2;
    option1.yaxis.inverseTransform = formate2;
    $.plot("#ot-curve", otCurveShow, option1);

    $("#ot-curve").bind("plothover", function(event, pos, item) {
      if (item) {
        var x = item.datapoint[0].toFixed(4);
        var y = item.datapoint[1].toFixed(2);
        $("#tooltip").html(x + ", " + y).css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });
  }

  function otPositionShow() {
    var otPositionVaration = eval($("#otPositionData").val());
    if (typeof (otPositionVaration) === "undefined")
    {
      return;
    }
    var firstPostion = [];
    var lastPostion = [];
    firstPostion[0] = otPositionVaration[0];
    lastPostion[0] = otPositionVaration[otPositionVaration.length - 1];
    var positionData = [{
        data: otPositionVaration,
        color: '#0099CC',
        points: {radius: 1} //fillColor: '#77b7c5'
      }, {
        data: firstPostion,
        color: '#FF6666',
        points: {radius: 3, fill: true, fillColor: "#FF6666"} //fillColor: '#77b7c5'
      }, {
        data: lastPostion,
        color: '#FF6666',
        points: {radius: 3} //fillColor: '#77b7c5'
      }
    ];
    option1.lines.show = true;
    option1.yaxis.transform = formate3;
    option1.yaxis.inverseTransform = formate3;
    $.plot("#ot-position-curve", positionData, option1);
  }

  function setNavi($c, $i) {
    var title = $i.attr('alt');
    $('#title span').text(title);
    var current = $c.triggerHandler('currentPosition');
    $('#pagenumber span').text(current + 1);
  }

  function cutImgShow() {
    $('#carousel').carouFredSel({
      prev: '#prev',
      next: '#next',
      auto: {
        button: '#play',
        pauseOnEvent: 'resume',
        timeoutDuration: 1000,
        progress: {bar: '#timer', interval: 0}
      },
      scroll: {
        items: 1,
        duration: 0,
        fx: 'fade',
        onBefore: function(data) {
          setNavi($(this), data.items.visible);
        }
      }
    });
    $('#carousel-wrapper').hover(function() {
      $('#navi').stop().animate({
        bottom: 0
      });
    }, function() {
      $('#navi').stop().animate({
        bottom: -30
      });
    });
    $('#start').on('click', function() {
      var startImgNum = $('#startImgNum').val();
      $('#carousel').trigger('slideTo', 0);
    });
    $('#end').on('click', function() {
      var totalImg = $('#totalImg').val();
      $('#carousel').trigger('slideTo', totalImg - 1);
    });
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

  function formatLink(cellvalue, options, rowObject) {
    var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=2&Radius.unit=arcmin&submit=submit%20query&Coord=";
    searchUrl += "";
    return "<a href='" + searchUrl + "' title='点击在simbad搜寻OT对应坐标' target='_blank'>" + cellvalue + "</a>";
  }


  function openDialog() {
    openwindow("show-fits-list.action?otName=&queryHis=",
            '_blank', 1050, 600, 1050, 600);
    return false;
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
});
