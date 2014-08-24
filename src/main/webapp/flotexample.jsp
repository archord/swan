<%-- 
    Document   : flotexample
    Created on : Aug 20, 2014, 7:44:35 PM
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Float example</title>
    <link href="styles/layout.css" rel="stylesheet" type="text/css"/>
    <script language="javascript" type="text/javascript" src="js/plot/jquery.js"></script>
    <script language="javascript" type="text/javascript" src="js/plot/jquery.flot.js"></script>
    <script language="javascript" type="text/javascript" src="js/plot/jquery.flot.axislabels.js"></script>
    <script language="javascript" type="text/javascript" src="js/plot/excanvas.compiled.js"></script>
    <script language="javascript" type="text/javascript" src="js/plot/ajax.js"></script>
    <script language="javascript" type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
  </head>
  <body>

    <div id="page" style="border: solid 1px gray;">

      <table style="margin-left: auto; margin-right: auto;">
        <tr><td><div class="title">近一天环境温度和CCD芯片温度变化图</div></td></tr>
        <tr><td><div id="placeholder1"></div></td></tr>
      </table>

    </div>

    <script type="text/javascript">
      makeRequest('getData.php?id=1');
    </script>

    <div id="footer">Copyright©2011 All rights reserved, maintained by NAOC.</div>

  </body>
</html>