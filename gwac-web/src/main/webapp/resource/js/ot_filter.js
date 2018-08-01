
$(function() {

  var ot2arr;
  loadOT2Type();
  $('#ccdFilterBtn').click(ccdFilterBtnClick);
  var ccdFilterTable = loadCcdFilterList();

  function ccdFilterBtnClick() {
    var formData = $("#ccdFilterAction").serialize();
    if (formData === '') {
      alter("过滤条件不能为空!");
    }
    var queryUrl = $("#ccdFilterAction").attr('action');
    $.post(queryUrl, formData,
            function(data) {
              console.log(data);
              setTimeout(function() {
                ccdFilterTable.ajax.reload();
              }, 500);
            }, "json");
  }

  function loadCcdFilterList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/ccd-pixel-filter-list.action"; //CcdPixelFilterList
    var ccdFilterTable = $('#ccd-filter-table').DataTable({
      "deferRender": true,
      "processing": true,
      "searching": true,
      "lengthChange": true,
      "pageLength": 20,
      "scrollX": true,
      "ajax": {
        url: queryUrl,
        dataSrc: 'gridModel'
      },
      "columns": [
        {"data": "Id"},
        {"data": "dpmId"},
        {"data": "minX"},
        {"data": "minY"},
        {"data": "maxX"},
        {"data": "maxY"},
        {"data": "otTypeId"},
        {"data": "Id"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "ID?",
          "render": formateRowNumber
        }, {
          "targets": 6,
          "data": "delete?",
          "render": formateOtType
        }, {
          "targets": 7,
          "data": "delete?",
          "render": formateOperation
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="10">10</option>' +
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
    return ccdFilterTable;
  }

  function loadOT2Type() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/get-ot-type-json.action";
    $.ajax({
      type: "get",
      url: queryUrl,
      data: '{}',
      async: false,
      dataType: 'json',
      success: function(data) {
        ot2arr = data.otTypes;
      }
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

  function formateOperation(data, type, full, meta) {
    return "<a href='javascript:void(0)' title='点击删除' onclick='deleteCcdFilter(" + full.cpfId + ");'>删除</a>";
  }


  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }
});

function deleteCcdFilter(id) {

  var gwacRootURL = $("#gwacRootURL").val();
  var queryUrl = gwacRootURL + "/gction/ccd-pixel-filter-delete.action?cpfId=" + id;
  $.get(queryUrl, function(data) {
    console.log(data);
    setTimeout(function() {
      $('#ccd-filter-table').DataTable().ajax.reload();
    }, 500);
  }, "json");
}