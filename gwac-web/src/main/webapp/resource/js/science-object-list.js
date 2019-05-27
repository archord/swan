
$(function () {
  var obsPlanTable;

  loadObsPlanList();
  
  $('#setFupParm').click(folowupParmSet);

  function folowupParmSet() {
    var url = $("#gwacRootURL").val() + "/followup/followup-parm.action";
    openwindow(url, '_blank', 830, 340, 830, 340);
  }

  function loadObsPlanList() {
    var queryUrl = $("#gwacRootURL").val() + "/get-sciobj-list.action?timestamp=" + new Date().getTime();
    obsPlanTable = $('#sci-obj-table').DataTable({
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
        {"data": "so_id"},
        {"data": "name"},
        {"data": "discovery_time_utc"},
        {"data": "mag"},
        {"data": "found_usno_r2"},
        {"data": "found_usno_b2"},
        {"data": "found_usno_i"},
        {"data": "obj_ra"},
        {"data": "fup_count"},
        {"data": "type"},
        {"data": "so_id"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "ID?",
          "render": formateRowNumber
        }, {
          "targets": 1,
          "render": formateOt2
        }, {
          "targets": 3,
          "render": formateMag
        }, {
          "targets": 4,
          "render": formateMag
        }, {
          "targets": 5,
          "render": formateMag
        }, {
          "targets": 6,
          "render": formateMag
        }, {
          "targets": 7,
          "render": formatePosition
        }, {
          "targets": 10,
          "render": formateTrue
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="8">8</option>' +
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

  function formateTrue(data, type, full, meta) {
//    return "<span>" +(meta.row + 1)+"<input type='checkbox'/></span>";
    return "<span><input name='soIds' type='checkbox' value='" + data + "' onclick='updateFalse(" + data + ");'/></span>";
  }
  
  
  function formateRowNumber(data, type, full, meta) {
    return "<span>" +(meta.row + 1)+"</span>";
  }
  /*full: json对象；meta：表格元素*/
  function formateOt2(data, type, full, meta) {
    var content = "";
    var gwacRootURL = $("#gwacRootURL").val();
    var searchUrl = gwacRootURL + "/gwac/pgwac-ot-detail2.action?otName=" + data;
    content = "<a href='" + searchUrl + "' title='点击打开OT2详细页面' target='_blank'>" + data + "</a>";
    return content;
  }
  function formateMag(data, type, full, meta) {
    return data.toFixed(2);
  }
  function formatePosition(data, type, full, meta) {
    meta.title = full.epoch;
    return "(" + full.obj_ra.toFixed(3) + "," + full.obj_dec.toFixed(3) + ")";
  }

  function reConstructParameter(data) {
    var parms = 'draw=' + data.draw;
    parms = parms + '&length=' + data.length;
    parms = parms + '&start=' + data.start;
    return parms;
  }

  Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
      "M+": this.getMonth() + 1, //月份 
      "d+": this.getDate(), //日 
      "h+": this.getHours(), //小时 
      "m+": this.getMinutes(), //分 
      "s+": this.getSeconds(), //秒 
      "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
      "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt))
      fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
      if (new RegExp("(" + k + ")").test(fmt))
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
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
  
  function updateFalse(soId) {

    var gwacRootURL = $("#gwacRootURL").val();
    var url = gwacRootURL + "/update-science-object.action";
    var formData = "isTrue=false&soId=" + soId;
    console.log(formData);
    console.log(url);
    $.post(url, formData,
            function (data) {
              console.log(data);
            }, "json");
  }

