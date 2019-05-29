
$(function () {
  var obsPlanTable;

  loadObsPlanList();
  $('#sciObjType').change(onTelescopeChange);
  $('#addFuObsBtn').click(addFollowUpObs);
  $('#editFuObsBtn').click(editFollowUpObs);
  
  function onTelescopeChange() {
    var formData = $("#getUnDonePlanForm").serialize();
    var queryUrl = $("#getUnDonePlanForm").attr('action') + "?timestamp=" + new Date().getTime() + "&" + formData;
    obsPlanTable.ajax.url(queryUrl).load();
  }

  function addFollowUpObs() {
    var gwacRootURL = $("#gwacRootURL").val();
    var url = gwacRootURL + "/science/science-object-final-add.action";
    //console.log(url);
    openwindow(url, '_blank', 830, 460, 830, 460);
  }

  function editFollowUpObs() {
    var sofIds = [];
    $('input[name="sofIds"]').each(function () {
      if ($(this).is(':checked')) {
        sofIds.push($(this).val());
      }
    });
    if (sofIds.length !== 1) {
      alert("请选择一个任务，且只能选择一个后随任务！");
    } else {
      var gwacRootURL = $("#gwacRootURL").val();
      var url = gwacRootURL + "/science/science-object-final-add.action?sofId=" + sofIds[0];
      //console.log(url);
      openwindow(url, '_blank', 830, 460, 830, 460);
    }
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
        {"data": "sof_id"},
        {"data": "name"}, //begin_time trigger_time
        {"data": "discovery_time"},
        {"data": "ra"},
        {"data": "mag_detect"},
        {"data": "type"},
        {"data": "duty_scientist"},
        {"data": "source"},
        {"data": "comments"}
      ],
      "columnDefs": [{
          "targets": 0,
          "render": formateRowNumber,
          "width": "10px"
        }, {
          "targets": 1,
          "render": formateOt2,
          "width": "120px"
        }, {
          "targets": 2,
          "render": formateTime,
          "width": "135px"
        }, {
          "targets": 3,
          "render": formatePosition,
          "width": "180px"
        }, {
          "targets": 4,
          "render": formateMag,
          "width": "80px"
        }, {
          "targets": 5,
          "width": "20px"
        }, {
          "targets": 6,
          "width": "60px"
        }, {
          "targets": 7,
          "width": "60px"
        }, {
          "targets": 8,
          "render": formateComment
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

  function formateComment(data, type, full, meta) {
    var show = data;
//    var showLen = 60;
//    if(data!==null && data.length>showLen){
//      show = data.substring(0,showLen) + "...";
//      show = "<span title='" + data + "'>" + show + "</span>";
//    }
    return show;
  }

  function formateRowNumber(data, type, full, meta) {
//    return "<span>" +(meta.row + 1)+"<input type='checkbox'/></span>";
    return "<span><input name='sofIds' type='checkbox' value='" + data + "'/></span>";
  }
  /*full: json对象；meta：表格元素*/
  function formateOt2(data, type, full, meta) {
    var content = "";
    var name = full.name;
    var gwacRootURL = $("#gwacRootURL").val();
    var searchUrl = gwacRootURL + "/gwac/pgwac-ot-detail2.action?otName=" + name;
    content = "<a href='" + searchUrl + "' title='点击打开OT2详细页面' target='_blank'>" + name + "</a>";
    return content;
  }
  function formatePosition(data, type, full, meta) {
    return "(" + full.ra + "," + full.dec + ")";
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
    return "<span title='后随时间：" + full.trigger_time + "'>" + tstr + "</span>";
  }
  //.toFixed(3)
  function formateMag(data, type, full, meta) {
    var mag_detect = full.mag_detect === null ? '_' : full.mag_detect;
    var mag_catalog = full.mag_catalog === null ? '_' : full.mag_catalog;
    var mag_absolute = full.mag_absolute === null ? '_' : full.mag_absolute;
    var amplitude = full.amplitude === null ? '_' : full.amplitude;
    
    var content = mag_detect +"("+amplitude+")";
    var title = "magCatalog:" + mag_catalog + ";magAbsolute:" + mag_absolute;
    return "<span title='" + title + "'>" + content + "</span>";
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

