<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 

<style>
  table tr td{
    margin: 2px;
    padding: 2px;
  }
</style>

<div class="ui-widget-content ui-corner-all " style="margin:0px;width:100%;">
  <p style="margin:0px; padding: 5px; "><b>curl后台上传文件示例：</b><br/>
    curl url -F currentDirectory=dirName 
    -F configFile=@configFileName <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    -F fileUpload=@filename1 
    -F fileUpload=@filenameN <br/>
    <b>参数说明：</b><br/>
    <b>currentDirectory:&nbsp;存储文件夹名，</b> 
    为参数，后面的值没有“@”。传输的所有文件都存放在这个文件夹中，可不填写，默认以当前日期命名。<br/>
    <b>configFile:&nbsp;参数配置文件，</b> 
    <a href="/svom/files/data-upload-config.properties" target="_blank" style="color:red" title="点击下载">
      示例文件</a>，为文件，后面的值有“@”，以“.properties”结尾。后面传输的文件必须在这里说明，
      其中包含OT列表文件名(OTList)，
    切图文件文件名列表(cutImage)，星表文件文件名(starList)，原始图像文件名(origImage)。<br/>
    <b>fileUpload:&nbsp;文件n，</b> 
    后面的值有“@”。待上传的所有文件都以该参赛进行传递。单次传送总文件大小不超过1ooMB。<br/>
    <b>url: 传输地址，</b>
    默认为：http://localhost:8080/svom/uploadAction.action
  </p>
  <br/>
</div>

<div id="result" class="result ui-widget-content ui-corner-all" style="color: red; font-size: 14px; width:100%;margin-left: 0px;">
  数据处理机名称，配置文件和文件n为必选项！
</div>

<div class="ui-widget-content ui-corner-all " style="font-size: 14px; width:100%;margin-left: 0px;">
  <!--  namespace="/" -->
  <s:form id="uploadform" action="uploadAction" method="POST" enctype="multipart/form-data">
    <s:textfield label="数据处理机名称" name="dpmName"/>
    <s:textfield label="存储文件夹名" name="currentDirectory"/>
    <s:file label="参数配置文件" name="configFile"/>
    <s:file label="文件1" name="fileUpload"/>
    <s:file label="文件2" name="fileUpload"/>
    <s:file label="文件3" name="fileUpload"/>
    <!--s:submit value="上传" name="submit"/-->
    <sj:submit
      targets="result" button="true" validate="true"
      validateFunction="customeValidation"
      onBeforeTopics="removeErrors"
      onSuccessTopics="removeErrors" value="上传" indicator="indicator" />
  </s:form>

</div>
<img id="indicator" src="images/indicator.gif" alt="Loading..." style="display:none"/>