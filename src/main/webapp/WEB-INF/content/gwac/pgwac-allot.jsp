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
<s:url var="otDetail" action="get-ot-image-list" namespace="/"/>

<script type="text/javascript">
  function formatLink(cellvalue, options, rowObject) {
    return "<a href='#' title='点击查看OT详细' onClick='return openDialog(\"" + cellvalue + "\");'>" + cellvalue + "</a>";
  }

  function openDialog(otName) {
    //$("#ot_detail").load("<s:property value="otDetail"/>?otName=" + otName);
    //$("#ot_detail").load("/svom/imageGallery.html");
    //$("#ot_detail").dialog('open');
//    window.open("<s:property value="otDetail"/>?otName=" + otName,
//	    'otDetailWindow',
//	    'height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=yes');
    openwindow("<s:property value="otDetail"/>?otName=" + otName,
            'otDetailWindow', 800, 500, 800, 500);
    return false;
  }
  function openwindow(url, name, width, height, iWidth, iHeight)
  {
    var iTop = (window.screen.availHeight - 30 - iHeight) / 2;       //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;           //获得窗口的水平位置;
    window.open(url, name,
            'height=' + height +
            ',innerHeight=' + iHeight +
            ',width=' + width +
            ',innerWidth=' + iWidth +
            ',top=' + iTop +
            ',left=' + iLeft +
            ',toolbar=no,menubar=no,scrollbars=auto,resizeable=yes,location=no,status=yes');
  }
</script>

<s:property value="echo" escape="%{escape}"/>

<div style="width:650px;text-align: left;">
  <s:form id="getOtBaseListForm"  action="get-ot-base-list" theme="simple" cssClass="yform" namespace="/">
    <table style="margin:auto; width:80%;">
      <tr>
        <td>开始日期：</td>
        <td><sj:datepicker value="yesterday" id="from" name="startDate" displayFormat="yy-mm-dd" label="开始日期" />
          <s:hidden name="isQuery" value="true"/></td>
        <td>结束日期：</td>
        <td><sj:datepicker value="today" id="to" name="endDate" displayFormat="yy-mm-dd" label="结束日期" /></td>
      </tr>
      <tr>
        <td>模板X坐标：</td>
        <td><sj:textfield name="xTemp" /></td>
        <td>模板Y坐标：</td>
        <td><sj:textfield name="yTemp" /></td>
      </tr>
      <tr>
        <td>望远镜：</td>
        <td><select name="telscope" >
            <option value="M01">M01</option>
            <option value="M02">M02</option>
            <option value="M03">M03</option>
            <option value="M04">M04</option>
            <option value="M05">M05</option>
            <option value="M06">M06</option>
            <option value="M07">M07</option>
            <option value="M08">M08</option>
            <option value="M09">M09</option>
            <option value="M10">M10</option>
            <option value="M11">M11</option>
            <option value="M12">M12</option>
          </select>
        </td>
        <td>搜索半径：</td>
        <td><sj:textfield name="searchRadius" /></td>
      </tr>
      <tr><td colspan="4" style="text-align: center;">
          <sj:a
            button="true"
            onClickTopics="reloadOtGrid"
            indicator="indicator"
            style="height:28px;width:80px;"
            >搜索</sj:a>
          </td></tr>
      </table>
  </s:form>
</div>

<!--strong>已注册用户列表:</strong-->
<s:url var="remoteurl" action="get-ot-base-list" namespace="/"/>
<!--width="700" resizable="true" 
shrinkToFit="true" 自动调节到表格的宽度 -->
<!--不能添加loadonce="true"，否则上面的“搜索”提交不起作用-->
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
  viewrecords="true"
  reloadTopics="reloadOtGrid"
  formIds="getOtBaseListForm">
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


<!--  navigator="true"
  navigatorSearch="true"
  formIds="formId"
  navigatorSearchOptions="{multipleSearch:true,
  sopt:['cn'],
  reloadAfterSubmit:true,
  }"
  navigatorAdd="false"
  navigatorEdit="true"
  navigatorView="false"
  navigatorDelete="false"
  navigatorInlineEditButtons="false" -->