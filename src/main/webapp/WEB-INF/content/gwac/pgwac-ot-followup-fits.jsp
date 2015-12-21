<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ch"> 
  <head>  
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content=",国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>

    <title>OT-<s:property value="otName"/>-后随FITS图</title>

    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/js9/js9support.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/js9/js9.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/js9/js9support.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/js9/js9.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/js9/js9plugins.js"></script>

    <style type="text/css">

      /*body{ text-align:center}*/ 

      #main{
        width: 900px;
        background-color: #fff;
        margin:0 auto;
      }
      #JS9, #JS9Menubar{
        float: left;
        text-align:left;
      }
      #navi{
        float:right;
        width: 380px;
      }
      #navi a{
        text-decoration: none;
        color: black;
      }

      .ImexamContainer .JS9PluginContainer {
        border: 1px solid grey;
      }
    </style>
  </head>
  <body>
    <div id="main">
      <div class="JS9Menubar" id="JS9Menubar"></div>
      <div id="navi">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
        <p id="title"></p>
        <p id="imageNumber">显示第<span style="font-weight:bold;font-size: 14px;">1</span>帧，共<span id='totalImage'></span>帧</p>
        <p id="start"><a href="#" id="showStart">起始帧</a></p>
        <p id="end"><a href="#" id="showEnd">结束帧</a></p>
        <p id="before"><a href="#" id="showBefore">上一帧</a></p>
        <p id="next"><a href="#" id="showNext">下一帧</a></p>
        <p style="color:red;">在图中按住鼠标左键拖动调节对比度，鼠标左键不要超出图的范围，如果依然不够清楚，可以在<span style="font-weight: bold;color:#990033;">Scale菜单</span>中选择其他调节算法。</p>
        <p style="color:red;">缩放请在<span style="font-weight: bold;color:#990033;">Zoom菜单</span>中选择。</p>
      </div>
      <div class="JS9" id="JS9"></div>

    </div>
    <script type="text/javascript">

      $(function() {
        var drawRadius = 10;
        var count = 1;
        var curNum = 1;
        var startNum = 1;
        var endNum = 0;
        var curFitsName = "";
        var fuots;
        var fitsList;
        var curFits;
        getFollowupFitsList();
        $("#showStart").click(showFits('start'));
        $("#showEnd").click(showFits('end'));
        $("#showBefore").click(showFits('before'));
        $("#showNext").click(showFits('next'));
        $("#totalImage").val(endNum)

        function getFollowupFitsList() {
          var gwacRootURL = $("#gwacRootURL").val();
          var otName = getUrlParameter("otName");
          var url = gwacRootURL + "/get-followup-fits-list.action?otName=" + otName;
          $.get(url, showFitsList, "json");
        }

        function showFitsList(data) {
          fuots = data.fuots;
          fitsList = data.fitsList;
          endNum = fitsList.length;
          showFits('start');
        }

        function showFits(command) {
          if (endNum === 0) {
            return false;
          }
          var url = '';
          if (command === 'start') {
            curNum = 1;
          } else if (command === 'end') {
            curNum = endNum;
          } else if (command === 'before') {
            if (curNum === 1) {
              curNum = endNum;
            } else {
              curNum = curNum - 1;
            }
          } else if (command === 'next') {
            if (curNum === endNum) {
              curNum = 1;
            } else {
              curNum = curNum + 1;
            }
          }
          curFits = fitsList[curNum - 1];
          url = curFits.path + curFits.fileName;
          $("#title").html(curFits.fileName);
          $("#imageNumber span").html(curNum);
          curFitsName = curFits.fileName;

          setJs9Fits(url);
          return false;
        }

        function setJs9Fits(url) {
          var option = {zoom: 'toFit', colormap: 'grey', contrast: 7.0, bias: 0.62109375, scale: "log"};
          JS9.Load(url, option);
//                JS9.Load(url);
          setJs9Parameter();
        }

        function setJs9Parameter() {
          if (JS9.GetLoadStatus(curFitsName) === "complete") {
            var records = curFits.records;
            var totalx = 0.0, totaly = 0.0;
            var size = records.length;
            $.each(records, function(i, item) {
              var circlex = item.x;
              var circley = item.y;
              var textx = item.x + drawRadius + 5;
              var texty = item.y + drawRadius + 5;
              var fuoTypeName = "ERROR";
              totalx += circlex;
              totaly += circley;
              $.each(fuots, function(i, item) {
                if (item.fuoTypeId === item.fuoTypeId) {
                  fuoTypeName = item.fuoTypeName;
                }
              });
              JS9.AddRegions({shape: 'circle', x: circlex, y: circley, radius: drawRadius});
              JS9.AddRegions({shape: 'text', x: textx, y: texty, text: fuoTypeName});
              console.log("circlex=" + circlex + ",circley" + circley);
            });
            JS9.SetPan(totalx / size, totaly / size);
          } else {
            setTimeout(setJs9Parameter, 100);
          }
        }
      });

      function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                sURLVariables = sPageURL.split('&'),
                sParameterName,
                i;

        for (i = 0; i < sURLVariables.length; i++) {
          sParameterName = sURLVariables[i].split('=');

          if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
          }
        }
      }
    </script>
  </body>
</html>