<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <meta name="robots" content="noindex">
        <title>目标分类页面</title>
        <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
        <meta http-equiv="Content-Style-Type" content="text/css" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="0" />
        <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
        <meta http-equiv="description" content="GWAC数据展示页面" />
        <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>

        <link href="${pageContext.request.contextPath}/resource/flatui/css/vendor/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resource/flatui/css/flat-ui.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resource/flatui/css/demo.css" rel="stylesheet">

        <style type="text/css"> 
            html, body {
                padding: 0;
                margin: 0;
                height: 100%;
                width:100%;  
                text-align: center;
            }
            .classify-top, classify-body {  
                margin: 20px 0;
                width:98%;
                text-align: center;
            } 
            classify-body {  
                height:90%;
            } 
        </style>  
    </head>
    <body>
        <div style="display: none;">
            <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
        </div>
        <div class="container-fluid"> <!--container container-fluid -->
            <div class="row classify-top">
                <div class="col-md-10 col-lg-10">
                    <select id="defaultType" style="height:28px">
                        <option value="1">默认类型1</option>
                        <option value="2" selected>默认类型2</option>
                    </select>
                    <select id="dataVersion" style="height:28px">
                        <option value="0">数据集</option>
                    </select>
                    <select id="dataDir" style="height:28px">
                    </select>
                </div>
                <div class="col-md-2 col-lg-2"></div>
            </div>
            <div class="row classify-body">
                <div class="col-md-10 col-lg-10">
                    <div>
                        <span id="image-title">图像名</span>
                    </div>
                    <div id="image-gallery" style="height:95%">
                        <img id="obj-image" src="/gclassify/resource/test-image/1.png" title="image1"/>
                    </div>
                </div>
                <div class="col-md-2 col-lg-2">
                    <div style="margin-top:50px; text-align: left;">
                        <input type="button" value="上一幅" class="btn btn-primary" id="headBtn"/><br/><br/>
                        <input type="button" value="下一幅" class="btn btn-primary" id="nextBtn"/><br/><br/>
                        <input type="radio" name="objClassify" value="0">疑难<br/><br/>
                        <input type="radio" name="objClassify" value="1">流星<br/><br/>
                        <input type="radio" name="objClassify" value="2">单峰<br/><br/>
                        <input type="radio" name="objClassify" value="3">多峰<br/><br/>
                        <input type="radio" name="objClassify" value="4">无峰<br/><br/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/flatui/js/flat-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/obj-classify.js"></script>

</body>
</html>
