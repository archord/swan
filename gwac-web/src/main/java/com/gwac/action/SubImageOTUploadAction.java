///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.gwac.action;
//
///**
// *
// * @author xy
// */
//import com.gwac.dao.ConfigFileDao;
//import com.gwac.model.ConfigFile;
//import com.gwac.service.UploadFileServiceImpl;
//import com.gwac.util.CommonFunction;
//import static com.opensymphony.xwork2.Action.ERROR;
//import static com.opensymphony.xwork2.Action.INPUT;
//import static com.opensymphony.xwork2.Action.SUCCESS;
//import com.opensymphony.xwork2.ActionContext;
//import com.opensymphony.xwork2.ActionSupport;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.struts2.convention.annotation.Action;
//import org.apache.struts2.convention.annotation.Result;
//
///*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
///* curl command example: */
///* curl http://localhost:8080/svom/resultAction.action */
///* -F dpmName=dpmName */
///* -F currentDirectory=dirName */
///* -F configFile=@configFileName */
///* -F fileUpload=@simulationUI2.tar.gz */
///* -F fileUpload=@simulationUI.tar.gz*/
///**
// * @author xy
// */
////@InterceptorRef("jsonValidationWorkflowStack")
////加了这句化，文件传不上来
////@ParentPackage(value="struts-default")
////@Controller()
////@Scope(BeanDefinition.SCOPE_PROTOTYPE)
//public class SubImageOTUploadAction extends ActionSupport {
//
//  private static final Log log = LogFactory.getLog(SubImageOTUploadAction.class);
//  private UploadFileServiceImpl ufService;
//  private String dpmName;
//  private String currentDirectory;
//  private List<File> fileUpload = new ArrayList<File>();
//  private List<String> fileUploadContentType = new ArrayList<String>();
//  private List<String> fileUploadFileName = new ArrayList<String>();
//  private String echo = "";
//
//  @Action(value = "uploadAction", results = {
//    @Result(location = "manage/result.jsp", name = SUCCESS),
//    @Result(location = "manage/result.jsp", name = INPUT),
//    @Result(location = "manage/result.jsp", name = ERROR)})
//  public String upload() {
//
//    boolean flag = true;
//    String result = SUCCESS;
//    echo = "";
//
//    //必须设置传输机器名称
//    if (null == dpmName) {
//      setEcho(echo + "Error, must set machine name(dpmName).\n");
//      flag = false;
//    }
//
//    //必须设置传输机器名称
//    if (null == currentDirectory) {
//      setEcho(echo + "Error, must set current date(currentDirectory).\n");
//      flag = false;
//    }
//
//    //必须传输数据文件
//    //Error, must transform data file
//    if (fileUpload.isEmpty()) {
//      setEcho(echo + "Error, must upload data file(fileUpload).\n");
//      flag = false;
//    }
//
//    if (fileUpload.size() != fileUploadFileName.size()) {
//      setEcho(echo + "Error，please check upload command and retry!\n");
//      flag = false;
//    }
//
//    //没有设置存储文件夹名, 使用当前日期，作为存储文件夹名。
//    //Do not set data store directory name, use current date, as data store directory name
//    if (flag) {
//      dpmName = dpmName.trim();
//      currentDirectory = currentDirectory.trim();
//      if (dpmName.isEmpty() || currentDirectory.isEmpty()) {
//        setEcho(echo + "Error, parameter error!\n");
//        log.error(echo);
//        return ERROR;
//      }
//      //由于跨天问题，这里不再自行判断currentDirectory是否为空，前面已将currentDirectory设置为必选项
//      /*
//       if (this.getCurrentDirectory() == null || this.getCurrentDirectory().isEmpty()) {
//       this.setCurrentDirectory(CommonFunction.getCurDateString());
//       setEcho(getEcho() + "Does not set store directory name(currentDirectory), use current date ");
//       setEcho(getEcho() + this.getCurrentDirectory());
//       setEcho(getEcho() + " as store directory name.\n");
//       }
//       */
//
//      String rootPath = getText("gwacDataRootDirectory");
//      String destPath = rootPath;
//      if (destPath.charAt(destPath.length() - 1) != '/') {
//        destPath += "/" + currentDirectory + "/" + dpmName + "/";
//      } else {
//        destPath += currentDirectory + "/" + dpmName + "/";
//      }
//
//      try {
//
//        File destDir = new File(destPath);
//        if (!destDir.exists()) {
//          destDir.mkdirs();
//          log.debug("create dir " + destDir);
//        }
//
//        //接收参数配置文件
//        String confPath = destPath + getText("gwac.data.cfgfile.directory") + "/";
//        File confDir = new File(confPath);
//        if (!confDir.exists()) {
//          confDir.mkdir();
//          log.debug("create dir " + confDir);
//        }
//
//        configFileFileName = configFileFileName.replace(".", "_" + CommonFunction.getCurTimeString() + ".");
//        File confFile = new File(confPath, configFileFileName);
//        log.debug("receive file " + confFile);
//        if (confFile.exists()) {
//          log.warn(confFile + " already exist, delete it.");
//          FileUtils.forceDelete(confFile);
//        }
//        FileUtils.moveFile(configFile, confFile);
//
//        ConfigFile cf = new ConfigFile();
//        cf.setFileName(configFileFileName);
//        cf.setStorePath(confPath.substring(rootPath.length() + 1));
//        cf.setIsSync(false);
//        cf.setIsStore(false);
//        if (!cfDao.exist(cf)) {
//          cfDao.save(cf);
//        }
//
//        //接受数据文件
//        int i = 0;
//        for (File file : fileUpload) {
//          String tfilename = fileUploadFileName.get(i++).trim();
//          if (tfilename.isEmpty()) {
//            continue;
//          }
//          log.debug("receive file " + tfilename);
//          File destFile = new File(destPath, tfilename);
////          log.info("receive file: " + destFile);
////          log.info("source file: " + file);
//          //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
//          if (destFile.exists()) {
//            log.warn(destFile + " already exist, delete it.");
//            FileUtils.forceDelete(destFile);
//          }
//          FileUtils.moveFile(file, destFile);
//        }
//
//        ufService.setConfigPath(confPath);
//        ufService.setConfigFile(configFileFileName);
////        ufService.setRootDir(rootPath);
////        ufService.setOtLDir(getText("gwac.data.otlist.directory"));
////        ufService.setStarLDir(getText("gwac.data.starlist.directory"));
////        ufService.setOrgIDir(getText("gwac.data.origimage.directory"));
////        ufService.setCutIDir(getText("gwac.data.cutimages.directory"));
////        ufService.setCfgDir(getText("gwac.data.cfgfile.directory"));
////        ufService.setVarLDir(getText("gwac.data.varlist.directory"));
//
//        int shouldFNum = ufService.parseConfigFile();
//        int validFNum = ufService.checkAndMoveDataFile(destPath);
//        if (validFNum != i || validFNum != shouldFNum) {
//          setEcho(echo + "Warn: should upload " + shouldFNum + " files, actual upload " + i
//                  + " files, " + validFNum + " valid files.\n");
//        } else {
//          setEcho(echo + "Success, total upload " + validFNum + " files.\n");
//        }
//        //otORDao.saveOTCopy(configFileFileName);
//      } catch (IOException ex) {
//        log.error("delete or move file errror ", ex);
//      }
//    } else {
//      result = ERROR;
//    }
//
//    log.debug(echo);
//    /* 如果使用struts2的标签，返回结果会有两个空行，这个显示在命令行不好看。
//     * 用jsp的out，则不会有两个空行。
//     * 在这里将结果信息存储在session中，在jsp页面获得返回信息。
//     */
//    ActionContext ctx = ActionContext.getContext();
//    ctx.getSession().put("echo", echo);
//
//    return result;
//  }
//
//  public String display() {
//    return NONE;
//  }
//
//  public List<File> getFileUpload() {
//    return fileUpload;
//  }
//
//  public void setFileUpload(List<File> fileUpload) {
//    this.fileUpload = fileUpload;
//  }
//
//  public List<String> getFileUploadContentType() {
//    return fileUploadContentType;
//  }
//
//  public void setFileUploadContentType(List<String> fileUploadContentType) {
//    this.fileUploadContentType = fileUploadContentType;
//  }
//
//  public void setFileUploadFileName(List<String> fileUploadFileName) {
//    this.fileUploadFileName = fileUploadFileName;
//  }
//
//  /**
//   * @param currentDirectory the currentDirectory to set
//   */
//  public void setCurrentDirectory(String currentDirectory) {
//    this.currentDirectory = currentDirectory;
//  }
//
//  /**
//   * @return the configFile
//   */
//  public File getConfigFile() {
//    return configFile;
//  }
//
//  /**
//   * @param configFile the configFile to set
//   */
//  public void setConfigFile(File configFile) {
//    this.configFile = configFile;
//  }
//
//  /**
//   * @param configFileFileName the configFileFileName to set
//   */
//  public void setConfigFileFileName(String configFileFileName) {
//    this.configFileFileName = configFileFileName;
//  }
//
//  /**
//   * @return the echo
//   */
//  public String getEcho() {
//    return echo;
//  }
//
//  /**
//   * @param echo the echo to set
//   */
//  public void setEcho(String echo) {
//    this.echo = echo;
//  }
//
//  /**
//   * @param dpmName the dpmName to set
//   */
//  public void setDpmName(String dpmName) {
//    this.dpmName = dpmName;
//  }
//
//  /**
//   * @param ufService the ufService to set
//   */
//  public void setUfService(UploadFileServiceImpl ufService) {
//    this.ufService = ufService;
//  }
//
//  /**
//   * @param cfDao the cfDao to set
//   */
//  public void setCfDao(ConfigFileDao cfDao) {
//    this.cfDao = cfDao;
//  }
//}
