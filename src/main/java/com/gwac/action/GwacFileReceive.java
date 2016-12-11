/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.SyncFileDao;
import com.gwac.model.SyncFile;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
public class GwacFileReceive extends ActionSupport {
  
  private static final Log log = LogFactory.getLog(GwacFileReceive.class);
  
  private DataProcessMachineDAO dpmDao;
  private SyncFileDao sfDao;
  
  private List<File> fileUpload = new ArrayList<File>();
  private List<String> fileUploadContentType = new ArrayList<String>();
  private List<String> fileUploadFileName = new ArrayList<String>();
  private String echo = "";
  
  @Action(value = "gwacFileReceive", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() throws Exception {
    
    boolean flag = true;
    String result = SUCCESS;
    
    if (fileUpload.isEmpty()) {
      setEcho(echo + "Error, must upload data file(fileUpload).\n");
      flag = false;
    }
    
    if (fileUpload.size() != fileUploadFileName.size()) {
      setEcho(echo + "Error，please check upload command and retry!\n");
      flag = false;
    }
    
    if (flag) {
      String destPath = getText("gwac.data.root.directory");
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/";
      }

      //接收参数配置文件
      String storeDirName = getText("gwac.monitorimage.directory");
      String otDstPath = destPath + storeDirName + "/";
      
      File tDir = new File(otDstPath);
      if (!tDir.exists()) {
        tDir.mkdirs();
      }

      //接受文件
      int i = 0;
      for (File file : fileUpload) {
        String tfilename = fileUploadFileName.get(i++).trim();
        if (tfilename.isEmpty()) {
          continue;
        }
        String updateFileName = tfilename;
        log.debug("receive file " + tfilename);

        //M4_07_161128_1_220060_0705_ccdimg.jpg
        if (tfilename.contains("ccdimg")) {
          if (tfilename.length() != "M01_ccdimg.jpg".length()) {
            String thumbnail = getText("gwac.data.thumbnail.directory");
            String dateStr = tfilename.substring(6, 12);
            String dpmIdStr = tfilename.substring(3, 5);
            String dpmName = tfilename.substring(0, 1) + dpmIdStr;
            String thumbnailPath = destPath + thumbnail + "/" + dateStr + "/" + dpmName + "/";
            File dir = new File(thumbnailPath);
            if (!dir.exists()) {
              dir.mkdirs();
            }
            
            String tfilename1 = dpmName + "_ccdimg.jpg";
            updateFileName = tfilename1;
            File destFile1 = new File(otDstPath, tfilename1);
            File destFile2 = new File(thumbnailPath, tfilename);
            if (destFile1.exists()) {
              log.info(destFile1 + " already exist, delete it.");
              FileUtils.forceDelete(destFile1);
            }
            if (destFile2.exists()) {
              log.info(destFile2 + " already exist, delete it.");
              FileUtils.forceDelete(destFile2);
            }
            if (file != null && file.exists()) {
              FileUtils.copyFile(file, destFile1);
              FileUtils.moveFile(file, destFile2);
            }
            
            try {
              int dpmId = Integer.parseInt(dpmIdStr);
              dpmDao.updateMonitorImageTime(dpmId);
            } catch (NumberFormatException nfe) {
              log.error("ccd img name " + tfilename + " wrong formate!");
            }
          } else {
            File destFile = new File(otDstPath, tfilename);
            if (destFile.exists()) {
              log.warn(destFile + " already exist, delete it.");
              FileUtils.forceDelete(destFile);
            }
            if (file != null && file.exists()) {
              FileUtils.moveFile(file, destFile);
            }
            
            try {
              int dpmId = Integer.parseInt(tfilename.substring(1, 3));
              dpmDao.updateMonitorImageTime(dpmId);
            } catch (NumberFormatException nfe) {
              log.error("ccd img name " + tfilename + " wrong formate!");
            }
          }
        } else {
          File destFile = new File(otDstPath, tfilename);
          //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
          if (destFile.exists()) {
            log.info(destFile + " already exist, delete it.");
            FileUtils.forceDelete(destFile);
          }
          if (file != null && file.exists()) {
            FileUtils.moveFile(file, destFile);
          }
        }
        
        try {
          boolean isBeiJingServer = Boolean.parseBoolean(getText("gwac.server.beijing"));
          
          if (!isBeiJingServer) {
            SyncFile tsf = new SyncFile();
//        tsf.setFileName(tfilename);
            tsf.setFileName(updateFileName);
            tsf.setIsSync(false);
            tsf.setIsSyncSuccess(false);
            tsf.setPath(storeDirName);
            tsf.setStoreTime(new Date());
            sfDao.save(tsf);
          }
        } catch (Exception e) {
          log.error("update syncfile error:", e);
        }
      }
      
      echo += "success\n";
    } else {
      result = ERROR;
    }
    log.debug(echo);
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
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

  /**
   * @return the fileUpload
   */
  public List<File> getFileUpload() {
    return fileUpload;
  }

  /**
   * @param fileUpload the fileUpload to set
   */
  public void setFileUpload(List<File> fileUpload) {
    this.fileUpload = fileUpload;
  }

  /**
   * @return the fileUploadContentType
   */
  public List<String> getFileUploadContentType() {
    return fileUploadContentType;
  }

  /**
   * @param fileUploadContentType the fileUploadContentType to set
   */
  public void setFileUploadContentType(List<String> fileUploadContentType) {
    this.fileUploadContentType = fileUploadContentType;
  }

  /**
   * @return the fileUploadFileName
   */
  public List<String> getFileUploadFileName() {
    return fileUploadFileName;
  }

  /**
   * @param fileUploadFileName the fileUploadFileName to set
   */
  public void setFileUploadFileName(List<String> fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }

  /**
   * @return the sfDao
   */
  public SyncFileDao getSfDao() {
    return sfDao;
  }

  /**
   * @param sfDao the sfDao to set
   */
  public void setSfDao(SyncFileDao sfDao) {
    this.sfDao = sfDao;
  }
  
}
