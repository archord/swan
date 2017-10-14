
$(function() {
  var ot2ListTable;

  loadSystemLogList();
  var ot2QueryInterval = setInterval(sysLogQueryBtnClick, 15000);
  //clearInterval(ot2QueryInterval);

  function sysLogQueryBtnClick() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-mount-list-json.action?timestamp=" + new Date().getTime();
    ot2ListTable.ajax.url(queryUrl).load();
  }


  function loadSystemLogList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-mount-list-json.action";
    ot2ListTable = $('#mount-list-table').DataTable({
      serverSide: false,
      "deferRender": true,
      "processing": true,
      "searching": false,
      "lengthChange": true,
      "pageLength": 10,
      "scrollX": true,
      "ajax": {
        url: queryUrl,
        dataSrc: 'mounts',
        type: 'GET'
      },
      "columns": [
        {"data": "mountId"},
        {"data": "groupId"},
        {"data": "unitId"},
        {"data": "status"},
        {"data": "opSn"},
        {"data": "obsType"},
        {"data": "skyName"},
        {"data": "ra"},
        {"data": "dec"}
      ],
      "columnDefs": [{
          "targets": 0,
          "render": formateRowNumber,
          "width": "1%"
        }, {
          "targets": 3,
          "render": formateStatus
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="10">10</option>' +
                '<option value="15">15</option>' +
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

  function reConstructParameter(data) {
    var parms = 'draw=' + data.draw;
    parms = parms + '&length=' + data.length;
    parms = parms + '&start=' + data.start;
    return parms;
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }
  function formateStatus(data, type, full, meta) {
    var t = "未知状态";
    if (data === 3) {
      t = "正常";
    } else if (data === 1) {
      t = "正常";
    } else if (data === 2) {
      t = "下线";
    } else if (data === 4) {
      t = "故障";
    } else if (data === 5) {
      t = "损坏";
    }
    return t;
  }

});
