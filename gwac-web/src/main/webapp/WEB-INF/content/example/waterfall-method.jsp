<%-- 
    Document   : waterfall-method
    Created on : 2018-10-27, 18:51:34
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <title>method</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/watefall/css/reset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/watefall/css/waterfall.css">
    </head>
    <body>
        <div id="header">
            <h1>method</h1>
        </div>
        <div id="trigger">
            <a href="javascript:void(0);" id="prepend">prepend</a>
            <a href="javascript:void(0);" id="append">append</a>
            <a href="javascript:void(0);" id="remove-items">remove items</a>
            <a href="javascript:void(0);" id="relayout">relayout</a>
            <a href="javascript:void(0);" id="change-gutter">option:change gutter</a>
            <a href="javascript:void(0);" id="change-max-page">option: change max page</a>
            <a href="javascript:void(0);" id="change-align-left">option: change align left</a>
        </div>
        <div id="container"></div>
        <script type="text/x-handlebars-template" id="waterfall-tpl">
            {{#result}}
            <div class="item">
            <img src="{{image}}" width="{{width}}" height="{{height}}" />
            </div>
            {{/result}}
        </script>
        <script src="${pageContext.request.contextPath}/resource/watefall/js/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/resource/watefall/js/libs/handlebars/handlebars.js"></script>
        <script src="${pageContext.request.contextPath}/resource/watefall/js/waterfall.min.js"></script>
        <script src="${pageContext.request.contextPath}/resource/watefall/js/libs/jquery.easing.js"></script>
        <script>
            var content = '<div class="item waterfall-item-fixed-left" style="height: 180px;  background: #2b8af8; color: #fff;"></div><div class="item waterfall-item-fixed-right" style="height: 120px; background: #0293ab; color: #fff;"></div><div class="item" style="height: 230px; background: #db561d; color: #fff;"></div><div class="item" style="height: 170px;  background: #0d9c00; color: #fff;"></div>';

            $('#container').waterfall({
              itemCls: 'item',
              colWidth: 222,
              gutterWidth: 15,
              gutterHeight: 15,
              maxPage: 1,
              checkImagesLoaded: false,
              path: function (page) {
                return '${pageContext.request.contextPath}/resource/watefall/data/data1.json?page=' + page;
              }
            });

            $('#prepend').bind('click', function () {
              $('#container').waterfall('prepend', $(content), function () {
                alert('prepended');
              });
            });

            $('#append').bind('click', function () {
              $('#container').waterfall('append', $(content), function () {
                alert('appended');
              });
            });

            $('#remove-items').bind('click', function () {
              $('#container').waterfall('removeItems', $('.item:lt(5)'), function () {
                alert('removed');
              });
            });

            $('#change-gutter').bind('click', function () {
              $('#container').waterfall('option', {
                gutterWidth: 30, // 数据块水平间距
                gutterHeight: 30 // 数据块垂直间距
              }, function () {
                alert('change gutter width and gutter height');
              });
            });

            $('#change-max-page').bind('click', function () {
              $('#container').waterfall('option', {
                maxPage: 20
              }, function () {
                alert('change max page 20.');
              });
            });

            $('#change-align-left').bind('click', function () {
              $('#container').waterfall('option', {
                align: 'left'
              }, function () {
                alert('change align left.');
              });
            });


        </script>

    </body>
</html>

