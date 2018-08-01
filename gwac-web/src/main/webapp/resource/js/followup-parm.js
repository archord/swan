
$(function() {

  initPage();

  function initPage() {
    $("#genObsPlanBtn").click(ot2QueryBtnClick);

    setFilter60();
    $("#telescope").change(function() {
      if ($("#telescope").val() === '2') {
        setFilter30();
      } else {
        setFilter60();
      }
    });

    var gwacRootURL = $("#gwacRootURL").val();
    var turl = gwacRootURL + "/followUpParmGet.action";
    $.ajax({
      type: "get",
      url: turl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function(data) {
        console.log(data);
        $("#priority").val(data.priority);
        $("#telescope").val(data.telescope);
        $("#exposeDuration").val(data.exposeDuration);
        $("#filter").val(data.filter);
        $("#frameCount").val(data.frameCount);
        $("#autoFollowUp").val(data.autoFollowUp);
        $("#filter").change();
      }
    });
  }


  function ot2QueryBtnClick() {
    var expTime = $("#expTime").val();
    var frameCount = $("#frameCount").val();
    if (expTime === "") {
      alert("请填写曝光时间");
      return;
    }
    if (frameCount === "") {
      alert("请填写帧数");
      return;
    }

    var formData = $("#followUpParmSet").serialize();
    var formUrl = $("#followUpParmSet").attr('action');
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
      var msg = "请选择合理的后随参数!";
      console.log(msg);
      alert(msg);
    }
  }

  function setFilter60() {
    var filter60 = ["Lum", "Green", "R", "Blue", "V", "I", "B", "Red", "U", "null"];
    $('#filter').find('option').remove();
    $.each(filter60, function(i, item) {
      $('#filter').append($('<option>', {
        value: item,
        text: item
      }));
    });
//    $("#filter").val('R');
//    $("#filter").change();
  }

  function setFilter30() {
    var filter30 = ["null", "R", "B", "I", "U", "V"];
    $('#filter').find('option').remove();
    $.each(filter30, function(i, item) {
      $('#filter').append($('<option>', {
        value: item,
        text: item
      }));
    });
//    $("#filter").val('R');
//    $("#filter").change();
  }

});

