<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>OT分布实时概览图-天球坐标</title>
    <link href="<%=request.getContextPath()%>/styles/examples.css" rel="stylesheet" type="text/css">
    <!--[if lte IE 8]><script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/excanvas.compiled.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.flot.js"></script>
    <script type="text/javascript">

      $(function() {

        var option1 = {
          series: {
            shadowSize: 0	// Drawing is faster without shadows
          },
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
          xaxis: {min: 0, max: 360},
          yaxis: {min: -20, max: 90}
        };

        var reqNum = 1;
        var plot = [];
        var ot1 = [];
        var ot2 = [];
        var ot2cur = [];
        var ot1Label = [];
        var ot2Label = [];
        var ot2curLabel = [];
        var drawData = [];
        var ot2curInterval = null;
        var intervalTime = 1500;
        var requestTime = 15000;

        var dataurl = "<%=request.getContextPath()%>/get-ot-xy-list.action";
        function onDataReceived(result) {
          var otLv1 = result.otLv1;
          var otLv2 = result.otLv2;
          var otLv2Cur = result.otLv2Cur;
          while (ot1.length > 0) {
            ot1.pop();
          }
          while (ot1Label.length > 0) {
            ot1Label.pop();
          }
          while (ot2.length > 0) {
            ot2.pop();
          }
          while (ot2Label.length > 0) {
            ot2Label.pop();
          }
          while (ot2cur.length > 0) {
            ot2cur.pop();
          }
          while (ot2curLabel.length > 0) {
            ot2curLabel.pop();
          }
          for (var i = 0; i < otLv1.length; i++) {
            ot1.push([otLv1[i].raD, otLv1[i].decD]);
            ot1Label.push([otLv1[i].dpmId, otLv1[i].ffNumber]);
          }
          for (var i = 0; i < otLv2.length; i++) {
            ot2.push([otLv2[i].ra, otLv2[i].dec]);
            ot2Label.push([otLv2[i].dpmId, otLv2[i].lastFfNumber, otLv2[i].name]);
          }
          for (var i = 0; i < otLv2Cur.length; i++) {
            ot2cur.push([otLv2Cur[i].ra, otLv2Cur[i].dec]);
            ot2curLabel.push([otLv2Cur[i].dpmId, otLv2Cur[i].lastFfNumber, otLv2Cur[i].name]);
          }

          drawData.push({
            label: "ot1",
            data: ot1,
            color: '#71c73e',
            points: {show: true, radius: 0.5}
          });
          drawData.push({
            label: "ot2",
            data: ot2,
            color: '#f59fb4', //#77b7c5
            points: {show: true, radius: 2}
          });
          drawData.push({
            label: "ot2-cur",
            data: ot2cur,
            color: 'purple',
            points: {show: true, radius: 3}
          });

          if (reqNum === 1) {
            plotAndBind();
            reqNum++;
          } else {
            plot.setData(drawData);//drawData
            plot.draw();
            reqNum++;
          }
          if (ot2curInterval !== null) {
            clearInterval(ot2curInterval);
          }
          ot2curInterval = setInterval(highlightCurOT2, intervalTime);
        }

        function update() {
          $.ajax({url: dataurl, type: "GET", dataType: "json", success: onDataReceived});
          setTimeout(update, requestTime);
        }
        update();


        $("<div id='tooltip'></div>").css({
          position: "absolute",
          display: "none",
          border: "1px solid #fdd",
          padding: "2px",
          "background-color": "#fee",
          opacity: 0.80
        }).appendTo("body");

        function plotAndBind() {
          var id = "#placeholder1";
          plot = $.plot(id, drawData, option1);
          $(id).bind("plothover", function(event, pos, item) {
            if (item) {
              var x = item.datapoint[0].toFixed(2);
              var y = item.datapoint[1].toFixed(2);
              if (item.series.label === "ot1") {
                var mName = "";
                if (ot1Label[item.dataIndex][0] < 10) {
                  mName = "M0" + ot1Label[item.dataIndex][0];
                } else {
                  mName = "M" + ot1Label[item.dataIndex][0];
                }
                $("#tooltip").html(mName + "-" + ot1Label[item.dataIndex][1] + " (" + x + ", " + y + ")")
                        .css({top: item.pageY + 5, left: item.pageX + 5})
                        .fadeIn(200);
              } else if (item.series.label === "ot2") {
                var mName = "";
                if (ot2Label[item.dataIndex][0] < 10) {
                  mName = "M0" + ot2Label[item.dataIndex][0];
                } else {
                  mName = "M" + ot2Label[item.dataIndex][0];
                }
                $("#tooltip").html(mName + "-" + ot2Label[item.dataIndex][1] + " (" + x + ", " + y + ")")
                        .css({top: item.pageY + 5, left: item.pageX + 5})
                        .fadeIn(200);
              } else if (item.series.label === "ot2-cur") {
                var mName = "";
                if (ot2curLabel[item.dataIndex][0] < 10) {
                  mName = "M0" + ot2curLabel[item.dataIndex][0];
                } else {
                  mName = "M" + ot2curLabel[item.dataIndex][0];
                }
                $("#tooltip").html(mName + "-" + ot2curLabel[item.dataIndex][1] + " (" + x + ", " + y + ")")
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
                openDialog(ot2Label[item.dataIndex][2]);
              } else if (item.series.label === "ot2-cur") {
                openDialog(ot2curLabel[item.dataIndex][2]);
              }
            }
          });
        }

        function highlightCurOT2() {
          for (var ii = 0; ii < ot2cur.length; ii++) {
            plot.highlight(2, ii);
          }
          setTimeout(unHighlightCurOT2, 50);
        }

        function unHighlightCurOT2() {
          for (var ii = 0; ii < ot2cur.length; ii++) {
            plot.unhighlight(2, ii);
          }
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
      <div class="demo-container" style="width:95%; height: 90%;">
        <div id="placeholder1" class="demo-placeholder" style="float:left; width:100%; height: 100%;"></div>
      </div>
    </div>

  </body>
</html>