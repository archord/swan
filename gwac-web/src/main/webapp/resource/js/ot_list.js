
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
//  var baseUrl = gwacRootURL + "/gction/get-ot-detail.action?queryHis=false&otName=";
  var baseUrl = gwacRootURL + "/gwac/pgwac-ot-detail2.action?otName=";
  var ot2arr;
  var ot2ListTable;
  var ot2QueryInterval;
  var alarmPlayer;

  var lastTotalOT2 = 0;
  var curTotalOT2 = 0;

  loadOT2Type();
  loadOT2Alarm();
  initAlarmPlayer();
  initAutoFollowUp();
  loadQueryParmeter();
  loadOT2List();

  ot2ListTable.on('xhr', function() {
    lastTotalOT2 = curTotalOT2;
    curTotalOT2 = 0;
    var ot2List = ot2ListTable.ajax.json().gridModel;
//    console.log(ot2List);
    $.each(ot2List, function(i, item) {
      if ((item.dataProduceMethod === '1' && item.isMatch === 1 && item.lookBackResult === 1)
              || (item.dataProduceMethod === '8' && item.isMatch === 1)) {
        curTotalOT2 = curTotalOT2 + 1;
      }
    });
    if ($('#autoRing').is(':checked') && lastTotalOT2 !== 0 && curTotalOT2 > lastTotalOT2) {
      playAlarm();
    }
//    console.log("lastTotalOT2:"+lastTotalOT2);
//    console.log("curTotalOT2:"+curTotalOT2);
  });


  function initAutoFollowUp() {

    getAutoFollowUp();

    $("#autoFollowUp").change(function() {
      if ($('#autoFollowUp').is(':checked')) {
        setAutoFollowUp('true');
      } else {
        setAutoFollowUp('false');
      }
    });
  }

  function getAutoFollowUp() {
    var gwacRootURL = $("#gwacRootURL").val();
    var setParameterUrl = "gction/get-app-parameter2.action"
    var url = gwacRootURL + "/" + setParameterUrl;
    $.ajax({
      type: "get",
      url: url,
      data: "parmName=AutoFollowUp",
      async: true,
      dataType: 'json',
      success: function(data) {
        console.log(data);
        if (data.AutoFollowUp==='true') {
          $("#autoFollowUp").attr("checked", "true");
        } else {
          $("#autoFollowUp").removeAttr("checked");
        }
      }
    });
  }

  function setAutoFollowUp(val) {
    var gwacRootURL = $("#gwacRootURL").val();
    var setParameterUrl = "gction/set-app-parameter2.action"
    var url = gwacRootURL + "/" + setParameterUrl;
    console.log(url);
    $.ajax({
      type: "get",
      url: url,
      data: "parmName=AutoFollowUp&parmValue=" + val,
      async: true,
      dataType: 'json',
      success: function(data) {
        console.log(data);
      }
    });
  }

  function initAlarmPlayer() {
    alarmPlayer = $("#alarm-player");
    alarmPlayer.jPlayer({
      ready: function() {
        alarmPlayer.jPlayer("setMedia", {
          mp3: "/gresources/" + $('#newOTAlarm').val()
        });
      },
      loop: false
    });

    $("#newOTAlarm").change(function() {
      playAlarm();
    });
  }

  function playAlarm() {
    var optionSelected = $("#newOTAlarm").find("option:selected");
    var valueSelected = optionSelected.val();
    alarmPlayer.jPlayer("setMedia", {
      mp3: "/gresources/" + valueSelected
    });
    alarmPlayer.jPlayer("play");
  }

  function loadQueryParmeter() {
    var option = {
      maxHeight: 200,
      nonSelectedText: '请选择',
      includeSelectAllOption: true,
      allSelectedText: '已全选',
      selectAllText: '全选'
    };
    $('#ot2ProcessType').multiselect(option);
    $('#ot2IsMatch').multiselect(option);
    $('#ot2MatchType').multiselect(option);
    $('#ot2Type').multiselect(option);
    $('#ot2Ccd').multiselect(option);
    $('#lookBackResult').multiselect(option);
    $("#ot2QueryBtn").click(ot2QueryBtnClick);
    $('#ot2ListTableAutoRefresh').change(setAutoRefresh);

    $(".ot2QueryParameter").change(function() {
      lastTotalOT2 = 0;
      curTotalOT2 = 0;
    });
    setAutoRefresh();
  }

  function setAutoRefresh() {
    if ($('#ot2ListTableAutoRefresh').is(':checked')) {
      ot2QueryInterval = setInterval(ot2QueryBtnClick, 15000);
    } else {
      clearInterval(ot2QueryInterval);
    }
  }

  function ot2QueryBtnClick() {
    var formData = $("#ot2QueryAction").serialize();
    if (formData === 'autoRefresh=on' || formData === '') {
      formData = "ot2qp.otName=";
    }
    var queryUrl = $("#ot2QueryAction").attr('action') + "?timestamp=" + new Date().getTime() + "&" + formData;
    ot2ListTable.ajax.url(queryUrl).load();
  }

  function loadOT2Type() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/get-ot-type-json.action";
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function(data) {
        ot2arr = data.otTypes;
        $.each(ot2arr, function(i, item) {
          if (item.ottName !== "未分类")
            $('#ot2Type').append($('<option>', {
              value: item.ottId,
              text: item.ottName
            }));
        });
      }
    });
  }

  function loadOT2Alarm() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/get-ot-alarm.action";
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function(data) {
//        $('#newOTAlarm').append($('<option>', {
//          value: "",
//          text: "请选择"
//        }));
        var mrs = data.multimediaResources;
        $.each(mrs, function(i, item) {
          if (item.type === '1')
            $('#newOTAlarm').append($('<option>', {
              value: item.path,
              text: item.enName
            }));
        });

        var defaultText = 'Beep';
        $('#newOTAlarm option').filter(function() {
          return $(this).text() === defaultText;
        }).prop('selected', true);
      }
    });
  }

  function loadOT2List() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/get-ot-level2-list2.action?ot2qp.otName=";
    ot2ListTable = $('#ot-list-table').DataTable({
      "deferRender": true,
      "processing": true,
      "searching": true,
      "lengthChange": true,
      "pageLength": 16,
      "scrollX": true,
      "ajax": {
        url: queryUrl,
        dataSrc: 'gridModel'
      },
      "columns": [
        {"data": "otId"},
        {"data": "name"},
        {"data": "ra"},
        {"data": "dec"},
        {"data": "xtemp"},
        {"data": "ytemp"},
        {"data": "identify"},
        {"data": "total"},
        {"data": "otType"},
        {"data": "lookBackResult"},
        {"data": "minorPlanetMatch"},
        {"data": "ot2HisMatch"},
        {"data": "usnoMatch"},
        {"data": "rc3Match"},
        {"data": "foCount"},
        {"data": "followUpResult"},
        {"data": "cvsMatch"},
        {"data": "otherMatch"},
        {"data": "firstNMark"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "ID?",
          "render": formateRowNumber
        }, {
          "targets": 1,
          "data": "OtName?",
          "render": formateOtName
        }, {
          "targets": [2, 3],
          "data": "dont know",
          "render": floatFormate3
        }, {
          "targets": [4, 5],
          "data": "dont know",
          "render": floatFormate2
        }, {
          "targets": 12,
          "data": "dont know",
          "render": formateRC3
        }, {
          "targets": 11,
          "data": "dont know",
          "render": formateOT2His
        }, {
          "targets": 9,
          "data": "dont know",
          "render": formateLookBack
        }, {
          "targets": 18,
          "data": "dont know",
          "render": formateFirstNMark
        }, {
          "targets": 8,
          "data": "dont know",
          "render": formateOtType
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="10">10</option>' +
                '<option value="16">16</option>' +
                '<option value="20">20</option>' +
                '<option value="-1">All</option>' +
                '</select> 条',
        "info": "显示第 _START_ 到 _END_ ，共 _TOTAL_ 条。<span style='color:red'>注：为提升体验，在默认(没有查询条件)情况下，该页面最多只显示最新的30条记录。</span>",
        "search": "模糊查询:",
        "paginate": {
          "first": "首页",
          "last": "尾页",
          "next": "下一页",
          "previous": "上一页"
        }
      },
      dom: 'lftrip'
    });
  }


  function formateOtType(data, type, full, meta) {
    var rst;
    if (data >= 0) {
      rst = ot2arr[data].ottName;
    } else {
      rst = "未分类";
    }
    return rst;
  }

  function floatFormate3(data, type, full, meta) {
    return data.toFixed(3);
  }

  function floatFormate2(data, type, full, meta) {
    return data.toFixed(2);
  }

  function formateRC3(data, type, full, meta) {
    return data ? "<span style='color:#D9006C;font-weight: bold;'>" + data + "</span>" : data;
  }

  function formateOT2His(data, type, full, meta) {
    return data ? data : "<span style='color:#FF0000;font-weight: bold;'>" + data + "</span>";
  }

  function formateLookBack(data, type, full, meta) {
    var result = data;
    if (data === 1) {
      result = 'OT';
    } else if (data === 2) {
      result = 'FOT';
    } else if (data === 3) {
      result = 'FNBG';
    } else if (data === 4) {
      result = 'FOBJ';
    } else if (data === 5) {
      result = 'FSUB';
    } else if (data === 6) {
      result = 'FRMS'; //图像匹配失败
    } else if (data === 7) {
      result = 'FWHM'; //图像匹配失败
    } else if (data === 8) {
      result = 'FMCH'; //图像匹配，未匹配到任何目标
    } else if (data === 9) {
      result = 'FHOT'; //图像匹配，未匹配到任何目标
    }
    return result;
  }

  function formateFirstNMark(data, type, full, meta) {
    return data ? '是' : '否';
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

  /*full: json对象；meta：表格元素*/
  function formateOtName(data, type, full, meta) {
    var url = baseUrl + data;
//    if (full.ot2HisMatch === 0 && full.rc3Match === 0 && full.minorPlanetMatch === 0 && full.usnoMatch === 0
//            && (full.lookBackResult === 0 || full.lookBackResult === 1)
//            && (full.otType===0 || full.otType===8 || full.otType===9 || full.otType===10 || full.otType===11)) {
    if (full.rc3Match > 0) {
      return "<a href='" + url + "' target='_blank' class='importantRC3' title='点击查看OT详细'>" + data + "</a>";
    } else if (((full.isMatch === 0 || full.isMatch === 1) && (full.lookBackResult === 0 || full.lookBackResult === 1)
            && (full.otType === 0 || full.otType === 8 || full.otType === 9 || full.otType === 10 || full.otType === 11))
            || (full.otType === 8 || full.otType === 9 || full.otType === 10 || full.otType === 11)) {
      return "<a href='" + url + "' target='_blank' class='importantOT2' title='点击查看OT详细'>" + data + "</a>";
    } else {
      return "<a href='" + url + "' target='_blank' title='点击查看OT详细'>" + data + "</a>";
    }
  }
});


function openDialog(otName) {
  var gwacRootURL = $("#gwacRootURL").val();
  var queryUrl = gwacRootURL + "/gwac/pgwac-ot-detail2.action?otName=" + otName;
  openwindow(queryUrl, '_blank', 1050, 600, 1050, 600);
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