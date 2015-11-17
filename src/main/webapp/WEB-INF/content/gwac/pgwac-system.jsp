<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<div id='grid-fixed'>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-fwhm.action" target="_blank" ><p>半高全宽</p></a>                  </div>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-objnum.action" target="_blank" ><p>星的数目</p></a>                       </div>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-bgbright.action" target="_blank" ><p>背景亮度</p></a>                     </div>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-limitmag.action" target="_blank" ><p>极限星等</p></a>                     </div>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-diffmag.action" target="_blank" ><p>亮星星等差</p></a>                    </div>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-track.action" target="_blank" ><p>图像偏移量</p></a>                      </div>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-xyrms.action" target="_blank" ><p>图像对齐精度</p></a>                    </div>
  <div class='mason-block'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-timeneed.action" target="_blank" ><p>数据处理消耗时间</p></a>             </div>
  <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-min-ccd-image-realtime.action" target="_blank" ><p>CCD图像预览</p></a>        </div>
  <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ot-realtimedisplay.action" target="_blank" ><p>OT分布-XY</p></a>          </div>
  <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ot-realtimedisplay-sphere.action" target="_blank" ><p>OT分布-RaDec</p></a></div>
  <div class='mason-block xl'><a href="<%=request.getContextPath()%>/gwac/pgwac-ot-realtimedisplay-sphere-d3.action" target="_blank" ><p>OT分布3D</p></a></div>
  <div class='mason-block x2'><a href="http://190.168.1.45/Mini_GWAC_Survey/survey_plan_db.php" target="_blank" ><p>Mini-GWAC观测策略</p></a></div>
  <div class='mason-block x2'><a href="http://190.168.1.45/GWACOC/gcn_trigger.php" target="_blank" ><p>GCN trigger</p></a></div>
</div>

<div id='mason-fillers'>
  <div class='mason-fill-filler' style="margin: 5px;"></div>
</div>

<script src="${pageContext.request.contextPath}/resource/mason/app.js"></script>
<script src="${pageContext.request.contextPath}/resource/mason/mason.js"></script>
<script>
  $(function() {
    $("#grid-fixed").mason({
      itemSelector: '.mason-block',
      ratio: 1,
      columns: [[14, 20, 5]],
      sizes: [[1, 1], [2, 1]],
//      promoted: [['xl', 2, 1]],
      filler: {itemSelector: '.mason-fill-filler', filler_class: 'mason_filler', keepDataAndEvents: true},
      layout: 'fluid',
      gutter: 5
    })
  });
</script>

