
$(function() {
  var ot2ListTable;
  var ot2QueryInterval;



  function loadQueryParmeter() {
    var option = {
      maxHeight: 200,
      nonSelectedText: '请选择',
      includeSelectAllOption: true,
      allSelectedText: '已全选',
      selectAllText: '全选'
    };
    loadLogCodeIPs();
    $('#log_code').multiselect(option);
    //$('#log_ip').multiselect(option);

    $("#sysLogQueryBtn").click(sysLogQueryBtnClick);
    $('#ot2ListTableAutoRefresh').change(setAutoRefresh);
  }

  function setAutoRefresh() {
    if ($('#ot2ListTableAutoRefresh').is(':checked')) {
      ot2QueryInterval = setInterval(sysLogQueryBtnClick, 15000);
    } else {
      clearInterval(ot2QueryInterval);
    }
  }

  function sysLogQueryBtnClick() {
    var formData = $("#sysLogQueryAction").serialize();
    var queryUrl = $("#sysLogQueryAction").attr('action') + "?timestamp=" + new Date().getTime() + "&" + formData;
    ot2ListTable.ajax.url(queryUrl).load();
  }

  function loadLogCodeIPs() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-sys-log-codeip-json.action";
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function(data) {
//        console.log(data);
        logCodes = eval(data.logCodes);
        msgIps = eval(data.msgIps);
//        console.log(logCodes);
//        console.log(msgIps);
        $.each(logCodes, function(i, item) {
          $('#log_code').append($('<option>', {
            value: item.log_code,
            text: item.log_code
          }));
        });
        $.each(msgIps, function(i, item) {
          $('#log_ip').append($('<option>', {
            value: item.msg_ip,
            text: item.msg_ip
          }));
        });
      }
    });
  }

  function loadSystemLogList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-ot-type-json.action";
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
        dataSrc:  'mounts',
        type: 'GET'
      },
      "columns": [
        {"data": "mountId"},
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
          "targets": 1,
          "width": "12%"
        }, {
          "targets": 3,
          "width": "2%"
        }, {
          "targets": 4,
          "width": "6%"
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

});
