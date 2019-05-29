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
    <title>增加科学目标</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="增加科学目标" />
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
                    增加科学目标</div>
                </div>
              </div>
              <form action="${pageContext.request.contextPath}/saveSciObjFinal.action" id="genObsPlanForm">
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <table>
                    </table>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <table>
                      <tr><td>名称：</td><td><input name="name" id="name"/></td>
                        <td></td>
                        <td></td></tr>
                      <tr><td width="150px">发现时间：</td><td width="250px"><input name="discoveryTime" id="discoveryTime" class="timeinput" 
                                onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate timeinput"/></td>
                        <td width="150px">触发时间：</td><td><input name="triggerTime" id="triggerTime" class="timeinput" 
                                onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate timeinput"/></td></tr>
                      <tr><td>赤经(度)：</td>
                        <td><input name="ra" id="ra"  /></td>
                        <td>赤纬(度)：</td>
                        <td><input name="dec" id="dec"  /></td></tr>
                      <tr><td>探测星等：</td>
                        <td><input name="magDetect" id="magDetect"  /></td>
                        <td>匹配星等：</td>
                        <td><input name="magCatalog" id="magCatalog"  /></td></tr>
                      <tr><td>绝对星等：</td>
                        <td><input name="magAbsolute" id="magAbsolute"  /></td>
                        <td>星等变化：</td>
                        <td><input name="amplitude" id="amplitude"  /></td></tr>
                      
                      <tr><td>GWAC类型：</td><td>
                          <select height="30" name="gwacType" id="gwacType">
                            <option value="11">耀发候选体</option>
                            <option value="8">OT候选体</option>
                            <option value="15">变星</option>
                          </select></td><td>类型：</td>
                          <td><input name="type" id="type"  /></td></tr>
                      <tr><td>观测者：</td>
                        <td><input name="dutyScientist" id="dutyScientist"  /></td>
                        <td>来源：</td>
                        <td><input name="source" id="source"  /></td></tr>
                      <tr><td>备注：</td>
                        <td colspan="3">
                          <textarea name="comments" id="comments"  rows="2"  style="width: 600px;margin: 2px;"></textarea>
                        </td></tr>
                      <tr><td colspan="4" style="text-align:center;">
                          <input type="hidden" name="sofId" id="sofId" value="0"/>
                          <input type="hidden" name="publicMsg" id="publicMsg" value=""/>
                          <input type="hidden" name="followup" id="followup" value=""/>
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
    <script src="<%=request.getContextPath()%>/resource/js/science-object-final-add.js"></script>


  </body>
</html>
