<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<strong>望远镜信息:</strong>
<div id="result" class="result ui-widget-content ui-corner-all">
  <table border="1" style="padding: 3px;"  class="result ui-widget-content ui-corner-all">
    <thead>
      <tr>
        <th>望远镜</th>
        <th>故障原因</th>
        <th>时间</th>
        <th>是否消除</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>1号</td>
        <td>焦距模糊</td>
        <td>2013年12月22日</td>
        <td>未解决</td>
      </tr>
      <tr>
        <td>2号</td>
        <td>CCD故障</td>
        <td>2013年12月22日</td>
        <td>已解决</td>
      </tr>
    </tbody>
  </table>

</div>
<strong>转台信息:</strong>
<div id="result" class="result ui-widget-content ui-corner-all">

</div>
<strong>数据处理机信息:</strong>
<div id="result" class="result ui-widget-content ui-corner-all">

</div>
<strong>OT信息:</strong>
<div id="result" class="result ui-widget-content ui-corner-all">

</div>


<s:url var="ajax" value="/ajax1.action"/>
<sj:a id="ajaxlink" href="%{ajax}" targets="result" indicator="indicator" 
      button="true" buttonIcon="ui-icon-refresh" >
  Run AJAX Action
</sj:a>
<img id="indicator" src="images/indicator.gif" alt="Loading..." style="display:none"/>
