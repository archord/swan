<%-- 
    Document   : pctl-observing-tab2
    Created on : 2017-8-21, 16:56:16
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="tabs-monitor-table-div" style="width:100%; height:100%;margin:0 auto;border:0;">
  <div>
    待执行观测计划列表&nbsp;&nbsp;&nbsp;&nbsp;
    <select height="30"><option>全部望远镜</option>
      <option>望远镜01</option>
      <option>望远镜02</option>
      <option>望远镜03</option>
      <option>望远镜04</option>
      <option>望远镜05</option>
      <option>望远镜06</option>
      <option>望远镜07</option>
      <option>望远镜08</option>
    </select>
  </div>
  <div id="ot-list">
    <table id="ot-list-table" class="display" cellspacing="0" width="100%">
      <thead><tr><th>ID</th><th>生成时间</th><th>望远镜(GID-UID)</th><th>观测类型</th><th>天区(GID-FID)</th>
          <th>RA</th><th>DEC</th><th>历元</th>
          <th>图像类型</th><th>曝光时间</th><th>延迟时间</th><th>总帧数</th><th>优先级</th><th>开始时间</th><th>结束时间</th>
          <th>ObjID</th><th>目标RA</th><th>目标DEC</th><th>目标历元</th><th>位置误差</th><th>分组ID</th><th>计划类型</th></tr></thead>

      <tfoot><tr><th>ID</th><th>生成时间</th><th>望远镜(GID-UID)</th><th>观测类型</th><th>天区(GID-FID)</th>
          <th>RA</th><th>DEC</th><th>历元</th>
          <th>图像类型</th><th>曝光时间</th><th>延迟时间</th><th>总帧数</th><th>优先级</th><th>开始时间</th><th>结束时间</th>
          <th>ObjID</th><th>目标RA</th><th>目标DEC</th><th>目标历元</th><th>位置误差</th><th>分组ID</th><th>计划类型</th></tr></tfoot>
    </table>
  </div>  
</div>
