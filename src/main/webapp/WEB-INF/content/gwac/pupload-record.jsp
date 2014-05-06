<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:property value="echo" escape="%{escape}"/>

<!--strong>已注册用户列表:</strong-->
<s:url var="remoteurl" action="get-upload-file-record-list" namespace="/"/>
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
  rownumbers="false"
  width="650"
  viewrecords="true">
  <sjg:gridColumn name="ufrId"	  index="id"	  title="ID" width="30" formatter="integer" 
		  sortable="false" displayTitle="false"/>
  <sjg:gridColumn name="storePath"  index=""	  title="存储路径" 
		  sortable="true"/>
  <sjg:gridColumn name="fileName"   index="fileName"	  title="文件名" 
		  sortable="false"/>
  <sjg:gridColumn name="fileType"    index="fileType"	  title="文件类型" width="50" 
		  sortable="false"/>
  <sjg:gridColumn name="uploadSuccess"    index="uploadSuccess"	  title="是否上传成功" width="80" 
		  sortable="false"/>
  <sjg:gridColumn name="uploadDate" index="uploadDate" title="上传时间" formatter="date" width="150"  
		  formatoptions="{newformat : 'Y-m-d H:i:s', srcformat : 'Y-m-d H:i:s'}" 
		  sortable="false" align="left"/>
</sjg:grid>
