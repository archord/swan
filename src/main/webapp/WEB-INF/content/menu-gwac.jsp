<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<sj:menu id="subMenuAccordion">
  <s:url var="url6" action="gwac/pgwac-allot"/>
  <sj:menuItem id="gwacallotlink" href="%{url6}" targets="content" title="OT列表" />
  
  <s:url var="url1" action="gwac/pgwac-system"/>
  <sj:menuItem id="gwacsystemlink" href="%{url1}" targets="content" title="系统实时信息概览" />

  <s:url var="url2" action="gwac/pgwac-grb"/>
  <sj:menuItem id="gwacgrblink" href="%{url2}" targets="content" title="GRB" />

  <s:url var="url3" action="gwac/pgwac-move"/>
  <sj:menuItem id="gwacmovelink" href="%{url3}" targets="content" title="移动天体" />

  <s:url var="url4" action="gwac/pgwac-boast"/>
  <sj:menuItem id="gwacboastlink" href="%{url4}" targets="content" title="耀星" />

  <s:url var="url5" action="gwac/pgwac-variation"/>
  <sj:menuItem id="gwacvariationlink" href="%{url5}" targets="content" title="变星" />

  <s:url var="url7" action="gwac/pgwac-sequence"/>
  <sj:menuItem id="gwacsequencelink" href="%{url7}" targets="content" title="光变序列" />
  
  <s:url var="url9" action="manage/ptelescope"/>
  <sj:menuItem id="ptelescopelink" href="%{url9}" targets="content" title="望远镜信息" />

  <s:url var="url10" action="manage/pdata-process-machine"/>
  <sj:menuItem id="gwacdataprocessmachinelink" href="%{url10}" targets="content" title="管理数据处理机信息" />

  <s:url var="url8" action="manage/pfile-upload"/>
  <sj:menuItem id="gwacuploadfilelink" href="%{url8}" targets="content" title="上传文件" />

  <s:url var="url11" action="gwac/pupload-unstore"/>
  <sj:menuItem id="puploadunstorelink" href="%{url11}" targets="content" title="已上传未入库文件列表" />

  <s:url var="url12" action="gwac/pupload-record"/>
  <sj:menuItem id="puploadrecordlink" href="%{url12}" targets="content" title="上传文件历史记录" />
</sj:menu>
