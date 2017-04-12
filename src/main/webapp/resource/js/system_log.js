
$(function() {
  var ot2ListTable;
  var ot2QueryInterval;

  loadOT2List();

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

  function loadOT2List() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-system-log-list.action";
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
        dataSrc: function(json) {
          return eval(json.dataStr);
        },
        data: function(d) {
          return reConstructParameter(d);
        },
        type: 'GET'
      },
      "columns": [
        {"data": "log_id"},
        {"data": "log_date"},
        {"data": "log_content"},
        {"data": "log_type"},
        {"data": "log_code"}
      ],
      "columnDefs": [{
          "targets": 0,
          "render": formateRowNumber,
          "width": "1%"
        },{
          "targets": 1,
          "width": "12%"
        },{
          "targets": 3,
          "width": "2%"
        },{
          "targets": 4,
          "width": "2%"
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
