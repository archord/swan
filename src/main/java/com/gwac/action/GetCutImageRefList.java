/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
public class GetCutImageRefList extends ActionSupport {

  private static final Log log = LogFactory.getLog(GetCutImageRefList.class);
  private String dpmName;
  private FitsFileCutRefDAO ffcrDao;
  private InputStream fileInputStream;
  private String fileName;
  private String rootWebDir;
  private String echo = "";

  @Action(value = "getCutImageRefList", results = {
    @Result(location = "forward.jsp", name = SUCCESS),
    @Result(location = "forward.jsp", name = INPUT),
    @Result(location = "forward.jsp", name = ERROR)})
  public String upload() {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须设置传输机器名称
    if (null == dpmName || dpmName.isEmpty()) {
      echo = echo + "Must set machine name(dpmName).\n";
      flag = false;
      log.warn("dpm name is empty!");
    }

    if (flag) {
      String rootPath = getText("gwac.data.root.directory");
      String destPath = rootPath;
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/tmp/";
      } else {
        destPath += "tmp/";
      }
      File tmpDir = new File(destPath);
      if (!tmpDir.exists()) {
        tmpDir.mkdirs();
        log.debug("create dir " + tmpDir);
      }

      dpmName = dpmName.trim();
      int dpmId = Integer.parseInt(dpmName.substring(1));
      String content = getFfcrDao().getUnCuttedStarList(dpmId);
      try {
        if (!content.isEmpty()) {
          fileName = dpmName + "_" + CommonFunction.getCurDateTimeString() + ".lst";
          File file = new File(destPath, fileName);
          if (!file.exists()) {
            file.createNewFile();
            log.debug("create cut image list file " + file);
          }
          FileWriter fw = new FileWriter(file.getAbsoluteFile());
          BufferedWriter bw = new BufferedWriter(fw);
          bw.write(content);
          bw.close();
        } else {
          fileName = "empty.lst";
          File file = new File(destPath, fileName);
          if (!file.exists()) {
            file.createNewFile();
            log.debug("create empty file " + file);
          }
//          log.debug("no cut images found at this time.");
        }
      } catch (IOException ex) {
        log.error("create or write file error ", ex);
      }
    } else {
      result = ERROR;
    }
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);
    ctx.getSession().put("fileName", rootWebDir + "/tmp/" + fileName);
    return result;
  }

  @Action(value = "getCutImageList1", results = {
    @Result(name = "success", type = "stream",
            params = {"contentType", "application/octet-stream",
              "inputName", "fileInputStream",
              "contentDisposition", "attachment;filename=\"${fileName}\"",
              "bufferSize", "4096"})})
  public String upload1() throws Exception {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须设置传输机器名称
    if (null == dpmName) {
      echo = echo + "Must set machine name(dpmName).\n";
      flag = false;
    }

    if (flag) {
      String rootPath = getText("gwac.data.root.directory");
      String destPath = rootPath;
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/tmp/";
      } else {
        destPath += "tmp/";
      }
      fileName = dpmName + "-" + ((int) (Math.random() * 1000) + 1) + ".lst";
      log.debug("destPath=" + destPath + fileName);
      File tmpDir = new File(destPath);
      if (!tmpDir.exists()) {
        tmpDir.mkdirs();
      }
      File file = new File(destPath, fileName);
      if (!file.exists()) {
        file.createNewFile();
      }
      int dpmId = Integer.parseInt(dpmName.substring(1));
      String content = getFfcrDao().getUnCuttedStarList(dpmId);
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(content);
      bw.close();
      fileInputStream = new FileInputStream(new File(destPath, fileName));
      echo = echo + "Upload success，total upload files.\n";
      //otORDao.saveOTCopy(configFileFileName);
    } else {
      result = ERROR;
    }

    log.info(echo);

    return result;
  }

  public String display() {
    return NONE;
  }

  public InputStream getFileInputStream() {
    return fileInputStream;
  }

  /**
   * @param dpmName the dpmName to set
   */
  public void setDpmName(String dpmName) {
    this.dpmName = dpmName;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @return the ffcrDao
   */
  public FitsFileCutRefDAO getFfcrDao() {
    return ffcrDao;
  }

  /**
   * @param ffcrDao the ffcrDao to set
   */
  public void setFfcrDao(FitsFileCutRefDAO ffcrDao) {
    this.ffcrDao = ffcrDao;
  }

  public void setRootWebDir(String rootWebDir) {
    this.rootWebDir = rootWebDir;
  }
}
