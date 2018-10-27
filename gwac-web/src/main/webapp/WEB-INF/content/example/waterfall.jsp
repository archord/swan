<%-- 
    Document   : waterfall
    Created on : 2018-10-27, 14:04:12
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <title>image watefall style -- infinitescroll</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/watefall/css/reset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/watefall/css/waterfall.css">
    </head>
    <body>
        <div id="header">
            <h1>infinitescroll</h1>
        </div>
        <div id="container"></div>
        <script type="text/x-handlebars-template" id="waterfall-tpl">
            {{#result}}
            <div class="item" style="text-align:center">
            <a href="{{image}}"  target="_blank"><img src="{{image}}" width="{{width}}" height="{{height}}" /></a>
            <span>{{width}}</span>
            </div>
            {{/result}}
        </script>
        <script src="${pageContext.request.contextPath}/resource/watefall/js/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/resource/watefall/js/libs/handlebars/handlebars.js"></script>
        <script src="${pageContext.request.contextPath}/resource/watefall/js/waterfall.min.js"></script>
        <script>
            $('#container').waterfall({
              itemCls: 'item',
              colWidth: 222,
              gutterWidth: 15,
              gutterHeight: 15,
              checkImagesLoaded: false,
              path: function (page) {
                return '${pageContext.request.contextPath}/resource/watefall/data/data1.json?page=' + page;
              }
            });
        </script>
    </body>
</html>
