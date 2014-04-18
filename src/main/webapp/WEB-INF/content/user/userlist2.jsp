<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:property value="echo" escape="%{escape}"/>

<!--strong>已注册用户列表:</strong-->
<s:url var="remoteurl" action="user-list" namespace="/"/>
<!--width="700" resizable="true" 
shrinkToFit="true" 自动调节到表格的宽度 -->
<sjg:grid 
  id="gridtable" 
  caption="已注册用户列表" 
  dataType="json" 
  href="%{remoteurl}" 
  pager="true" 
  gridModel="gridModel"
  rowList="10,15,20" 
  rowNum="15" 
  rownumbers="true"
  viewrecords="true">
  <sjg:gridColumn name="uiId"	      index="id"	  title="ID" width="30" formatter="integer" 
		  sortable="false" displayTitle="false"/>
  <sjg:gridColumn name="name"	      index="name"	  title="Name" 
		  sortable="true"/>
  <sjg:gridColumn name="password"   index="password"	  title="Country" 
		  sortable="false"/>
  <sjg:gridColumn name="address"    index="address"	  title="City" 
		  sortable="false"/>
  <sjg:gridColumn name="createDate" index="createDate" title="Credit Limit" formatter="date" 
		  formatoptions="{newformat : 'Y-m-d H:i:s', srcformat : 'Y-m-d H:i:s'}" 
		  sortable="false" align="left"/>
</sjg:grid>
