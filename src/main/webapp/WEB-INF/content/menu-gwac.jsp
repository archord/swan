<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<sj:menu id="subMenuAccordion">
  <s:url var="url6" action="gwac/gwac-allot"/>
  <sj:menuItem id="gwacallotlink" href="%{url6}" targets="content" title="OT列表" />
  
  <s:url var="url1" action="gwac/gwac-system"/>
  <sj:menuItem id="gwacsystemlink" href="%{url1}" targets="content" title="系统实时信息概览" />

  <s:url var="url2" action="gwac/gwac-grb"/>
  <sj:menuItem id="gwacgrblink" href="%{url2}" targets="content" title="GRB" />

  <s:url var="url3" action="gwac/gwac-move"/>
  <sj:menuItem id="gwacmovelink" href="%{url3}" targets="content" title="移动天体" />

  <s:url var="url4" action="gwac/gwac-boast"/>
  <sj:menuItem id="gwacboastlink" href="%{url4}" targets="content" title="耀星" />

  <s:url var="url5" action="gwac/gwac-variation"/>
  <sj:menuItem id="gwacvariationlink" href="%{url5}" targets="content" title="变星" />

  <s:url var="url7" action="gwac/gwac-sequence"/>
  <sj:menuItem id="gwacsequencelink" href="%{url7}" targets="content" title="光变序列" />

  <s:url var="url8" action="manage/pfile-upload"/>
  <sj:menuItem id="gwacuploadfilelink" href="%{url8}" targets="content" title="上传文件" />

  <s:url var="url9" action="manage/pdata-process-machine"/>
  <sj:menuItem id="gwacdataprocessmachinelink" href="%{url9}" targets="content" title="管理数据处理机信息" />
</sj:menu>
