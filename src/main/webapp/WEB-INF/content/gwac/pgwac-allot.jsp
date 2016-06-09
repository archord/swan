<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<!DOCTYPE html>
<html lang="ch"> 
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC数据展示页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

    <title>GWAC信息展示平台 - <s:text name="showcase.version"/></title>


    <s:if test="%{theme == 'showcase' || theme == null}">
      <sj:head debug="true" compressed="true" jquerytheme="showcase" customBasepath="themes" 
               loadFromGoogle="%{google}" ajaxhistory="%{ajaxhistory}" 
               defaultIndicator="myDefaultIndicator" defaultLoadingText="Please wait ..."/>
    </s:if>
    <s:else>
      <sj:head debug="true" compressed="true" jquerytheme="%{theme}" loadFromGoogle="%{google}" 
               ajaxhistory="%{ajaxhistory}" defaultIndicator="myDefaultIndicator" 
               defaultLoadingText="Please wait ..."/>
    </s:else>
    <link href="<s:url value="/resource/themes/showcase/jquery-ui.css" />" rel="stylesheet" type="text/css" />
    <link href="<s:url value="/resource/styles/flexible-grids.css" />" rel="stylesheet" type="text/css" />
    <!--[if lte IE 7]>
    <link href="<s:url value="/resource/yaml/core/iehacks.min.css" />" rel="stylesheet" type="text/css" />
    <![endif]-->

    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- This files are needed for AJAX Validation of XHTML Forms -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/struts/utils.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/struts/xhtml/validation.js"></script>

    <!-- This file includes necessary functions/topics for validation and all topic examples -->
    <script type="text/javascript" src="<s:url value="/resource/js/showcase.js" />"></script>
    <!-- Extend the Struts2 jQuery Plugin with an richtext editor -->
    <script type="text/javascript" src="<s:url value="/resource/js/extendplugin.js" />"></script>
    <link href="${pageContext.request.contextPath}/resource/css/gwac-style.css" rel="stylesheet" type="text/css" />
    

  </head>
  <body>

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
    <s:url var="otDetail" action="gwac/pgwac-ot-detail2" namespace="/"/>

    <s:property value="echo" escape="%{escape}"/>

    <div class="ot-query-form">
      <s:form id="getOtListForm"  action="query-ot-level2" theme="simple" cssClass="yform" namespace="/">
        <table style="margin:auto; width:90%;">
          <tr style="height:20px;">
            <td>OT名称：</td>
            <td><sj:textfield name="ot2qp.otName" /></td>
            <td>开始日期(UTC)：</td>
            <!--value="yesterday" value="today" -->
            <td><sj:datepicker id="from" name="ot2qp.startDate" displayFormat="yy-mm-dd" label="开始日期" /></td>
            <td>结束日期(UTC)：</td>
            <td><sj:datepicker  id="to" name="ot2qp.endDate" displayFormat="yy-mm-dd" label="结束日期" />
              <input type="hidden" id="queryHis" value="<s:property value="total"/>"/></td>
          </tr>
          <tr style="height:20px;">
            <td>X坐标(模板)：</td>
            <td><sj:textfield name="ot2qp.xtemp" /></td>
            <td>Y坐标(模板)：</td>
            <td><sj:textfield name="ot2qp.ytemp" /></td>
            <td>搜索半径(pix)：</td>
            <td><sj:textfield name="ot2qp.planeRadius" /></td>
          </tr>
          <tr style="height:20px;">
            <td>Ra(度)：</td>
            <td><sj:textfield name="ot2qp.ra" /></td>
            <td>Dec(度)：</td>
            <td><sj:textfield name="ot2qp.dec" /></td>
            <td>搜索半径(度)：</td>
            <td><sj:textfield name="ot2qp.sphereRadius" /></td>
          </tr>
          <tr style="height:20px;">
            <td>数据处理方式：</td>
            <td><select name="ot2qp.processType" >
                <option value="">All</option>
                <option value="1">星表匹配</option>
                <option value="8">图像相减</option>
              </select></td>
            <td>匹配状态：</td>
            <td><select name="ot2qp.isMatch" >
                <option value="">All</option>
                <option value="1">不成功</option>
                <option value="0">未匹配</option>
                <option value="2">成功</option>
              </select></td>
            <td>匹配类别：</td>
            <td>
              <select name="ot2qp.matchType">
                <option value="">All</option>
                <option value="cvs_match">CVS</option>
                <option value="rc3_match">RC3</option>
                <option value="minor_planet_match">小行星</option>
                <option value="ot2_his_match">OT2历史</option>
                <option value="other_match">其他类型</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>CCD：</td>
            <td><select name="ot2qp.telscope" >
                <option value="">All</option>
                <option value="1">M01</option>
                <option value="2">M02</option>
                <option value="3">M03</option>
                <option value="4">M04</option>
                <option value="5">M05</option>
                <option value="6">M06</option>
                <option value="7">M07</option>
                <option value="8">M08</option>
                <option value="9">M09</option>
                <option value="10">M10</option>
                <option value="11">M11</option>
                <option value="12">M12</option>
              </select>
            </td>
            <td>分类标识：</td>
            <td>
              <select name="ot2qp.otType">
                <option value="">All</option>
                <option value="1">假OT</option>
                <option value="2">小行星</option>
                <option value="3">移动目标</option>
                <option value="4">鬼像</option>
                <option value="5">坏像素</option>
                <option value="6">坏像列</option>
                <option value="7">热像素</option>
                <option value="8">OT候选体</option>
                <option value="9">超新星</option>
                <option value="10">GRB</option>
                <option value="11">耀发候选体</option>
                <option value="12">灰尘</option>
                <option value="13">彗星</option>
                <option value="14">近地天体</option>
                <option value="15">变星</option>
                <option value="16">空场</option>
                <option value="17">凹槽</option>
                <option value="18">跟踪不好</option>
              </select></td>
            <td>回看：</td>
            <td>
              <select name="ot2qp.lookBackResult" id="lookBackResult">
                <option value="">All</option>
                <option value="0">未处理</option>
                <option value="1">OT</option>
                <option value="2">FOT</option>
              </select></td>
          </tr>
          <tr style="height:20px;"><td colspan="6" style="text-align: center;">
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
    <s:url var="remoteurl" action="get-ot-level2-list" namespace="/"/>
    <!--width="700" resizable="true" 
    shrinkToFit="true" 自动调节到表格的宽度 -->
    <!--不能添加loadonce="true"，否则上面的“搜索”提交不起作用-->
    <sjg:grid 
      id="gridtable" 
      cssClass="ot-query-grid"
      cssStyle="margin:auto;"
      shrinkToFit="true"
      caption="二级OT列表" 
      dataType="json" 
      href="%{remoteurl}" 
      pager="true" 
      gridModel="gridModel"
      rowList="10,15,30" 
      rowNum="15" 
      rownumbers="true"
      width="1170"
      viewrecords="true"
      reloadTopics="reloadOtGrid"
      formIds="getOtListForm">
      <sjg:gridColumn name="name"   index="name"	  title="OT名" width="150"  formatter="formatLink"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="ra"    index="ra"	  title="RA" width="80" formatter="floatFormate4"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="dec"    index="dec"	  title="DEC" width="80" formatter="floatFormate4"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="xtemp"    index="xtemp"	  title="模板X" width="80" formatter="floatFormate2"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="ytemp"    index="ytemp"	  title="模板Y" width="80" formatter="floatFormate2"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="identify"    index="identify"	  title="首帧标识字符串" width="230" 
                      sortable="false" align="center"/>
      <sjg:gridColumn name="total"    index="total"	  title="记录总数" width="70" 
                      sortable="false" align="center"/>
      <sjg:gridColumn name="rc3Match"    index="rc3Match"	  title="RC3" width="50"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="minorPlanetMatch"    index="minorPlanetMatch"	  title="小行星" width="70"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="cvsMatch"    index="cvsMatch"	  title="CVS" width="50"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="otherMatch"    index="otherMatch"	  title="Other" width="70"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="ot2HisMatch"    index="ot2HisMatch"	  title="OT2" width="50"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="firstNMark"    index="firstNMark"	  title="前N分钟" width="70" formatter="formatLink1"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="foCount"    index="foCount"	  title="后随次数" width="70" 
                      sortable="false" align="center"/>
      <sjg:gridColumn name="otType"    index="otType"	  title="分类标识" width="70" formatter="formateOtType"
                      sortable="false" align="center"/>
      <sjg:gridColumn name="name"    index="ot2Download"	  title="下载" width="70" formatter="ot2InfoDownload"
                      sortable="false" align="center"/>
    </sjg:grid>


    <script type="text/javascript">

      $(function() {
        var ot2arr;
        loadOT2Type();

      });
      function formatLink(cellvalue, options, rowObject) {
        var url = "<s:property value="otDetail"/>?otName=" + cellvalue;
        return "<a href='" + url + "' target='_blank' title='点击查看OT详细'>" + cellvalue + "</a>";
      }

      function formatLink1(cellvalue, options, rowObject) {
        var showVal = "";
        if (cellvalue === false) {
          showVal = "否";
        } else if (cellvalue === true) {
          showVal = "是";
        }
        return showVal;
      }

      function floatFormate4(cellvalue, options, rowObject) {
        return cellvalue.toFixed(4);
      }

      function floatFormate2(cellvalue, options, rowObject) {
        return cellvalue.toFixed(2);
      }

      function formatLinkn(cellvalue, options, rowObject) {
        var showVal = "";
        if (cellvalue === 0) {
          showVal = "未匹配";
        } else if (cellvalue === 1) {
          showVal = "不成功";
        } else if (cellvalue === 2) {
          showVal = "成功";
        }
        return showVal;
      }

      function formateOtType(cellvalue, options, rowObject) {
        var rst;
        console.log(cellvalue);
        if (cellvalue >= 0) {
          rst = ot2arr[cellvalue].ottName;
        } else {
          rst = "未分类";
        }
        return rst;
      }

      function ot2InfoDownload(otName, options, rowObject) {
        var gwacRootURL = "${pageContext.request.contextPath}";
        var downloadUrl = gwacRootURL + "/downloadot2.action?otName=" + otName;
        var resultStr;
        if (otName.charAt(8) === 'C') {
          resultStr = "<a href='" + downloadUrl + "' target='_blank' title='点击下载OT详细信息'>下载</a>";
        } else {
          resultStr = "<a href='#' target='_blank' onclick='return false;' title='图像相减OT暂时没有下载内容'>下载</a>";
        }
        return resultStr;
      }

      function openDialog(url, otName) {
        var queryHis = $("#queryHis").val();
        openwindow(url + "?otName=" + otName + "&queryHis=" + queryHis,
                '_blank', 1050, 600, 1050, 600);
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

      function loadOT2Type() {
        var gwacRootURL = "${pageContext.request.contextPath}";
        console.log(gwacRootURL);
        var queryUrl = gwacRootURL + "/get-ot-type-json.action";
        $.ajax({
          type: "get",
          url: queryUrl,
          data: '{}',
          async: false,
          dataType: 'json',
          success: function(data) {
            ot2arr = data.otTypes;
          }
        });
      }

    </script>

  </body>
</html>