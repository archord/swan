<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<!DOCTYPE html>
<html lang="ch"> 
  <head>  
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="struts2, jquery, jquery-ui, plugin, showcase, jqgrid" />
    <meta http-equiv="description" content="A Showcase for the Struts2 jQuery Plugin" />

    <title>OT详细页面</title>

    <s:if test="%{theme == 'showcase' || theme == null}">
      <sj:head debug="true" compressed="true" jquerytheme="showcase" customBasepath="themes" 
               loadFromGoogle="%{google}" ajaxhistory="%{ajaxhistory}" 
               defaultIndicator="myDefaultIndicator" defaultLoadingText="Please wait ..."/>
    </s:if>
    <s:else>
      <sj:head debug="true" compressed="true" jquerytheme="%{theme}" loadFromGoogle="%{google}" 
               ajaxhistory="%{ajaxhistory}" defaultIndicator="myDefaultIndicator" 
               defaultLoadingText="Please wait ..."/>
    </s:else>


    <link href="<s:url value="/styles/flexible-grids.css" />" rel="stylesheet" type="text/css" />
    <link href="<s:url value="/styles/gwac-style.css" />" rel="stylesheet" type="text/css" />
    <!--[if lte IE 7]>
    <link href="<s:url value="/yaml/core/iehacks.min.css" />" rel="stylesheet" type="text/css" />
    <![endif]-->

    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- This files are needed for AJAX Validation of XHTML Forms -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/struts/utils.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/struts/xhtml/validation.js"></script>


    <!-- This file includes necessary functions/topics for validation and all topic examples -->
    <script type="text/javascript" src="<s:url value="/js/showcase.js" />"></script>
    <!-- Extend the Struts2 jQuery Plugin with an richtext editor -->
    <script type="text/javascript" src="<s:url value="/js/extendplugin.js" />"></script>
    <script src="js/jquery.carouFredSel-6.2.1-packed.js" type="text/javascript"></script>
    <script type="text/javascript">
      $(function() {
        function setNavi($c, $i) {
          var title = $i.attr('alt');
          $('#title').text(title);

          var current = $c.triggerHandler('currentPosition');
          $('#pagenumber span').text(current + 1);

        }
        $('#carousel').carouFredSel({
          prev: '#prev',
          next: '#next',
          auto: {
            button: '#play',
            progress: '#timer',
            pauseOnEvent: 'resume',
            timeoutDuration: 1000
          },
          scroll: {
            fx: 'fade',
            onBefore: function(data) {
              setNavi($(this), data.items.visible);
            }
          }
        });
        $('#carousel-wrapper').hover(function() {
          $('#navi').stop().animate({
            bottom: 0
          });
        }, function() {
          $('#navi').stop().animate({
            bottom: -60
          });
        });

        $('#reset').on('click', function() {
          var startImgNum = $('#startImgNum').val();
          alert(startImgNum);
          //$('#carousel').trigger('slideTo', startImgNum);
        });

      });
    </script>
    <style type="text/css">

      #carousel-wrapper {
        border: 1px solid #ccc;
        background-color: #fff;
        width: 600px;
        height: 430px;
        padding-bottom: 0px;
        padding-left: 20px;
        padding-right: 20px;
        padding-top: 20px;
        margin: -220px 0 0 -320px;
        position: absolute;
        left: 50%;
        top: 230px;
        box-shadow: 0 5px 10px #ccc;
      }

      #otRecord{
        width: 1380px;
        padding: 0px;
        margin: 0 0 0 0;
        position: absolute;
        float: left;
        top: 480px;
      }

      #inner {
        position: relative;
        width: 600px;
        height: 400px;
        overflow: hidden;
        text-align: center;
      }
      #carousel{
        width: 400px;
        left: 100px;
      }

      #navi {
        background-color: #333;
        background-color: rgba(30, 30, 30, 0.8);
        border-top: 1px solid #000;
        width: 600px;
        height: 70px;
        position: absolute;
        bottom: -60px;
        left: 0;
        z-index: 10;
      }
      #navi2 {
        width: 600px;
        height: 30px;
        margin: 0 20px 0 20px;
        position: absolute;
        bottom: 0px;
        left: 0;
        z-index: 20;
        vertical-align: bottom;
      }
      #pagenumber, #title, #pager, #reset {
        font-size: 12px;
        margin: 0;
        padding: 0;
      }
      #pagenumber {
        width: 130px;
        float: left;
      }
      #title {
        text-align: center;
        width: 360px;
        float: left;
      }
      #reset{
        float: right;
      }

      #carousel img {
        display: block;
        float: left;
      }

      #timer {
        background-color: #222;
        background-color: rgba(20, 20, 20, 0.8);
        width: 0;
        height: 70px;
        position: absolute;
        z-index: 20;
        top: 0;
        left: 0;
      }
      #prev, #next, #play {
        display: block;
        position: absolute;
        z-index: 30;
      }
      #prev, #next {
        width: 47px;
        height: 47px;
        top: 13px;
      }
      #play {
        width: 53px;
        height: 53px;
        top: 10px;
        background: url(images/imageGallery/ui/pause.png) 0 0 no-repeat transparent;
        left: 50%;
        margin-left: -27px;
      }
      #play.paused {
        background: url(images/imageGallery/ui/play.png) 0 0 no-repeat transparent;
      }
      #prev {
        background: url(images/imageGallery/ui/prev.png) 0 0 no-repeat transparent;
        left: 220px;
      }
      #next {
        background: url(images/imageGallery/ui/next.png) 0 0 no-repeat transparent;
        right: 220px;
      }

    </style>
  </head>
  <body>

    <div id="carousel-wrapper">
      <div id="inner">
        <div id="carousel">
          <s:iterator value="ffcList">
            <img src="<s:property value="storePath"/>/<s:property value="fileName"/>" 
                 alt="<s:property value="fileName"/>" 
                 title="<s:property value="fileName"/>" 
                 width="400" height="400" border="0" />
          </s:iterator>
        </div>
        <div id="navi">
          <div id="timer"></div>
          <a id="prev" href="#"></a>
          <a id="play" href="#"></a>
          <a id="next" href="#"></a>
        </div>
      </div>
      <div id="navi2">
        <p id="pagenumber">显示第<span style="font-weight:bold;font-size: 14px;"></span>帧，共<s:property value="totalImage"/>帧</p>
        <p id="title"></p>
        <p id="reset"><a href="#">跳转到OT起始帧</a><input type="hidden" id="startImgNum" value="<s:property value="startImgNum"/>"/></p>
      </div>
    </div>
    <div id="otRecord">
      <s:url var="remoteurl" action="ot-observe-record" namespace="/">
        <s:param name="otName" value="%{otName}" /> 
      </s:url>
      <!--width="700" resizable="true" 
      shrinkToFit="true" 自动调节到表格的宽度 -->
      <sjg:grid 
        id="gridtable" 
        caption="OT观测记录详细信息" 
        dataType="json" 
        href="%{remoteurl}" 
        pager="true" 
        gridModel="gridModel"
        rowList="10,15,20" 
        rowNum="10" 
        rownumbers="true"
        viewrecords="true">
        <sjg:gridColumn name="ffName"   index="ffName"	  title="原FITS图" width="250" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="raD"    index="raD"	  title="RA" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="decD"    index="decD"	  title="DEC" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="XTemp"    index="xTemp"	  title="模板X" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="YTemp"    index="yTemp"	  title="模板Y" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="flux"    index="flux"		  title="流量" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="background"    index="background"		  title="背景" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="threshold"    index="threshold"		  title="阈值" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="magAper"    index="magAper"		  title="星等" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="magerrAper"    index="magerrAper"		  title="星等误差" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="ellipticity"    index="ellipticity"		  title="椭率" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="classStar"    index="classStar"		  title="分类星" width="80" 
                        sortable="false" align="center"/>
        <sjg:gridColumn name="dateUt" index="dateUt" title="时间(UTC)" formatter="date" width="150"  
                        formatoptions="{newformat : 'Y-m-d H:i:s', srcformat : 'Y-m-d H:i:s'}" 
                        sortable="false" align="center"/>
      </sjg:grid>
    </div>

    <!--        <img src="images/imageGallery/rally1.jpg" alt="rally1" width="600" height="400" border="0" />
        <img src="images/imageGallery/rally2.jpg" alt="rally2" width="600" height="400" border="0" />
        <img src="images/imageGallery/rally3.jpg" alt="rally3" width="600" height="400" border="0" />
        <img src="images/imageGallery/rally4.jpg" alt="rally4" width="600" height="400" border="0" />-->

  </body>
</html>