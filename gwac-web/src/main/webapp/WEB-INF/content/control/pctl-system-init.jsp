<%-- 
    Document   : rect
    Created on : 2016-8-9, 18:20:23
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="ocks-org do-not-copy">
  <head>
    <meta charset="utf-8">
    <title>GWAC系统初始化</title>
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.css" rel="stylesheet">
    <style>
      html {
        height: 99%;
        width: 100%;
        position: relative;
      }
      body {
        height: 100%;
        width: 100%;
        background: #FFF;
        position: relative;
      }
      #monitor-header{
        margin: 10px 0;
        float:left;
        width: 100%;
        display: block;
        position: relative;
        text-align: center;
        font-size: 32px;
      }
      #monitor-footer{
        float:left;
        width: 100%;
        display: block;
        position: relative;
        text-align: center;
      }
      #monitor-center {
        position: relative;
        float:left;
        display: block;
        width: 100%;
        min-height: 80%;
        background: #FFF;
      }
      .background {fill: #000;}
      line {stroke: #000;}
      text.active {fill: red;}
      #main{width:100%;padding: 5px 10px 5px 10px;}
      .axisLabel{fill: #fff; stroke: none;}
      #system-init{
        text-align: center;
      }
      #setStatus{
        height:34px;
        width:60px;
        border-radius: 3px;
        border-color: #ccc;
        cursor: pointer;
      }
      #setStatusBtn{
        background-color: #fff;
        border-color: #2e6da4;
        display: inline-block;
        padding: 6px 12px;
        margin-bottom: 0;
        font-size: 14px;
        font-weight: 400;
        line-height: 1.42857143;
        text-align: center;
        cursor: pointer;
        background-image: none;
        border: 1px solid #ccc;
        border-radius: 4px;
      }
    </style>
  </head>
  <body>
    <div style="display: none;">
      <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
    </div>      
    <div id="monitor-header">GWAC望远镜初始化</div>
    <div id="system-init">
      <form action="${pageContext.request.contextPath}/update-system-init-status.action" id="updateSystemInitStatusAction">
        <select name="mounts" id="mountsSelect" multiple="multiple" class="ot2QueryParameter">
          <option value="01">转台01</option>
          <option value="02">转台02</option>
          <option value="03">转台03</option>
          <option value="04">转台04</option>
          <option value="05">转台05</option>
          <option value="06">转台06</option>
          <option value="07">转台07</option>
          <option value="08">转台08</option>
          <option value="09">转台09</option>
          <option value="10">转台10</option>
        </select>
        <select name="ccds" id="ccdsSelect" multiple="multiple" class="ot2QueryParameter">
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
        </select>
        <select name="status" id="setStatus">
          <option value="1">上线</option>
          <option value="2" style="background-color:#ABABAB">下线</option>
          <option value="3" style="background-color:#00EE00" selected>正常</option>
          <option value="4" style="background-color:#EEAD0E">故障</option>
          <option value="5" style="background-color:#CD2626">损坏</option>
        </select>
        <input type="button" value="设置" id="setStatusBtn"/>
      </form>
    </div>
    <div id="monitor-center"></div>
    <div id="monitor-footer">
      <p>每个设备的状态用背景颜色标示：白（上线）、<span style="background-color: #00EE00">绿（正常）</span>、
        <span style="background-color: #EEAD0E">橙（有故障）</span>、
        <span style="background-color: #CD2626">红（损坏）</span>、
        <span style="background-color: #ABABAB">灰（下线）</span></p>
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/resource/js/telescope_monitor.js"></script>
    <script>

      $(function() {
        var root = "<%=request.getContextPath()%>";
        var url = "";
        var tmonitor = $.tmonitor("#monitor-center", root, url);
        tmonitor.initSelect();
        tmonitor.drawAll();
        tmonitor.initEffects();
        aa = tmonitor.svg.select("#mount01").attr("value");
        console.log("aa")
        console.log(aa)
//        setInterval(function() {
//          tmonitor.randomEffects();
//        }, 2000);
      });
    </script>
  </body>
</html>

