/* 
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

$(function () {
  function loadDataVersionList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/get-data-version-list.action" + "?timestamp=" + new Date().getTime();
    $.ajax({
      type: "get",
      url: queryUrl,
      async: false,
      dataType: 'json',
      success: function (data) {
        //console.log(data)
        var objs = eval(data.parStr);
        if (objs !== null && objs.length > 0) {
          $.each(objs, function (i, item) {
            $('#dataVersion').append($('<option>', {
              value: item.dv_id,
              text: item.dv_name
            }));
          });
        }
      }
    });
  }
  function loadDateSetList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var dvId = $("#dataVersion").val();
    var defaultType = $("#defaultType").val();
    var queryUrl = gwacRootURL + "/get-data-set-list.action?dvId=" + dvId + "&defaultType=" + defaultType + "&timestamp=" + new Date().getTime();

    $('#dataDir')
            .find('option')
            .remove()
            .end()
            .append('<option value="0">请选择数据目录</option>')
            .val('0');

    $.ajax({
      type: "get",
      url: queryUrl,
      async: false,
      dataType: 'json',
      success: function (data) {
        var objs = eval(data.parStr);
        //console.log(objs)
        if (objs !== null && objs.length > 0) {
          $.each(objs, function (i, item) {
            $('#dataDir').append($('<option>', {
              value: item.ds_id,
              text: item.ds_dir_name+"("+item.ds_img_num+":"+item.left_img_num+")"
            }));
          });
        }
      }
    });
  }
  function loadImageList() {
    var gwacRootURL = $("#gwacRootURL").val();
    var dsId = $("#dataDir").val();
    var queryUrl = gwacRootURL + "/get-image-list.action?dsId=" + dsId + "&timestamp=" + new Date().getTime();
    console.log(queryUrl);
    $.ajax({
      type: "get",
      url: queryUrl,
      async: false,
      dataType: 'json',
      success: function (data) {
        imgList = eval(data.parStr);
        //console.log(imgList);
        showImage(0);
      }
    });
  }

  function nextImage() {
    var ttype = $('input[type=radio][name=objClassify]:checked').val();
    //console.log(ttype);
    if (ttype === '0') {
      updateObjType(0);
    }
    showImage(1);
  }

  function headImage() {
    var ttype = $('input[type=radio][name=objClassify]:checked').val();
    //console.log(ttype);
    if (ttype === '0') {
      updateObjType(0);
    }
    showImage(-1);
  }

  function showImage(num) {
    var gwacRootURL = "/images";
    if (imgList !== null && imgList.length > 0) {
      var imgNum = imgList.length;
      var imgType = 0
      imgListIdx = imgListIdx + num;
      if (imgListIdx < 0) {
        imgListIdx = 0;
        alert("already the first one");
      } else if (imgListIdx >= imgNum) {
        imgListIdx = imgNum - 1;
        alert("already the last one");
      } else {
        var timg = imgList[imgListIdx];
        //
        var turl = gwacRootURL + "/" + timg.ds_path.substring(16) + "/" + timg.ds_dir_name + "/" + timg.img_name;
        curImgId = timg.img_id;
        imgType = parseInt(timg.img_type);
        //console.log(turl);
        $('#obj-image').attr("src", turl);
        $('#image-title').html(imgListIdx+" : " +timg.img_name);
        if (imgType === -1) {
          imgType = 0;
        }
        var $radios = $('input:radio[name=objClassify]');
        $radios.filter('[value=' + imgType + ']').prop('checked', true);
      }
    } else {
      var dsId = $("#dataDir").val();
      var dsDir = $("#dataDir option[value='" + dsId + "']").text();
      alert("no image in dir: " + dsDir);
    }
  }

  function radioChange() {
    var type = this.value;
    updateObjType(type);
  }

  function updateObjType(type) {
    var gwacRootURL = $("#gwacRootURL").val();
    var queryUrl = gwacRootURL + "/img-classify.action?imgId=" + curImgId + "&imgType=" + type;
    console.log(queryUrl);
    $.ajax({
      type: "get",
      url: queryUrl,
      async: false,
      dataType: 'json',
      success: function (data) {
        console.log(data);
      }
    });
  }

  var imgListIdx = 0;
  var curImgId = 0;
  var imgList = [];
  loadDataVersionList();
  $('#dataVersion').change(loadDateSetList);
  $('#dataDir').change(loadImageList);
  $('input[type=radio][name=objClassify]').change(radioChange);
  $('#headBtn').click(headImage);
  $('#nextBtn').click(nextImage);

});



