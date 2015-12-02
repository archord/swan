<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
    <title>Mini-GWAC bgbright Monitor</title>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.js"></script>
    <script type="text/javascript">

      $(function() {
        
        function updateImage() {
          for (var i = 1; i <= 12; i++) {
            var orgSrc = $('#img' + i).attr('src');
            orgSrc += "?timestamp=123456";
            orgSrc = orgSrc.substring(0, orgSrc.indexOf('?'));
            orgSrc += "?timestamp=" + new Date().getTime();
            $("#img" + i).attr("src", orgSrc);
          }
        }
        updateImage();
        setInterval(updateImage, 15000);
        
        function shijian()
        {
          var date = new Date();
          var xiaoshi = date.getHours();
          var fen = date.getMinutes();
          var miao = date.getSeconds();
          if (fen <= 9)fen = "0" + fen;
          if (miao <= 9)miao = "0" + miao;
          var xianshi = "<font class='b'>" + xiaoshi + ":" + fen + ":" + miao + "</font>";
          $("#a").html(xianshi);
        }
        setInterval(shijian, 1000);
      });
    </script>
    <style type="text/css">
      <!--
      .style3 {font-size: 28px;font-weight: bold;}
      .style5 {
        font-size: 16px;
        font-weight: bold;
      }
      .style6 {font-size: 22px;font-weight: bold;}
      .style7 {font-size: 12px}
      .imgStyle{
        width:450px;
        height:250px;
        border-radius: 6px; 
      }
      -->
    </style></head>

  <body>
    <table width="900" border="0">
      <!--=E to get the time-->
      <!--=E ======================-->
      <tr>
        <td>
          <div align="center">
            <span class="style3">----- Mini-GWAC bgbright Monitor ----- </span>
            <div id="a"></div>
            <!--=E ===============-->
          </div></td>
      </tr>

      <tr>
        <td>
          <table width="616" border="0">
            <tr>
              <td width="454"><div align="center" class="style6"><span id="span1">M01</span> </div></td>
              <td width="454"><div align="center" class="style6"><span id="span2">M02</span> </div></td>
              <td width="454"><div align="center" class="style6"><span id="span3">M03</span></div></td>
              <td width="454"><div align="center" class="style6"><span id="span4">M04</span></div></td>
            </tr>
            <tr>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M01_bgbright.jpg"><img id="img1" src="/images/realTimeOtDistribution/M01_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M02_bgbright.jpg"><img id="img2" src="/images/realTimeOtDistribution/M02_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M03_bgbright.jpg"><img id="img3" src="/images/realTimeOtDistribution/M03_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M04_bgbright.jpg"><img id="img4" src="/images/realTimeOtDistribution/M04_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
            </tr>
            <tr>
              <td width="454"><div align="center" class="style6"><span id="span5">M05</span> </div></td>
              <td><div align="center" class="style6"><span id="span6">M06</span> </div></td>
              <td><div align="center" class="style6"><span id="span7">M07</span></div></td>
              <td><div align="center" class="style6"><span id="span8">M08</span></div></td>
            </tr>
            <tr>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M05_bgbright.jpg"><img id="img5" src="/images/realTimeOtDistribution/M05_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M06_bgbright.jpg"><img id="img6" src="/images/realTimeOtDistribution/M06_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M07_bgbright.jpg"><img id="img7" src="/images/realTimeOtDistribution/M07_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M08_bgbright.jpg"><img id="img8" src="/images/realTimeOtDistribution/M08_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
            </tr>
            <tr>
              <td width="454"><div align="center" class="style6"><span id="span9">M09</span> </div></td>
              <td width="454"><div align="center" class="style6"><span id="span10">M10</span> </div></td>
              <td width="454"><div align="center" class="style6"><span id="span11">M11</span></div></td>
              <td width="454"><div align="center" class="style6"><span id="span12">M12</span></div></td>
            </tr>
            <tr>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M09_bgbright.jpg"><img id="img9" src="/images/realTimeOtDistribution/M09_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M10_bgbright.jpg"><img id="img10" src="/images/realTimeOtDistribution/M10_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M11_bgbright.jpg"><img id="img11" src="/images/realTimeOtDistribution/M11_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
              <td width="454"><div align="center">
                  <a href="/images/realTimeOtDistribution/M12_bgbright.jpg"><img id="img12" src="/images/realTimeOtDistribution/M12_bgbright.jpg" class="imgStyle" border="0"/></a></div></td>
            </tr>
          </table>
      <tr>
        <td><div align="center">
            <p class="style7">Copyright: GWAC project. </p>
          </div></td>
        <td>&nbsp;</td>
      </tr>
    </table>
  </body>
</html>
