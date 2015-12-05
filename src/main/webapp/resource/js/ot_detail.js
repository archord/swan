
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var baseUrl = gwacRootURL + "/get-ot-detail.action?otName=";
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
  getOt2Detail();
  loadOT2Record();
  loadOT2Match();


//  $(window).resize(function() {
//    otCurveShow();
//    otPositionShow();
//  });

  function getOt2Detail() {
    var gwacRootURL = $("#gwacRootURL").val();
    var otName = getUrlParameter("otName");
    var url = gwacRootURL + "/get-ot-detail-json.action?otName=" + otName;
    $.get(url, ot2Show, "json");
  }

  function ot2Show(data) {
    initLoginInfo(data);
    initFollowUpInfo(data);
    cutImgShow(data);
    otSkyCoordinateShow(data);
    otCurveShow(data);
    otPositionShow(data);
  }

  function initLoginInfo(data) {
    var user = data.userInfo;
    if (user !== null) {
      $('#userName').val(user.loginName);
      $('#user-login-div').hide();
      $('#user-logout-div').show();
      $('#user-welcome-username').append(user.name);
    }

    bootbox.setLocale("zh_CN");
    $("#user-login").click(function() {
      bootbox.confirm({
        title: '用户登录',
        className: 'login-field-dialog',
        message: "<div id='login-field' style='width:300px;margin:auto'>\
                <div><label id='login-error-info' style='color:red;display:none;'>用户名或密码错误！</label></div>\
                <form id='loginForm' method='post'>\
                <div>用户名:\
                <input type='text' class='form-control required' name='loginName' id='loginName' minlength='2' maxlength='20'/></div>\
                <div>密&nbsp;&nbsp;&nbsp;&nbsp;码:\
                <input type='password' class='form-control required' name='loginPass' id='loginPass' minlength='6' maxlength='20'/></div></form></div>",
        callback: function(result) {
          if (result) {
            if ($("#loginForm").valid()) {
              var gwacRootURL = $("#gwacRootURL").val();
              var loginUrl = gwacRootURL + "/user-login.action";
              var formData = $("#loginForm").serialize();
              $.post(loginUrl, formData,
                      function(data) {
                        console.log(data);
                        if (data.flag === '1') {
                          var user = data.userInfo;
                          if (user !== null) {
                            $('#userName').val(user.loginName);
                            $('#user-login-div').hide();
                            $('#user-logout-div').show();
                            $('#user-welcome-username').html(user.name);
                          }
                          bootbox.hideAll();
                        } else {
                          $("#login-error-info").show();
                        }
                      }, "json");
            }
            return false;
          } else {
            return true;
          }
        }});
      $("#loginForm").validate();
    });

    $("#user-logout").click(function() {
      bootbox.confirm({
        className: 'login-field-dialog',
        message: "是否注销",
        callback: function(result) {
          if (result) {
            var gwacRootURL = $("#gwacRootURL").val();
            var userName = $("#userName").val();
            var logoutUrl = gwacRootURL + "/user-logout.action?userName=" + userName;
            $.get(logoutUrl,
                    function(data) {
                      console.log(data);
                      if (data.flag === '1') {
                        $('#userName').val('mini-GWAC');
                        $('#user-login-div').show();
                        $('#user-logout-div').hide();
                        bootbox.hideAll();
//                        alert('注销成功！');
                      } else {
                        alert('注销失败！');
                      }
                    }, "json");
          }
          return false;
        }});
      $("#loginForm").validate();
    });
  }

  function initFollowUpInfo(data) {
    var ot2 = data.ot2;
    $("#otName").val(ot2.name);
    $("#fuRa").val(ot2.ra);
    $("#fuDec").val(ot2.dec);

    bootbox.setLocale("zh_CN");
    $("#followBtn").click(function() {
      bootbox.confirm({
        size: 'small',
        message: "确定发送后随信息？",
        callback: function(result) {
          if (result) {
            var formData = $("#otFollowUp").serialize();
            console.log(formData);
            $.post("otFollowUp.action", formData,
                    function(data) {
                      console.log(data);
                      alert(data.result);
                    }, "json");
          }
        }
      });
    });
//    $("#followBtn").click(function() {
//      if (window.confirm('确定发送后随信息？')) {
//        var formData = $("#otFollowUp").serialize();
//        console.log(formData);
//        $.post("otFollowUp.action", formData,
//                function(data) {
//                  console.log(data);
//                  alert(data.result);
//                }, "json");
//      }
//    });
  }

  function cutImgShow(data) {
    var dataRootWebMap = data.dataRootWebMap;
    var ot2 = data.ot2;
    var ffcRef = data.ffcRef;
    var ffcList = data.ffcList;

    var cutImageShow;
    if (ot2.dataProduceMethod === '1') {
      $("#cut-image-show").css({width: "406px"});
      cutImageShow = "<div id=\"ref-image\">" +
              "  <div id=\"ref-image-show\">" +
              "  </div>" +
              "  <div id=\"navi2\">" +
              "    <p id=\"title2\"></p>" +
              "  </div>" +
              "</div>" +
              "<div id=\"obj-image\">" +
              "  <div id=\"carousel-wrapper\">" +
              "    <div id=\"carousel\">" +
              "    </div>" +
              "    <div id=\"navi\">" +
              "      <div id=\"timer\"></div>" +
              "      <a id=\"prev\" href=\"#\"></a>" +
              "      <a id=\"play\" href=\"#\"></a>" +
              "      <a id=\"next\" href=\"#\"></a>" +
              "    </div>" +
              "  </div>" +
              "  <div id=\"navi1\">" +
              "    <div style=\"width: 100%;height:20px;\">" +
              "      <p id=\"pagenumber\"></p>" +
              "      <p id=\"end\"><a href=\"#\">结束帧</a><input type=\"hidden\" id=\"totalImg\" value=\"\"/></p>" +
              "      <p id=\"start\"><a href=\"#\">起始帧</a><input type=\"hidden\" id=\"startImgNum\" value=\"\"/></p>" +
              "    </div>" +
              "    <p id=\"title\" style=\"width: 100%;\">切图名：<span></span></p>" +
              "  </div>" +
              "</div>";
      $("#showOt2Fits").show();
    } else if (ot2.dataProduceMethod === '8') {
      $("#cut-image-show").css({width: "620px"});
      cutImageShow = "<div id=\"obj-image-cut\">" +
              "  <div id=\"carousel-wrapper\">" +
              "    <div id=\"carousel\">" +
              "    </div>" +
              "    <div id=\"navi\">" +
              "      <div id=\"timer\"></div>" +
              "      <a id=\"prev\" href=\"#\"></a>" +
              "      <a id=\"play\" href=\"#\"></a>" +
              "      <a id=\"next\" href=\"#\"></a>" +
              "    </div>" +
              "  </div>" +
              "  <div id=\"navi1\">" +
              "    <div style=\"width: 100%;height:20px;\">" +
              "      <p id=\"pagenumber\"></p>" +
              "      <p id=\"title\">切图名：<span></span></p>" +
              "      <p id=\"end\"><a href=\"#\">结束帧</a><input type=\"hidden\" id=\"totalImg\" value=\"\"/></p>" +
              "      <p id=\"start\"><a href=\"#\">起始帧</a><input type=\"hidden\" id=\"startImgNum\" value=\"\"/></p>" +
              "    </div>" +
              "  </div>" +
              "</div>";
    }
    $("#cut-image-show").append(cutImageShow);

    if (ot2.dataProduceMethod === '1') {
      var refImg = $('<img/>', {
        src: dataRootWebMap + "/" + ffcRef.storePath + "/" + ffcRef.fileName + ".jpg",
        alt: ffcRef.fileName + ".jpg",
        title: ffcRef.fileName + ".jpg",
        width: "100%",
        height: "200px",
        border: 0
      });

      $("#ref-image-show").append(refImg);
      $("#title2").append("模板时间：" + ffcRef.generateTime + "(U)");
    }

    $.each(ffcList, function(i, item) {
      var objImg;
      if (ot2.dataProduceMethod === '1') {
        objImg = $('<img/>', {
          src: dataRootWebMap + "/" + item.storePath + "/" + item.fileName + ".jpg",
          alt: item.fileName + ".jpg",
          title: item.fileName + ".jpg",
          style: "width:200px;height:200px;border:0"
        });
      } else if (ot2.dataProduceMethod === '8') {
        objImg = $('<img/>', {
          src: dataRootWebMap + "/" + item.storePath + "/" + item.fileName,
          alt: item.fileName,
          title: item.fileName,
          style: "width:618px;height:200px;border:0"
        });
      }
      $("#carousel").append(objImg);
    });

    $("#pagenumber").append("第<span style='font-weight:bold;font-size: 14px;'>0</span>帧，共" + ffcList.length + "帧");
    $("#totalImg").val(ffcList.length);
    $("#startImgNum").val(ot2.firstFfNumber);


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

  function otSkyCoordinateShow(data) {
    var ot2 = data.ot2;
    var ra = ot2.ra;
    var dec = ot2.dec;
    var siderealTime = degreeToHMS(ra);
    var pitchAngle = degreeToDMS(dec);
    $("#skyCordDetail").append("OT坐标(赤经,赤纬)：(" + siderealTime + ",&nbsp;" + pitchAngle + ")&nbsp;&nbsp;&nbsp;(" + ra + ",&nbsp;" + dec + ")&nbsp;&nbsp;&nbsp;");

    //点击查看fits原图
    $("#showOt2Fits").click(function() {
      var gwacRootURL = $("#gwacRootURL").val();
      var otName = $("#otName").val();
      var queryHis = $("#queryHis").val();
      var url = gwacRootURL + "/show-fits-list.action?otName=" + otName + "&queryHis=" + queryHis;
      openwindow(url, '_blank', 1050, 600, 1050, 600);
      return false;
    });
  }

  function otCurveShow(data) {
    var otCurveData = eval(data.otOpticalVaration);
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
    otCurve = $.plot("#ot-curve", otCurveShow, option1);

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

  function otPositionShow(data) {
    var otPositionVaration = eval(data.otPositionVaration);
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
    otPosition = $.plot("#ot-position-curve", positionData, option1);
  }

  function loadOT2Match() {
    var gwacRootURL = $("#gwacRootURL").val();
    var otName = getUrlParameter("otName");
    var queryUrl = gwacRootURL + "/get-ot-match-list.action?otName=" + otName;
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
        }, {
          "targets": 3,
          "data": "OtName?",
          "render": formateOtName
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
    var gwacRootURL = $("#gwacRootURL").val();
    var otName = getUrlParameter("otName");
    var queryUrl = gwacRootURL + "/ot-observe-record.action?otName=" + otName;
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
        {"data": "dateUt"},
        {"data": "raD"},
        {"data": "decD"},
        {"data": "XTemp"},
        {"data": "YTemp"},
        {"data": "magAper"},
        {"data": "magerrAper"},
        {"data": "ffName"},
        {"data": "x"},
        {"data": "y"},
        {"data": "flux"},
        {"data": "background"},
        {"data": "threshold"},
        {"data": "ellipticity"},
        {"data": "classStar"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "dont know",
          "render": formateRowNumber
        }, {
          "targets": [2, 3],
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
    var ra = $('#fuRa').val();
    var dec = $('#fuDec').val();
    var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=2&Radius.unit=arcmin&submit=submit%20query&Coord=";
    searchUrl += ra + "%20" + dec;
    return "<a href='" + searchUrl + "' title='点击在simbad搜寻OT对应坐标' target='_blank'>" + data + "</a>";
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

  function formateOtName(data, type, full, meta) {
    var result = "";
    if (!(data === "" || data === 'null' || data === undefined || data === null)) {
      var url = baseUrl + data;
      result = "<a href='" + url + "' target='_blank' title='点击查看OT详细'>" + data + "</a>";
    }
    return result;
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

  function formatLink(cellvalue, options, rowObject) {
    var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=2&Radius.unit=arcmin&submit=submit%20query&Coord=";
    searchUrl += "";
    return "<a href='" + searchUrl + "' title='点击在simbad搜寻OT对应坐标' target='_blank'>" + cellvalue + "</a>";
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
    return d + ":" + m + ":" + s;
  }

  function degreeToHMS(degree) {
    var second = degree * 3600 * 24 / 360;
    var h = Math.floor(second / 3600);
    var m = Math.floor((second % 3600) / 60);
    var s = (second % 60).toFixed(3);
    return h + ":" + m + ":" + s;
  }

});
