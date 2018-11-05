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
                    <option value="G011">G011</option>
                    <option value="G012">G012</option>
                    <option value="G013">G013</option>
                    <option value="G014">G014</option>
                    <option value="G015">G015</option>
                    <option value="G021">G021</option>
                    <option value="G022">G022</option>
                    <option value="G023">G023</option>
                    <option value="G024">G024</option>
                    <option value="G025">G025</option>
                    <option value="G031">G031</option>
                    <option value="G032">G032</option>
                    <option value="G033">G033</option>
                    <option value="G034">G034</option>
                    <option value="G035">G035</option>
                    <option value="G041">G041</option>
                    <option value="G042">G042</option>
                    <option value="G043">G043</option>
                    <option value="G044">G044</option>
                    <option value="G045">G045</option>
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
              
              if(reqNum===1){
                $('#container').waterfall(toption);
              }else{
                $('#container').waterfall('removeItems', $('.item'));
              }
              reqNum=reqNum+1;
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
                data: 'dataType=ccdList&dateStr='+dateStr,
                async: false,
                dataType: 'json',
                success: function (data) {
                  var ccdStr = data.rstData;
                  var dateS = ccdStr.split(",");
                  $.each(dateS, function (i, item) {
                    $('#ccdList').append($('<option>', {
                      value: item,
                      text: item
                    }));
                  });
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