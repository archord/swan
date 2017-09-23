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
    <table id="obs-plan-table" class="display" cellspacing="0" width="100%">
      <thead><tr><th>1</th><th>ID</th><th>生成时间</th><th title="(GID-UID)">望远镜</th><th>观测类型</th><th title="(GID-FID)">天区</th>
          <th title="历元">位置(RA-DEC)</th><th>图像类型</th><th>曝光(延迟)时间</th><th>总帧数</th><th>优先级</th><th>起止时间</th>
          <th>ObjID</th><th title="(RA-DEC-ERR)J2000">目标位置</th><th>分组ID</th><th>计划类型</th></tr></thead>

      <tfoot><tr><th>1</th><th>ID</th><th>生成时间</th><th title="(GID-UID)">望远镜</th><th>观测类型</th><th title="(GID-FID)">天区</th>
          <th title="J2000">位置(RA-DEC)</th><th>图像类型</th><th>曝光(延迟)时间</th><th>总帧数</th><th>优先级</th><th>起止时间</th>
          <th>ObjID</th><th title="(RA-DEC-ERR)J2000">目标位置</th><th>分组ID</th><th>计划类型</th></tr></tfoot>
    </table>
  </div>  
</div>
