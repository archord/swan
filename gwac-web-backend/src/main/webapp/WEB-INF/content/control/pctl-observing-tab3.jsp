<%-- 
    Document   : pctl-observing-tab3
    Created on : 2017-8-21, 16:56:23
    Author     : xy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="row">
  <div class="col-xs-12 col-sm-12 col-md-12 manual_container_col">
    <div class="manual_container_tel">
      <select height="30"><option>望远镜控制</option>
        <option>望远镜01</option>
        <option>望远镜02</option>
        <option>望远镜03</option>
        <option>望远镜04</option>
        <option>望远镜05</option>
        <option>望远镜06</option>
        <option>望远镜07</option>
        <option>望远镜08</option>
      </select>
      <button type="button">初始化</button>
      <button type="button">开始</button>
      <button type="button">暂停</button>
      <button type="button">停止</button></div>
  </div>
</div>
<div class="row">
  <div class="col-xs-12 col-sm-12 col-md-4 manual_container_col">
    <div class="manual_container">
      <div>
        <select height="30"><option>转台控制</option>
          <option>转台01</option>
          <option>转台02</option>
          <option>转台03</option>
          <option>转台04</option>
          <option>转台05</option>
          <option>转台06</option>
          <option>转台07</option>
          <option>转台08</option>
        </select>
        <button type="button">初始化</button>
        <button type="button">复位</button>
        <button type="button">跟踪</button>
        <button type="button">暂停</button>
        <button type="button">旋转</button>
        <button type="button">停止</button>
      </div>
      <div>
        指向：赤经<input name="ra" value="60.0" style="width:60px;"/>度，赤经<input name="ra" value="60.0" style="width:60px;"/>度
      </div>
      <div>

      </div></div>
  </div>
  <div class="col-xs-12 col-sm-12 col-md-4 manual_container_col">
    <div class="manual_container">
      <div>
        <select height="30"><option>CCD控制</option>
          <option>CCD01</option>
          <option>CCD02</option>
          <option>CCD03</option>
          <option>CCD04</option>
          <option>CCD05</option>
          <option>CCD06</option>
          <option>CCD07</option>
          <option>CCD08</option>
          <option>CCD09</option>
          <option>CCD10</option>
          <option>CCD11</option>
          <option>CCD12</option>
          <option>CCD13</option>
          <option>CCD14</option>
          <option>CCD15</option>
          <option>CCD16</option>
          <option>CCD17</option>
          <option>CCD18</option>
          <option>CCD19</option>
          <option>CCD20</option>
          <option>CCD21</option>
          <option>CCD22</option>
          <option>CCD23</option>
          <option>CCD24</option>
          <option>CCD25</option>
          <option>CCD26</option>
          <option>CCD27</option>
          <option>CCD28</option>
          <option>CCD29</option>
          <option>CCD30</option>
          <option>CCD31</option>
          <option>CCD32</option>
          <option>CCD33</option>
          <option>CCD34</option>
          <option>CCD35</option>
          <option>CCD36</option>
          <option>CCD37</option>
          <option>CCD38</option>
          <option>CCD39</option>
          <option>CCD40</option>
        </select>
        <button type="button">初始化</button>
        <button type="button">本底</button>
        <button type="button">暗场</button>
        <button type="button">平场</button>
        <button type="button">常规观测</button>
        <button type="button">停止</button>
      </div>
      <div>
        <table>
          <tr><td rowspan="4" width="40">观测<br/>属性</td><td>开始时间：</td><td><input name="ra" value="2017-12-20 22:00:00" style="width:160px;"/></td></tr>
          <tr><td>结束时间：</td><td><input name="ra" value="2017-12-20 22:00:00" style="width:160px;"/></td></tr>
          <tr><td>曝光间隔：</td><td><input name="ra" value="10" style="width:60px;"/>秒</td></tr>
          <tr><td>间隔间隔：</td><td><input name="ra" value="5" style="width:60px;"/>秒</td></tr>
        </table></div><div>

      </div></div>
  </div>
  <div class="col-xs-12 col-sm-12 col-md-4 manual_container_col">
    <div class="manual_container">
      <div>
        <select height="30"><option>镜头控制</option>
          <option>望远镜01</option>
          <option>望远镜02</option>
          <option>望远镜03</option>
          <option>望远镜04</option>
          <option>望远镜05</option>
          <option>望远镜06</option>
          <option>望远镜07</option>
          <option>望远镜08</option>
          <option>望远镜09</option>
          <option>望远镜10</option>
          <option>望远镜11</option>
          <option>望远镜12</option>
          <option>望远镜13</option>
          <option>望远镜14</option>
          <option>望远镜15</option>
          <option>望远镜16</option>
          <option>望远镜17</option>
          <option>望远镜18</option>
          <option>望远镜19</option>
          <option>望远镜20</option>
          <option>望远镜21</option>
          <option>望远镜22</option>
          <option>望远镜23</option>
          <option>望远镜24</option>
          <option>望远镜25</option>
          <option>望远镜26</option>
          <option>望远镜27</option>
          <option>望远镜28</option>
          <option>望远镜29</option>
          <option>望远镜30</option>
          <option>望远镜31</option>
          <option>望远镜32</option>
          <option>望远镜33</option>
          <option>望远镜34</option>
          <option>望远镜35</option>
          <option>望远镜36</option>
          <option>望远镜37</option>
          <option>望远镜38</option>
          <option>望远镜39</option>
          <option>望远镜40</option>
        </select>
        <button type="button">初始化</button>
        <button type="button">自动调焦</button>
        <button type="button">停止</button>
      </div>
      <div>
        手动调焦：调焦量<input name="ra" value="60.0" style="width:60px;"/>，
        <button type="button">正方向</button><button type="button">反方向</button>
      </div>
      <div>
      </div>
    </div>

  </div>
