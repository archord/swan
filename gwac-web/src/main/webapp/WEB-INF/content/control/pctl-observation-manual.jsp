<%-- 
    Document   : observing
    Created on : 2017-8-18, 10:09:04
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="robots" content="noindex">
    <title>GWAC观测-手动控制</title>
    <meta name="viewport" content="width=1000, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="Content-Style-Type" content="text/css" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <meta http-equiv="keywords" content="国家天文台，svom, gwac，望远镜阵列" />
    <meta http-equiv="description" content="GWAC观测手动控制页面" />
    <link type="image/x-icon" rel="shortcut icon" href="${pageContext.request.contextPath}/resource/sysimg/favicon.ico"/>
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resource/multiselect/bootstrap-multiselect.css" rel="stylesheet">

    <style type="text/css"> 
      html, body {
        padding: 0;
        margin: 0;
        height: 100%;
        width:100%;  
        text-align: center;
      }
      .obs_plan_table {width:98%}
      .obs_plan {  
        width:98%;  
        height: 98%;
        overflow:auto;  
        white-space: nowrap;
      }  
      select {
        width: 100px;
        border: 1px solid #555;
        padding: 0.5em;
        font-size: 15px;
        line-height: 1.2em;
        background: #fff;
        -webkit-appearance: none;
        -webkit-box-shadow: 1px 1px 1px #fff;
        -webkit-border-radius: 0.5em;
      }
      .tab_container{
        width:99%; 
        height: 93%;
        text-align: center;
        border:0;
      }
      .manual_container1{
        border: 3px solid #c5c5c5;
        margin: 0 0 10px 0;
        padding: 5px;
        text-align: left;
        //min-width: 430px;
      }
      .manual_container1 span{  
        white-space: nowrap;  /*强制span不换行*/
        display: inline-block;  /*将span当做块级元素对待*/
        overflow: hidden;  /*超出宽度部分隐藏*/
        text-overflow: ellipsis;  /*超出部分以点号代替*/
        line-height: 0.9;  /*数字与之前的文字对齐*/
      }
      .manual_container1 input{  
        margin: 2px;
      }
      .manual_container_title{
        text-align: center;
        font-size: 16px;
        background-color: #eee;
        margin:  -5px 0px 5px 0px;
      }
      .manual_container2{
        border: 3px solid #c5c5c5;
        min-height: 343px;
        padding: 5px;
        margin-bottom: 10px;
      }
      .manual_container1_col{padding: 5px}
      .background {fill: #000;}
      line {stroke: #000;}
    </style>  
  </head>
  <body>

    <div id="tabs" style="width:98%; height:100%;margin:0 auto;border:0;">
      <div style="display: none;">
        <input type="hidden" id="gwacRootURL" value="${pageContext.request.contextPath}"/>
      </div>      

      <div class="tab_container">
        <div class="row">
          <div class="col-xs-12 col-sm-12 col-md-4 manual_container1_col">
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    观测计划</div>
                </div>
              </div>
              <form action="/gwebend/observationPlanUpload.action" id="genObsPlanForm">
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <select height="30" name="unitId">
                      <option value="000">转台选择</option>
                      <option value="001">转台01</option>
                      <option value="002">转台02</option>
                      <option value="003">转台03</option>
                      <option value="004">转台04</option>
                      <option value="005">转台05</option>
                      <option value="006">转台06</option>
                      <option value="007">转台07</option>
                      <option value="008">转台08</option>
                    </select>
                    <select height="30" name="obsType">
                      <option value="">观测类型</option>
                      <option value="monitor">监测型</option>
                      <option value="template">模板观测型</option>
                      <option value="patrol">巡视型</option>
                      <option value="synchronize">同步型</option>
                      <option value="ToO_auto">自动触发型</option>
                      <option value="ToO_manual">手动触发型</option>
                      <option value="manual">手动型</option>
                    </select>
                    <select height="30" name="imgType">
                      <option value="">图像类型</option>
                      <option value="bias">本底</option>
                      <option value="dark">暗场</option>
                      <option value="flat">平场</option>
                      <option value="light">常规观测</option>
                      <option value="focus">调焦</option>
                    </select>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-12 col-sm-12 col-md-12">
                    <table>
                      <tr><td>赤经：</td><td><input name="ra" value="60.0" style="width:60px;"/>秒</td>
                        <td>赤纬：</td><td><input name="dec" value="60.0" style="width:60px;"/>秒</td></tr>
                      <tr><td>曝光时间：</td><td><input name="expusoreDuring" value="10" style="width:60px;"/>秒</td>
                        <td>曝光间隔：</td><td><input name="delay" value="5" style="width:60px;"/>秒</td></tr>
                      <tr><td>观测帧数：</td><td><input name="frameCount" style="width:60px;"/>帧</td>
                        <td>优先级：</td><td><input name="priority" style="width:60px;"/></td></tr>
                      <tr><td>开始时间：</td><td><input name="beginTime" style="width:160px;" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate" /></td>
                        <td colspan="2" rowspan="2" style="text-align: right"><button type="button" id="genObsPlanBtn">生成观测计划</button></td></tr>
                      <tr><td>结束时间：</td><td><input name="endTime" style="width:160px;" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate" /></td></tr>
                    </table>
                  </div>
                </div>
              </form>
            </div>
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    转台</div>
                </div>
              </div>
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12">
                  <select height="30">
                    <option value="000">转台选择</option>
                    <option value="001">转台01</option>
                    <option value="002">转台02</option>
                    <option value="003">转台03</option>
                    <option value="004">转台04</option>
                    <option value="005">转台05</option>
                    <option value="006">转台06</option>
                    <option value="007">转台07</option>
                    <option value="008">转台08</option>
                  </select>
                  <select height="30">
                    <option>初始化</option>
                    <option>复位</option>
                    <option>跟踪</option>
                    <option>暂停</option>
                    <option>旋转</option>
                    <option>停止</option>
                  </select></div>
                <div class="col-xs-12 col-sm-12 col-md-12">
                  <div>
                    赤经<input name="ra" value="60.0" style="width:60px;"/>度，赤经<input name="ra" value="60.0" style="width:60px;"/>度，
                    <button type="button">执行</button>
                  </div>
                </div>
              </div>
            </div>
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    CCD</div>
                </div>
              </div>
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12"><select height="30">
                    <option value="000">CCD选择</option>
                    <option value="001">CCD01</option>
                    <option value="002">CCD02</option>
                    <option value="003">CCD03</option>
                    <option value="004">CCD04</option>
                    <option value="005">CCD05</option>
                    <option value="006">CCD06</option>
                    <option value="007">CCD07</option>
                    <option value="008">CCD08</option>
                    <option value="009">CCD09</option>
                    <option value="010">CCD10</option>
                    <option value="011">CCD11</option>
                    <option value="012">CCD12</option>
                    <option value="013">CCD13</option>
                    <option value="014">CCD14</option>
                    <option value="015">CCD15</option>
                    <option value="016">CCD16</option>
                    <option value="017">CCD17</option>
                    <option value="018">CCD18</option>
                    <option value="019">CCD19</option>
                    <option value="020">CCD20</option>
                    <option value="00">CCD21</option>
                    <option value="00">CCD22</option>
                    <option value="00">CCD23</option>
                    <option value="00">CCD24</option>
                    <option value="00">CCD25</option>
                    <option value="00">CCD26</option>
                    <option value="00">CCD27</option>
                    <option value="00">CCD28</option>
                    <option value="00">CCD29</option>
                    <option value="00">CCD30</option>
                    <option value="00">CCD31</option>
                    <option value="00">CCD32</option>
                    <option value="00">CCD33</option>
                    <option value="00">CCD34</option>
                    <option value="00">CCD35</option>
                    <option value="00">CCD36</option>
                    <option value="00">CCD37</option>
                    <option value="00">CCD38</option>
                    <option value="00">CCD39</option>
                    <option value="00">CCD40</option>
                  </select>
                  <select height="30"><option>初始化</option>
                    <option>本底</option>
                    <option>暗场</option>
                    <option>平场</option>
                    <option>常规观测</option>
                    <option>停止</option>
                  </select></div>
              </div>

              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12">
                  <table>
                    <tr><td>曝光时间：</td><td><input name="ra" value="10" style="width:60px;"/>秒</td><td>曝光间隔：</td><td><input name="ra" value="5" style="width:60px;"/>秒</td></tr>
                    <tr><td>观测帧数：</td><td><input name="frameCount" style="width:60px;"/>帧</td><td>优先级：</td><td><input name="priority" style="width:60px;"/></td></tr>
                    <tr><td>开始时间：</td><td><input name="ra" style="width:160px;" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate" /></td>
                      <td colspan="2" rowspan="2" style="text-align: right"><button type="button">执行</button></td></tr>
                    <tr><td>结束时间：</td><td><input name="ra" style="width:160px;" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" class="Wdate" /></td></tr>
                  </table>
                </div>
              </div>
            </div>
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    镜头</div>
                </div>
              </div>
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12"> 
                  <select height="30">
                    <option value="000">镜头选择</option>
                    <option value="001">望远镜01</option>
                    <option value="002">望远镜02</option>
                    <option value="003">望远镜03</option>
                    <option value="004">望远镜04</option>
                    <option value="005">望远镜05</option>
                    <option value="006">望远镜06</option>
                    <option value="007">望远镜07</option>
                    <option value="008">望远镜08</option>
                    <option value="009">望远镜09</option>
                    <option value="010">望远镜10</option>
                    <option value="011">望远镜11</option>
                    <option value="012">望远镜12</option>
                    <option value="013">望远镜13</option>
                    <option value="014">望远镜14</option>
                    <option value="015">望远镜15</option>
                    <option value="016">望远镜16</option>
                    <option value="017">望远镜17</option>
                    <option value="018">望远镜18</option>
                    <option value="019">望远镜19</option>
                    <option value="020">望远镜20</option>
                    <option value="01">望远镜21</option>
                    <option value="01">望远镜22</option>
                    <option value="01">望远镜23</option>
                    <option value="01">望远镜24</option>
                    <option value="01">望远镜25</option>
                    <option value="01">望远镜26</option>
                    <option value="01">望远镜27</option>
                    <option value="01">望远镜28</option>
                    <option value="01">望远镜29</option>
                    <option value="01">望远镜30</option>
                    <option value="01">望远镜31</option>
                    <option value="01">望远镜32</option>
                    <option value="01">望远镜33</option>
                    <option value="01">望远镜34</option>
                    <option value="01">望远镜35</option>
                    <option value="01">望远镜36</option>
                    <option value="01">望远镜37</option>
                    <option value="01">望远镜38</option>
                    <option value="01">望远镜39</option>
                    <option value="01">望远镜40</option>
                  </select>
                  <select height="30"><option>初始化</option>
                    <option>自动调焦</option>
                    <option>手动调焦</option>
                    <option>停止</option>
                  </select></div>
                <div class="col-xs-12 col-sm-12 col-md-12">
                  <div>
                    调 焦 量：<input name="ra" value="-60.0" style="width:60px;"/>  <button type="button">执行</button>
                  </div></div>
              </div>
            </div>
            <div class="manual_container1">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    望远镜</div>
                </div>
              </div>
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-6">
                  <select height="30">
                    <option value="000">望远镜选择</option>
                    <option value="001">望远镜01</option>
                    <option value="002">望远镜02</option>
                    <option value="003">望远镜03</option>
                    <option value="004">望远镜04</option>
                    <option value="005">望远镜05</option>
                    <option value="006">望远镜06</option>
                    <option value="007">望远镜07</option>
                    <option value="008">望远镜08</option>
                  </select>
                  <select height="30"><option>初始化</option>
                    <option>开始</option>
                    <option>暂停</option>
                    <option>停止</option>
                  </select>
                </div>
                <div class="col-xs-12 col-sm-12 col-md-6">
                  <button type="button">执行</button>
                </div>
              </div>
            </div>
          </div>
          <div class="col-xs-12 col-sm-12 col-md-8 manual_container1_col">
            <div class="manual_container2">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    位置偏移曲线
                  </div>
                </div>
              </div>
            </div>

            <div class="manual_container2">
              <div class="row">
                <div class="col-xs-12 col-sm-12 col-md-12 " >
                  <div class="manual_container_title">
                    FWHM变化曲线
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>

    <script src="${pageContext.request.contextPath}/resource/multiselect/jquery-2.1.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/multiselect/bootstrap-3.3.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/date/My97DatePicker/WdatePicker.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/d3/d3.min.js"></script>
    <script src="<%=request.getContextPath()%>/resource/js/pctl-observation-manual.js"></script>


  </body>
</html>
