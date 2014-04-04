/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.service.StoreStarInfoImpl;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
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
public class MultipleFileUploadAction extends ActionSupport {

  private static final Log log = LogFactory.getLog(UserRegister.class);
  
  private OtObserveRecordDAO otORDao;
  
  private String dpmName;
  private String currentDirectory;
  private File configFile;
  private String configFileFileName;
  private List<File> fileUpload = new ArrayList<File>();
  private List<String> fileUploadContentType = new ArrayList<String>();
  private List<String> fileUploadFileName = new ArrayList<String>();
  private String echo = "";

  @Action(value = "uploadAction", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() throws Exception {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须传输参数配置文件
    //Must transform parameter config file
    if (null == configFile) {
      setEcho(getEcho() + "必须传输参数配置文件(configFile)。\n");
      flag = false;
    }

    //必须传输数据文件
    //Must transform data file
    if (fileUpload.isEmpty()) {
      setEcho(getEcho() + "必须传输数据文件(fileUpload)。\n");
      flag = false;
    }

    if (fileUpload.size() != fileUploadFileName.size()) {
      setEcho(getEcho() + "上传数据错误，请重试或联系管理员。\n");
      flag = false;
    }

    //没有设置存储文件夹名, 使用当前日期，作为存储文件夹名。
    //Do not set data store directory name, use current date, as data store directory name
    if (flag) {
      if (this.getCurrentDirectory() == null || this.getCurrentDirectory().isEmpty()) {
        this.setCurrentDirectory(CommonFunction.getCurDateString());
        setEcho(getEcho() + "没有设置存储文件夹名currentDirectory）, 使用当前日期");
        setEcho(getEcho() + this.getCurrentDirectory());
        setEcho(getEcho() + "作为存储文件夹名。\n");
      }

      String destPath = getText("gwac.data.root.directory");
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/" + getCurrentDirectory() + "/";
      } else {
        destPath += getCurrentDirectory() + "/";
      }

      //接收参数配置文件
      File confFile = new File(destPath, configFileFileName);
      if (confFile.exists()) {
        FileUtils.forceDelete(confFile);
      }
      FileUtils.moveFile(configFile, confFile);

      //接受数据文件
      int i = 0;
      for (File file : fileUpload) {
        File destFile = new File(destPath, fileUploadFileName.get(i++));
        if (destFile.exists()) {
          FileUtils.forceDelete(destFile);
        }
        FileUtils.moveFile(file, destFile);
      }

      StoreStarInfoImpl ssii = new StoreStarInfoImpl(destPath, configFileFileName);
      ssii.setOtLDir(getText("gwac.data.otlist.directory"));
      ssii.setStarLDir(getText("gwac.data.starlist.directory"));
      ssii.setOrgIDir(getText("gwac.data.origimage.directory"));
      ssii.setCutIDir(getText("gwac.data.cutimages.directory"));
      
      int shouldFNum = ssii.parseConfigFile();
      int validFNum = ssii.checkAndMoveDataFile(destPath);
      if (validFNum != i || validFNum != shouldFNum) {
        setEcho(getEcho() + "警告：应该传输文件" + shouldFNum + "个，实际传输文件" + i
                + "个，有效文件" + validFNum + "个。\n");
      } else {
        setEcho(getEcho() + "文件上传成功。\n");
      }
      //otORDao.saveOTCopy(configFileFileName);
    } else {
      result = ERROR;
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

  public List<File> getFileUpload() {
    return fileUpload;
  }

  public void setFileUpload(List<File> fileUpload) {
    this.fileUpload = fileUpload;
  }

  public List<String> getFileUploadContentType() {
    return fileUploadContentType;
  }

  public void setFileUploadContentType(List<String> fileUploadContentType) {
    this.fileUploadContentType = fileUploadContentType;
  }

  public List<String> getFileUploadFileName() {
    return fileUploadFileName;
  }

  public void setFileUploadFileName(List<String> fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }

  /**
   * @return the currentDirectory
   */
  public String getCurrentDirectory() {
    return currentDirectory;
  }

  /**
   * @param currentDirectory the currentDirectory to set
   */
  public void setCurrentDirectory(String currentDirectory) {
    this.currentDirectory = currentDirectory;
  }

  /**
   * @return the configFile
   */
  public File getConfigFile() {
    return configFile;
  }

  /**
   * @param configFile the configFile to set
   */
  public void setConfigFile(File configFile) {
    this.configFile = configFile;
  }

  /**
   * @return the configFileFileName
   */
  public String getConfigFileFileName() {
    return configFileFileName;
  }

  /**
   * @param configFileFileName the configFileFileName to set
   */
  public void setConfigFileFileName(String configFileFileName) {
    this.configFileFileName = configFileFileName;
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
   * @return the otORDao
   */
  public OtObserveRecordDAO getOtORDao() {
    return otORDao;
  }

  /**
   * @param otORDao the otORDao to set
   */
  public void setOtORDao(OtObserveRecordDAO otORDao) {
    this.otORDao = otORDao;
  }
}