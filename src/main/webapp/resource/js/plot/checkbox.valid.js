
$(document).ready(function() {

  $("input[name='box[]']").click(function () {
    if($("input[name='box[]']:checked").length>7){
      //this.checked = false;
      alert("最多只能选择7颗星!");
      return false;
    }
  });
});