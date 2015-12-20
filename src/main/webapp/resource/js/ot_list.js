
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var baseUrl = gwacRootURL + "/get-ot-detail.action?queryHis=false&otName=";
  var ot2arr;
  loadOT2Type();
  loadQueryParmeter();
  var ot2ListTable = loadOT2List();
  var ot2QueryInterval;

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
    var queryUrl = gwacRootURL + "/get-ot-type-json.action";
    $.ajax({
      type: "get",
      url: queryUrl,
      data: '{}',
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

  function loadOT2List() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-ot-level2-list2.action?ot2qp.otName=";
    var ot2ListTable = $('#ot-list-table').DataTable({
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
        {"data": "otId"},
        {"data": "name"},
        {"data": "ra"},
        {"data": "dec"},
        {"data": "xtemp"},
        {"data": "ytemp"},
        {"data": "identify"},
        {"data": "total"},
        {"data": "rc3Match"},
        {"data": "minorPlanetMatch"},
        {"data": "cvsMatch"},
        {"data": "otherMatch"},
        {"data": "ot2HisMatch"},
        {"data": "usnoMatch"},
        {"data": "lookBackResult"},
        {"data": "firstNMark"},
        {"data": "foCount"},
        {"data": "followUpResult"},
        {"data": "otType"}
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
          "targets": 8,
          "data": "dont know",
          "render": formateRC3
        }, {
          "targets": 12,
          "data": "dont know",
          "render": formateOT2His
        }, {
          "targets": 14,
          "data": "dont know",
          "render": formateLookBack
        }, {
          "targets": 15,
          "data": "dont know",
          "render": formateFirstNMark
        }, {
          "targets": 18,
          "data": "dont know",
          "render": formateOtType
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="10">10</option>' +
                '<option value="20">20</option>' +
                '<option value="50">50</option>' +
                '<option value="100">100</option>' +
                '<option value="-1">All</option>' +
                '</select> 条',
        "info": "显示第 _START_ 到 _END_ ，共 _TOTAL_ 条",
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
    return ot2ListTable;
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
    if (full.ot2HisMatch === 0 && full.rc3Match === 0 && full.minorPlanetMatch === 0 && full.usnoMatch === 0
            && (full.lookBackResult === 0 || full.lookBackResult === 1)) {
      return "<a href='" + url + "' target='_blank' class='importantOT2' title='点击查看OT详细'>" + data + "</a>";
    } else if (full.rc3Match > 0) {
      return "<a href='" + url + "' target='_blank' class='importantRC3' title='点击查看OT详细'>" + data + "</a>";
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