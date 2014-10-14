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
public class GetCutImageList extends ActionSupport {

  private static final Log log = LogFactory.getLog(GetCutImageList.class);
  private String dpmName;
  private FitsFileCutDAO ffcDao;
  private InputStream fileInputStream;
  private String fileName;
  private String echo = "";

  @Action(value = "getCutImageList", results = {
    @Result(location = "forward.jsp", name = SUCCESS),
    @Result(location = "forward.jsp", name = INPUT),
    @Result(location = "forward.jsp", name = ERROR)})
  public String upload() throws Exception {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须设置传输机器名称
    if (null == dpmName) {
      echo = echo + "Must set machine name(dpmName).\n";
      flag = false;
    }

    if (flag) {
      dpmName = dpmName.trim();
      String rootPath = getText("gwac.data.root.directory");
      String destPath = rootPath;
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/tmp/";
      } else {
        destPath += "tmp/";
      }
//      fileName = dpmName + "-" + ((int) (Math.random() * 1000) + 1) + ".lst";
//      fileName = dpmName + ".lst";
      fileName = dpmName + "_" + CommonFunction.getCurDateTimeString() + ".lst";
      log.debug("request cut image list destPath=" + destPath + fileName);
      File tmpDir = new File(destPath);
      if (!tmpDir.exists()) {
        tmpDir.mkdirs();
      }
      File file = new File(destPath, fileName);
      if (!file.exists()) {
        file.createNewFile();
      }
      int dpmId = Integer.parseInt(dpmName.substring(1));
      String content = ffcDao.getUnCuttedStarList(dpmId);
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(content);
      bw.close();
      log.info("ot file name "+file.getAbsolutePath());
      log.info("ot list file "+ fileName +" size "+ file.length());
    } else {
      result = ERROR;
    }
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);
    ctx.getSession().put("fileName", "/images/tmp/"+fileName);
    return result;
  }

  @Action(value = "getCutImageList1", results = { 
    @Result(name = "success", type = "stream", 
            params = { "contentType", "application/octet-stream", 
              "inputName", "fileInputStream", 
              "contentDisposition", "attachment;filename=\"${fileName}\"", 
              "bufferSize", "4096" }) })
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
      String content = ffcDao.getUnCuttedStarList(dpmId);
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
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

}
