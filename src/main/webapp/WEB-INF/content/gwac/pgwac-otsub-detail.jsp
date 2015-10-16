<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<!DOCTYPE html>
<html lang="ch"> 
    <head>  
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <meta http-equiv="Content-Style-Type" content="text/css" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="0" />
        <meta http-equiv="keywords" content=",国家天文台，svom, gwac，望远镜阵列" />
        <meta http-equiv="description" content="GWAC数据展示页面" />
        <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/sysimg/favicon.ico"/>

        <title>OT-<s:property value="otName"/>-详细页面</title>

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

        <link href="${pageContext.request.contextPath}/themes/showcase/jquery-ui.css?s2j=3.6.1" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/styles/flexible-grids.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/resource/css/gwac-style.css" rel="stylesheet" type="text/css" />
        <!--[if lte IE 7]>
        <link href="${pageContext.request.contextPath}/yaml/core/iehacks.min.css" rel="stylesheet" type="text/css" />
        <![endif]-->

        <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

        <!-- This files are needed for AJAX Validation of XHTML Forms -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/struts/utils.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/struts/xhtml/validation.js"></script>


        <!-- This file includes necessary functions/topics for validation and all topic examples -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/showcase.js" />"></script>
    <!-- Extend the Struts2 jQuery Plugin with an richtext editor -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/extendplugin.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.flot.js"></script>
    <script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/plot/jquery.flot.categories.js"></script>
    <script type="text/javascript">
        $(function() {

            $("<div id='tooltip'></div>").css({
                position: "absolute",
                display: "none",
                border: "1px solid #fdd",
                padding: "2px",
                "background-color": "#fee",
                opacity: 0.80
            }).appendTo("body");

            var option1 = {
                legend: {show: false},
                series: {shadowSize: 0},
                points: {show: true},
                lines: {show: true, fill: false},
                grid: {hoverable: true, color: '#646464', borderColor: 'transparent', borderWidth: 20, clickable: true},
                selection: {mode: "xy"},
                xaxis: {show: true, tickColor: 'transparent'},
                yaxis: {show: true, tickDecimals: 2, tickFormatter: formate1, transform: formate2, inverseTransform: formate2}
            };
            //min: 0, max: 10, tickSize: 1, tickDecimals: 2,

            function formate1(val, axis) {
                return (val).toFixed(axis.tickDecimals);
            }

            function formate2(val) {
                return -val;
            }

            function formate3(val) {
                return val;
            }

            var opticalData = [{
                    data: <s:property value="otOpticalVaration"/>,
                    color: '#71c73e',
                    points: {radius: 2} //fillColor: '#77b7c5'
                }
            ];

            var otPositionVaration =<s:property value="otPositionVaration"/>;
            var firstPostion = [];
            var lastPostion = [];
            firstPostion[0] = otPositionVaration[0];
            lastPostion[0] = otPositionVaration[otPositionVaration.length - 1];

            var positionData = [{
                    data: otPositionVaration,
                    color: '#77b7c5',
                    points: {radius: 1} //fillColor: '#77b7c5'
                }, {
                    data: firstPostion,
                    color: '#FF6666',
                    points: {radius: 3, fill: true, fillColor: "#FF6666"} //fillColor: '#77b7c5'
                }, {
                    data: lastPostion,
                    color: '#FF6666',
                    points: {radius: 3} //fillColor: '#77b7c5'
                }
            ];
            $.plot("#ot-curve", opticalData, option1);
            option1.lines.show = true;
            option1.yaxis.transform = formate3;
            option1.yaxis.inverseTransform = formate3;
            $.plot("#ot-position-curve", positionData, option1);

            $("#ot-curve").bind("plothover", function(event, pos, item) {
                if (item) {
                    var x = item.datapoint[0].toFixed(4);
                    var y = item.datapoint[1].toFixed(2);
                    $("#tooltip").html(x + ", " + y).css({top: item.pageY - 25, left: item.pageX + 10}).fadeIn(200);
                } else {
                    $("#tooltip").hide();
                }
            });
        });

        function formatLink(cellvalue, options, rowObject) {
            var searchUrl = "http://simbad.u-strasbg.fr/simbad/sim-coo?CooFrame=FK5&CooEpoch=2000&CooEqui=2000&CooDefinedFrames=none&Radius=2&Radius.unit=arcmin&submit=submit%20query&Coord=";
            searchUrl += <s:property value="ra"/> + "%20" + <s:property value="dec"/>;
            return "<a href='" + searchUrl + "' title='点击在simbad搜寻OT对应坐标' target='_blank'>" + cellvalue + "</a>";
        }

    </script>
    <style type="text/css">

        body{ text-align:center} 

        #main{
            width: 1000px;
            background-color: #fff;
            margin:0 auto;
            text-align:center
        }

        #cut-image-show{
            margin: 10px 0px 10px 0px;
            background-color: #fff;
            width: 1000px;
            float:left;
        }

        #ot-curves{
            /*margin: 10px 10px 10px 10px;*/
            background-color: #fff;
            width: 1000px;
            height: 200px;
            float:left;
        }

        #ot-curve-show{
            width: 600px;
            height: 200px;
            float:left;
        }

        #ot-position-var{
            width: 400px;
            height: 200px;
            float:left;
        }

        #ot-curve,#ot-position-curve{width: 100%; height: 180px;}
        #ot-curve-title,#ot-position-var-title{width: 100%;  height: 20px; text-align:center;}
        #ref-image{width: 404px;height: 400px;padding: 0px;margin: 0px 4px 0px 0px;float: left;}
        #skyCoordinate{width: 100%;  height: 30px; text-align:center; font-size:20px;margin:30px 0px 10px 0px;}

        #carousel-wrapper {
            width: 400px;
            height: 430px;
            padding: 0px;
            margin: 0px;
            float: right;
            /*position: absolute;*/
            /*left: 50%;*/
            /*top: 230px;*/
            /*box-shadow: 0 5px 10px #ccc;*/
        }

        #otRecord{
            width: 1000px;
            padding: 0px;
            /*margin: 10px;*/
            float: left;
        }

        #inner {
            position: relative;
            width: 400px;
            height: 400px;
            overflow: hidden;
            text-align: center;
        }
        #carousel{
            position: relative;
            /*width: 400px;*/
            /*left: 100px;*/
        }

        #navi {
            background-color: #333;
            background-color: rgba(30, 30, 30, 0.8);
            border-top: 1px solid #000;
            width: 400px;
            height: 70px;
            position: absolute;
            bottom: -60px;
            left: 0;
            z-index: 10;
        }
        #navi2 {
            width: 380px;
            height: 30px;
            padding: 0px 10px 0px 10px;
            position: relative;
            bottom: 0px;
            left: 0;
            z-index: 20;
            vertical-align: bottom;
        }
        #pagenumber, #title, #title2, #pager, #start, #end {
            font-size: 12px;
            margin: 0;
            padding: 0;
        }
        #pagenumber {
            float: left;
        }
        #title {
            text-align: center;
            width:160px;
            float: left;
        }
        #title2 {
            text-align: center;
            width:380px;
            float: left;
        }

    </style>
