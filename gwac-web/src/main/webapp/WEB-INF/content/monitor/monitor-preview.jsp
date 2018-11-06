<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="ch"> 
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台,svom,gwac,望远镜阵列,光变分类" />
    <meta http-equiv="description" content="GWAC参数监控" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>GWAC历史图像预览- <s:text name="showcase.version"/></title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/watefall/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/watefall/css/waterfall.css">
    <link href="${pageContext.request.contextPath}/resource/css/gwac-ui.css" rel="stylesheet">

    <style type="text/css">
      .container-fluid{
        margin: auto;
        text-align: center;
        padding: 20px;
      }

    </style>

  </head>
  <body>
    <div class="container-fluid">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>
      <div >
        <select name="dateList" id="dateList" class="form-control select select-primary" data-toggle="select">
          <option value="" selected>未选择</option>
        </select>&nbsp;
        <select name="ccdList" id="ccdList" class="form-control select select-primary" data-toggle="select">
          <option value="" selected>未选择</option>
        </select>&nbsp;
        <input type="button" value="加载" class="btn btn-primary" id="imgPreBtn"/>
      </div>
    </div>
    <div id="container"></div>
    <script type="text/x-handlebars-template" id="waterfall-tpl">
      {{#result}}
      <div class="item" style="text-align:center">
      <a href="{{fullUrl}}"  target="_blank"><img src="{{preUrl}}" width="{{width}}" height="{{height}}" /></a>
      <span style="font-size:16px">{{imgName}}</span>
    </div>
     {{/result}}
    </script>
    <script src="${pageContext.request.contextPath}/resource/watefall/js/libs/jquery/jquery.js"></script>
<script src="${pageContext.request.contextPath}/resource/watefall/js/libs/handlebars/handlebars.js"></script>
<script src="${pageContext.request.contextPath}/resource/watefall/js/waterfall.js"></script>
<script>
var reqNum = 1;
var toption = {
  itemCls: 'item',
  colWidth: 430,
  gutterWidth: 15,
  gutterHeight: 15,
  checkImagesLoaded: false,
  path: function (page) {
    var dateStr = $('#dateList').val();
    var ccdStr = $('#ccdList').val();
    var turl = '${pageContext.request.contextPath}/image-preview-json.action?dataType=imageList&page=' + page + "&dateStr=" + dateStr + "&ccdStr=" + ccdStr;
    return turl;
  },
  callbacks: {
    renderData: function (data, dataType) {
      var tpl, template;
      if (dataType === 'json' || dataType === 'jsonp') { // json or jsonp format
        tpl = $('#waterfall-tpl').html();
        template = Handlebars.compile(tpl);
        var tdataObj = eval('(' + data.rstData + ')');
        //console.log(tdataObj);
        return template(tdataObj);
      } else { // html format
        return data;
      }
    }
  }
}
function reloadImgs() {

  var dateStr = $('#dateList').val();
  var ccdStr = $('#ccdList').val();
  if (dateStr !== "" && ccdStr !== "") {
    if (reqNum === 1) {
      $('#container').waterfall(toption);
    } else {
      $('#container').waterfall('removeItems', $('.item'));
    }
    reqNum = reqNum + 1;
  }
}
function loadDateList() {
  var gwacRootURL = $("#gwacRootURL").val();
  var queryUrl = gwacRootURL + "/image-preview-json.action";
  $('#dateList').find('option').remove();
  $.ajax({
    type: "get",
    url: queryUrl,
    data: 'dataType=dateList',
    async: false,
    dataType: 'json',
    success: function (data) {
      var dateStr = data.rstData;
      var dateS = dateStr.split(",");
      dateS.sort();
      dateS.reverse()
      $('#dateList').append($('<option>', {
        value: "",
        text: "未选择"
      }));
      $.each(dateS, function (i, item) {
        $('#dateList').append($('<option>', {
          value: item,
          text: item
        }));
      });
    }
  });
}
function loadCCDList() {
  var dateStr = $('#dateList').val();
  var gwacRootURL = $("#gwacRootURL").val();
  var queryUrl = gwacRootURL + "/image-preview-json.action";
  $('#ccdList').find('option').remove();
  $.ajax({
    type: "get",
    url: queryUrl,
    data: 'dataType=ccdList&dateStr=' + dateStr,
    async: false,
    dataType: 'json',
    success: function (data) {
      var ccdStr = data.rstData;
      if (ccdStr.length > 0) {
        var dateS = ccdStr.split(",");
        $.each(dateS, function (i, item) {
          $('#ccdList').append($('<option>', {
            value: item,
            text: item
          }));
        });
      } else {
        $('#ccdList').append($('<option>', {
          value: "",
          text: "未选择"
        }));
      }
    }
  });
}
$(function () {
  loadDateList();
//$('#container').waterfall(toption);
  $('#dateList').change(loadCCDList);
//$('#ccdList').change(reloadImgs);
  $("#imgPreBtn").click(reloadImgs);
});
</script>
</body>
</html>