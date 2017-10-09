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
        $('.imgStyle').each(function() {
          $(this).css("width", imgWidth);
          $(this).css("height", imgHeight);
        });

        var centerWidth = imgWidth * 3;
        $('#imgCenter').css('width', centerWidth + 'px');
        $('#imgCenter').css('height', centerWidth + 'px');
        $('#imgCenter').css('border-radius', '12px');

        var dataurl = "<%=request.getContextPath()%>/get-camera-monitor-image-time.action";
        $.ajax({url: dataurl, type: "GET", dataType: "json", success: onImageReceived});
        
        allImgUpdate = setInterval(updateImage, 15000);
        centerImgUpdate = setInterval(updateCenterImage, 1000);

        $('img').hover(overImg, outImg)


        function overImg() {
          clearInterval(allImgUpdate);
          clearInterval(centerImgUpdate);
          ///images/realTimeOtDistribution/G001_ccdimg_sub.jpg
          var imgUrl = $(this).attr('src');
          var startIdx = imgUrl.indexOf('G');
          var idx = parseInt(imgUrl.substring(startIdx + 1, startIdx + 4)) - 1;
          updateCenterImageNumber(idx);
          //console.log($(this).attr('src'));
          //console.log(idx);
        }

        function outImg() {
          allImgUpdate = setInterval(updateImage, 15000);
          centerImgUpdate = setInterval(updateCenterImage, 1000);
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
          for (var i = 1; i <= totalImgNum; i++) {
            var src = "/images/realTimeOtDistribution/G" + pad(i, 3) + "_ccdimg_sub.jpg?timestamp=" + new Date().getTime();
            $("#img" + i).attr("src", src);
          }
          $.ajax({url: dataurl, type: "GET", dataType: "json", success: onImageReceived});
        }

        function updateCenterImage() {
          updateCenterImageNumber(centerImgIdx);
          centerImgIdx = (centerImgIdx + 1) % totalImgNum;
        }


        function updateCenterImageNumber(idx) {
          console.log(cameras);
          var cameraId = pad((idx + 1), 3);
          var origSrc = "/images/realTimeOtDistribution/G" + cameraId + "_ccdimg.jpg?timestamp=" + new Date().getTime();
          var cenSrc = $("#img" + (idx + 1)).attr("src");
          var cenSpan = $("#span" + (idx + 1)).html();
          $('#imgCenter').attr("src", cenSrc);
          $('#imgCenterUrl').attr("href", origSrc);
          if (Object.getOwnPropertyNames(cameras).length !== 0) {
            cenSpan = cenSpan +"&nbsp;&nbsp;(" + cameras[cameraId] + ")";
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
      .imgStyle{
        width:100px;
        height:100px;
        border-radius: 6px; 
      }
      -->
    </style></head>

  <body>
    <table width="100%" border="0">
      <tr>
        <td><div align="center" class="style6"><span id="span1">G001</span> </div></td>
        <td><div align="center" class="style6"><span id="span2">G002</span> </div></td>
        <td><div align="center" class="style6"><span id="span3">G003</span> </div></td>
        <td colspan="3">
          <div align="center" class="style6">
            <span id="centerSpan">G00N</span>
          </div>
        </td>
        <td><div align="center" class="style6"><span id="span4">G004</span></div></td>
        <td><div align="center" class="style6"><span id="span5">G005</span></div></td>
        <td><div align="center" class="style6"><span id="span6">G006</span></div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G001_ccdimg.jpg"><img id="img1" src="/images/realTimeOtDistribution/G001_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G002_ccdimg.jpg"><img id="img2" src="/images/realTimeOtDistribution/G002_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G003_ccdimg.jpg"><img id="img3" src="/images/realTimeOtDistribution/G003_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td colspan="3" rowspan="5"><div align="center">
            <a id="imgCenterUrl" href="/images/realTimeOtDistribution/GWAC_ccdimg.jpg"><img id="imgCenter" src="/images/realTimeOtDistribution/GWAC_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G004_ccdimg.jpg"><img id="img4" src="/images/realTimeOtDistribution/G004_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G005_ccdimg.jpg"><img id="img5" src="/images/realTimeOtDistribution/G005_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G006_ccdimg.jpg"><img id="img6" src="/images/realTimeOtDistribution/G006_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span7">G007</span></div></td>
        <td><div align="center" class="style6"><span id="span8">G008</span></div></td>
        <td><div align="center" class="style6"><span id="span9">G009</span></div></td>
        <td><div align="center" class="style6"><span id="span10">G010</span> </div></td>
        <td><div align="center" class="style6"><span id="span11">G011</span> </div></td>
        <td><div align="center" class="style6"><span id="span12">G012</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G007_ccdimg.jpg"><img id="img7" src="/images/realTimeOtDistribution/G007_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G008_ccdimg.jpg"><img id="img8" src="/images/realTimeOtDistribution/G008_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img9" src="/images/realTimeOtDistribution/G009_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G010_ccdimg.jpg"><img id="img10" src="/images/realTimeOtDistribution/G010_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G011_ccdimg.jpg"><img id="img11" src="/images/realTimeOtDistribution/G011_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G012_ccdimg.jpg"><img id="img12" src="/images/realTimeOtDistribution/G012_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span13">G013</span> </div></td>
        <td><div align="center" class="style6"><span id="span14">G014</span> </div></td>
        <td><div align="center" class="style6"><span id="span15">G015</span> </div></td>
        <td><div align="center" class="style6"><span id="span16">G016</span> </div></td>
        <td><div align="center" class="style6"><span id="span17">G017</span> </div></td>
        <td><div align="center" class="style6"><span id="span18">G018</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G013_ccdimg.jpg"><img id="img13" src="/images/realTimeOtDistribution/G013_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G014_ccdimg.jpg"><img id="img14" src="/images/realTimeOtDistribution/G014_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G015_ccdimg.jpg"><img id="img15" src="/images/realTimeOtDistribution/G015_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G016_ccdimg.jpg"><img id="img16" src="/images/realTimeOtDistribution/G016_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G017_ccdimg.jpg"><img id="img17" src="/images/realTimeOtDistribution/G017_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G018_ccdimg.jpg"><img id="img18" src="/images/realTimeOtDistribution/G018_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span19">G019</span> </div></td>
        <td><div align="center" class="style6"><span id="span20">G020</span> </div></td>
        <td><div align="center" class="style6"><span id="span21">G021</span> </div></td>
        <td><div align="center" class="style6"><span id="span22">G022</span> </div></td>
        <td><div align="center" class="style6"><span id="span23">G023</span> </div></td>
        <td><div align="center" class="style6"><span id="span24">G024</span> </div></td>
        <td><div align="center" class="style6"><span id="span25">G025</span> </div></td>
        <td><div align="center" class="style6"><span id="span26">G000</span> </div></td>
        <td><div align="center" class="style6"><span id="span27">G000</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G019_ccdimg.jpg"><img id="img19" src="/images/realTimeOtDistribution/G019_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G020_ccdimg.jpg"><img id="img20" src="/images/realTimeOtDistribution/G020_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G021_ccdimg.jpg"><img id="img21" src="/images/realTimeOtDistribution/G021_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G022_ccdimg.jpg"><img id="img22" src="/images/realTimeOtDistribution/G022_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G023_ccdimg.jpg"><img id="img23" src="/images/realTimeOtDistribution/G023_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G024_ccdimg.jpg"><img id="img24" src="/images/realTimeOtDistribution/G024_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G025_ccdimg.jpg"><img id="img25" src="/images/realTimeOtDistribution/G025_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/GWAC_ccdimg.jpg"><img id="img26" src="/images/realTimeOtDistribution/GWAC_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/GWAC_ccdimg.jpg"><img id="img27" src="/images/realTimeOtDistribution/GWAC_ccdimg_sub.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
    </table>
  </body>
</html>
