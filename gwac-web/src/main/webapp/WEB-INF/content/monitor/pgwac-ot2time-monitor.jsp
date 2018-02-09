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
    <meta http-equiv="description" content="OT2生命周期时间统计" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>OT2生命周期时间统计- <s:text name="showcase.version"/></title>

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
      #curve-commment{
        width: 100%;
        text-align: left;
        padding:  0 30px;
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
            <select name="formqp.starList" id="starList" class="form-control select select-primary" data-toggle="select" size="13">
              <option value="T00" title="OT2产生时间" selected>T00 OT2产生</option>
              <option value="T01" title="OT1列表1上传时间">T01</option>
              <option value="T02" title="OT1列表1处理时间">T02</option>
              <option value="T03" title="OT1列表2上传时间">T03</option>
              <option value="T04" title="OT1列表2处理时间">T04</option>
              <option value="T10" title="OT2回看时间">T10 OT2回看</option>
              <option value="T11" title="切图1请求时间">T11</option>
              <option value="T12" title="切图1上传时间">T12</option>
              <option value="T13" title="切图2请求时间">T13</option>
              <option value="T14" title="切图2上传时间">T14</option>
              <option value="T15" title="模板请求时间">T15</option>
              <option value="T16" title="模板上传时间">T16</option>
              <option value="T20" title="OT2后随时间">T20 OT2后随</option>
              <option value="T21" title="后随处理时间">T21</option>
            </select><br/>
            <!--多条曲线过滤显示，请参考:http://www.flotcharts.org/flot/examples/series-toggle/index.html-->
            <select name="formqp.mountList" id="mountList" class="form-control select select-primary" data-toggle="select" size="5">
              <option value="01">转台1</option>
              <option value="02">转台2</option>
              <option value="03">转台3</option>
              <option value="04">转台4</option>
              <option value="05">转台5</option>
            </select><br/>
            <select name="formqp.ccdList" id="ccdList" class="form-control select select-primary" data-toggle="select" size="8"  multiple="multiple">
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
              <option value="044">044</option>
              <option value="045">045</option>
            </select>
          </div>
        </div>
        <div class="col-xs-10 col-sm-10 col-md-11 ">
          <div class="row star-list-main">  
            <div id="star-light-curve-show">
              <div id="star-light-curve-title">
                <span>处理消耗时间随时间的变化统计（X轴(OT2产生时间）/小时，Y轴（处理过程消耗时间）/秒，X轴开始于<span id="startDay">0</span>[Local])</span>
              </div>
              <div id="star-light-curve"></div>
            </div>
          </div>
          <div class="row star-list-main">  
            <div class="col-xs-12 col-sm-12 col-md-12">
              <div id="curve-commment">
                <h4>说明：各个部分的时间采集于不同的机器，Web服务器、数据库和数据处理机的时间或许存在差异。</h4>
                T00：OT2产生时间，从OT2被探测到的第一幅图像到OT2生成<br/>
                T01：OT1列表1上传时间，从OT2被探测到的第一幅图像到其OT1列表完成上传<br/>
                T02：OT1列表1处理时间，OT2的第一幅图像的OT1列表处理时间<br/>
                T03：OT1列表2上传时间，从OT2被探测到的第二幅图像到其OT1列表完成上传<br/>
                T04：OT1列表2处理时间，OT2的第二幅图像的OT1列表处理时间<br/>
                T10：OT2回看时间，从OT2生成到OT2完成回看<br/>
                T11：切图1请求时间，从切图1产生到被请求切图<br/>
                T12：切图1上传时间，从切图1被请求切图到切图上传成功<br/>
                T13：切图2请求时间，从切图2产生到被请求切图<br/>
                T14：切图2上传时间，从切图2被请求切图到切图上传成功<br/>
                T15：模板请求时间，从模板切图产生到被请求切图<br/>
                T16：模板上传时间，从模板切图被请求切图到切图上传成功<br/>
                T20：OT2后随时间，从OT2回看完成到后随结果上传数据库<br/>
                T21：后随处理时间，从OT2触发后随到后随结果上传数据库<br/>
              </div>
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
    <script src="${pageContext.request.contextPath}/resource/js/monitor_ot2time.js"></script>
  </body>
</html>