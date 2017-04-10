<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="ch"> 
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台,svom,gwac,望远镜阵列,光变分类" />
    <meta http-equiv="description" content="GWAC光变分类页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>GWAC光变曲线- <s:text name="showcase.version"/></title>

    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/gwac-ui.css" rel="stylesheet">

    <style type="text/css">
      .container-fluid{
        min-width: 800px;
        margin: 5px;
      }
      .star-list-left{
        margin-top: 50px;
      }

      #star-light-curve-show{
        width: 100%;
        height: 800px;
        margin-top: 10px
      }

      #star-light-curve-title{
        width: 100%;  
        height: 20px; 
        text-align:center;
      }
      #star-light-curve{
        width: 100%; 
        height: 700px;
      }
    </style>

  </head>
  <body>
    <div class="container-fluid">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
        <input type="hidden" id="otName" value="<%=request.getParameter("otName")%>"/>
      </div>

      <div class="row ">
        <div class="col-xs-2 col-sm-2 col-md-1 ">
          <div class="row star-list-left">
            <select name="formqp.starList" id="starList" class="form-control select select-primary" data-toggle="select" size="12">
              <option value="fwhm" selected>半高全宽</option>
              <option value="obj_num">星的数目</option>
              <option value="bg_bright">背景亮度</option>
              <option value="avg_limit">极限星等</option>
              <option value="s2n">亮星星等差</option>
              <option value="xshift">图像偏移量X</option>
              <option value="yshift">图像偏移量Y</option>
              <option value="xrms">图像对齐精度X</option>
              <option value="yrms">图像对齐精度Y</option>
              <option value="proc_time">数据处理时间</option>
              <option value="temperature_actual">实际温度</option>
            </select>
          </div>
        </div>
        <div class="col-xs-10 col-sm-10 col-md-11 ">
          <div class="row star-list-main">  

            <div id="star-light-curve-show">
              <div id="star-light-curve-title">
                <span>X轴为时间，单位(分钟)，开始于<span id="startDay">0</span>(UTC)</span>
              </div>
              <div id="star-light-curve"></div>
            </div>
          </div>
        </div>
      </div>

    </div>
    <div id='tooltip'></div>
    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/plot/jquery.flot.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/plot/jquery.flot.categories.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/plot/jquery.flot.resize.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/plot/jquery.flot.errorbars.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/plot/jquery.flot.navigate.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/plot/jquery.flot.crosshair.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/parm_monitor.js"></script>
  </body>
</html>