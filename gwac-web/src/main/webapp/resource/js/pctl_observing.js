
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();
  var obsPlanTable;

  initPage();
  loadObsPlanList();

  function initPage() {
    $("#genObsPlanBtn").click(ot2QueryBtnClick);
  }

  function ot2QueryBtnClick() {
    var formData = $("#genObsPlanForm").serialize() + "&opSn=0&groupId=1&gridId=0&fieldId=0&pairId=0&epoch=2000&opTime="
            + encodeURI(new Date().Format("yyyy-MM-dd hh:mm:ss"));
    var formUrl = $("#genObsPlanForm").attr('action');
    if (formData !== '') {
      var queryUrl = formUrl + "?timestamp=" + new Date().getTime() + "&" + formData;
      console.log(queryUrl);
      $.ajax({
        type: "post",
        url: formUrl + "?timestamp=" + new Date().getTime(),
        data: formData,
        async: false,
        success: function(data) {
          console.log(data);
        }
      });
    } else {
      console.log("please select valid observation plan parameters!");
    }
  }


  function loadObsPlanList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-observation-plan-list.action";
    console.log(queryUrl);
    obsPlanTable = $('#obs-plan-table').DataTable({
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
        {"data": "id"},
        {"data": "op_sn"},
        {"data": "op_time"},
        {"data": "unit_id"},
        {"data": "obs_type"},
        {"data": "field_id"},
        {"data": "ra"},
        {"data": "img_type"},
        {"data": "delay"},
        {"data": "frame_count"},
        {"data": "priority"},
        {"data": "begin_time"},
        {"data": "obj_id"},
        {"data": "obj_ra"},
        {"data": "pair_id"},
        {"data": "op_type"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "ID?",
          "render": formateRowNumber
        }, {
          "targets": 3,
          "data": "group-unit-id",
          "render": formateGroupUnitId
        }, {
          "targets": 5,
          "data": "grid-field-id",
          "render": formateGridFieldId
        }, {
          "targets": 6,
          "data": "position(ra,dec)",
          "render": formatePosition
        }, {
          "targets": 8,
          "data": "expusore-delay",
          "render": formateExpusore
        }, {
          "targets": 11,
          "data": "start-end-time",
          "render": formateStartEndTime
        }, {
          "targets": 13,
          "data": "position(ra,dec)",
          "render": formateObjPosition
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


  function formateRowNumber(data, type, full, meta) {
    return meta.row + 1;
  }

  /*full: json对象；meta：表格元素*/
  function formateGroupUnitId(data, type, full, meta) {
    return full.group_id + "-" + full.unit_id;
  }
  function formateGridFieldId(data, type, full, meta) {
    return full.grid_id + "-" + full.field_id;
  }
  function formatePosition(data, type, full, meta) {
    meta.title = full.epoch;
    return "(" + full.ra + "," + full.dec + ")";
  }
  function formateExpusore(data, type, full, meta) {
    return full.expusore_during + "(" + full.delay + ")";
  }
  function formateStartEndTime(data, type, full, meta) {
    var endTime = full.end_time;
    var beginTime = full.begin_time;
    if (beginTime === null) {
      beginTime = '_';
    }
    if (endTime !== null & endTime !== undefined && endTime.length > 0) {
      //endTime = endTime.substring(endTime.indexOf(" "));
      endTime = endTime
    } else {
      endTime = '_';
    }
    return beginTime + "至" + endTime;
  }
  //.toFixed(3)
  function formateObjPosition(data, type, full, meta) {
    var objRa = full.obj_ra === null ? '_' : full.obj_ra;
    var objDec = full.obj_dec === null ? '_' : full.obj_dec;
    var objError = full.obj_error === null ? '_' : full.obj_error;
    return "(" + objRa + "," + objDec + "," + objError + ")";
  }

  function reConstructParameter(data) {
    var parms = 'draw=' + data.draw;
    parms = parms + '&length=' + data.length;
    parms = parms + '&start=' + data.start;
    return parms;
  }

  Date.prototype.Format = function(fmt) { //author: meizz 
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
});

