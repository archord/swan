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
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>OT-<s:property value="otName"/>-FITS切图</title>

    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resource/js/js9/js9support.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resource/js/js9/js9.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/js9/js9support.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/js9/js9.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/js9/js9plugins.js"></script>

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
        <input type="hidden" id="ffcPath" value="<s:property value="ffcPath"/>"/>
        <input type="hidden" id="ffcListStr" value="<s:property value="ffcListStr"/>"/>
        <input type="hidden" id="ffcrStorePath" value="<s:property value="ffcrStorePath"/>"/>
        <input type="hidden" id="ffcrName" value="<s:property value="ffcrName"/>"/>
        <input type="hidden" id="totalImage" value="<s:property value="totalImage"/>"/>
        <p id="title"></p>
        <p id="imageNumber">显示第<span style="font-weight:bold;font-size: 14px;">1</span>帧，共<s:property value="totalImage"/>帧</p>
        <p id="start"><a href="#" onClick="return showFits('start');">起始帧</a></p>
        <p id="end"><a href="#" onClick="return showFits('end');">结束帧</a></p>
        <p id="before"><a href="#" onClick="return showFits('before');">上一帧</a></p>
        <p id="next"><a href="#" onClick="return showFits('next');">下一帧</a></p>
        <p id="template"><a href="#" onClick="return showFits('template');">显示模板</a></p>
        <p style="color:red;">在图中按住鼠标左键拖动调节对比度，鼠标左键不要超出图的范围，如果依然不够清楚，可以在Scale菜单中选择其他调节算法。</p>
      </div>
      <div class="JS9" id="JS9"></div>

    </div>
    <script type="text/javascript">

      var count = 1;
      var ffcPath = $("#ffcPath").val();
      var ffcListStr = $("#ffcListStr").val();
      var ffcrPath = $("#ffcrStorePath").val();
      var ffcrName = $("#ffcrName").val();
      var curNum = 1;
      var startNum = 1;
      var endNum = $("#totalImage").val();
      var curFitsName = "";

//            console.log(ffcPath);
//            console.log(ffcListStr);
//            console.log(ffcrPath);
//            console.log(ffcrName);

      var ffcList = [];
      if (ffcListStr !== "") {
        ffcList = ffcListStr.substring(0, ffcListStr.length - 1).split(',');
      }
      var ffcr = $("#ffcrStorePath").val();

      showFits('start');

      function showFits(command) {
        if (endNum === 0 || ffcList.length === 0) {
          return false;
        }
        var url = '';
        if (command === 'template') {
          url = ffcrPath + ffcrName;
          $("#title").html(ffcrName);
          curFitsName = ffcrName;
        } else {
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
          url = ffcPath + ffcList[curNum - 1];
          $("#title").html(ffcList[curNum - 1]);
          $("#imageNumber span").html(curNum);
          curFitsName = ffcList[curNum - 1];
        }
        setJs9Fits(url);
        return false;
      }

      function setJs9Fits(url) {
        var option = {zoom: 'toFit', colormap: 'grey', contrast: 7.0, bias: 0.4, scale: "log"};
        JS9.Load(url, option);
//                JS9.Load(url);
        setJs9Parameter();
      }

      function setJs9Parameter() {
        if (JS9.GetLoadStatus(curFitsName) === "complete") {
//                    JS9.SetZoom(4);
//                    JS9.SetColormap('grey', 7.0, 0.3);
//                    JS9.SetScale('linear');
          JS9.AddRegions({shape: 'circle', x: 51, y: 51, radius: 5});
        } else {
          setTimeout(setJs9Parameter, 100);
        }
      }
    </script>
  </body>
</html>