</head>
<body>
    <div id="main">
        <div id="skyCoordinate">
            <span>OT坐标(赤经,赤纬)：(<s:property value="siderealTime"/>,&nbsp;<s:property value="pitchAngle"/>)&nbsp;&nbsp;&nbsp;
                (<s:property value="ra"/>,&nbsp;<s:property value="dec"/>)</span>
        </div>
        <div id="ot-curves">
            <div id="ot-curve-show">
                <div id="ot-curve-title">
                    <span>OT光变曲线（X轴为时间，单位/分钟，开始于<s:date name="ob.foundTimeUtc" format="yyyy-MM-dd HH:mm:ss" />U，Y轴为星等值）</span>
                </div>
                <div id="ot-curve"></div>
            </div>
            <div id="ot-position-var">
                <div id="ot-position-var-title">
                    <span>OT坐标变化（与首帧差值，单位/像素，xy轴分别为模板XY坐标）</span>
                </div>
                <div id="ot-position-curve"></div>
            </div>
        </div>
        <div id="otRecord">
            <s:url var="remoteurl" action="ot-observe-record" namespace="/" escapeAmp="false">
                <s:param name="otName" value="%{otName}" /> 
                <s:param name="queryHis" value="%{queryHis}" /> 
            </s:url>
            <!--width="700" resizable="true" 
            shrinkToFit="true" 自动调节到表格的宽度 autowidth="true"
            scroll="true"添加之后不分页
            -->
            <sjg:grid 
                id="gridtable" 
                width="1000"
                hiddengrid="true"
                caption="OT观测记录详细信息" 
                dataType="json" 
                href="%{remoteurl}" 
                pager="true" 
                gridModel="gridModel"
                rowList="10,15,20" 
                rowNum="10" 
                rownumbers="true"
                viewrecords="true">
                <sjg:gridColumn name="ffName"   index="ffName"	  title="原FITS图" width="4"
                                sortable="false" align="center"/>
                <sjg:gridColumn name="dateUt" index="dateUt" title="时间(UTC)" formatter="date" width="2"  
                                formatoptions="{newformat : 'Y-m-d H:i:s', srcformat : 'Y-m-d H:i:s'}" 
                                sortable="false" align="center"/>
                <sjg:gridColumn name="raD"    index="raD"	  title="RA"  formatter="formatLink" width="1"
                                sortable="false" align="center"/>
                <sjg:gridColumn name="decD"    index="decD"	  title="DEC"  formatter="formatLink" width="1"
                                sortable="false" align="center"/>
                <sjg:gridColumn name="x"    index="x"	  title="X" width="1"
                                sortable="false" align="center"/>
                <sjg:gridColumn name="y"    index="y"	  title="Y" width="1"
                                sortable="false" align="center"/>
                <sjg:gridColumn name="magAper"    index="magAper"	title="星等" width="1"
                                sortable="false" align="center"/>
                <sjg:gridColumn name="magerrAper"    index="magerrAper" title="星等误差"  width="1"
                                sortable="false" align="center"/>
            </sjg:grid>
        </div>
                
        <div id="cut-image-show">
            <table>
                <!--<tr><td>图像序号</td><td>图像</td></tr>-->
            <s:iterator value="ffcList">
                <tr><td width="100px"><span><s:property value="number"/></span></td><td><img src="<s:property value="storePath"/>/<s:property value="fileName"/>" 
                     alt="<s:property value="fileName"/>" 
                     title="<s:property value="fileName"/>" border="0" /></td></tr>
            </s:iterator>
                </table>
        </div>
    </div>
</body>
</html>