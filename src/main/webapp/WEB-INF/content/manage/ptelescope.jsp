<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<!--h5>望远镜信息管理</h5-->
<!--p class="text">
    望远镜信息主要包括编号，名称，望远镜ra， 望远镜dec，口径，焦比， CCD型号。
</p-->
<s:url var="remoteurl" action="get-telescope-list" namespace="/"/>
<s:url var="editurl" action="edit-telescope-entry" namespace="/"/>
<sjg:grid
  id="gridedittable"
  caption="望远镜信息管理"
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
  width="600"
  shrinkToFit="true"
  >
  <sjg:gridColumn name="tspId" frozen="true" index="tspIds" title="ID" width="30" editable="false"
                  sortable="false" search="false"/>
  <sjg:gridColumn name="name" index="name" title="名称" width="100" editable="true" edittype="text" sortable="false"
                  search="false"/>
  <sjg:gridColumn name="ra" index="ra" title="RA" sortable="false" editable="true"
                  edittype="text"/>
  <sjg:gridColumn name="dec" index="dec" title="DEC" sortable="false" editable="true"
                  edittype="text"/>
  <sjg:gridColumn name="diameter" index="diameter" title="直径" sortable="false" editable="true"
                  edittype="text"/>
  <sjg:gridColumn name="focalRatio" index="focalRatio" title="焦比" sortable="false" editable="true"
                  edittype="text"/>
  <sjg:gridColumn name="ccdType" index="ccdType" title="CCD型号" sortable="false" editable="true"
                  edittype="text"/>
</sjg:grid>

