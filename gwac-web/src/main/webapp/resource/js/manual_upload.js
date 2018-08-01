
$(function () {
  var ot2ListTable;

  loadUploadFileList();
  
  $("#uploadFileBtn").click(function () {
    var formData = new FormData($('#uploadFileAction')[0]);
    $.ajax({
      type: 'post',
      url: $("#uploadFileAction").attr('action'),
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
    var queryUrl = gwacRootURL + "/gction/get-manual-file-list.action?timestamp=" + new Date().getTime();
    ot2ListTable.ajax.url(queryUrl).load();
  }


  function loadUploadFileList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/get-manual-file-list.action";
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
        {"data": "muf_id"},
        {"data": "name"},
        {"data": "comments"},
        {"data": "time"}
      ],
      "columnDefs": [{
          "targets": 0,
          "render": formateRowNumber,
          "width": "1%"
        }, {
          "targets": 1,
          "width": "20%",
          "render": formateFileName
        }, {
          "targets": 3,
          "width": "20%"
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
  function formateFileName(data, type, full, meta) {
    var gwacRootURL = $("#gwacRootURL").val();
    var url = "/images/manual_upload/"+data;
    return "<a href='" + url + "' target='_blank' title='点击下载文件'>" + data + "</a>";
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
