<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC系统批处理管理</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC系统批处理管理" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/jquery.dataTables.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/css/gwac-ui.css" rel="stylesheet">

    <style type="text/css"> 
      #ttAddTable{
        margin: auto; 
      }
      #ttAddTable tr{
        height:30px;
      }
      #ttAddTable td{
        text-align: left;
      }
      .input2{
        width:180px;
        height: 26px;
        padding: 1px;
      }
      #ttAddTable textarea{
        width:480px;
      }
    </style>
  </head>
  <body>
    <div class="container-fluid">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>      

      <div class="row" style="margin-top:20px">
        <div class="col-xs-12 col-sm-12 col-md-12">

          <form action="${pageContext.request.contextPath}/gction/addTimingTask.action" id="addTTForm"  method="post" enctype="multipart/form-data">
            <table id="ttAddTable">
              <tr><td>名称：</td>
                <td><input name="ttName" id="ttName" class="input2"/>
                  <input type="hidden" name="actionType" id='actionType' value="addTTForm"/>
                  <input type="hidden" name="ttId" id="ttId" value="-1"/>
                </td>
                <td>执行命令：</td>
                <td><input name="ttCommand" id="ttCommand" class='input2'/></td></tr>
              <tr><td>执行机器：</td><td>
                  <select height="30" name="dpmName" id="dpmName" class="dropdown-toggle btn btn-default">
                    <option value="All">All</option>
                    <option value="011">011</option>
                    <option value="012">012</option>
                    <option value="013">013</option>
                    <option value="014">014</option>
                    <option value="015">015</option>
                    <option value="021">021</option>
                    <option value="022">022</option>
                    <option value="023">023</option>
                    <option value="024">024</option>
                    <option value="025">025</option>
                    <option value="031">031</option>
                    <option value="032">032</option>
                    <option value="033">033</option>
                    <option value="034">034</option>
                    <option value="035">035</option>
                    <option value="041">041</option>
                    <option value="042">042</option>
                    <option value="043">043</option>
                    <option value="044">044</option>
                    <option value="045">045</option>
                  </select></td>
                <td>执行方式：</td><td>
                  <select height="30" name="type" id="type" class="dropdown-toggle btn btn-default">
                    <option value="1">单次立即执行</option>
                    <option value="2">单次定时执行</option>
                    <option value="3">多次定时执行</option>
                  </select></td></tr>
              <tr><td>执行文件：</td>
                <td><input type="file" name="ttFileName" id="ttFileName"></td>
                <td>执行路径：</td>
                <td><input name="executePath" id="executePath"  class='input2'/></td></tr>
              <tr><td width="100px">开始日期：</td>
                <td width="200px"><input name="planStartDate" id="planStartDate" onclick="WdatePicker()" class="Wdate, input2"/></td>
                <td width="100px">结束日期：</td>
                <td width="200px"><input name="planEndDate" id="planEndDate" onclick="WdatePicker()" class="Wdate, input2"/></td></tr>
              <tr><td>开始时间：</td>
                <td><input name="planStartTime" id="planStartTime" onclick="WdatePicker({dateFmt:'HH:mm:ss'})" class="Wdate, input2"/></td>
                <td>结束时间：</td>
                <td><input name="planEndTime" id="planEndTime" onclick="WdatePicker({dateFmt:'HH:mm:ss'})" class="Wdate, input2"/></td></tr>
              <tr><td>注释：</td>
                <td colspan="3"><textarea rows="2" cols="60" name='comments' id='comments'></textarea></td></tr>
              <tr><td colspan="4" style="text-align:center;"><button type="button" id="addTTBtn">提交</button></td></tr>
            </table>
          </form>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 ">
          <span id="uploadResult"></span>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 ">
          <span id="uploadResult"></span>
        </div>
      </div>

      <div class="row ot-list-body">
        <div id="ot-list">
          <table id="ot-list-table" class="display" cellspacing="0" width="100%">
            <thead><tr><th>ID</th><th>任务名</th><th>命令</th><th>目标机器</th><th>类型</th><th>状态</th><th>起止时间</th><th>注释</th><th>修改</th><th>删除</th></tr></thead>
            <tfoot><tr><th>ID</th><th>任务名</th><th>命令</th><th>目标机器</th><th>状态</th><th>起止时间</th><th>注释</th><th>修改</th><th>删除</th></tr></tfoot>
          </table>
        </div>                                   
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/batch_script.js"></script>

  </body>
</html>
