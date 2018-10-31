
$(function () {
  var obsPlanTable;

  loadObsPlanList();
  $('#executeStatus').change(onTelescopeChange);
  $('#triggerType').change(onTelescopeChange);
  $('#processResult').change(onTelescopeChange);
  $('#addFuObsBtn').click(addFollowUpObs);
  $('#editFuObsBtn').click(editFollowUpObs);
  $('#delFuObsBtn').click(delFollowUpObs);
  $('#folowupParmSetBtn').click(folowupParmSet);

  function onTelescopeChange() {
    var formData = $("#getUnDonePlanForm").serialize();
    var queryUrl = $("#getUnDonePlanForm").attr('action') + "?timestamp=" + new Date().getTime() + "&" + formData;
    obsPlanTable.ajax.url(queryUrl).load();
  }

  function addFollowUpObs() {
    var gwacRootURL = $("#gwacRootURL").val();
    var url = gwacRootURL + "/followup/followup-add.action";
    //console.log(url);
    openwindow(url, '_blank', 830, 570, 830, 570);
  }

  function editFollowUpObs() {
    var opIds = [];
    $('input[name="opIds"]').each(function () {
      if ($(this).is(':checked')) {
        opIds.push($(this).val());
      }
    });
    if (opIds.length !== 1) {
      alert("请选择一个任务，且只能选择一个后随任务！")
    } else {
      var gwacRootURL = $("#gwacRootURL").val();
      var url = gwacRootURL + "/followup/followup-add.action?foId=" + opIds[0];
      //console.log(url);
      openwindow(url, '_blank', 830, 570, 830, 570);
    }
  }

  function delFollowUpObs() {
    var opIds = [];
    $('input[name="opIds"]').each(function () {
      if ($(this).is(':checked')) {
        opIds.push($(this).val());
      }
    });
    if (opIds.length === 0) {
      alert("请至少选择一个任务！")
    } else {
      var url = $("#gwacRootURL").val() + "/followup/deleteOtFollowUp.action?foIds=" + opIds;
      console.log(url);
      $.ajax({
        type: "get",
        url: url,
        data: "p1=1",
        async: true,
        success: function (data) {
          console.log(data);
          alert("删除成功！");
        }
      });
    }
  }

  function folowupParmSet() {
    var url = $("#gwacRootURL").val() + "/followup/followup-parm.action";
    openwindow(url, '_blank', 830, 540, 830, 540);
  }

  function loadObsPlanList() {
    var formData = $("#getUnDonePlanForm").serialize();
    var queryUrl = $("#getUnDonePlanForm").attr('action') + "?timestamp=" + new Date().getTime() + "&" + formData;
    obsPlanTable = $('#obs-plan-table').DataTable({
      serverSide: true,
      "deferRender": true,
      "processing": true,
      "searching": false,
      "lengthChange": true,
      "pageLength": 8,
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
        {"data": "fo_id"},
        {"data": "begin_time"}, //begin_time trigger_time
        {"data": "fo_name"},
        {"data": "ot_id"},
        {"data": "telescope_id"},
        {"data": "ra"},
        {"data": "expose_duration"},
        {"data": "frame_count"},
        {"data": "filter"},
        {"data": "priority"},
        {"data": "trigger_type"},
        {"data": "user_name"},
        {"data": "execute_status"},
        {"data": "process_result"},
        {"data": "trigger_time"}
      ],
      "columnDefs": [{
          "targets": 0,
          "data": "ID?",
          "render": formateRowNumber
        }, {
          "targets": 1,
          "data": "begin_time",
          "render": formateTime
        }, {
          "targets": 2,
          "data": "fo_name",
          "render": formateFollowName
        }, {
          "targets": 3,
          "data": "ot2_name",
          "render": formateOt2
        }, {
          "targets": 4,
          "data": "telescope",
          "render": formateTelescope
        }, {
          "targets": 5,
          "data": "position(ra,dec)",
          "render": formatePosition
        }, {
          "targets": 10,
          "data": "triggerType",
          "render": formateTriggerType
        }, {
          "targets": 12,
          "data": "executeStatus",
          "render": formateExecuteStatus
        }, {
          "targets": 13,
          "data": "executeStatus",
          "render": formateProcessResult
        }, {
          "targets": 14,
          "data": "triggerTime",
          "render": formateTime
        }],
      "language": {
        "lengthMenu": '显示 <select>' +
                '<option value="5">5</option>' +
                '<option value="8">8</option>' +
                '<option value="16">16</option>' +
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
//    return "<span>" +(meta.row + 1)+"<input type='checkbox'/></span>";
    return "<span><input name='opIds' type='checkbox' value='" + data + "'/></span>";
  }

  function formateRaDec(data, type, full, meta) {
    var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=5&Radius.unit=arcmin&submit=submit%20query&Coord=";
    searchUrl += full.raD + "%20" + full.decD;
    return "<a href='" + searchUrl + "' title='点击在simbad搜寻OT对应坐标' target='_blank'>" + data + "</a>";
  }
  /*full: json对象；meta：表格元素*/
  function formateOt2(data, type, full, meta) {
    var content = "";
    if (full.fo_name.trim().length === 18 && full.fo_name[7] === '_') {//fo_name在数据库中定长为20, G180620_C00678_001
      var name = full.fo_name.substring(0, 14);
      var gwacRootURL = $("#gwacRootURL").val();
      var searchUrl = gwacRootURL + "/gwac/pgwac-ot-detail2.action?otName=" + name;
      content = "<a href='" + searchUrl + "' title='点击打开OT2详细页面' target='_blank'>" + name + "</a>";
    }
    return content;
  }
  function formateFollowName(data, type, full, meta) {
    var gwacRootURL = $("#gwacRootURL").val();
    var searchUrl = gwacRootURL + "/followup/followup-detail.action?foName=" + data;
    var content = "<a href='" + searchUrl + "' title='点击打开后随详细页面' target='_blank'>" + data + "</a>";
    return content;
  }

  /*full: json对象；meta：表格元素*/
  function formateTelescope(data, type, full, meta) {
    var name = "60公分";
    if (full.telescope_id === 2) {
      name = "30公分";
    }
    return name;
  }
  function formateTriggerType(data, type, full, meta) {
    var name = "未知";
    if (data === '0') {
      name = "AUTO";
    } else if (data === '1') {
      name = "MANUAL";
    } else if (data === '2') {
      name = "PLANNING";
    }
    return name;
  }
  function formateProcessResult(data, type, full, meta) {
    var name = "未处理";
    if (data === '0') {
      name = "未处理";
    } else if (data === '1') {
      name = "MINIOT";
    } else if (data === '2') {
      name = "CATAS";
    } else if (data === '3') {
      name = "NEWOT";
    }
    return name;
  }
  function formateExecuteStatus(data, type, full, meta) {
    var name = "未知";
    if (data === '0') {
      name = "未执行";
    } else if (data === '1') {
      name = "已执行";
    } else if (data === '2') {
      name = "过时";
    } else if (data === '3') {
      name = "删除";
    } else if (data === '4') {
      name = "已发送";
    }
    return name;
  }
  function formatePosition(data, type, full, meta) {
    meta.title = full.epoch;
    return "(" + full.ra.toFixed(3) + "," + full.dec.toFixed(3) + ")";
  }
  function formateExpusore(data, type, full, meta) {
    return full.expusore_during + "(" + full.delay + ")";
  }
  function formateTime(data, type, full, meta) {
    var tstr = "";
    if (data !== null & data !== undefined && data.length > 0) {
      if (data.indexOf(".") > -1) {
        tstr = data.substring(0, data.indexOf("."));
      } else {
        tstr = data;
      }
    } else {
      tstr = '_';
    }
    return "<span title='结束时间：" + full.end_time + "'>" + tstr + "</span>";
  }
  //.toFixed(3)
  function formateObjPosition(data, type, full, meta) {
    var objRa1 = full.obj_ra === null ? '_' : full.obj_ra.toFixed(2);
    var objDec1 = full.obj_dec === null ? '_' : full.obj_dec.toFixed(2);
    var objRa2 = full.obj_ra === null ? '_' : full.obj_ra;
    var objDec2 = full.obj_dec === null ? '_' : full.obj_dec;
    var objError = full.obj_error === null ? '_' : full.obj_error;
    var content = "(" + objRa1 + "," + objDec1 + ")";
    var title = "(" + objRa2 + "," + objDec2 + "," + objError + ")";
    return "<span title='(" + title + ")'>" + content + "</span>";
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

