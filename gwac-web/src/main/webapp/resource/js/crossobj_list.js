
$(function () {
  var gwacRootURL = $("#gwacRootURL").val();
//  var baseUrl = gwacRootURL + "/get-ot-detail.action?queryHis=false&otName=";
  var baseUrl = gwacRootURL + "/gwac/pgwac-crossobj-detail.action?name=";
  var ot2arr;
  var ot2ListTable;
  var ot2QueryInterval;

//  initDateStr();
  loadCrossTask();
  loadOT2Type();
  initAutoFollowUp();
  loadQueryParmeter();
  loadOT2List();

  function initDateStr() {
    var today = new Date();
    var y = today.getUTCFullYear();
    var m = today.getUTCMonth() + 1;
    var d = today.getUTCDate();
    m = m < 10 ? "0" + m : m;
    d = d < 10 ? "0" + d : d;
    var dateStr = (y - 2000) + '' + m + d;
    $('#dateStr').val(dateStr);

//    $("#dateStr").change(function () {
//      loadCrossTask();
//    });
  }

  function initAutoFollowUp() {

    getAutoFollowUp();

    $("#autoFollowUp").change(function () {
      if ($('#autoFollowUp').is(':checked')) {
        setAutoFollowUp('true');
      } else {
        setAutoFollowUp('false');
      }
    });
  }

  function getAutoFollowUp() {
    var gwacRootURL = $("#gwacRootURL").val();
    var setParameterUrl = "get-app-parameter2.action"
    var url = gwacRootURL + "/" + setParameterUrl;
    $.ajax({
      type: "get",
      url: url,
      data: "parmName=AutoFollowUp",
      async: true,
      dataType: 'json',
      success: function (data) {
        console.log(data);
        if (data.AutoFollowUp === 'true') {
          $("#autoFollowUp").attr("checked", "true");
        } else {
          $("#autoFollowUp").removeAttr("checked");
        }
      }
    });
  }

  function setAutoFollowUp(val) {
    var gwacRootURL = $("#gwacRootURL").val();
    var setParameterUrl = "set-app-parameter2.action"
    var url = gwacRootURL + "/" + setParameterUrl;
    console.log(url);
    $.ajax({
      type: "get",
      url: url,
      data: "parmName=AutoFollowUp&parmValue=" + val,
      async: true,
      dataType: 'json',
      success: function (data) {
        console.log(data);
      }
    });
  }

  function loadQueryParmeter() {
    var option = {
      maxHeight: 200,
      nonSelectedText: '请选择',
      includeSelectAllOption: true,
      allSelectedText: '已全选',
      selectAllText: '全选'
    };
    $('#ot2IsMatch').multiselect(option);
    $('#ot2MatchType').multiselect(option);
    $('#ot2Type').multiselect(option);
//    $('#frameNumber').multiselect(option);
    $('#lookBackResult').multiselect(option);
//    $('#lookBackCnn').multiselect(option);
    $("#ot2QueryBtn").click(ot2QueryBtnClick);
    $('#ot2ListTableAutoRefresh').change(setAutoRefresh);

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
    console.log(formData);
    if (formData === 'ot2qp.lookBackCnn=' || formData === 'autoRefresh=on' || formData === '') {
      formData = "ot2qp.coId=";
    }
    var queryUrl = $("#ot2QueryAction").attr('action') + "?timestamp=" + new Date().getTime() + "&" + formData;
    ot2ListTable.ajax.url(queryUrl).load();
  }

  function loadOT2Type() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-ot-type-json.action";
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function (data) {
        ot2arr = data.otTypes;
        $.each(ot2arr, function (i, item) {
          if (item.ottName !== "未分类")
            $('#ot2Type').append($('<option>', {
              value: item.ottId,
              text: item.ottName
            }));
        });
      }
    });
  }

  function loadOT2List() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-cross-object-list.action?ot2qp.coId=";
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
        {"data": "coId"},
        {"data": "name"},
        {"data": "foundTimeUtc"},
        {"data": "ra"},
        {"data": "dec"},
        {"data": "xtemp"},
        {"data": "ytemp"},
        {"data": "total"},
        {"data": "otType"},
        {"data": "lookBackResult"},
        {"data": "probability"},
        {"data": "minorPlanetMatch"},
        {"data": "hisMatch"},
        {"data": "usnoMatch"},
        {"data": "rc3Match"},
        {"data": "foCount"},
        {"data": "cvsMatch"},
        {"data": "otherMatch"},
        {"data": "magDiff"}
      ],
      "columnDefs": [{
          "targets": 0,
          "render": formateRowNumber
        }, {
          "targets": 1,
          "render": formateOtName
        }, {
          "targets": 2,
          "render": formateTime
        }, {
          "targets": [3, 4],
          "render": floatFormate3
        }, {
          "targets": [5, 6],
          "render": floatFormate2
        }, {
          "targets": 11,
          "render": formatBool
        }, {
          "targets": 12,
          "render": formatBool
        }, {
          "targets": 13,
          "render": formatBool
        }, {
          "targets": 14,
          "data": "dont know",
          "render": formateRC3
        }, {
          "targets": 16,
          "render": formatBool
        }, {
          "targets": 17,
          "render": formatBool
        }, {
          "targets": 9,
          "render": formateLookBack
        }, {
          "targets": 10,
          "render": formateLookBackCNN
        }, {
          "targets": 8,
          "render": formateOtType
        }, {
          "targets": 18,
          "render": formateMagDiff
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

  function formateMagDiff(data, type, full, meta) {
    return data.toFixed(1);
  }

  function floatFormate3(data, type, full, meta) {
    return data.toFixed(3);
  }

  function floatFormate2(data, type, full, meta) {
    return data.toFixed(2);
  }

  function formateRC3(data, type, full, meta) {
    return data ? "<span style='color:#D9006C;font-weight: bold;'>" + 1 + "</span>" : 0;
  }

  function formatBool(data, type, full, meta) {
    return data ? 1 : 0;
  }

  function formateOT2His(data, type, full, meta) {
    return data ? 1 : "<span style='color:#FF0000;font-weight: bold;'>" + 0 + "</span>";
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
    } else if (data === 10) {
      result = 'F2OB'; //图像匹配失败
    } else if (data === 11) {
      result = 'FLUX'; //图像匹配，未匹配到任何目标
    } else if (data === 12) {
      result = 'FRNB'; //图像匹配，未匹配到任何目标
    }
    return result;
  }

  function formateLookBackCNN2(data, type, full, meta) {
    var result = "<span title='" + data + "' onmouseover='showOT2LBCNNImg(event,\"" + full.name + "\")' onmouseout='hiddenOT2LBCNNImg()'>";
    if (data >= 0.5) {
      result = result + "OT";
    } else if (data > -0.9) {
      result = result + "FOT";
    } else {
      result = result + "未处理";
    }
    result = result + "</span>";
    return result;
  }
  function formateLookBackCNN(data, type, full, meta) {
    return data;
  }

  function formateTime(data, type, full, meta) {
    //foundTimeUtc:"2018-10-01T21:20:05"
    return data.substring(11);
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

  /*full: json对象；meta：表格元素*/
  function formateOtName(data, type, full, meta) {
    var url = baseUrl + data;
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

function showOT2LBCNNImg(e, otName) {
  var dateStr = otName.substring(1, 7);
  $("#ot2lbcnn").attr("src", "/images/ot2lbcnn/" + dateStr + "/" + otName + ".jpg");
  //$("#ot2lbcnn").attr("src", "/images/ot2lbcnn/181011/G181011_C05424.jpg");
  $("#ot2lbcnn").css({top: e.clientY - 80 - 40, left: e.clientX - 115, position: 'fixed'}).fadeIn(200);
  $('#ot2lbcnn').show();
  return false;
}

function hiddenOT2LBCNNImg() {
  $("#ot2lbcnn").hide();
  return false;
}

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

function loadCrossTask() {
  var gwacRootURL = $("#gwacRootURL").val();
  var dateStr = $("#dateStr").val();
  var queryUrl = gwacRootURL + "/get-cross-task-json.action";
  $('#ctId').find('option').remove();
  $('#ctId').append($('<option>', {
    value: '0',
    text: 'ALL'
  }));
  $.ajax({
    type: "get",
    url: queryUrl,
    data: 'dateStr=' + dateStr,
    async: false,
    dataType: 'json',
    success: function (data) {
      var objs = data.objs;
      if (objs) {
        $.each(objs, function (i, item) {
          $('#ctId').append($('<option>', {
            value: item.ctId,
            text: item.ctName
          }));
        });
      } else {
        console.log("cannot find cross task.");
      }
    }
  });
//  $('#ctId').multiselect(option);
//  $("#ctId").val('R');
//  $("#ctId").change();
}