
$(function() {
  loadQueryParmeter();
  loadOT2List();

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
    $("#ot2QueryBtn").click(ot2QueryBtnClick);
  }

  function ot2QueryBtnClick() {
    var formData = $("#ot2QueryAction").serialize();
    console.log(formData);
    $.post($("#ot2QueryAction").attr('action'), formData,
            function(data) {
              console.log(data);
              alert(data.result);
            }, "json");
  }

  function loadOT2List() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-ot-level2-list2.action?ot2qp.otName=";
    $('#ot-list-table').DataTable({
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
        {"data": "firstNMark"},
        {"data": "foCount"},
        {"data": "isRecognize"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "ID?",
          "render": formateRowNumber
        }, {
          "targets": 13,
          "data": "dont know",
          "render": formateFirstNMark
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="5">5</option>' +
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
  }

  function formateFirstNMark(data, type, full, meta) {
    return data ? '是' : '否';
  }

  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

  function openDialog() {
    openwindow("show-fits-list.action?otName=&queryHis=",
            '_blank', 1050, 600, 1050, 600);
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
});
