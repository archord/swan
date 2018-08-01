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
    <title>GWAC观测-设置默认后随参数</title>
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
                  <div class="manual_container_title">
                    设置默认后随参数</div>
                </div>
              </div>
              <form action="${pageContext.request.contextPath}/gction/followUpParmSet.action" id="followUpParmSet">
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <table>
                      <tr><td width="150px">望远镜：</td><td width="250px">
                          <select height="30" name="telescope" id="telescope">
                            <option value="1">60公分</option>
                            <option value="2">30公分</option>
                          </select></td>
                        <td width="150px">滤光片：</td><td>
                          <select height="30" name="filter" id="filter">
                          </select></td></tr>
                      <tr><td>曝光时间：</td><td><input name="exposeDuration" id="exposeDuration" /></td>
                        <td>帧数：</td><td><input name="frameCount" id="frameCount"/></td></tr>
                      <tr><td>优先级：</td><td>
                          <select height="30" name="priority" id="priority">
                            <option value="10">10</option>
                            <option value="20">20</option>
                            <option value="30">30</option>
                            <option value="40">40</option>
                            <option value="50">50</option>
                            <option value="60">60</option>
                            <option value="70">70</option>
                            <option value="80">80</option>
                            <option value="90">90</option>
                          </select></td>
                          <td>是否自动后随</td><td><select height="30" name="autoFollowUp" id="autoFollowUp">
                            <option value="true">是</option>
                            <option value="false">否</option>
                          </select></td>
                      </tr>
                      <tr><td colspan="4" style="text-align:center;">
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
    <script src="<%=request.getContextPath()%>/resource/js/followup-parm.js"></script>


  </body>
</html>
