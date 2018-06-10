

var ot2ListTable;

function reloadUploadFileList() {
  var gwacRootURL = $("#gwacRootURL").val();
  var queryUrl = gwacRootURL + "/get-timing-task-list.action?timestamp=" + new Date().getTime();
  ot2ListTable.ajax.url(queryUrl).load();
}

function deleteTTask(ttId) {
  console.log(ttId);
  $.ajax({
    type: 'get',
    url: $("#addTTForm").attr('action'),
    data: 'actionType=deleteTTForm&ttId=' + ttId,
    cache: false,
    processData: false,
    contentType: false,
  }).success(function (data) {
    alert(data)
    reloadUploadFileList();
  }).error(function () {
    alert("upload error");
  });
  return false;
}

function updateTTask(ttObj) {
  console.log(ttObj);
  $('#actionType').val('updateTTForm');
  $('#ttId').val(ttObj.tt_id);
  $('#ttName').val(ttObj.tt_name);
  $('#ttCommand').val(ttObj.tt_command);
  $('#dpmName').val(ttObj.dpm_name);
  $('#planStartTime').val(ttObj.plan_start_time);
  $('#planEndTime').val(ttObj.plan_end_time);
  $('#comments').val(ttObj.comments);
  //$('#ttFileName').val(ttObj.tt_file_name);
  $('#type').val(ttObj.type);
  $('#executePath').val(ttObj.execute_path);
  $('#planStartDate').val(ttObj.plan_start_date);
  $('#planEndDate').val(ttObj.plan_end_date);
  return false;
}

$(function () {

  var msOption = {
    maxHeight: 200,
    nonSelectedText: '请选择',
    includeSelectAllOption: true,
    allSelectedText: '已全选',
    selectAllText: '全选'
  };
  //$('#dpmName').multiselect(msOption);

  loadUploadFileList();

  $("#addTTBtn").click(function () {
    var formData = new FormData($('#addTTForm')[0]);
    $.ajax({
      type: 'post',
      url: $("#addTTForm").attr('action'),
      data: formData,
      cache: false,
      processData: false,
      contentType: false,
    }).success(function (data) {
      $("#uploadResult").text(data);
      reloadUploadFileList();
    }).error(function () {
      $("#uploadResult").text("upload error");
    });
    $('#actionType').val('addTTForm');
    $('#ttId').val(-1);
    $('#ttName').val('');
    $('#ttCommand').val('');
    $('#dpmName').val('');
    $('#planStartTime').val('');
    $('#planEndTime').val('');
    $('#comments').val('');
    $('#ttFileName').val(null);
    $('#type').val('');
    $('#executePath').val('');
    $('#planStartDate').val('');
    $('#planEndDate').val('');
  });

  function loadUploadFileList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-timing-task-list.action";
    ot2ListTable = $('#ot-list-table').DataTable({
      serverSide: true,
      "deferRender": true,
      "processing": true,
      "searching": false,
      "lengthChange": true,
      "pageLength": 16,
      "scrollX": true,
      "ajax": {
        url: queryUrl,
        dataSrc: function (json) {
          return eval(json.dataStr);
        },
        data: function (d) {
          return reConstructParameter(d);
        },
        type: 'GET'
      },
      "columns": [
        {"data": "tt_id"},
        {"data": "tt_name"},
        {"data": "tt_command"},
        {"data": "dpm_name"},
        {"data": "type"},
        {"data": "status"},
        {"data": "plan_start_time"},
        {"data": "comments"},
        {"data": "tt_id"},
        {"data": "tt_id"}
      ],
      "columnDefs": [{
          "targets": 0,
          "render": formateRowNumber,
          "width": "1%"
        }, {
          "targets": 1,
          "width": "10%"
        }, {
          "targets": 2,
          "render": formateCommand,
          "width": "30%"
        }, {
          "targets": 3,
          "width": "10%"
        }, {
          "targets": 4,
          "render": formateType,
          "width": "8%"
        }, {
          "targets": 5,
          "render": formateStatus,
          "width": "5%"
        }, {
          "targets": 6,
          "render": formateDateTime,
          "width": "10%"
        }, {
          "targets": 7,
          "render": formateCommend,
          "width": "15%"
        }, {
          "targets": 8,
          "render": formateUpdate,
          "width": "5%"
        }, {
          "targets": 9,
          "render": formateDelete,
          "width": "5%"
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="10">10</option>' +
                '<option value="16">16</option>' +
                '<option value="20">20</option>' +
                '<option value="-1">All</option>' +
                '</select> 条',
        "info": "显示第 _START_ 到 _END_ ，共 _TOTAL_ 条。",
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

  /*full: json对象；meta：表格元素*/
  function formateCommand(data, type, full, meta) {
    return "<span title='" + full.execute_path + ":" + full.tt_file_name + "'>" + data + "</span>";
  }
  function formateDateTime(data, type, full, meta) {
    var sdate = full.plan_start_date;
    var edate = full.plan_end_date;
    var stime = full.plan_start_time;
    var etime = full.plan_end_time;
    var retime = full.real_end_time;
    var tstr = "开始日期: " + sdate + "&#13;"
            + "结束日期: " + edate + "&#13;"
            + "开始时间: " + stime + "&#13;"
            + "结束时间: " + etime + "&#13;"
            + "实际结束: " + retime + "&#13;";
    return "<span title='" + tstr + "'>" + data + "</span>";
  }
  function formateType(data, type, full, meta) {
    var rst = "未知类型";
    if (data === '1') {
      rst = "单次立即执行";
    } else if (data === '2') {
      rst = "单次定时执行";
    } else if (data === '3') {
      rst = "多次定时执行";
    }
    return rst;
  }
  function formateStatus(data, type, full, meta) {
    var rst = "未知状态";
    if (data === '1') {
      rst = "未执行";
    } else if (data === '2') {
      rst = "执行中";
    } else if (data === '3') {
      rst = "执行完成";
    } else if (data === '4') {
      rst = "执行失败";
    } else if (data === '5') {
      rst = "取消";
    }
    return rst;
  }
  function formateUpdate(data, type, full, meta) {
    var rst = "修改";
    return "<a href='#' onclick='updateTTask(" + JSON.stringify(full) + ");'>" + rst + "</a>";
  }
  function formateDelete(data, type, full, meta) {
    var rst = "删除";
    return "<a href='#' onclick='deleteTTask(" + data + ");'>" + rst + "</a>";
  }

  function formateCommend(data, type, full, meta) {
    var showStr = data;
    var tlen = 10;
    if (data.length > tlen) {
      showStr = data.substring(0, tlen) + "...";
    }
    return "<span title='" + data + "'>" + showStr + "</span>";
  }

  function reConstructParameter(data) {
    var parms = 'draw=' + data.draw;
    parms = parms + '&length=' + data.length;
    parms = parms + '&start=' + data.start;
    return parms;
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

});
