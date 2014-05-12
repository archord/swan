<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<script src="js/jquery.carouFredSel-6.2.1-packed.js" type="text/javascript"></script>
<style type="text/css">

  #wrapper {
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

  #inner {
    position: relative;
    width: 600px;
    height: 400px;
    overflow: hidden;
  }

  #resetDiv {
    position: relative;
    width: 600px;
    height: 30px;
    overflow: hidden;
    text-align: center;
    line-height: 28px;
    visibility: hidden;
  }

  #carousel img {
    display: block;
    float: left;
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
<script type="text/javascript">
  $(function() {
    $('#carousel').carouFredSel({
      prev: '#prev',
      next: '#next',
      auto: {
        button: '#play',
        progress: '#timer',
        pauseOnEvent: 'resume'
      },
      scroll: {
        fx: 'fade'
      }
    });
    $('#wrapper').hover(function() {
      $('#navi').stop().animate({
        bottom: 0
      });
    }, function() {
      $('#navi').stop().animate({
        bottom: -60
      });
    });

    $('#reset').on('click', function() {
      $('#carousel').trigger('slideTo', 3);
    });

  });
</script>
</head>
<body>
  <div id="wrapper">
    <div id="inner">
      <div id="carousel">
        <img src="images/imageGallery/rally1.jpg" alt="rally1" width="600" height="400" border="0" />
        <img src="images/imageGallery/rally2.jpg" alt="rally2" width="600" height="400" border="0" />
        <img src="images/imageGallery/rally3.jpg" alt="rally3" width="600" height="400" border="0" />
        <img src="images/imageGallery/rally4.jpg" alt="rally4" width="600" height="400" border="0" />
      </div>
      <div id="navi">
        <div id="timer"></div>
        <a id="prev" href="#"></a>
        <a id="play" href="#"></a>
        <a id="next" href="#"></a>
      </div>
    </div>
    <div id="resetDiv">
      <a id="reset" href="#">跳转到OT起始帧</a>
    </div>
  </div>