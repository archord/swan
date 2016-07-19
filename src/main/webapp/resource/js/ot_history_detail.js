
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var baseUrl = gwacRootURL + "/get-ot-history-detail-json.action?otName=";
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

  $(window).resize(function() {
    otCurveShow();
    otPositionShow();
  });

  function getOt2Detail() {
    var gwacRootURL = $("#gwacRootURL").val();
    var otName = getUrlParameter("otName");
    var url = gwacRootURL + "/get-ot-history-detail-json.action?otName=" + otName;
    $.get(url, ot2Show, "json");
  }


  function ot2Show(data) {
    cutImgShow(data);
    otSkyCoordinateShow(data);
    otCurveShow(data);
    otPositionShow(data);
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
    var refImg = "";
    if (ot2.dataProduceMethod === '1') {
      if (ffcRef === null) {
        refImg = $('<img/>');
      } else {
        refImg = $('<img/>', {
          src: dataRootWebMap + "/" + ffcRef.storePath + "/" + ffcRef.fileName + ".jpg",
          alt: ffcRef.fileName + ".jpg",
          title: ffcRef.fileName + ".jpg",
          width: "100%",
          height: "200px",
          border: 0
        });
      }

      $("#ref-image-show").append(refImg);
      if (ffcRef !== null) {
        $("#title2").append("模板时间：" + ffcRef.generateTime + "(U)");
      }

      //添加OT2信息下载链接
      var downloadUrl = gwacRootURL + "/downloadot2.action?otName=" + ot2.name;
      var downloadOT2Info = "<div id='download-ot2-info'><a href='" + downloadUrl + "' target='_blank' title='点击下载OT详细信息'>下载详细</a></div>";
      $("#cut-image-show").append(downloadOT2Info);
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
