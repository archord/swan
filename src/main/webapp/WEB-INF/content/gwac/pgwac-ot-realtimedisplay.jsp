<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Flot Examples: AJAX</title>
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
        var dataObj = [];
        var otl2 = [];
        var otl2cur = [];
        var drawData = [];
        for (var m = 0; m < 12; m++) {
          otl2[m] = [];
          otl2cur[m] = [];
        }
        var dataurl = "<%=request.getContextPath()%>/get-ot-xy-list.action";
        function onDataReceived(result) {
          dataObj = result.gridModel;
          for (var i = 0; i < dataObj.length; i++) {
            var mNum = parseInt(dataObj[i].dpmName.substring(1));
            if (dataObj[i].lastFfNumber === 722 || dataObj[i].lastFfNumber === 592) {
              otl2cur[mNum].push([dataObj[i].xtemp, dataObj[i].ytemp]);
            } else {
              otl2[mNum].push([dataObj[i].xtemp, dataObj[i].ytemp]);
            }
          }
          for (var m = 0; m < 12; m++) {
            drawData[m] = [
              {
                label: "ot-level2",
                data: otl2[m],
                color: '#71c73e',
                points: {show: true, radius: 1}
              },
              {
                label: "ot-level2-cur",
                data: otl2cur[m],
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
              $("#tooltip").html(dataObj[item.dataIndex].identify + " (" + x + ", " + y + ")")
                      .css({top: item.pageY + 5, left: item.pageX + 5})
                      .fadeIn(200);
            } else {
              $("#tooltip").hide();
            }
          });

          $(id).bind("plotclick", function(event, pos, item) {
            if (item) {
              openDialog(dataObj[item.dataIndex].name);
              plot[number].highlight(item.series, item.datapoint);
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