<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
    <title>GWAC CCD图像实时预览</title>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/jquery.js"></script>
    <script type="text/javascript">

      $(function() {
        var cameras = {};
        var totalImgNum = 25;
        var centerImgIdx = 0;
        var winWidth = $(window).width();
        var winHeight = $(window).height();
        var imgWidth = winWidth / 9 - (15);
        var imgHeight = imgWidth;
        var allImgUpdate;
        var centerImgUpdate;
        $('.imgStyle, .imgStyle2').each(function() {
          $(this).css("width", imgWidth);
          $(this).css("height", imgHeight);
        });

        var centerWidth = imgWidth * 3;
        $('#imgCenter').css('width', centerWidth + 'px');
        $('#imgCenter').css('height', centerWidth + 'px');
        $('#imgCenter').css('border-radius', '12px');

        var dataurl = "<%=request.getContextPath()%>/gction/get-camera-monitor-image-time.action";
        $.ajax({url: dataurl, type: "GET", dataType: "json", success: onImageReceived});

        allImgUpdate = setInterval(updateImage, 15000);
        centerImgUpdate = setInterval(updateCenterImage, 2000);

        $('img.imgStyle').hover(overImg, outImg)


        function overImg() {
          clearInterval(allImgUpdate);
          clearInterval(centerImgUpdate);
          ///images/realTimeOtDistribution/G001_ccdimg_sub.jpg
          var imgUrl = $(this).attr('src');
          var startIdx = imgUrl.indexOf('G');
          var idx = parseInt(imgUrl.substring(startIdx + 1, startIdx + 4));
          updateCenterImageNumber(idx);
          //console.log($(this).attr('src'));
          //console.log(idx);
        }

        function outImg() {
          allImgUpdate = setInterval(updateImage, 15000);
          centerImgUpdate = setInterval(updateCenterImage, 2000);
        }

        function onImageReceived(result) {
          var tcameras = result.cameras;
          for (var i = 0; i < tcameras.length; i++) {
            cameras[tcameras[i].name] = tcameras[i].monitorImageTime;
          }
        }

        function pad(str, max) {
          str = str.toString();
          return str.length < max ? pad("0" + str, max) : str;
        }


        function updateImage() {
          $(".imgStyle").each(function(i, item) {
            var turl = $(item).attr("src");
            var tidx = turl.indexOf('?');
            if (tidx > 0) {
              turl = turl.substring(0, tidx);
            }
            turl = turl + "?timestamp=" + new Date().getTime();
            $(item).attr("src", turl);
          });
          $.ajax({url: dataurl, type: "GET", dataType: "json", success: onImageReceived});
        }

        function updateCenterImage() {
          var imgNum = (parseInt(centerImgIdx / 5) + 1) + "" + (centerImgIdx % 5 + 1)
          updateCenterImageNumber(imgNum);
          centerImgIdx = (centerImgIdx + 1) % totalImgNum;
        }


        function updateCenterImageNumber(idx) {
          var cameraId = pad(idx, 3);
          var origSrc = "/images/realTimeOtDistribution/G" + cameraId + "_ccdimg.jpg?timestamp=" + new Date().getTime();
          var cenSrc = $("#img" + (cameraId)).attr("src");
          var tidx = cenSrc.indexOf('?');
          if (tidx > 0) {
            cenSrc = cenSrc.substring(0, tidx);
          }
          var cenSpan = $("#span" + (cameraId)).html();
          $('#imgCenter').attr("src", cenSrc);
          $('#imgCenterUrl').attr("href", origSrc);
          if (Object.getOwnPropertyNames(cameras).length !== 0) {
            cenSpan = cenSpan + "&nbsp;&nbsp;(" + cameras[cameraId] + ")";
          }
          $("#centerSpan").html(cenSpan);
        }

        function shijian()
        {
          var date = new Date();
          var xiaoshi = date.getHours();
          var fen = date.getMinutes();
          var miao = date.getSeconds();
          if (fen <= 9)
            fen = "0" + fen;
          if (miao <= 9)
            miao = "0" + miao;
          var xianshi = "<font class='b'>" + xiaoshi + ":" + fen + ":" + miao + "</font>";
          $("#a").html(xianshi);
        }
        //setInterval(shijian, 1000);
      });
    </script>
    <style type="text/css">
      <!--
      .style3 {font-size: 28px;font-weight: bold;}
      .style5 {
        font-size: 16px;
        font-weight: bold;
      }
      .style6 {font-size: 14px;font-weight: bold;}
      .style7 {font-size: 12px}
      .imgStyle, .imgStyle2, #imgCenter{
        width:100px;
        height:100px;
        border-radius: 6px; 
      }
      -->
    </style></head>

  <body>
    <table width="100%" border="0">
      <tr>
        <td><div align="center" class="style6"><span id="span011">G011</span> </div></td>
        <td><div align="center" class="style6"><span id="span012">G012</span> </div></td>
        <td><div align="center" class="style6"><span id="span013">G013</span> </div></td>
        <td colspan="3">
          <div align="center" class="style6">
            <span id="centerSpan">G00N</span>
          </div>
        </td>
        <td><div align="center" class="style6"><span id="span014">G014</span></div></td>
        <td><div align="center" class="style6"><span id="span015">G015</span></div></td>
        <td><div align="center" class="style6"><span id="span021">G021</span></div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G011_ccdimg.jpg"><img id="img011" src="/images/realTimeOtDistribution/G011_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G012_ccdimg.jpg"><img id="img012" src="/images/realTimeOtDistribution/G012_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G013_ccdimg.jpg"><img id="img013" src="/images/realTimeOtDistribution/G013_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td colspan="3" rowspan="5"><div align="center">
            <a id="imgCenterUrl" href="/images/realTimeOtDistribution/GWAC_ccdimg.jpg"><img id="imgCenter" src="/images/realTimeOtDistribution/GWAC_ccdimg_sub.jpg" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G014_ccdimg.jpg"><img id="img014" src="/images/realTimeOtDistribution/G014_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G015_ccdimg.jpg"><img id="img015" src="/images/realTimeOtDistribution/G015_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G021_ccdimg.jpg"><img id="img021" src="/images/realTimeOtDistribution/G021_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span022">G022</span></div></td>
        <td><div align="center" class="style6"><span id="span023">G023</span></div></td>
        <td><div align="center" class="style6"><span id="span024">G024</span></div></td>
        <td><div align="center" class="style6"><span id="span025">G025</span> </div></td>
        <td><div align="center" class="style6"><span id="span031">G031</span> </div></td>
        <td><div align="center" class="style6"><span id="span032">G032</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G022_ccdimg.jpg"><img id="img022" src="/images/realTimeOtDistribution/G022_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G023_ccdimg.jpg"><img id="img023" src="/images/realTimeOtDistribution/G023_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G024_ccdimg.jpg"><img id="img024" src="/images/realTimeOtDistribution/G024_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G025_ccdimg.jpg"><img id="img025" src="/images/realTimeOtDistribution/G025_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G031_ccdimg.jpg"><img id="img031" src="/images/realTimeOtDistribution/G031_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G032_ccdimg.jpg"><img id="img032" src="/images/realTimeOtDistribution/G032_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span033">G033</span> </div></td>
        <td><div align="center" class="style6"><span id="span034">G034</span> </div></td>
        <td><div align="center" class="style6"><span id="span035">G035</span> </div></td>
        <td><div align="center" class="style6"><span id="span041">G041</span> </div></td>
        <td><div align="center" class="style6"><span id="span042">G042</span> </div></td>
        <td><div align="center" class="style6"><span id="span043">G043</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G033_ccdimg.jpg"><img id="img033" src="/images/realTimeOtDistribution/G033_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G034_ccdimg.jpg"><img id="img034" src="/images/realTimeOtDistribution/G034_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G035_ccdimg.jpg"><img id="img035" src="/images/realTimeOtDistribution/G035_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G041_ccdimg.jpg"><img id="img041" src="/images/realTimeOtDistribution/G041_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G042_ccdimg.jpg"><img id="img042" src="/images/realTimeOtDistribution/G042_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G043_ccdimg.jpg"><img id="img043" src="/images/realTimeOtDistribution/G043_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span044">G044</span> </div></td>
        <td><div align="center" class="style6"><span id="span045">G045</span> </div></td>
        <td><div align="center" class="style6"><span id="span051">G051</span> </div></td>
        <td><div align="center" class="style6"><span id="span052">G052</span> </div></td>
        <td><div align="center" class="style6"><span id="span053">G053</span> </div></td>
        <td><div align="center" class="style6"><span id="span054">G054</span> </div></td>
        <td><div align="center" class="style6"><span id="span055">G055</span> </div></td>
        <td><div align="center" class="style6"><span id="span061">G000</span> </div></td>
        <td><div align="center" class="style6"><span id="span062">G000</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G044_ccdimg.jpg"><img id="img044" src="/images/realTimeOtDistribution/G044_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G045_ccdimg.jpg"><img id="img045" src="/images/realTimeOtDistribution/G045_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G051_ccdimg.jpg"><img id="img051" src="/images/realTimeOtDistribution/G051_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G052_ccdimg.jpg"><img id="img052" src="/images/realTimeOtDistribution/G052_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G053_ccdimg.jpg"><img id="img053" src="/images/realTimeOtDistribution/G053_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G054_ccdimg.jpg"><img id="img054" src="/images/realTimeOtDistribution/G054_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G055_ccdimg.jpg"><img id="img055" src="/images/realTimeOtDistribution/G055_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/GWAC_ccdimg.jpg"><img id="img061" src="/images/realTimeOtDistribution/GWAC_ccdimg_sub.jpg" class="imgStyle2" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/GWAC_ccdimg.jpg"><img id="img062" src="/images/realTimeOtDistribution/GWAC_ccdimg_sub.jpg" class="imgStyle2" border="0"/></a></div></td>
      </tr>
    </table>
  </body>
</html>
