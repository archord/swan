<%-- 
    Document   : observing
    Created on : 2017-8-18, 10:09:04
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC观测-管理后随观测任务</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC观测手动控制页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.css" rel="stylesheet">

    <style type="text/css"> 
      html, body {
        padding: 0;
        margin: 0;
        height: 100%;
        width:100%;  
        text-align: center;
      }
      .obs_plan_table {width:98%}
      .obs_plan {  
        width:98%;  
        height: 98%;
        overflow:auto;  
        white-space: nowrap;
      }  
      .tab_container{
        width:99%; 
        height: 93%;
        text-align: center;
        border:0;
      }
      .manual_container1_col{padding: 5px; text-align: center}
      .manual_container1{
        border: 3px solid #c5c5c5;
        margin: 0 auto;
        padding: 5px;
        text-align: left;
        width: 800px;
        font-size: 20px;
      }
      .manual_container1 span{  
        white-space: nowrap;  /*强制span不换行*/
        display: inline-block;  /*将span当做块级元素对待*/
        overflow: hidden;  /*超出宽度部分隐藏*/
        text-overflow: ellipsis;  /*超出部分以点号代替*/
        line-height: 0.9;  /*数字与之前的文字对齐*/
      }
      .manual_container1 input{  
        margin: 2px;
        width: 200px;
      }
      .manual_container1 select {
        width: 200px;
        background: #fff;
        margin: 2px;
        height: 35px;
      }
      .manual_container1 .timeinput{  
        margin: 2px;
        font-size: 20px;
        width: 200px;
      }
      .manual_container_title{
        text-align: center;
        font-size: 26px;
        background-color: #eee;
        margin:  -5px 0px 5px 0px;
      }
      .manual_container2{
        border: 3px solid #c5c5c5;
        min-height: 343px;
        padding: 5px;
        margin-bottom: 10px;
      }
      .background {fill: #000;}
      line {stroke: #000;}
      #genObsPlanBtn{
        margin-top: 10px;
      }
    </style>  
  </head>
  <body>

    <div id="tabs" style="width:98%; height:100%;margin:0 auto;border:0;">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>      

      <div class="tab_container">
        <div class="row">
          <div class="col-xs-12 col-sm-12 col-md-12 manual_container1_col">
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title" id="manual_container_title">
                    增加后随观测任务</div>
                </div>
              </div>
              <form action="${pageContext.request.contextPath}/otFollowUp.action" id="genObsPlanForm">
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <table>
                    </table>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <table>
                      <tr><td width="150px">本地时间：</td><td width="250px"><input id="localTime" class="timeinput"/></td>
                        <td width="150px">UTC时间：</td><td><input id="utcTime" class="timeinput"/></td></tr>
                      <tr><td>本地恒星时：</td><td><input id="siderealTime" class="timeinput"/></td>
                        <td>儒略日：</td><td><input id="julDay" class="timeinput"/></td></tr>
                      <tr><td>后随名称：</td><td><input name="ot2fp.followName" id="followName" title="如果'目标名称'不为空，则以'目标名称'为基准生成'后随名称'；
如果'后随名称'不为空，且'目标名称'为空，则用该字段作为'后随名称'，且设置'目标名称'为'后随名称'；
如果'后随名称'为空，且'目标名称'也为空，则系统自动生成'后随名称'，且设置'目标名称'为'后随名称'；
"/></td>
                        <td>目标名称：</td><td><input name="ot2fp.otName" id="otName" title="非OT2后随时，可以为空；
如果多次观测，则'目标名称'必须相同，否则系统会存储为两个不同的目标；"/></td></tr>
                      <tr><td>赤经(度)：</td>
                        <td><input name="ot2fp.ra" id="ra" style="text-align: right;width: 200px;" value="-99"/></td>
                        <td>赤纬(度)：</td>
                        <td><input name="ot2fp.dec" id="dec" style="text-align: right;width: 200px;" value="-99"/></td></tr>
                      <tr><td>赤经(时分秒)：</td>
                        <td>
                          <input id="rah" style="width:40px;text-align: right;" value="0"/>时
                          <input id="ram" style="width:40px;text-align: right;" value="0"/>分
                          <input id="ras" style="width:40px;text-align: right;" value="0"/>秒</td>
                        <td>赤纬(度分秒)：</td>
                        <td>
                          <input id="decd" style="width:40px;text-align: right;" value="0"/>度
                          <input id="decm" style="width:40px;text-align: right;" value="0"/>分
                          <input id="decs" style="width:40px;text-align: right;" value="0"/>秒</td></tr>
                      <tr><td>望远镜：</td><td>
                          <select height="30" name="ot2fp.telescope" id="telescope">
                            <option value="1">60公分</option>
                            <option value="2">30公分</option>
                          </select></td>
                        <td>滤光片：</td><td>
                          <select height="30" name="ot2fp.filter" id="filter">
                          </select></td></tr>
                      <tr><td>曝光时间：</td><td><input name="ot2fp.expTime" id="expTime" /></td>
                        <td>帧数：</td><td><input name="ot2fp.frameCount" id="frameCount"/></td></tr>
                      <tr><td>开始时间：</td><td><input name="ot2fp.begineTime" id="begineTime" class="timeinput" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate timeinput" 
                                                   title="如果开始时间与当前时间小于1分钟，则直接将观测计划发送给后随望远镜"/></td>
                        <td>结束时间：</td><td><input name="ot2fp.endTime" id="endTime" class="timeinput" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate timeinput" /></td></tr>
                      <tr><td>图像类型：</td><td>
                          <select height="30" name="ot2fp.imageType" id="imageType">
                            <option value="BIAS">BIAS</option>
                            <option value="DARK">DARK</option>
                            <option value="FLAT">FLAT</option>
                            <option value="LIGHT" selected="selected">LIGHT</option>
                            <option value="FOCUS">FOCUS</option>
                          </select></td>
                        <td>触发类型：</td><td>
                          <select height="30" name="ot2fp.triggerType" id="triggerType">
                            <option value="0">AUTO</option>
                            <option value="1">MANUAL</option>
                            <option value="2"  selected="selected">PLANNING</option>
                          </select></td></tr>
                      <tr>
                        <td>优先级：</td><td>
                          <select height="30" name="ot2fp.priority" id="priority">
                            <option value="10">10</option>
                            <option value="20">20</option>
                            <option value="30">30</option>
                            <option value="40">40</option>
                            <option value="50" selected="selected">50</option>
                            <option value="60">60</option>
                            <option value="70">70</option>
                            <option value="80">80</option>
                            <option value="90">90</option>
                          </select></td><td></td><td></td></tr>
                      <tr><td>备注：</td>
                        <td colspan="3">
                          <textarea name="ot2fp.comment" id="comment"  rows="2"  style="width: 600px;margin: 2px;"></textarea>
                        </td></tr>
                      <tr><td colspan="4" style="text-align:center;">
                          <input type="hidden" name="ot2fp.foName" id="foName" value=""/>
                          <input type="hidden" name="foId" id="foId" value="0"/>
                          <input type="hidden" name="ot2fp.epoch" id="epoch" value="2000"/>
                          <button type="button" id="genObsPlanBtn">提交</button>
                        </td></tr>
                    </table>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>

    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/followup-add.js"></script>


  </body>
</html>
