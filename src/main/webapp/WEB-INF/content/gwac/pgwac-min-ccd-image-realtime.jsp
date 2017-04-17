<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
    <title>Mini-GWAC CCD图像实时预览</title>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/plot/jquery.js"></script>
    <script type="text/javascript">

      $(function() {
        $('.imgStyle').each(function() {
          var winWidth = $(window).width();
          var winHeight = $(window).height();
          var imgWidth = winHeight / 4 - (25);
          var imgHeight = imgWidth;
          $(this).css("width", imgWidth);
          $(this).css("height", imgHeight);
        });

        var dataurl = "<%=request.getContextPath()%>/get-dpm-monitor-image-time.action";

        function onImageReceived(result) {
          var dpms = result.dpms;
          for (var i = 0; i < dpms.length; i++) {
            $("#span" + (i + 1)).html(dpms[i].name + " (" + dpms[i].curProcessNumber + ")");
          }
        }

        function updateImage() {
          for (var i = 1; i <= 25; i++) {
            var orgSrc = $('#img' + i).attr('src');
            orgSrc += "?timestamp=123456";
            orgSrc = orgSrc.substring(0, orgSrc.indexOf('?'));
            orgSrc += "?timestamp=" + new Date().getTime();
            $("#img" + i).attr("src", orgSrc);
          }
//          $.ajax({url: dataurl, type: "GET", dataType: "json", success: onImageReceived});
        }
        updateImage();
        setInterval(updateImage, 15000);

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
        width:200px;
        height:200px;
        border-radius: 6px; 
      }
      -->
    </style></head>

  <body>
    <table width="100%" border="0">
      <tr>
        <td><div align="center" class="style6"><span id="span1">G001</span> </div></td>
        <td><div align="center" class="style6"><span id="span2">G002</span> </div></td>
        <td><div align="center" class="style6"><span id="span3">G003</span></div></td>
        <td><div align="center" class="style6"><span id="span4">G004</span></div></td>
        <td><div align="center" class="style6"><span id="span5">G005</span></div></td>
        <td><div align="center" class="style6"><span id="span6">G006</span></div></td>
        <td><div align="center" class="style6"><span id="span7">G007</span></div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G001_ccdimg.jpg"><img id="img1" src="/images/realTimeOtDistribution/G001_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G002_ccdimg.jpg"><img id="img2" src="/images/realTimeOtDistribution/G002_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G003_ccdimg.jpg"><img id="img3" src="/images/realTimeOtDistribution/G003_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G004_ccdimg.jpg"><img id="img4" src="/images/realTimeOtDistribution/G004_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G004_ccdimg.jpg"><img id="img5" src="/images/realTimeOtDistribution/G005_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G006_ccdimg.jpg"><img id="img6" src="/images/realTimeOtDistribution/G006_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G007_ccdimg.jpg"><img id="img7" src="/images/realTimeOtDistribution/G007_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span8">G008</span></div></td>
        <td><div align="center" class="style6"><span id="span9">G009</span></div></td>
        <td><div align="center" class="style6"><span id="span10">G010</span></div></td>
        <td><div align="center" class="style6"><span id="span11">G011</span> </div></td>
        <td><div align="center" class="style6"><span id="span12">G012</span> </div></td>
        <td><div align="center" class="style6"><span id="span13">G013</span> </div></td>
        <td><div align="center" class="style6"><span id="span14">G014</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G008_ccdimg.jpg"><img id="img8" src="/images/realTimeOtDistribution/G008_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G005_ccdimg.jpg"><img id="img9" src="/images/realTimeOtDistribution/G009_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G005_ccdimg.jpg"><img id="img10" src="/images/realTimeOtDistribution/G010_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img11" src="/images/realTimeOtDistribution/G011_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img12" src="/images/realTimeOtDistribution/G012_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img13" src="/images/realTimeOtDistribution/G013_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img14" src="/images/realTimeOtDistribution/G014_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span15">G015</span> </div></td>
        <td><div align="center" class="style6"><span id="span16">G016</span> </div></td>
        <td><div align="center" class="style6"><span id="span17">G017</span> </div></td>
        <td><div align="center" class="style6"><span id="span18">G018</span> </div></td>
        <td><div align="center" class="style6"><span id="span19">G019</span> </div></td>
        <td><div align="center" class="style6"><span id="span20">G020</span> </div></td>
        <td><div align="center" class="style6"><span id="span21">G021</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img15" src="/images/realTimeOtDistribution/G015_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img16" src="/images/realTimeOtDistribution/G016_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img17" src="/images/realTimeOtDistribution/G017_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img18" src="/images/realTimeOtDistribution/G018_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img19" src="/images/realTimeOtDistribution/G019_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img20" src="/images/realTimeOtDistribution/G020_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img21" src="/images/realTimeOtDistribution/G021_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
      <tr>
        <td><div align="center" class="style6"><span id="span22">G022</span> </div></td>
        <td><div align="center" class="style6"><span id="span23">G023</span> </div></td>
        <td><div align="center" class="style6"><span id="span24">G024</span> </div></td>
        <td><div align="center" class="style6"><span id="span25">G025</span> </div></td>
        <td><div align="center" class="style6"><span id="span23">G000</span> </div></td>
        <td><div align="center" class="style6"><span id="span24">G000</span> </div></td>
        <td><div align="center" class="style6"><span id="span25">G000</span> </div></td>
      </tr>
      <tr>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img22" src="/images/realTimeOtDistribution/G022_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img23" src="/images/realTimeOtDistribution/G023_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img24" src="/images/realTimeOtDistribution/G024_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img25" src="/images/realTimeOtDistribution/G025_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img26" src="/images/realTimeOtDistribution/G000_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img27" src="/images/realTimeOtDistribution/G000_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
        <td><div align="center">
            <a href="/images/realTimeOtDistribution/G009_ccdimg.jpg"><img id="img28" src="/images/realTimeOtDistribution/G000_ccdimg.jpg" class="imgStyle" border="0"/></a></div></td>
      </tr>
    </table>
  </body>
</html>
