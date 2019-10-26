
$(function () {
  var gwacRootURL = $("#gwacRootURL").val();
  var baseUrl = gwacRootURL + "/gwac/pgwac-ot-history-detail.action?name=";
  var option1 = {
    legend: {show: false},
    series: {shadowSize: 0},
    points: {show: true},
    lines: {show: false, fill: false},
    grid: {hoverable: true, color: '#646464', borderColor: 'transparent', borderWidth: 20, clickable: true},
    selection: {mode: "xy"},
    xaxis: {show: true, tickColor: 'transparent'},
    yaxis: {show: true, tickDecimals: 2, tickFormatter: formate1, transform: formate2, inverseTransform: formate2}
  };
  var filter60 = ["Lum", "Green", "R", "Blue", "V", "I", "B", "Red", "U", "null"];
  var filter30 = ["null", "R", "B", "I", "U", "V", "null"];
  getOt2Detail();
  loadOT2Record();
  loadOT2Match();
  loadOT2FollowupObjects();
  //getOt2FollowupDetail();


//  $(window).resize(function() {
//    otCurveShow();
//    otPositionShow();
//  });

  function getOt2Detail() {
    var gwacRootURL = $("#gwacRootURL").val();
    var coId = getUrlParameter("name");
    var url = gwacRootURL + "/get-crossobj-detail-json.action?name=" + coId;
    $.get(url, ot2Show, "json");
  }

  function getOt2FollowupDetail() {
    var gwacRootURL = $("#gwacRootURL").val();
    var coId = getUrlParameter("coId");
    var url = gwacRootURL + "/get-ot-followup-object-magpos.action?coId=" + coId;
    $.get(url, ot2ShowFollowup, "json");
  }

  function ot2ShowFollowup(data) {
    ot2FollowupMag(data);
  }

  function ot2Show(data) {
    initLoginInfo(data);
    initFollowUpInfo(data);
    otClassify(data);
    cutImgShow(data);
    otSkyCoordinateShow(data);
    otCurveShow(data);
    otxyPositionShow(data);
    otxytempPositionShow(data);
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
    $("#user-login").click(function () {
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
        callback: function (result) {
          if (result) {
            if ($("#loginForm").valid()) {
              var gwacRootURL = $("#gwacRootURL").val();
              var loginUrl = gwacRootURL + "/user-login.action";
              var formData = $("#loginForm").serialize();
              $.post(loginUrl, formData,
                      function (data) {
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

    $("#user-logout").click(function () {
      bootbox.confirm({
        className: 'login-field-dialog',
        message: "是否注销",
        callback: function (result) {
          if (result) {
            var gwacRootURL = $("#gwacRootURL").val();
            var userName = $("#userName").val();
            var logoutUrl = gwacRootURL + "/user-logout.action?userName=" + userName;
            $.get(logoutUrl,
                    function (data) {
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
    $("#otId").val(ot2.coId);
    $("#otName").val(ot2.name);
    $("#fuRa").val(ot2.ra);
    $("#fuDec").val(ot2.dec);

    setFilter60();
    $("#telescope").change(function () {
      if ($("#telescope").val() === '2') {
        setFilter30();
      } else {
        setFilter60();
      }
    });

    bootbox.setLocale("zh_CN");
    $("#followBtn").click(function () {
      bootbox.confirm({
        size: 'small',
        message: "确定发送后随信息？",
        callback: function (result) {
          if (result) {
            var gwacRootURL = $("#gwacRootURL").val();
            var fuUrl = gwacRootURL + "/otFollowUp.action";
            var formData = $("#otFollowUp").serialize();
//            console.log(formData);
            $.post(fuUrl, formData,
                    function (data) {
                      console.log(data);
//                      alert(data.result);
                    }, "json");
          }
        }
      });
    });
  }

  function setFilter60() {
    var filter60 = ["Lum", "Green", "R", "Blue", "V", "I", "B", "Red", "U", "null"];
    $('#filter').find('option').remove();
    $.each(filter60, function (i, item) {
      $('#filter').append($('<option>', {
        value: item,
        text: item
      }));
    });
    $("#filter").val('R');
    $("#filter").change();
  }

  function setFilter30() {
    var filter30 = ["null", "R", "B", "I", "U", "V"];
    $('#filter').find('option').remove();
    $.each(filter30, function (i, item) {
      $('#filter').append($('<option>', {
        value: item,
        text: item
      }));
    });
    $("#filter").val('R');
    $("#filter").change();
  }

  function otClassify(data) {
    var otTypes = data.otTypes;
    var ot2 = data.ot2;
    $.each(otTypes, function (i, item) {
      $('#ot2Classify').append($('<option>', {
        value: item.ottId,
        text: item.ottName
      }));
    });

    if (ot2.otType === 'null' || ot2.otType === null) {
      $("#ot2Classify").val(0);
    } else {
      $("#ot2Classify").val(ot2.otType);
    }

    $("#ot2Classify").change(function () {
      var gwacRootURL = $("#gwacRootURL").val();
      var otTypeId = $("#ot2Classify").val();
      var otId = $("#otId").val();
      var url = gwacRootURL + "/cross-ot-classify.action";
      var formData = "otId=" + otId + "&otTypeId=" + otTypeId;
//      console.log(formData);
//      console.log(url);
      $.post(url, formData,
              function (data) {
                console.log(data);
//                if (data.flag === '1') {
//                  alert("分类成功!");
//                } else {
//                  alert("分类失败!");
//                }
              }, "json");
    });

//    $("#ot2ClassifyBtn").click(function() {
//      var gwacRootURL = $("#gwacRootURL").val();
//      var otTypeId = $("#ot2Classify").val();
//      var otId = $("#otId").val();
//      var url = gwacRootURL + "/ot-classify.action";
//      var formData = "otId=" + otId + "&otTypeId=" + otTypeId;
////      console.log(formData);
//      $.post(url, formData,
//              function(data) {
//                console.log(data);
//                if (data.flag === '1') {
//                  alert("分类成功!");
//                } else {
//                  alert("分类失败!");
//                }
//              }, "json");
//    });
  }

  function cutImgShow(data) {
    var dataRootWebMap = data.dataRootWebMap;
    var ot2 = data.ot2;
    var ffcList = data.ffcList;
    if(ffcList.length>0){
      ffcList=eval(ffcList);
    }

    var cutImageShow;
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
    $("#cut-image-show").append(cutImageShow);

    $.each(ffcList, function (i, item) {
      var objImg;
        objImg = $('<img/>', {
          src: dataRootWebMap + "/" + item.stamp_path + "/" + item.stamp_name,
          alt: item.stamp_name,
          title: item.stamp_name,
          style: "width:618px;height:200px;border:0"
        });
      $("#carousel").append(objImg);
    });

    $("#pagenumber").append("第<span style='font-weight:bold;font-size: 14px;'>1</span>帧，共" + ffcList.length + "帧");
    if(ffcList.length>0){
      $('#title span').text(ffcList[0].stamp_name);
    }
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
        onBefore: function (data) {
          setNavi($(this), data.items.visible);
        }
      }
    });
    $('#carousel-wrapper').hover(function () {
      $('#navi').stop().animate({
        bottom: 0
      });
    }, function () {
      $('#navi').stop().animate({
        bottom: -30
      });
    });
    $('#start').on('click', function () {
      var startImgNum = $('#startImgNum').val();
      $('#carousel').trigger('slideTo', 0);
    });
    $('#end').on('click', function () {
      var totalImg = $('#totalImg').val();
      $('#carousel').trigger('slideTo', totalImg - 1);
    });
  }

  function otSkyCoordinateShow(data) {
    var searchVizieRUrl = "http://vizier.u-strasbg.fr/viz-bin/VizieR-4?-ref=VIZ5bd5371da602&-out.add=_r&-out.add=_RAJ%2C_DEJ&-sort=_r&-order=I&-oc.form=sexa&-meta.foot=1&-meta=1&-meta.ucd=2&-c.geom=r&-c.eq=J2000&-c.u=arcmin&-c.r=+0.5&-c=";
    var searchMPCUrl = "http://www.minorplanetcenter.net/cgi-bin/mpcheck.cgi?TextArea=&radius=15&limit=20.0&oc=327&sort=d&mot=h&tmot=s&pdes=u&needed=f&ps=n&type=p&which=pos&";
    var searchAAVSOUrl = "https://www.aavso.org/vsx/index.php?view=results.submit1&ql=1&getCoordinates=0&plotType=Search&special=index.php%3Fview%3Dresults.special%26sid%3D2&ident=&constid=0&format=s&fieldunit=2&geometry=r&filter%5B%5D=0&filter%5B%5D=1&filter%5B%5D=2&filter%5B%5D=3&order=9&fieldsize=10&targetcenter=";
    
    var ot2 = data.ot2;
    var ra = ot2.ra;
    var dec = ot2.dec;
    var datetimeUt = ot2.foundTimeUtc;
    var siderealTime = degreeToHMS(ra);
    var pitchAngle = degreeToDMS(dec);
    $("#skyCordDetail").append("OT坐标(赤经,赤纬)：(" + siderealTime + ",&nbsp;" + pitchAngle + ")&nbsp;&nbsp;&nbsp;(" + ra + ",&nbsp;" + dec + ")&nbsp;&nbsp;&nbsp;");
    
    var dateTime = datetimeUt.split("T");
    var date = dateTime[0].split("-");
    var time = dateTime[1].split(":");
    var day = parseInt(date[2]) + (parseInt(time[0]) + parseInt(time[1]) / 60 + parseInt(time[2]) / 3600) / 24;
    var raStr = degreeToHMS2(ra, "+");
    var decStr = degreeToDMS2(dec, "+");
    
    searchVizieRUrl += ra + "%2C" + dec;
    searchMPCUrl += "year=" + date[0] + "&month=" + date[1] + "&day=" + day + "&ra=" + raStr + "&decl=" + decStr;
    searchAAVSOUrl += ra + "%2C" + dec;
    
    $("#searchMPC").attr("href", searchMPCUrl);
    $("#searchVizieR").attr("href", searchVizieRUrl);
    $("#searchAAVSO").attr("href", searchAAVSOUrl);

    //点击查看fits原图
    $("#showOt2Fits").click(function () {
      var gwacRootURL = $("#gwacRootURL").val();
      var otName = $("#otName").val();
      var url = gwacRootURL + "/show-fits-list.action?otName=" + otName;
      openwindow(url, '_blank', 1050, 600, 1050, 600);
      return false;
    });
  }

  function otCurveShow(data) {
    var ot2 = data.ot2;
    if (typeof (ot2) !== "undefined")
    {
      $("#otFoundTimeUtc").html(ot2.foundTimeUtc);
    }
    var otCurveData = eval(data.otOpticalVaration);
    if (typeof (otCurveData) === "undefined")
    {
      return;
    }
    var firstPoint = [];
    var lastPoint = [];
    firstPoint[0] = otCurveData[0];
    lastPoint[0] = otCurveData[otCurveData.length - 1];
    var otCurveShow = [{
        data: otCurveData,
        color: '#71c73e',
        points: {radius: 1} //fillColor: '#77b7c5'
      }, {
        data: firstPoint,
        color: '#FF6666',
        points: {radius: 3, fill: true, fillColor: "#FF6666"} //fillColor: '#77b7c5'
      }, {
        data: lastPoint,
        color: '#FF6666',
        points: {radius: 3} //fillColor: '#77b7c5'
      }
    ];
    option1.lines.show = true;
    option1.yaxis.transform = formate2;
    option1.yaxis.inverseTransform = formate2;
    otCurve = $.plot("#ot-curve", otCurveShow, option1);

    $("#ot-curve").bind("plothover", function (event, pos, item) {
      if (item) {
        var x = item.datapoint[0].toFixed(4);
        var y = item.datapoint[1].toFixed(2);
        $("#tooltip").html(x + ", " + y).css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });
  }

  function ot2FollowupMag(data) {
    var fuCheckObj = data.fuCheckObj;
    if (fuCheckObj !== null && typeof (fuCheckObj) !== "undefined")
    {
      $("#otFollowupStartTimeUtc").html(fuCheckObj.startTimeUtc);
    }
    var fuMags = eval(data.mags);
    if (typeof (fuMags) === "undefined" || fuMags.length === 0)
    {
      return;
    }
    var fuMagsShow = [];
    $.each(fuMags, function (i, item) {
      fuMagsShow[i] = {
        label: fuMags[i].objName,
        data: eval(fuMags[i].objMag),
        points: {radius: 2}
      };
    });
    option1.lines.show = false;
    option1.legend.show = true;
    option1.yaxis.transform = formate2;
    option1.yaxis.inverseTransform = formate2;

    otFollowupMagCurve = $.plot("#ot-followup-mag-curve", fuMagsShow, option1);

    $("#ot-followup-mag-curve").bind("plothover", function (event, pos, item) {
      if (item) {
        var x = item.datapoint[0].toFixed(4);
        var y = item.datapoint[1].toFixed(2);
        $("#tooltip").html(x + ", " + y).css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
      } else {
        $("#tooltip").hide();
      }
    });


    //点击查看后随fits原图
    $("#showOt2FollowupFits").click(function () {
      var gwacRootURL = $("#gwacRootURL").val();
      var otName = $("#otName").val();
      var url = gwacRootURL + "/gwac/pgwac-ot-followup-fits.action?otName=" + otName;
      openwindow(url, '_blank', 1050, 600, 1050, 600);
      return false;
    });
  }

  function otxyPositionShow(data) {
    var otPositionVaration = eval(data.otxyVaration);
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
    option1.lines.show = false;
    option1.yaxis.transform = formate3;
    option1.yaxis.inverseTransform = formate3;
    otxyPosition = $.plot("#ot-xy-curve", positionData, option1);
  }

  function otxytempPositionShow(data) {
    var otPositionVaration = eval(data.otxyTempVaration);
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
    option1.lines.show = false;
    option1.yaxis.transform = formate3;
    option1.yaxis.inverseTransform = formate3;
    otxytempPosition = $.plot("#ot-xytemp-curve", positionData, option1);
  }

  function loadOT2FollowupObjects() {
    var gwacRootURL = $("#gwacRootURL").val();
    var otName = getUrlParameter("otName");
    var queryUrl = gwacRootURL + "/get-ot-followup-object-list.action?otName=" + otName;
    $('#ot2-followup-object-table').DataTable({
      "deferRender": true,
      "processing": true,
      "searching": true,
      "lengthChange": true,
      "pageLength": 5,
      "scrollX": true,
      "ajax": {
        url: queryUrl,
        dataSrc: 'objs'
      },
      "columns": [
        {"data": "fuoId"},
        {"data": "fuoName"},
        {"data": "startTimeUtc"},
        {"data": "lastRa"},
        {"data": "lastDec"},
        {"data": "lastX"},
        {"data": "lastY"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "dont know",
          "render": formateRowNumber
        }, {
          "targets": [3],
          "data": "dont know",
          "render": formateRaDec60
        }, {
          "targets": [4],
          "data": "dont know",
          "render": formateRaDec602
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
          text: 'OT2后随结果目标列表',
          action: ot2LoadButtonClick
        }
      ]
    });
  }

  function loadOT2Match() {
    var gwacRootURL = $("#gwacRootURL").val();
    var otName = getUrlParameter("name");
    var queryUrl = gwacRootURL + "/get-cross-object-match-list.action?otName=" + otName;
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
        }, {
          "targets": 6,
          "data": "OtName?",
          "render": formateMatchDistance
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
    var otName = getUrlParameter("name");
    var queryUrl = gwacRootURL + "/cross-ot-observe-record.action?otName=" + otName;
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
        {"data": "crId"},
        {"data": "dateUtc"},
        {"data": "ra"},
        {"data": "dec"},
        {"data": "x"},
        {"data": "y"},
        {"data": "mag"},
        {"data": "magerr"},
        {"data": "ffName"},
        {"data": "fwhm"},
        {"data": "ellipticity"},
        {"data": "XTemp"},
        {"data": "YTemp"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "dont know",
          "render": formateRowNumber
        }, {
          "targets": [2],
          "data": "dont know",
          "render": formateRaDec
        }, {
          "targets": [3],
          "data": "dont know",
          "render": formateRaDec2
        }, {
          "targets": [7],
          "data": "dont know",
          "render": formateMagErr
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
    //var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=5&Radius.unit=arcmin&submit=submit%20query&Coord=";
    //searchUrl += full.raD + "%20" + full.decD;
    var searchUrl = "http://vizier.u-strasbg.fr/viz-bin/VizieR-4?-ref=VIZ5bd5371da602&-out.add=_r&-out.add=_RAJ%2C_DEJ&-sort=_r&-order=I&-oc.form=sexa&-meta.foot=1&-meta=1&-meta.ucd=2&-c.geom=r&-c.eq=J2000&-c.u=arcmin&-c.r=+0.5&-c=";
    //110.58181%2C+30.451553
    searchUrl += full.raD + "%2C" + full.decD;
    return "<a href='" + searchUrl + "' title='点击在VizieR搜寻OT对应坐标' target='_blank'>" + data + "</a>";
  }

  function formateRaDec2(data, type, full, meta) {
    //dateUt: "2015-12-26T22:13:24"
    var dateTime = full.dateUtc.split("T");
    var date = dateTime[0].split("-");
    var time = dateTime[1].split(":");
    var day = parseInt(date[2]) + (parseInt(time[0]) + parseInt(time[1]) / 60 + parseInt(time[2]) / 3600) / 24;
    var raStr = degreeToHMS2(full.raD, "+");
    var decStr = degreeToDMS2(full.decD, "+");
    var searchUrl = "http://www.minorplanetcenter.net/cgi-bin/mpcheck.cgi?TextArea=&radius=15&limit=20.0&oc=327&sort=d&mot=h&tmot=s&pdes=u&needed=f&ps=n&type=p&which=pos&";
    searchUrl += "year=" + date[0] + "&month=" + date[1] + "&day=" + day + "&ra=" + raStr + "&decl=" + decStr;
    return "<a href='" + searchUrl + "' title='点击在IAU小行星网站搜寻OT对应坐标' target='_blank'>" + data + "</a>";
  }

  function formateRaDec60(data, type, full, meta) {
    //var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=5&Radius.unit=arcmin&submit=submit%20query&Coord=";
    //searchUrl += full.raD + "%20" + full.decD;
    var searchUrl = "http://vizier.u-strasbg.fr/viz-bin/VizieR-4?-ref=VIZ5bd5371da602&-out.add=_r&-out.add=_RAJ%2C_DEJ&-sort=_r&-order=I&-oc.form=sexa&-meta.foot=1&-meta=1&-meta.ucd=2&-c.geom=r&-c.eq=J2000&-c.u=arcmin&-c.r=+0.1&-c=";
    //110.58181%2C+30.451553
    searchUrl += full.lastRa + "%2C" + full.lastDec;
    return "<a href='" + searchUrl + "' title='点击在VizieR搜寻OT对应坐标' target='_blank'>" + data + "</a>";
  }

  function formateRaDec602(data, type, full, meta) {
    //dateUt: "2015-12-26T22:13:24"
    var dateTime = full.startTimeUtc.split("T");
    var date = dateTime[0].split("-");
    var time = dateTime[1].split(":");
    var day = parseInt(date[2]) + (parseInt(time[0]) + parseInt(time[1]) / 60 + parseInt(time[2]) / 3600) / 24;
    var raStr = degreeToHMS2(full.lastRa, "+");
    var decStr = degreeToDMS2(full.lastDec, "+");
    var searchUrl = "http://www.minorplanetcenter.net/cgi-bin/mpcheck.cgi?TextArea=&radius=15&limit=20.0&oc=327&sort=d&mot=h&tmot=s&pdes=u&needed=f&ps=n&type=p&which=pos&";
    searchUrl += "year=" + date[0] + "&month=" + date[1] + "&day=" + day + "&ra=" + raStr + "&decl=" + decStr;
    return "<a href='" + searchUrl + "' title='点击在IAU小行星网站搜寻OT对应坐标' target='_blank'>" + data + "</a>";
  }
  
  function formateMagErr(data, type, full, meta) {
    var s2n=0;
    if(data>0){
      s2n=1.087/data;
      s2n=s2n.toFixed(1);
    }
    return s2n;
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

  function formateMatchDistance(data, type, full, meta) {
    return data * 60;
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
    var result = "";
    if (degree < 0) {
      result = "-";
      degree = Math.abs(degree);
    }
    var second = degree * 3600;
    var d = Math.floor(degree);
    var m = Math.floor((second % 3600) / 60);
    var s = (second % 60).toFixed(3);

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


  function degreeToDMS2(degree, split) {
    var result = "";
    if (degree < 0) {
      result = "-";
      degree = Math.abs(degree);
    }
    var second = degree * 3600;
    var d = Math.floor(degree);
    var m = Math.floor((second % 3600) / 60);
    var s = (second % 60).toFixed(3);

    if (d < 10) {
      result = result + "0";
    }
    result = result + d + split;
    if (m < 10) {
      result = result + "0";
    }
    result = result + m + split;
    if (s < 10) {
      result = result + "0";
    }
    result = result + s;
    return result;
  }

  function degreeToHMS2(degree, split) {
    var second = degree * 3600 * 24 / 360;
    var h = Math.floor(second / 3600);
    var m = Math.floor((second % 3600) / 60);
    var s = (second % 60).toFixed(3);
    var result = "";
    if (h < 10) {
      result = result + "0";
    }
    result = result + h + split;
    if (m < 10) {
      result = result + "0";
    }
    result = result + m + split;
    if (s < 10) {
      result = result + "0";
    }
    result = result + s;
    return result;
  }
});
