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
    <meta http-equiv="description" content="GWAC参数监控" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>GWAC CCD温度监控- <s:text name="showcase.version"/></title>

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
      .legendLabel{
        text-align: left;
        font-size: 14px;
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
            <select name="formqp.dayList" id="dayList" class="form-control select select-primary" data-toggle="select" size="8">
              <option value="1" selected>1天</option>
              <option value="2">2天</option>
              <option value="3">3天</option>
              <option value="7">1周</option>
              <option value="30">1月</option>
              <option value="180">6月</option>
              <option value="365">1年</option>
              <option value="3650">所有</option>
            </select><br/>
            <select name="formqp.ccdList" id="ccdList" class="form-control select select-primary" data-toggle="select" size="8">
              <option value="011">011</option>
              <option value="012">012</option>
              <option value="013">013</option>
              <option value="014">014</option>
              <option value="015">015</option>
              <option value="021">021</option>
              <option value="022">022</option>
              <option value="023">023</option>
              <option value="024">024</option>
              <option value="025">025</option>
              <option value="031">031</option>
              <option value="032">032</option>
              <option value="033">033</option>
              <option value="034">034</option>
              <option value="035">035</option>
              <option value="041">041</option>
              <option value="042">042</option>
              <option value="043">043</option>
              <option value="044" selected>044</option>
              <option value="045">045</option>
            </select>
          </div>
        </div>
        <div class="col-xs-10 col-sm-10 col-md-11 ">
          <div class="row star-list-main">  

            <div id="star-light-curve-show">
              <div id="star-light-curve-title">
                <span>X轴为时间，单位(天)，开始于<span id="startDay">0</span>(UTC)</span>
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
    <script src="${pageContext.request.contextPath}/resource/js/monitor_temperature.js"></script>
  </body>
</html>