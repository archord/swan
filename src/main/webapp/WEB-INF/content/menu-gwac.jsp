<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<sj:menu id="subMenuAccordion">
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
  
  <s:url var="url6" action="gwac/gwac-allot"/>
  <sj:menuItem id="gwacallotlink" href="%{url6}" targets="content" title="全部OT" />

  <s:url var="url7" action="gwac/gwac-sequence"/>
  <sj:menuItem id="gwacsequencelink" href="%{url7}" targets="content" title="光变序列" />
</sj:menu>
