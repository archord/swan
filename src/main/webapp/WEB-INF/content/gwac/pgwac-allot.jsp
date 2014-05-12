<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<sj:dialog 
  id="ot_detail" 
  title="OT详细内容" 
  autoOpen="false" 
  modal="true"
  resizable="true"
  position="top"
  draggable="true"
  width="800"
  height="500"
  />
<s:url var="otDetail" action="pgwac-ot-detail" />

<script type="text/javascript">
  function formatLink(cellvalue, options, rowObject) {
    return "<a href='#' title='点击查看OT详细' onClick='return openDialog(\"" + cellvalue + "\");'>" + cellvalue + "</a>";
  }

  function openDialog(otName) {
    $("#ot_detail").load("<s:property value="otDetail"/>?id=" + otName);
    //$("#ot_detail").load("/svom/imageGallery.html");
    $("#ot_detail").dialog('open');
    return false;
  }
</script>

<s:property value="echo" escape="%{escape}"/>

<!--strong>已注册用户列表:</strong-->
<s:url var="remoteurl" action="get-ot-base-list" namespace="/"/>
<!--width="700" resizable="true" 
shrinkToFit="true" 自动调节到表格的宽度 -->
<sjg:grid 
  id="gridtable" 
  caption="上传文件历史记录" 
  dataType="json" 
  href="%{remoteurl}" 
  pager="true" 
  gridModel="gridModel"
  rowList="10,15,20" 
  rowNum="15" 
  rownumbers="true"
  width="650"
  viewrecords="true">
  <sjg:gridColumn name="name"   index="name"	  title="OT名" width="140"  formatter="formatLink"
                  sortable="false" align="center"/>
  <sjg:gridColumn name="ra"    index="ra"	  title="RA" width="80" 
                  sortable="false" align="center"/>
  <sjg:gridColumn name="dec"    index="dec"	  title="DEC" width="80" 
                  sortable="false" align="center"/>
  <sjg:gridColumn name="xtemp"    index="xtemp"	  title="模板X" width="80" 
                  sortable="false" align="center"/>
  <sjg:gridColumn name="ytemp"    index="ytemp"	  title="模板Y" width="80" 
                  sortable="false" align="center"/>
  <sjg:gridColumn name="identify"    index="identify"	  title="标识字符串" width="190" 
                  sortable="false" align="center"/>
</sjg:grid>

  <!--sjg:gridColumn name="foundTimeUtc" index="foundTimeUtc" title="发现时间(UTC)" formatter="date" width="150"  
                  formatoptions="{newformat : 'Y-m-d H:i:s', srcformat : 'Y-m-d H:i:s'}" 
                  sortable="false" align="center"/-->