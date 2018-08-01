
$(function() {

  initPage();
  loadMountList();

  function updateMountStatus() {

    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/get-mount-list-json.action";
    var formData = $("#mountListForm").serialize();
    console.log(formData);
  }

  function initPage() {
    $("#setMountButton").click(updateMountStatus);
    $("#checkallmount").change(function() {
      if ($('#checkallmount').is(':checked')) {
        $("input[name='mountControl']").prop('checked', true);
      } else {
        $("input[name='mountControl']").prop('checked', false);
      }
    });
  }

  function loadMountList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/gction/get-mount-list-json.action";
    $.ajax({
      type: "get",
      url: queryUrl,
      data: 'p1=1',
      async: false,
      dataType: 'json',
      success: function(data) {
        var mounts = data.mounts;
        var tnum = mounts.length;
        $.each(mounts, function(i, item) {
          var mcon;
          if (i < tnum / 2) {
            mcon = $('#mount-list-container1');
          } else {
            mcon = $('#mount-list-container2');
          }
          var tcheck = $('<input>', {type: "checkbox", value: item.mountId, name: "mountControl"});
          if (item.status === 1) {
            tcheck.prop('checked', true)
          }
          mcon.append(tcheck);
          mcon.append("转台" + item.unitId);
          mcon.append("<br/>");
        });
      }
    });
  }

});
