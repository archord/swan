
$(function() {
  var gwacRootURL = $("#gwacRootURL").val();

  initPage();

  function initPage() {
    $("#genObsPlanBtn").click(ot2QueryBtnClick);
  }

  function ot2QueryBtnClick() {
    var formData = $("#genObsPlanForm").serialize() + "&epoch=2000&opSn=0&opTime="+encodeURI(new Date().Format("yyyy-MM-dd hh:mm:ss"));
    var formUrl = $("#genObsPlanForm").attr('action');
    if (formData !== '') {
      var queryUrl = formUrl + "?timestamp=" + new Date().getTime() + "&" + formData;
      console.log(queryUrl);
      $.ajax({
        type: "post",
        url: formUrl + "?timestamp=" + new Date().getTime(),
        data: formData,
        async: false,
        success: function(data) {
          console.log(data);
        }
      });
    } else {
      console.log("please select valid observation plan parameters!");
    }
  }

  Date.prototype.Format = function(fmt) { //author: meizz 
    var o = {
      "M+": this.getMonth() + 1, //月份 
      "d+": this.getDate(), //日 
      "h+": this.getHours(), //小时 
      "m+": this.getMinutes(), //分 
      "s+": this.getSeconds(), //秒 
      "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
      "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt))
      fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
      if (new RegExp("(" + k + ")").test(fmt))
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
  }
});

