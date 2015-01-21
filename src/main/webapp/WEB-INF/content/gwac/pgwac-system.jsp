<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<strong>
  <a href="<%=request.getContextPath() %>/gwac/pgwac-ccd-image-realtime.action" target="_blank">CCD图像预览&nbsp;&nbsp;&nbsp;</a>
  <a href="<%=request.getContextPath() %>/gwac/pgwac-ot-realtimedisplay.action" target="_blank">OT实时分布-XY&nbsp;&nbsp;&nbsp;</a>
  <a href="<%=request.getContextPath() %>/gwac/pgwac-ot-realtimedisplay-sphere.action" target="_blank">OT实时分布-RaDec&nbsp;&nbsp;&nbsp;</a>
  <!--<a href="<%=request.getContextPath() %>/gwac/pgwac-ot-realtimedisplay-sphere2.action" target="_blank">OT实时分布4&nbsp;&nbsp;&nbsp;</a>-->
</strong><br/><br/>
<strong>望远镜信息:</strong>
<div id="tlp-info" class="result ui-widget-content ui-corner-all">
  <table style="padding: 3px;"  class="table-style">
    <thead>
      <tr><th>望远镜</th><th>故障原因</th><th>时间</th><th>是否消除</th></tr>
    </thead>
    <tbody>
      <tr><td>1号</td><td>焦距模糊</td><td>2013年12月22日</td><td>未解决</td></tr>
      <tr><td>2号</td><td>CCD故障</td><td>2013年12月22日</td><td>已解决</td></tr>
    </tbody>
  </table>

</div>
<strong>转台信息:</strong>
<div id="turn-info" class="result ui-widget-content ui-corner-all">
  <table style="padding: 3px;"  class="table-style">
    <thead>
      <tr><th>转台</th><th>故障原因</th><th>时间</th><th>是否消除</th></tr>
    </thead>
    <tbody>
      <tr><td>3号</td><td>连接线路故障</td><td>2013年12月22日</td><td>未解决</td></tr>
      <tr><td>5号</td><td>转轴卡住</td><td>2013年12月22日</td><td>已解决</td></tr>
    </tbody>
  </table>

</div>
<strong>数据处理机信息:</strong>
<div id="mch-info" class="result ui-widget-content ui-corner-all">
  <table style="padding: 3px;"  class="table-style">
    <thead>
      <tr><th>处理机器</th><th>故障原因</th><th>时间</th><th>是否消除</th></tr>
    </thead>
    <tbody>
      <tr><td>M7</td><td>磁盘已满</td><td>2013年12月22日</td><td>未解决</td></tr>
      <tr><td>M8</td><td>无法链接</td><td>2013年12月22日</td><td>已解决</td></tr>
    </tbody>
  </table>

</div>
<strong>OT信息:</strong>
<div id="ot-info" class="result ui-widget-content ui-corner-all">
  <table style="padding: 3px;"  class="table-style">
    <thead>
      <tr><th>序号</th><th>OT数量</th><th>时间</th></tr>
    </thead>
    <tbody>
      <tr><td>1</td><td>5个</td><td>2013年12月22日</td></tr>
      <tr><td>2</td><td>20个</td><td>2013年12月22日</td></tr>
    </tbody>
  </table>

</div>
