
$(function() {

  initPage();

  function initPage() {
    $("#genObsPlanBtn").click(ot2QueryBtnClick);
    initEditPage();
  }

  function initEditPage() {
    var path = window.location.href;
    if (path.indexOf("sofId") > 0) {
      $("#manual_container_title").html("修改科学目标");
      var sofId = getUrlVars()["sofId"];
      var gwacRootURL = $("#gwacRootURL").val();
      var turl = gwacRootURL + "/get-sciobjfinal-detail.action";
      $.ajax({
        type: "get",
        url: turl,
        data: 'sofId=' + sofId,
        async: false,
        dataType: 'json',
        success: function(data) {
          var tObj = eval(data.dataStr)[0];
          console.log(tObj);
          $("#sofId").val(tObj.sof_id);
          $("#name").val(tObj.name);
          $("#discoveryTime").val(tObj.discovery_time);
          $("#triggerTime").val(tObj.trigger_time);
          $("#ra").val(tObj.ra);
          $("#dec").val(tObj.dec);
          $("#magDetect").val(tObj.mag_detect);
          $("#magCatalog").val(tObj.mag_catalog);
          $("#magAbsolute").val(tObj.mag_absolute);
          $("#amplitude").val(tObj.amplitude);
          $("#dutyScientist").val(tObj.duty_scientist);
          $("#followup").val(tObj.followup);
          $("#type").val(tObj.type);
          $("#comments").val(tObj.comments);
          $("#source").val(tObj.source);
          $("#publicMsg").val(tObj.public_msg);
          $("#gwacType").val(tObj.gwac_type);
        }
      });
    }
  }

  function ot2QueryBtnClick() {
    var expTime = $("#expTime").val();
    var frameCount = $("#frameCount").val();
    var ra = parseFloat($("#ra").val());
    var dec = parseFloat($("#dec").val());
    if (expTime === "") {
      alert("请填写曝光时间");
      return;
    }
    if (frameCount === "") {
      alert("请填写帧数");
      return;
    }
    if (ra<0 || ra>360) {
      alert("请填写正确的赤经值");
      return;
    }
    if (dec<-90 || dec>90) {
      alert("请填写正确的赤纬值");
      return;
    }

    var formData = $("#genObsPlanForm").serialize();
    var formUrl = $("#genObsPlanForm").attr('action');
    console.log(formData);
    console.log(formUrl);
    if (formData !== '') {
      var queryUrl = formUrl;
      $.ajax({
        type: "post",
        url: queryUrl,
        data: formData,
        async: true,
        success: function(data) {
          console.log(data);
          alert("提交成功！");
        }
      });
    } else {
      var msg = "请填写合理的参数!";
      console.log(msg);
      alert(msg);
    }
  }



  function getUrlVars()
  {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++)
    {
      hash = hashes[i].split('=');
      vars.push(hash[0]);
      vars[hash[0]] = hash[1];
    }
    return vars;
  }
});

