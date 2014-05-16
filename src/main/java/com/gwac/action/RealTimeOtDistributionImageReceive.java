/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.ConfigFileDao;
import com.gwac.model.ConfigFile;
import com.gwac.service.UploadFileServiceImpl;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* curl command example: */
/* curl -F currentDirectory=dirName */
/* -F configFile=@configFileName */
/* -F fileUpload=@simulationUI2.tar.gz */
/* -F fileUpload=@simulationUI.tar.gz http://localhost:8080/svom/resultAction.action*/
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class RealTimeOtDistributionImageReceive extends ActionSupport {

  private static final Log log = LogFactory.getLog(RealTimeOtDistributionImageReceive.class);

  private File fileUpload;
  private String fileUploadFileName;
  private String echo = "";

  @Action(value = "realTimeOtDstImageUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() throws Exception {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";
    if (fileUpload != null && fileUploadFileName != null && getFileUpload().exists() && !fileUploadFileName.isEmpty()) {
      String destPath = getText("gwac.data.root.directory");
      if (destPath.charAt(destPath.length() - 1) != '/') {
	destPath += "/";
      }

      //接收参数配置文件
      String otDstPath = destPath + getText("gwac.real.time.ot.distribution") + "/";
      File otDstImage = new File(otDstPath, fileUploadFileName);
      if (otDstImage.exists()) {
	FileUtils.forceDelete(otDstImage);
      }
      FileUtils.moveFile(getFileUpload(), otDstImage);
      echo += "upload success. file name is " + fileUploadFileName + "\n";
    }else{
      echo += "upload error. upload file does not exist.\n";
    }
    log.info(echo);
    /* 如果使用struts2的标签，返回结果会有两个空行，这个显示在命令行不好看。
     * 用jsp的out，则不会有两个空行。
     * 在这里将结果信息存储在session中，在jsp页面获得返回信息。
     */
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", getEcho());

    return result;
  }

  public String display() {
    return NONE;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param echo the echo to set
   */
  public void setEcho(String echo) {
    this.echo = echo;
  }

  /**
   * @return the fileUpload
   */
  public File getFileUpload() {
    return fileUpload;
  }

  /**
   * @param fileUpload the fileUpload to set
   */
  public void setFileUpload(File fileUpload) {
    this.fileUpload = fileUpload;
  }

  /**
   * @return the fileUploadFileName
   */
  public String getFileUploadFileName() {
    return fileUploadFileName;
  }

  /**
   * @param fileUploadFileName the fileUploadFileName to set
   */
  public void setFileUploadFileName(String fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }
  
}
