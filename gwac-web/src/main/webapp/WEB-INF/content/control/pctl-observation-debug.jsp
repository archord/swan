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
    <title>GWAC观测-设备调试</title>
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
      .manual_container1{
        border: 3px solid #c5c5c5;
        margin: 0 0 10px 0;
        padding: 5px;
        text-align: center;
        width: 100%;
      }
      .tel-center-div{
        margin: 0 auto;
        width: 450px;
        text-align: left;
        height: 150px;
      }
      .cam-center-div{
        margin: 0 auto;
        width: 680px;
        text-align: left;
        height: 150px;
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
        width: 100px;
      }
      .manual_container1 select {
        width: 100px;
        background: #fff;
        margin: 2px;
        height: 28px;
      }
      .manual_container_title{
        text-align: center;
        font-size: 16px;
        background-color: #eee;
        margin:  -5px 0px 5px 0px;
      }
      .manual_container2{
        border: 3px solid #c5c5c5;
        min-height: 343px;
        padding: 5px;
        margin-bottom: 10px;
      }
      .manual_container1_col{padding: 5px}
      .background {fill: #000;}
      line {stroke: #000;}
    </style>  
  </head>
  <body>

    <div id="tabs" style="width:98%; height:100%;margin:0 auto;border:0;">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>      

      <div class="tab_container">
        <div class="row">
          <div class="col-xs-12 col-sm-12 col-md-5 manual_container1_col">
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    转台操作</div>
                </div>
              </div>
              <form action="/gwebend/observationPlanUpload.action" id="genObsPlanForm">
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <div class="tel-center-div">
                      <table>
                        <tr><td width="80px">操作指令：</td><td  width="150px">
                            <select height="30" name="obsType" id="obsType">
                              <option value="" selected>未选择</option>
                              <option value="find_home">搜索零点</option>
                              <option value="home_sync">修正零点</option>
                              <option value="slewto">指向赤道坐标</option>
                              <option value="guide">导星</option>
                              <option value="park">复位</option>
                              <option value="abort_slew">终止指向</option>
                            </select></td><td></td><td></td></tr>
                        <tr><td width="80px">望远镜组：</td><td width="150px">
                            <select height="30" name="groupId" id="groupId">
                              <option value="">未选择</option>
                              <option value="001" selected>组001</option>
                            </select></td>
                          <td width="80px">单元(转台)：</td><td>
                            <select height="30" name="unitId" id="unitId">
                              <option value="">未选择</option>
                              <option value="001">转台01</option>
                              <option value="002">转台02</option>
                              <option value="003">转台03</option>
                              <option value="004">转台04</option>
                              <option value="005">转台05</option>
                              <option value="006">转台06</option>
                              <option value="007">转台07</option>
                              <option value="008">转台08</option>
                              <option value="009">转台09</option>
                              <option value="010">转台10</option>
                            </select></td></tr>
                        <tr><td>赤经(度)：</td>
                          <td><input name="ra" id="ra" style="text-align: right;" value="0"/></td>
                          <td>赤纬(度)：</td>
                          <td><input name="dec" id="dec" style="text-align: right;" value="0"/></td></tr>
                        <tr><td>赤经(时)：</td>
                          <td>
                            <input name="rah" id="rah" style="width:24px;text-align: right;" value="0"/>:
                            <input name="ram" id="ram" style="width:24px;text-align: right;" value="0"/>:
                            <input name="ras" id="ras" style="width:24px;text-align: right;" value="0"/></td>
                          <td>赤纬(度)：</td>
                          <td>
                            <input name="decd" id="decd" style="width:24px;text-align: right;" value="0"/>:
                            <input name="decm" id="decm" style="width:24px;text-align: right;" value="0"/>:
                            <input name="decs" id="decs" style="width:24px;text-align: right;" value="0"/></td></tr>
                        <tr><td colspan="4" style="text-align:center;"><button type="button" id="genObsPlanBtn">执行</button></td></tr>
                      </table>

                    </div></div>
                </div>
              </form>
            </div>
          </div>
          <div class="col-xs-12 col-sm-12 col-md-7 manual_container1_col">
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    相机操作</div>
                </div>
              </div>
              <form action="/gwebend/observationPlanUpload.action" id="genObsPlanForm">
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <div class="cam-center-div">
                      <table>
                        <tr><td>操作指令：</td><td>
                            <select height="30" name="obsType" id="obsType">
                              <option value="" selected>未选择</option>
                              <option value="focus">调焦</option>
                              <option value="focus_sync">修正调焦零点</option>
                              <option value="mcover">开关镜盖</option>
                              <option value="take_image">启动曝光</option>
                              <option value="abort_image">终止曝光</option>
                            </select></td>
                            <td>镜盖开关：</td><td>
                            <select height="30" name="obsType" id="obsType">
                              <option value="open">开</option>
                              <option value="close">关</option>
                            </select></td>
                        </tr>
                        <tr><td width="95px">望远镜组：</td><td width="120px">
                            <select height="30" name="groupId" id="groupId">
                              <option value="">未选择</option>
                              <option value="001" selected>组001</option>
                            </select></td>
                          <td width="80px">单元(转台)：</td><td  width="120px">
                            <select height="30" name="unitId" id="unitId">
                              <option value="">未选择</option>
                              <option value="001">转台01</option>
                              <option value="002">转台02</option>
                              <option value="003">转台03</option>
                              <option value="004">转台04</option>
                              <option value="005">转台05</option>
                              <option value="006">转台06</option>
                              <option value="007">转台07</option>
                              <option value="008">转台08</option>
                              <option value="009">转台09</option>
                              <option value="010">转台10</option>
                            </select></td>
                          <td width="95px">相机：</td><td  width="120px">
                            <select height="30" name="ccds" id="ccdsSelect">
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
                              <option value="051">051</option>
                              <option value="052">052</option>
                              <option value="053">053</option>
                              <option value="054">054</option>
                              <option value="055">055</option>
                              <option value="061">061</option>
                              <option value="062">062</option>
                              <option value="063">063</option>
                              <option value="064">064</option>
                              <option value="065">065</option>
                              <option value="071">071</option>
                              <option value="072">072</option>
                              <option value="073">073</option>
                              <option value="074">074</option>
                              <option value="075">075</option>
                              <option value="081">081</option>
                              <option value="082">082</option>
                              <option value="083">083</option>
                              <option value="084">084</option>
                              <option value="085">085</option>
                              <option value="091">091</option>
                              <option value="092">092</option>
                              <option value="093">093</option>
                              <option value="094">094</option>
                              <option value="095">095</option>
                              <option value="101">101</option>
                              <option value="102">102</option>
                              <option value="103">103</option>
                              <option value="104">104</option>
                              <option value="105">105</option>
                            </select></td></tr>
                        <tr><td>目标ID：</td>
                          <td><input name="dec" id="dec" style="text-align: right;" value="0"/></td>
                          <td>图像类型：</td>
                          <td><select height="30" name="imgType" id="imgType">
                              <option value="">未选择</option>
                              <option value="bias">本底</option>
                              <option value="dark">暗场</option>
                              <option value="flat">平场</option>
                              <option value="light" selected>常规观测</option>
                              <option value="focus">调焦</option>
                            </select></td>
                          <td>曝光时间(秒):</td>
                          <td><input name="expusoreDuring" id="expusoreDuring" style="text-align: right;" value="10"/></td></tr>
                        <tr><td>曝光间隔(秒)：</td>
                          <td><input name="dec" id="delay" style="text-align: right;" value="5"/></td>
                          <td>观测帧数：</td>
                          <td><input name="frameCount" id="frameCount"style="text-align: right;" /></td>
                          <td>滤光片:</td>
                          <td><input name="filter" id="expusoreDuring" style="text-align: right;" value=""/></td></tr>
                        <tr><td colspan="6" style="text-align:center;"><button type="button" id="genObsPlanBtn">执行</button></td></tr>
                      </table>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-xs-12 col-sm-12 col-md-12 manual_container1_col">
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    操作历史</div>
                </div>
              </div>
              <form action="/gwebend/observationPlanUpload.action" id="genObsPlanForm">
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">

                  </div>
                </div>
              </form>
            </div></div>
        </div>
      </div>

    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/pctl-observation-manual.js"></script>


  </body>
</html>
