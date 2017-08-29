<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:property value="echo" escape="%{escape}"/>

<!--strong>已注册用户列表:</strong-->
<s:url var="remoteurl" action="user-list" namespace="/"/>
<s:url var="editurl" action="registerAction" namespace="/"/>
<!--width="700" resizable="true" 
shrinkToFit="true" 自动调节到表格的宽度 -->
<sjg:grid 
  id="gridedittable"
  caption="用户信息管理"
  dataType="json"
  href="%{remoteurl}"
  pager="true"
  navigator="true"
  navigatorAddOptions="{height:280,reloadAfterSubmit:true}"
  navigatorEditOptions="{height:280,reloadAfterSubmit:false}"
  navigatorSearch="false"
  navigatorEdit="false"
  navigatorView="false"
  navigatorDelete="true"
  navigatorDeleteOptions="{height:280,reloadAfterSubmit:true}"
  navigatorInlineEditButtons="true"
  gridModel="gridModel"
  rowList="10,15,20"
  rowNum="15"
  editurl="%{editurl}"
  editinline="true"
  onSelectRowTopics="rowselect"
  onEditInlineSuccessTopics="oneditsuccess"
  viewrecords="true"
  width="700"
  shrinkToFit="true"
  >
  <sjg:gridColumn name="uiId"	      index="id"	  title="ID" width="30" formatter="integer" 
		  sortable="false" displayTitle="false"/>
  <sjg:gridColumn name="name"	      index="name"	  title="姓名"  editable="true" edittype="text"
		  sortable="true"/>
  <sjg:gridColumn name="password"   index="password"	  title="密码"  editable="true" edittype="text"
		  sortable="false"/>
  <sjg:gridColumn name="address"    index="address"	  title="地址"  editable="true" edittype="text"
		  sortable="false"/>
  <sjg:gridColumn name="createDate" index="createDate" title="Credit Limit" formatter="date"  editable="true" edittype="text"
		  formatoptions="{newformat : 'Y-m-d H:i:s', srcformat : 'Y-m-d H:i:s'}" 
		  sortable="false" align="left"/>
</sjg:grid>
