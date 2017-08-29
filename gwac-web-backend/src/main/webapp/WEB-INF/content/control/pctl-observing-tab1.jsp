<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div>
  <table style="text-align: center; " border=1>
    <tr><td><input type="checkbox"/>望远镜</td><td><input type="checkbox"/>转台</td><td><input type="checkbox"/>GWAC相机</td><td><input type="checkbox"/>Mini相机</td></tr>
    <tr><td><span style="background-color: greenyellow"><input type="checkbox" name="mount" value="m01"/>望01</span></td>
      <td><input type="checkbox" name="mount" value="m01" />转01</td>
      <td><input type="checkbox" name="camera" value="m01"/>相01，<input type="checkbox" name="camera" value="m01"/>相02，
        <input type="checkbox" name="camera" value="m01"/>相03，<input type="checkbox" name="camera" value="m01"/>相04</td>
      <td><input type="checkbox" name="camera" value="m01"/>相05</td></tr>
    <tr><td><input type="checkbox" name="mount" value="m02"/>望02</td><td>
        <input type="checkbox" name="mount" value="m01"/>转02</td>
      <td><span style="background-color: yellow"><input type="checkbox" name="camera" value="m01"/>相06</span>，<input type="checkbox" name="camera" value="m01"/>相07，
        <input type="checkbox" name="camera" value="m01"/>相08，<input type="checkbox" name="camera" value="m01"/>相09</td>
      <td><input type="checkbox" name="camera" value="m01"/>相10</td></tr>
    <tr><td><input type="checkbox" name="mount" value="m03"/>望03</td>
      <td><span style="background-color: red"><input type="checkbox" name="mount" value="m01"/>转03</span></td>
      <td><input type="checkbox" name="camera" value="m01"/>相11，<input type="checkbox" name="camera" value="m01"/>相12，
        <input type="checkbox" name="camera" value="m01"/>相13，<input type="checkbox" name="camera" value="m01"/>相14</td>
      <td><input type="checkbox" name="camera" value="m01"/>相15</td></tr>
    <tr><td><input type="checkbox" name="mount" value="m04"/>望04</td>
      <td><input type="checkbox" name="mount" value="m01"/>转04</td>
      <td><input type="checkbox" name="camera" value="m01"/>相16，<input type="checkbox" name="camera" value="m01"/>相17，
        <span style="background-color: #d0d0d0"><input type="checkbox" name="camera" value="m01"/>相18</span>，<input type="checkbox" name="camera" value="m01"/>相19</td>
      <td><input type="checkbox" name="camera" value="m01"/>相20</td></tr>
    <tr><td><input type="checkbox" name="mount" value="m05"/>望05</td>
      <td><input type="checkbox" name="mount" value="m01"/>转03</td>
      <td><input type="checkbox" name="camera" value="m01"/>相21，<input type="checkbox" name="camera" value="m01"/>相22，
        <input type="checkbox" name="camera" value="m01"/>相23，<input type="checkbox" name="camera" value="m01"/>相24</td>
      <td><input type="checkbox" name="camera" value="m01"/>相25</td></tr>
    <tr><td><input type="checkbox" name="mount" value="m06"/>望06</td>
      <td><input type="checkbox" name="mount" value="m01"/>转04</td>
      <td><input type="checkbox" name="camera" value="m01"/>相26，<input type="checkbox" name="camera" value="m01"/>相27，
        <input type="checkbox" name="camera" value="m01"/>相28，<input type="checkbox" name="camera" value="m01"/>相29</td>
      <td><input type="checkbox" name="camera" value="m01"/>相30</td></tr>
    <tr><td><input type="checkbox" name="mount" value="m07"/>望07</td>
      <td><input type="checkbox" name="mount" value="m01"/>转03</td>
      <td><input type="checkbox" name="camera" value="m01"/>相31，<input type="checkbox" name="camera" value="m01"/>相32，
        <input type="checkbox" name="camera" value="m01"/>相33，<input type="checkbox" name="camera" value="m01"/>相34</td>
      <td><input type="checkbox" name="camera" value="m01"/>相35</td></tr>
    <tr><td><input type="checkbox" name="mount" value="m08"/>望08</td>
      <td><input type="checkbox" name="mount" value="m01"/>转04</td>
      <td><input type="checkbox" name="camera" value="m01"/>相36，<input type="checkbox" name="camera" value="m01"/>相37，
        <input type="checkbox" name="camera" value="m01"/>相38，<input type="checkbox" name="camera" value="m01"/>相39</td>
      <td><input type="checkbox" name="camera" value="m01"/>相40</td></tr>
  </table>
</div>

<div>
  <p>每个设备的状态用设备的背景颜色标示：白（未开机）、绿（正常）、黄（有故障）、红（损坏）、灰（下线）</p>
</div>