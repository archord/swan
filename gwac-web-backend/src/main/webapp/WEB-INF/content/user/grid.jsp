<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<strong>客户列表:</strong>
<s:url var="remoteurl" action="grid-data-provider" namespace="/"/>
<sjg:grid id="gridtable" caption="Customers Examples" dataType="json" 
  href="%{remoteurl}" pager="true" gridModel="gridModel"
  rowList="10,15,20" rowNum="15" rownumbers="true"
  resizable="true" width="700" shrinkToFit="true" >
  <sjg:gridColumn name="id" index="id" title="ID" width="30" formatter="integer" sortable="false" displayTitle="false"/>
  <sjg:gridColumn name="name" index="name" title="Name" width="290" sortable="true"/>
  <sjg:gridColumn name="country" index="country" width="100" title="Country" sortable="false"/>
  <sjg:gridColumn name="city" index="city" width="100" title="City" sortable="false"/>
  <sjg:gridColumn name="creditLimit" index="creditLimit" width="100" title="Credit Limit" align="right" formatter="currency" sortable="false"/>
</sjg:grid>