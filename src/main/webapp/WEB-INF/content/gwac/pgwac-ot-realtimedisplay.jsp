<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>OT分布实时概览图-XY</title>
    <link href="<%=request.getContextPath()%>/styles/examples.css" rel="stylesheet" type="text/css">
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/excanvas.compiled.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.flot.js"></script>
    <script type="text/javascript">

      $(function() {

        var option1 = {
          points: {
            show: true
          },
          grid: {
            hoverable: true,
            clickable: true
          },
          selection: {
            mode: "xy"
          },
          xaxis: {min: 0, max: 3200},
          yaxis: {min: 0, max: 3200}
        };

        var plot = [];
        var otLv1 = [];
        var otLv2 = [];
        var otLv2Cur = [];
        var ot1 = [];
        var ot2 = [];
        var ot2cur = [];
        var ot1Label = [];
        var ot2Label = [];
        var ot2curLabel = [];
        var drawData = [];
        for (var m = 0; m < 12; m++) {
          ot1[m] = [];
          ot2[m] = [];
          ot2cur[m] = [];
          ot1Label[m] = [];
          ot2Label[m] = [];
          ot2curLabel[m] = [];
        }
        var dataurl = "<%=request.getContextPath()%>/get-ot-xy-list.action";
        function onDataReceived(result) {
          otLv1 = result.otLv1;
          otLv2 = result.otLv2;
          otLv2Cur = result.otLv2Cur;
          for (var i = 0; i < otLv1.length; i++) {
            ot1[otLv1[i].dpmId].push([otLv1[i].XTemp, otLv1[i].YTemp]);
            ot1Label[otLv1[i].dpmId].push([otLv1[i].dpmId, otLv1[i].ffNumber]);
          }
          for (var i = 0; i < otLv2.length; i++) {
            ot2[otLv2[i].dpmId].push([otLv2[i].xtemp, otLv2[i].ytemp]);
            ot2Label[otLv2[i].dpmId].push([otLv2[i].identify, otLv2[i].name]);
          }
          for (var i = 0; i < otLv2Cur.length; i++) {
            ot2cur[otLv2Cur[i].dpmId].push([otLv2Cur[i].xtemp, otLv2Cur[i].ytemp]);
            ot2curLabel[otLv2Cur[i].dpmId].push([otLv2Cur[i].identify, otLv2Cur[i].name]);
          }
          for (var m = 0; m < 12; m++) {
            drawData[m] = [
              {
                label: "ot1",
                data: ot1[m],
                color: '#71c73e',
                points: {show: true, radius: 1}
              },
              {
                label: "ot2",
                data: ot2[m],
                color: '#77b7c5',
                points: {show: true, radius: 2}
              },
              {
                label: "ot2-cur",
                data: ot2cur[m],
                color: 'purple',
                points: {show: true, radius: 3}
              }
            ];
            plotAndBind(m);
          }
        }

        $.ajax({
          url: dataurl,
          type: "GET",
          dataType: "json",
          success: onDataReceived
        });


        $("<div id='tooltip'></div>").css({
          position: "absolute",
          display: "none",
          border: "1px solid #fdd",
          padding: "2px",
          "background-color": "#fee",
          opacity: 0.80
        }).appendTo("body");

        function plotAndBind(number) {
          var id = "#placeholder" + (number + 1);
          plot[number] = $.plot(id, drawData[number], option1);

          $(id).bind("plothover", function(event, pos, item) {
            if (item) {
              var x = item.datapoint[0].toFixed(2);
              var y = item.datapoint[1].toFixed(2);
              if (item.series.label === "ot1") {
                var mName = "";
                if (ot1Label[number][item.dataIndex][0] < 10) {
                  mName = "M0" + ot1Label[number][item.dataIndex][0];
                } else {
                  mName = "M" + ot1Label[number][item.dataIndex][0];
                }
                $("#tooltip").html(mName + "-" + ot1Label[number][item.dataIndex][1] + " (" + x + ", " + y + ")")
                        .css({top: item.pageY + 5, left: item.pageX + 5})
                        .fadeIn(200);
              } else if (item.series.label === "ot2") {
                $("#tooltip").html(ot2Label[number][item.dataIndex][0] + " (" + x + ", " + y + ")")
                        .css({top: item.pageY + 5, left: item.pageX + 5})
                        .fadeIn(200);
              } else if (item.series.label === "ot2-cur") {
                $("#tooltip").html(ot2curLabel[number][item.dataIndex][0] + " (" + x + ", " + y + ")")
                        .css({top: item.pageY + 5, left: item.pageX + 5})
                        .fadeIn(200);
              }
            } else {
              $("#tooltip").hide();
            }
          });

          $(id).bind("plotclick", function(event, pos, item) {
            if (item) {
              if (item.series.label === "ot2") {
              openDialog(ot2Label[number][item.dataIndex][1]);
              } else if (item.series.label === "ot2-cur") {
              openDialog(ot2curLabel[number][item.dataIndex][1]);
              }
//              plot[number].highlight(item.series, item.datapoint);
            }
          });
        }

        function openDialog(otName) {
          openwindow("get-ot-image-list.action?otName=" + otName, '_blank', 800, 500, 800, 500);
          return false;
        }
        function openwindow(url, name, width, height, iWidth, iHeight)
        {
          var iTop = (window.screen.availHeight - 30 - iHeight) / 2;       //获得窗口的垂直位置;
          var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;           //获得窗口的水平位置;
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

    </script>
  </head>
  <body>

    <div id="content">
      <div class="demo-container">
        <div id="placeholder1" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder2" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder3" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder4" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder5" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder6" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder7" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder8" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder9" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder10" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder11" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
        <div id="placeholder12" class="demo-placeholder" style="float:left; width:23%; height: 30%; margin:5px;"></div>
      </div>
    </div>

  </body>
</html>