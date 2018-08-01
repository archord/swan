<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/resource/js/plot/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/resource/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/resource/js/d3/topojson.min.js"></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/resource/js/d3/d3.geo.zoom.js"></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/resource/js/mot_timesequence_sphere_debug.js"></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/resource/js/shiftzoom.js" ></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery/jquery-ui.min.js"></script>
    <link href="${pageContext.request.contextPath}/resource/js/d3/maps.css" rel="stylesheet" type="text/css">
    <script>
      $(function() {
        var root = "${pageContext.request.contextPath}";
        var url = "get-mov-ot-sequence-list.action?dateStr=";
        var mainHeight = $("#main").height();
        var headerHeight = $("#header").height();
        $("#sphereDisplay").height(mainHeight - headerHeight - 10);

        var gwac = $.gwac("#sphereDisplay", root, url);
        gwac.loadSkyList();
        gwac.loadDpmList();
        gwac.loadDateStrList();
        loadData();


        $('#dynamicDrawOt1').change(function() {
          if ($(this).is(":checked")) {
            gwac.updateShowData();
            if (typeof (gwac.ot1DrawInterval) !== "undefined" && gwac.ot1DrawInterval !== null) {
              clearInterval(gwac.ot1DrawInterval);
            }
            gwac.ot1DrawInterval = setInterval(dynamicDrawOt1, gwac.playSpeed);
          } else {
            clearInterval(gwac.ot1DrawInterval);
          }
        });

        $('#obsDate').change(function() {
          loadData();
        });

        $('#movType').change(function() {
          gwac.updateShowData();
          //保证在选择类型时，currentFrame不增加，因为dynamicDrawOt1会对currentFrame增加playInterval
          gwac.currentFrame = gwac.startFrame + (gwac.currentFrame - gwac.startFrame - gwac.playInterval) % (gwac.endFrame - gwac.startFrame + 1);
          dynamicDrawOt1();
        });

        $('#changeView').click(function() {
          gwac.changeView(gwac);
        });

        function loadData() {

          var dateStr = $("#obsDate").val();
          if (dateStr !== '0') {

            if ($('#dynamicDrawOt1').attr("checked")) {
              $('#dynamicDrawOt1').attr("checked", false);
            }

            if (typeof (gwac.ot1DrawInterval) !== "undefined" && gwac.ot1DrawInterval !== null) {
              clearInterval(gwac.ot1DrawInterval);
            }

            var movUrl = root + "/" + url + dateStr;
//            console.log(movUrl);

            d3.json(movUrl, function(errors, reqData) {
              var motList = reqData.motList;
              var ot1List = reqData.ot1List;
              if (typeof (motList) !== "undefined" && motList !== null && motList !== ""
                      && typeof (ot1List) !== "undefined" && ot1List !== null && ot1List !== "") {
                gwac.parseData(reqData);
                gwac.draw();
                setTimeout(function() {
                  gwac.ot1DrawInterval = setInterval(dynamicDrawOt1, gwac.playSpeed);
                }, gwac.startAnimationDuration);

                if (!$('#dynamicDrawOt1').attr("checked")) {
                  $('#dynamicDrawOt1').attr("checked", true);
                }
              } else {
                console.log("Cannot find data on day " + dateStr + ".");
                alert("Cannot find data on day " + dateStr + ".");
              }
            });
          }
        }

        function dynamicDrawOt1() {
          gwac.currentFrame = gwac.startFrame + (gwac.currentFrame - gwac.startFrame + gwac.playInterval) % (gwac.endFrame - gwac.startFrame + 1);
          $('#currentFrame').val(gwac.currentFrame);
          if (gwac.currentFrame > 1) {
            gwac.svg.selectAll(".ot1").remove();
            gwac.svg.selectAll(".motLine").remove();
            gwac.svg.selectAll(".motPoint").remove();
          }
          gwac.drawOt1();
          gwac.drawMot();
        }

        $(window).resize(function() {
          var winWidth = $(window).width();
          var winHeight = $(window).height();
          $("#main").width(winWidth);
          $("#main").height(winHeight);
          $("#header").width(winWidth);
          $("#sphereDisplay").width(winWidth);
          $("#sphereDisplay").height(winHeight - $("#header").height() - 10);
          gwac.draw();
        });


        $("#dragDiv").draggable({
          cursor: "crosshair"
        });
        $("#imageShowArea").mouseover(function() {
          $("#dragDiv").draggable("disable");
        });
        $("#imageShowArea").mouseout(function() {
          $("#dragDiv").draggable("enable");
        });
        $("#closeDragDiv").click(function() {
          $("#dragDiv").hide();
        });
        shiftzoom.defaultCurpath = '${pageContext.request.contextPath}/resource/images/cursors/';

        $("#aheadButton").click(showAheadImg);
        $("#nextButton").click(showNextImg);

        function showAheadImg() {
          showOtOrigImg(-1);
        }
        function showNextImg() {
          showOtOrigImg(1);
        }

        function showOtOrigImg(interval) {
          //http://10.0.10.236/images/thumbnail/161003/M11/M6_11_161003_1_351020_0026_ccdimg.jpg
          var otImgUrl = $("#otImgShow_img").attr("src");
          if (typeof (otImgUrl) === "undefined" || otImgUrl === null) {
            otImgUrl = $("#otImgShow").attr("src");
          }
          if (typeof (otImgUrl) !== "undefined" && otImgUrl !== null) {
            var fname = otImgUrl.substring(otImgUrl.lastIndexOf("/") + 1);
            var fnamber = parseInt(fname.substring(22, 26)) + interval;
            var turl = otImgUrl.substring(0, otImgUrl.lastIndexOf("/") + 1);
            var lastfname = fname.substring(0, 22) + prefixInteger(fnamber, 4);
            var lasturl = turl + lastfname + "_ccdimg.jpg";

            var img = document.getElementById('otImgShow');
            $("#otCoordinate").html(lastfname + ".fit");
            $("#otImgShow").attr("src", lasturl);
            $("#otImgShow_img").attr("src", lasturl);
//            setTimeout(function() {
//              shiftzoom.zooming(img, 100);
//            }, 1000);
          } else {
            console.log("cannot find ot orign image!");
          }
        }

        function prefixInteger(num, length) {
          return (Array(length).join('0') + num).slice(-length);
        }

        function loadOTImg(src, imgX, imgY) {
          var img = document.getElementById('otImgShow');
          $("#otImgShow").attr("src", src);
          $("#otImgShow").load(function() {
            shiftzoom.add(img, {showcoords: true, relativecoords: true, zoom: 100});
          });
          setTimeout(function() {
            var img = document.getElementById('otImgShow');
            shiftzoom.moveto(img, parseInt(imgX), parseInt(imgY));
          }, 500);
          $("#dragDiv").show();
        }
      });

    </script>
    <style>
      /*@import url(${pageContext.request.contextPath}/resource/js/d3/maps.css);*/

      body{background-color: black;}
      path {fill: none;stroke-linejoin: round;}
      #main{width:100%;height: 100%;text-align: center;position: absolute; top: 0; left: 0;}
      #tooltip{position:absolute;z-index:10;visibility:hidden;color:white;}
      #header{width: 100%;height: 40px;font-size: 28px;text-align: center; color: white;padding-top: 5px;}
      #sphereDisplay{margin: auto;width:100%;height: 90%;}
      #toolbar {position: absolute;bottom: auto;right: 10px;top: 45px;left: auto;width: 180px;z-index: 10;text-align: left;color: white;font-size: 14px;}
      #toolbar label{display: block; margin: 3px 0; padding-left: 15px; text-indent: -15px; cursor: pointer;}
      #toolbar input[type='checkbox']{width: 14px;height: 14px;padding: 0;margin:0 5px 0 0;vertical-align: bottom;position: relative;top: -1px;*overflow: hidden; cursor: pointer;}
      .ot1-input{width: 80px;height: 20px; padding:0;margin:0 0 0 10px;border:none;background-color: transparent;outline: none;color:#fff;}
      #toolbar select{border: solid 1px #fff; background:transparent;}


      /*.graticule {fill: none;stroke: #636B62;stroke-width: .5px;stroke-dasharray: 2,2;}*/
      .graticule{stroke: #a9a9a9;stroke-width: 0.3px;}
      .sphere{stroke: #636B62;stroke-width: 1px;}
      .equator {stroke: #636B62;stroke-width: 1px;}
      .primemeridian {stroke: #636B62;stroke-width: 1px;}
      .origin{stroke: #636B62;stroke-width: 5px;fill: #636B62;}
      .ot1{stroke-width: 1px;}
      .ot1:hover{stroke-width: 2px;stroke: red;}
      .ot2{stroke: #993399;stroke-width: 3px;fill: #993399;}
      .ot2mch{stroke: #FFFF99;stroke-width: 3px;fill: #FFFF99;}
      .ot2cur{stroke: #FF33CC;stroke-width: 5px;fill: #FF33CC;}
      .motLine{stroke-width: 1px;}
      .motLine3{stroke-width: 0.5px;}
      .motPoint{stroke-width: 1px;}
      .motPoint:hover{stroke-width: 2px;stroke: red;}

      img.shiftzoom { visibility: hidden; }

      #aheadButton {
        margin-right:5px;
        float:right;
        border: 10px solid #999;
        border-right-color: #000;
        width: 0;
        height: 0;
      }
      #nextButton{
        float:right;
        border: 10px solid #999;
        border-left-color: #000;
        width: 0;
        height: 0;
      }
      #closeButton{float:right;width:21px; height:21px;}
    </style>

  </head>
  <body>
    <div id="main">
      <div id="header">OT分布实时概览图-天球坐标</div>
      <div id="sphereDisplay"></div>
      <div id="toolbar">
        <table style="border:0">
          <tr><td colspan="2"><input type="checkbox" checked="" id="dynamicDrawOt1"><span id="playot2">播放</span></td></tr>
          <tr><td width="70px">播放速度:</td><td><input type="text" id="playSpeed" class="ot1-input" value="400"/></td></tr>
          <tr><td>每次间隔:</td><td><input type="text" id="playInterval" class="ot1-input" value="1"/></td></tr>
          <tr><td>开始帧:</td><td><input type="text" id="startFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>当前帧:</td><td><input type="text" id="currentFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>结束帧:</td><td><input type="text" id="endFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>总帧数:</td><td><input type="text" id="totalFrame" class="ot1-input" value="1"/></td></tr>
          <tr><td>最少帧数:</td><td><input type="text" id="miniFrameNumber" class="ot1-input" value="1"/></td></tr>
          <tr><td>选择日期</td><td><select name="formqp.obsDate" id="obsDate" class="ot1-input" data-toggle="select">
                <option style="background-color:#000" value="0" selected>请选择</option>
              </select></td></tr>
          <tr><td>目标类别:</td><td><select id="movType" class="ot1-input" data-toggle="select">
                <option style="background-color:#000" value="0">所有类型</option>
                <option style="background-color:#000" value="1" selected>多帧单点</option>
                <option style="background-color:#000" value="2">多帧两点</option>
                <option style="background-color:#000" value="3">多帧多点</option>
                <option style="background-color:#000" value="4">一帧多点</option>
                <option style="background-color:#000" value="5">其他</option>
              </select></td></tr>
          <tr><td>选择天区:</td><td><select name="formqp.obsSky" id="obsSky" class="ot1-input" data-toggle="select">
                <option style="background-color:#000" value="0"  selected>请选择</option>
              </select></td></tr>
          <tr><td>选择CCD:</td><td><select name="formqp.obsCcd" id="obsCcd" class="ot1-input" data-toggle="select">
                <option style="background-color:#000" value="0" selected>请选择</option>
              </select></td></tr>
          <tr><td>边界左上:</td><td><input type="text" id="leftTopBound" class="ot1-input" value="60,60"/></td></tr>
          <tr><td>边界右下:</td><td><input type="text" id="rightBottomBound" class="ot1-input" value="70,70"/></td></tr>
          <tr><td colspan="2"><a href="javascript:void(0);" id="changeView" style="text-decoration:none;color:#fff;">切换视角</a>
              <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/></td></tr>
        </table>
      </div>
    </div>
    <div id="tooltip">a simple tooltip</div>

    <div id="dragDiv" style="position: absolute; top:150px;left:300px; width:410px; height:422px;padding-top: 6px;background: black; display: none;">
      <div style="background: #999999;width:400px; height:22px;text-align: center;margin:0px 5px 0px 5px;">
        <span id="otCoordinate" style="font-size: 14px; text-decoration: none;font-style: normal;color: white;font-family:'Times New Roman',Georgia,Serif;">坐标信息</span>
        <div id="closeButton">
          <img id="closeDragDiv" src="${pageContext.request.contextPath}/resource/images/close3.png" style="width:21px; height:21px;" title="点击关闭"/></div>
        <div id="nextButton" title="下一幅图"></div>
        <div id="aheadButton" title="上一幅图"></div>
      </div>
      <div id="imageShowArea" style="position:relative;float:left;width:400px; height:400px; border:0px; background: black; padding: 0px 5px 5px 5px;"> 
        <div style="width:400px; height:400px; background: url(${pageContext.request.contextPath}/resource/images/indicator.gif) 50% 50% no-repeat;border:0px;">
          <img id="otImgShow" width="400" height="400" alt="ot image" border="0" />
        </div>
        <div style="position:absolute; top:193px; left:196px; width:15px; height:15px;border:1px solid #F00;border-radius: 3px;  z-index: 10000"></div>
      </div>
    </div>
  </body>
</html>