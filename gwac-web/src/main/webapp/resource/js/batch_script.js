
$(function () {
  var ot2ListTable;

    var msOption = {
      maxHeight: 200,
      nonSelectedText: '请选择',
      includeSelectAllOption: true,
      allSelectedText: '已全选',
      selectAllText: '全选'
    };
  $('#dpmName').multiselect(msOption);
  
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
  });

  function reloadUploadFileList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-timing-task-list.action?timestamp=" + new Date().getTime();
    ot2ListTable.ajax.url(queryUrl).load();
  }


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
        {"data": "comments"}
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
          "width": "5%"
        }, {
          "targets": 5,
          "width": "5%"
        }, {
          "targets": 6,
          "render": formateDateTime,
          "width": "10%"
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
    return "<span title='"+full.execute_path+":"+full.tt_file_name+"'>" + data + "</span>";
  }
  function formateDateTime(data, type, full, meta) {
    var sdate = full.plan_start_date;
    var edate = full.plan_end_date;
    var stime = full.plan_start_time;
    var etime = full.plan_end_time;
    var retime = full.real_end_time;
    var tstr = "StartDate: "+ sdate + "&#13;"
      + "EndDate: "+ edate + "&#13;"
      + "StartTime: "+ stime + "&#13;"
      + "EndTime: "+ etime + "&#13;"
      + "RealEndTime: "+ retime + "&#13;";
    return "<span title='"+tstr+"'>" + data + "</span>";
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
